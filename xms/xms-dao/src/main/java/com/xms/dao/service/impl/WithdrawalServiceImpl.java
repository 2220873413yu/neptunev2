package com.xms.dao.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uduncloud.sdk.domain.ResultMsg;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.*;
import com.xms.common.core.domain.AjaxResult;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.CollectionUtil;
import com.xms.common.utils.JuSign;
import com.xms.common.utils.SignUtil;
import com.xms.common.utils.WalletUtil;
import com.xms.dao.api.PullThirdInfo;
import com.xms.dao.domain.DestroyOrder;
import com.xms.dao.entity.bo.RewardRecordBo;
import com.xms.dao.entity.bo.WithdrawalBo;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.mapper.WithdrawalMapper;
import com.xms.dao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 提现表 服务实现类
 * </p>
 *
 * @since 2023-06-30
 */
@Service
@Slf4j
public class WithdrawalServiceImpl extends ServiceImpl<WithdrawalMapper, Withdrawal> implements WithdrawalService {

	@Autowired
	private UserWalletService userWalletServiceImpl;
	@Autowired
	private WalletUtil walletUtil;

	@Autowired
	private PullThirdInfo pullThirdInfo;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private IUserMoneyService userMoneyServiceImpl;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private  Environment environment;

	@Autowired
	private IWalletTransferOrderService walletTransferOrderServiceImpl;

	@Autowired
	private IW3UserLevelConfigService w3UserLevelConfigService;

	@Autowired
	private IWalletTransferOrderService walletTransferOrderService;

	@Value("${lq.md5Key}")
	private String md5Key;
	@Value("${lq.tokenName}")
	private String tokenName;

	@Value("${lq.baseUrl}")
	private String baseUrl;

	@Value("${lq.skAddress}")
	private String skAddress;
	/**
	 * 查询提现列表
	 *
	 * @param withdrawal 提现
	 * @return 提现
	 */
	@Override
	public List<Withdrawal> selectWithdrawalList(Withdrawal withdrawal) {
		List<Withdrawal> withdrawals = baseMapper.selectWithdrawalList(withdrawal);
		return withdrawals;
	}

