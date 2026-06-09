package com.xms.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.xms.app.entity.bo.DestroyCallbackBo;
import com.xms.app.entity.bo.RechargeCallbackBo;
import com.xms.app.entity.dto.RechargeRecordDto;
import com.xms.app.entity.req.CreateRechargeOrder;
import com.xms.app.entity.req.JuNotifyReq;
import com.xms.app.entity.resp.CreateOrderResp;
import com.xms.app.handler.CustomException;
import com.xms.app.service.BizRechargeService;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.ConstantType;
import com.xms.common.constant.RedisConstant;
import com.xms.common.constant.SysConstant;
import com.xms.common.core.domain.BaseEntity;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.*;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.RechargeRecord;
import com.xms.dao.domain.UserRechangeAddress;
import com.xms.dao.entity.bo.UserMoneyBo;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.UserMoney;
import com.xms.dao.service.*;
import com.xms.dao.service.impl.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xms.app.service.impl.BizUserServiceImpl.checkWallet;

@Service
@Slf4j
public class BizRechargeServiceImpl implements BizRechargeService {
	@Autowired
	private IRechargeRecordService rechargeRecordService;

	@Autowired
	private UserWalletService userWalletServiceImpl;

	@Autowired
	private ICoinPriceService iCoinPriceService;

	@Value("${lq.md5Key}")
	private String md5Key;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private XmsRedis xmsRedis;

	@Autowired
	private IUserMoneyService userMoneyService;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addRechargeLog(JuNotifyReq req) {
		//  参数校验
		validateRechargeRequest(req);
		// 用户信息
		UserInfo userInfo = getUserInfoByOpenId(req.getOpenId());
		//  幂等性检查
		checkRechargeExists(req.getOrderId());
		//  处理充值
		processRecharge(userInfo, req);
	}

	private void processRecharge(UserInfo userInfo, JuNotifyReq req) {
		// 处理金额
		BigDecimal amount = new BigDecimal(req.getAmount()).setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		log.info("用户 {} 充值 {} {}，金额: {}", userInfo.getUserId(), amount, req.getCurrency(), amount);
		// 创建充值记录
		RechargeRecord recharge = createRechargeRecord(userInfo.getUserId(), req, amount, ConstantType.user_money_coin_type.type_1);
		// 保存充值记录
		rechargeRecordService.save(recharge);
		//充值加钱
		int count = userWalletServiceImpl.handerUserMoney(amount,
			recharge.getOrderNo(), recharge.getUserId(),
			recharge.getUserId(), ConstantType.user_money_log_source_type.type_25,
			ConstantType.user_money_coin_type.type_1);
		if (count != 1) {
			throw new ServiceException(ResponseCode.CODE_1002);
		}
	}


	private RechargeRecord createRechargeRecord(Long userId, JuNotifyReq req, BigDecimal amount, Integer coinType) {
		RechargeRecord recharge = RechargeRecord.builder().userId(userId).txId(req.getOrderId())
			.orderNo(IDUtils.getSnowflakeStr()).status(1).rechargeAmount(amount).coinType(coinType)
			.remark(JSON.toJSONString(req)).build();
		recharge.setCreateTime(new Date(req.getCreateTime()));
		return recharge;
	}

	private void checkRechargeExists(String orderId) {
		RechargeRecord existingRecharge = rechargeRecordService.lambdaQuery().eq(RechargeRecord::getTxId, orderId).one();
		if (existingRecharge != null) {
			log.warn("充值记录已存在，orderId: {}", orderId);
			throw new ServiceException("充值记录已存在");
		}
	}

	private UserInfo getUserInfoByOpenId(String openId) {
		UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getAccount, openId).one();
		if (userInfo == null) {
			throw new ServiceException("openId不存在: " + openId);
		}
		return userInfo;
	}

	/**
	 * 参数校验
	 */
	private void validateRechargeRequest(JuNotifyReq req) {
		if (req == null || Func.isBlank(req.getOpenId()) || Func.isBlank(req.getOrderId()) || Func.isBlank(req.getCurrency())) {
			throw new ServiceException("充值请求参数不完整");
		}

		// 检查币种是否支持
		if (!isSupportedCurrency(req.getCurrency())) {
			throw new ServiceException("不支持的币种: " + req.getCurrency());
		}
	}

	/**
	 * 检查币种是否支持
	 */
	private boolean isSupportedCurrency(String currency) {
		return "USDT".equalsIgnoreCase(currency);
	}

	/**
	 * 充值回调
	 *
	 * @param req
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista rechargeCallback(DestroyCallbackBo req) {
		log.info("充值订单支付回调 req:{}", req);

		Map<String, Object> map = BeanUtil.beanToMap(req);

		String sign = SignUtil.getSign(map, false, false, md5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}
		if (req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return ResultPista.fail("充值金额不能小于0");
		}
		//设置金额
		req.setAmount(req.getAmount().setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));

		RechargeRecord rechargeRecord = rechargeRecordService.lambdaQuery()
			.eq(RechargeRecord::getTxId, req.getHash())
			.one();
		if (rechargeRecord != null) {
			return ResultPista.data("success");
		}


		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getAccount, req.getAddress())
			.one();

		RechargeRecord rechargeOrder = new RechargeRecord();
		rechargeOrder.setId(rechargeRecord.getId());
		rechargeOrder.setStatus(1);
		if (userInfo != null) {
			rechargeOrder.setUserId(userInfo.getUserId());
		} else {
			rechargeOrder.setUserId(0L);
		}
		rechargeOrder.setOrderNo(IDUtils.getSnowflakeStr());
		rechargeOrder.setRechargeAmount(req.getAmount());
		rechargeOrder.setCoinType(req.getCoinType());
		rechargeOrder.setTxId(req.getHash());
		rechargeOrder.setRemark(req.getAddress());
		rechargeRecordService.save(rechargeOrder);

		if (userInfo != null) {
			//充值加钱
			int count = userWalletServiceImpl.handerUserMoney(rechargeRecord.getRechargeAmount(),
				rechargeRecord.getOrderNo(), rechargeRecord.getUserId(),
				rechargeRecord.getUserId(), ConstantType.user_money_log_source_type.type_1,
				req.getCoinType());
			if (count != 1) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}
		}

		return ResultPista.data("success");
	}

	/**
	 * 创建充值订单
	 *
	 * @param req
	 * @return
	 */
	@Override
	public ResultPista<CreateOrderResp> createOrder(CreateRechargeOrder req) {
		if (req.getRechargeAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ServiceException(ResponseCode.CODE_1002);
		}
		Long userId = SecurityUtils.getFrontUserId();
		//查询用户
		UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getUserId, userId).one();

		//验签，随机数
		checkWallet(req.getRandomNum(), req.getSignature(), userInfo.getAccount(), xmsRedis);

		req.setRechargeAmount(req.getRechargeAmount().setScale(ConstantStatic.twoScale, ConstantStatic.roundingModeNew));
		BigDecimal maiPirce = iCoinPriceService.getMaiPrice();
		String orderNo = IDUtils.getSnowflakeStr();
		RechargeRecord rechargeRecord = new RechargeRecord();
		BigDecimal rechargeValidNum2 = req.getRechargeAmount().divide(maiPirce, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		rechargeRecord.setUserId(userId);
		rechargeRecord.setOrderNo(orderNo);
		rechargeRecord.setRechargeAmount(req.getRechargeAmount());
		//rechargeRecord.setRechargeValidNum2(rechargeValidNum2);
		rechargeRecord.setStatus(0);
		rechargeRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
		rechargeRecord.setCreateTime(new Date());
		boolean save = rechargeRecordService.save(rechargeRecord);
		if (!save) {
			throw new ServiceException(ResponseCode.CODE_1002);
		}
		CreateOrderResp resp = new CreateOrderResp();
		resp.setOrderNo(orderNo);
		resp.setUsdtValue(req.getRechargeAmount());
		resp.setValidNum1Value(rechargeValidNum2);
		return ResultPista.data(resp);
	}

	/**
	 * 充值记录
	 *
	 * @param lastId
	 * @return
	 */
	@Override
	public ResultPista<List<RechargeRecordDto>> listRechargeRecord(Long lastId) {
		List<RechargeRecordDto> list = rechargeRecordService.lambdaQuery()
			.eq(RechargeRecord::getUserId, SecurityUtils.getFrontUserId())
			.eq(RechargeRecord::getStatus, 1)
			.lt(Func.isNotEmpty(lastId), RechargeRecord::getId, lastId)
			.orderByDesc(RechargeRecord::getId).last(SysConstant.PAGE_LIMIT)
			.select(BaseEntity::getCreateTime, RechargeRecord::getId,
				RechargeRecord::getUserId, RechargeRecord::getOrderNo, RechargeRecord::getRechargeAmount,
				RechargeRecord::getCoinType, RechargeRecord::getStatus, RechargeRecord::getTxId)
			.list().stream().map(item->{
				RechargeRecordDto dto = new RechargeRecordDto();
				dto.setId(item.getId());
				dto.setUserId(item.getUserId());
				dto.setOrderNo(item.getOrderNo());
				dto.setRechargeAmount(item.getRechargeAmount());
				dto.setCoinType(item.getCoinType());
				dto.setTxId(item.getTxId());
				dto.setRemark(item.getRemark());
				return dto;
			}).collect(Collectors.toList());
		return ResultPista.data(list);
	}

//	static void checkWallet(String randomNum, String signature, String address, XmsRedis xmsRedis) {
//		String osName = SystemUtil.getOsInfo().getName();
//		if (osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
//			return;
//		}
//		if (!xmsRedis.hasKey(ConstantStatic.USER_RANDOM + address + randomNum)) {
//			throw new CustomException(ResponseCode.RANDOM_NOT_EXIT);
//		}
//		boolean validate = MetaMaskUtil.validate(signature, randomNum, address);
//		if (!validate) {
//			throw new CustomException(ResponseCode.SIGN_VALIDATE_ERROR);
//		}
//		xmsRedis.del(ConstantStatic.USER_RANDOM + address + randomNum);
//	}
}