	@Override
	@RedisLock(value = RedisConstant.LockConstant.XMS_WITHDRAW_CHECK, param = "#withdrawal.id")
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult updateCheckStatusById(Withdrawal withdrawal) {
		if (!withdrawal.getStatus().equals(SysConstant.ONE) && !withdrawal.getStatus().equals(SysConstant.TWO)) {
			return AjaxResult.error("请选择审核状态");
		}
		Withdrawal temp = getById(withdrawal.getId());
		if (!temp.getStatus().equals(SysConstant.ZERO)) {
			return AjaxResult.error("提现记录已被审核,刷新页面后重试");
		}

		boolean res = lambdaUpdate().set(Withdrawal::getStatus, withdrawal.getStatus())
			.set(Withdrawal::getUpdateTime, new Date())
			.eq(Withdrawal::getStatus, SysConstant.ZERO).eq(Withdrawal::getId, withdrawal.getId()).update();
		if (!res) {
			return AjaxResult.error();
		}

		if (withdrawal.getStatus().equals(SysConstant.TWO)) {
			//1.退还提现金额
			int i = userWalletServiceImpl.handerUserMoney(temp.getChangeBalance(), temp.getCode(), temp.getUserId(), temp.getUserId()
				, ConstantType.user_money_log_source_type.type_20, temp.getCoinType());
			if (i != 1) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return AjaxResult.error();
			}

/*			if(temp.getFeeBalance().compareTo(BigDecimal.ZERO)>0){
				//退还提现手续费
				i = userWalletServiceImpl.handerUserMoney(temp.getFeeBalance(), temp.getCode(), temp.getUserId(), temp.getUserId()
					, ConstantType.user_money_log_source_type.type_32, temp.getCoinType());
				if (i != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return AjaxResult.error();
				}
			}*/
		}else if(withdrawal.getStatus().equals(SysConstant.ONE)){
			//发送到合约(内扣)
			String tokenName = "H";
			if(!withdrawal.getCoinType().equals(ConstantType.user_money_coin_type.type_9)){
				tokenName = "ACP";
			}

			Map<String, Object> formParams = new HashMap<>();
			formParams.put("orderNo", temp.getCode());
			formParams.put("address", temp.getAccountNo());
			formParams.put("tokenName", tokenName);
			BigDecimal finalAmount = temp.getChangeBalance().subtract(temp.getFeeBalance());
			formParams.put("amount", finalAmount.stripTrailingZeros().toPlainString());
			String sign = SignUtil.getSign(formParams, false, false, md5Key);
			log.info("提现业务完整参数 param:{},sign:{}", formParams,sign);
			sendWithdrawalRequest(formParams,sign);
		}
		return AjaxResult.success();
	}


	/**
	 * 获取提现合约余额
	 *
	 */
	public BigDecimal getWithdrawalCaBalance() {
		// 改为 GET，并固定查询参数 tokenName=h
		String url = baseUrl + "/api/v1/balance?tokenName=acp";
		HttpRequest request = HttpUtil.createGet(url)
			.timeout(5000); // 设置超时时间（毫秒）
		// 发送请求并获取响应
		HttpResponse response;
		try {
			response = request.execute();
		} catch (IORuntimeException ex) {
			log.error("withdrawal balance request timeout, url:{}", url, ex);
			throw new ServiceException("提现通道请求超时，请稍后重试");
		}

		// 获取响应状态码
		int statusCode = response.getStatus();
		log.info("Status Code:{}", statusCode);

		// 获取响应体
		String responseBody = response.body();
		log.info("responseBody:{}", responseBody);
		JSONObject jsonResponse = JSONUtil.parseObj(response.body());
		Integer code = jsonResponse.getInt("code");
		if (code == null || !code.equals(200)) {
			String errorMsg = jsonResponse.getStr("error");
			if(StrUtil.isNotBlank(errorMsg)){
				throw new ServiceException(errorMsg);
			}
			throw new ServiceException(StrUtil.blankToDefault(jsonResponse.getStr("message"), "获取提现合约余额失败"));
		}
		JSONObject data = jsonResponse.getJSONObject("data");
		if (data == null) {
			throw new ServiceException("获取提现合约余额失败: data为空");
		}
		String balanceStr = data.getStr("balance");
		if (StrUtil.isBlank(balanceStr)) {
			throw new ServiceException("获取提现合约余额失败: balance为空");
		}
		try {
			return new BigDecimal(balanceStr);
		} catch (NumberFormatException ex) {
			throw new ServiceException("获取提现合约余额失败: balance格式错误");
		}
	}
	/**
	 * 发起提现请求
	 *
	 * @param formParams
	 */
	public void sendWithdrawalRequest(Map<String, Object> formParams,String sign) {
		// 使用HttpRequest创建请求对象
		String url = baseUrl + "/api/v1/withdraw";
		HttpRequest request = HttpUtil.createPost(url)
			.body(JSON.toJSONString(formParams)) // JSON 形式传参
			.header("Content-Type", "application/json")
			.header("Custom-Header", "HeaderValue")
			.header("sign", sign)
			.timeout(5000); // 设置超时时间（毫秒）
		// 发送请求并获取响应
		HttpResponse response;
		try {
			response = request.execute();
		} catch (IORuntimeException ex) {
			log.error("withdrawal request timeout, params:{}, url:{}", formParams, url, ex);
			throw new ServiceException("提现通道请求超时，请稍后重试");
		}

		// 获取响应状态码
		int statusCode = response.getStatus();
		log.info("Status Code:{}", statusCode);

		// 获取响应体
		String responseBody = response.body();
		log.info("responseBody:{}", responseBody);
		JSONObject jsonResponse = JSONUtil.parseObj(response.body());
		Integer code = jsonResponse.getInt("code");
		if (code == null || !code.equals(200)) {
			String errorMsg = jsonResponse.getStr("error");
			if(StrUtil.isNotBlank(errorMsg)){
				throw new ServiceException(errorMsg);
			}
			throw new ServiceException(jsonResponse.getStr("message"));
		}
	}

	@Override
	public BigDecimal selectUsdtTotalBalance() {
		return baseMapper.selectUsdtTotalBalance();
	}

	@Override
	public BigDecimal selectBPayTotalBalance() {
		return baseMapper.selectBPayTotalBalance();
	}

	/**
	 * 提现记录
	 * @param pageIndex 当前页 默认1
	 * @param pageSize 每页长度 默认20(最大20)
	 * @return
	 */
	@Override
	public PageInfo<WithdrawalBo> listWithdrawRecord(Integer pageIndex, Integer pageSize,Long userId) {
		PageHelper.startPage(pageIndex, pageSize);

		PageInfo<WithdrawalBo> pageInfo = new PageInfo<WithdrawalBo>(this.baseMapper.listWithdrawRecord(userId));
		return pageInfo;
	}

	public void doBurn( BigDecimal reward, String orderNo) {

		Map<String, Object> formParams = new HashMap<>();
		formParams.put("orderNo", orderNo);
		formParams.put("amount", reward.stripTrailingZeros().toPlainString());
		formParams.put("accountAddress", sysParaServiceImpl.getValue(ConstantSys.biz_sk_address));
		formParams.put("tokenAddress", tokenName);
		formParams.put("sign", SignUtil.getSign(formParams, false, false, md5Key));
		// 不签名mintAmount
		formParams.put("mintAmount", reward.stripTrailingZeros().toPlainString());
		log.info("每日分红手续费业务 param:{}", formParams);
		// 使用HttpRequest创建请求对象
		//String url = baseUrl + "/api/burn";
		String url = baseUrl + "api/common/withdraw";
		HttpRequest request = HttpRequest.post(url)
			.form(formParams) // 设置表单参数
			.header("Custom-Header", "HeaderValue") // 设置自定义请求头
			.timeout(5000); // 设置超时时间（毫秒）
		// 发送请求并获取响应

		HttpResponse response;
		try {
			response = request.execute();
		} catch (IORuntimeException ex) {
			log.error("withdrawal request timeout, params:{}, url:{}", formParams, url, ex);
			throw new ServiceException("分发项目方钱包超时，请稍后重试");
		}

		// 获取响应状态码
		int statusCode = response.getStatus();
		log.info("Status Code:{}", statusCode);

		// 获取响应体
		String responseBody = response.body();
		log.info("responseBody:{}", responseBody);
		JSONObject jsonResponse = JSONUtil.parseObj(response.body());
		Integer code = jsonResponse.getInt("code");
		if (!code.equals(200)) {
			log.error("往项目方打币合约调用失败");
			throw new ServiceException(jsonResponse.getStr("msg"));
		}
	}
}
