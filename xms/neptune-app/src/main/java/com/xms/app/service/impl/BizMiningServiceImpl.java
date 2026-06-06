package com.xms.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xms.app.entity.TotalEarningsDto;
import com.xms.app.entity.bo.DestroyCallbackBo;
import com.xms.app.entity.bo.DestroyInfoBo;
import com.xms.app.entity.req.NodePackageReq;
import com.xms.app.entity.req.SwapOrderCallbackReq;
import com.xms.common.mq.dynamic.OrderMsgDO;
import com.xms.dao.entity.dto.DestroyOrderDto;
import com.xms.dao.entity.dto.InterestPackDto;
import com.xms.dao.entity.dto.InterestStatDayDto;
import com.xms.app.entity.dto.ReleaseConfigDto;
import com.xms.app.entity.dto.mining.PackageOrderDto;
import com.xms.app.entity.req.ReleaseOrderReq;
import com.xms.app.entity.resp.CreateOrderResp;
import com.xms.common.constant.ConstantType;
import com.xms.app.entity.req.RedeemVo;
import com.xms.app.entity.vo.*;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.app.service.BizMiningService;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.config.redis.delayqueue.RedissonDelayHandler;
import com.xms.common.config.redis.delayqueue.RedissonDelayOrder;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.*;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.Func;
import com.xms.common.utils.SecurityUtils;
import com.xms.common.utils.SignUtil;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.*;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.UserMoney;
import com.xms.dao.service.*;
import com.xms.dao.service.impl.MiningPackageOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BizMiningServiceImpl implements BizMiningService {
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private IW3MiningPackageService w3MiningPackageService;

	@Autowired
	private AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementServiceImpl;

	@Autowired
	private IW3UserLevelConfigService w3UserLevelConfigService;

	@Autowired
	private RedissonDelayHandler redissonDelayHandler;

	@Autowired
	private IDestroyOrderService destroyOrderService;

	@Value("${lq.md5Key}")
	private String md5Key;

	@Value("${lq.tokenName}")
	private String tokenName;

	@Value("${lq.baseUrl}")
	private String baseUrl;

	@Autowired
	private ICoinPriceService iCoinPriceService;

	@Autowired
	private IIdoOrderService iIdoOrderService;

	@Autowired
	private ISwapOrderService iSwapOrderService;

	/**
	 * 返回相差几秒，如果当前时间晚于结束时间则返回固定的10秒
	 *
	 * @param current 当前时间
	 * @param endTime 结束时间
	 * @return 相差的秒数
	 */
	public static Long getEndTime(Date current, Date endTime) {
		if (current == null || endTime == null) {
			throw new IllegalArgumentException("时间参数不能为空");
		}

		if (current.after(endTime)) {
			return 10L; // 当前时间晚于结束时间时返回10秒
		} else {
			// 计算时间差（毫秒）
			long diffMillis = endTime.getTime() - current.getTime();
			// 转换为秒
			return diffMillis / 1000;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<String> nodePackageCallback(NodePackageReq req) {

		Map<String, Object> map = BeanUtil.beanToMap(req);

		String sign = SignUtil.getSign(map, false, false, md5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}

		//查询是否存在了
		return ResultPista.data("success");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<String> swapOrderCallback(SwapOrderCallbackReq req) {

		Map<String, Object> map = BeanUtil.beanToMap(req);
		String sign = SignUtil.getSign(map, false, false, md5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}
		SwapOrder swapOrder = new SwapOrder();
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getAccount, req.getAddress())
			.select(UserInfo::getUserId)
			.one();
		if(userInfo == null){
			swapOrder.setUserId(0L);
			swapOrder.setBizStatus(1);
		}else{
			swapOrder.setUserId(userInfo.getUserId());
			swapOrder.setBizStatus(2);
		}
		swapOrder.setBizStatus1(0);
		swapOrder.setAddress(req.getAddress());
		swapOrder.setTxHash(req.getHash());
		swapOrder.setCreateTime(new Date());
		//当前时间加多少秒
		Integer expireTime = Integer.valueOf(sysParaServiceImpl.getValue(ConstantSys.biz_swap_order_expire_time));
		swapOrder.setExpireTime(new Date());swapOrder.setExpireTime(DateUtil.offsetSecond(new Date(), expireTime));
		swapOrder.setSwapAmount(req.getSwapAmount());
		BigDecimal effectiveRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_swap_order_effective_ratio))
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		//设置有效额度
		swapOrder.setAvailableAmount(req.getSwapAmount().multiply(effectiveRatio)
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		boolean save = iSwapOrderService.save(swapOrder);
		if(!save){
			return ResultPista.fail("保存失败");
		}
		//计算延迟队列
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				//默认24小时后加提现额度
				Long targetTime = BizMiningServiceImpl.getEndTime(swapOrder.getCreateTime(), swapOrder.getExpireTime());
				//发送消息到延迟队列
				redissonDelayHandler.add(new RedissonDelayOrder(swapOrder.getId().toString(), targetTime, SysConstant.THREE,
					null, RedisConstant.StreamMsgConstant.DELAY_ORDER_TIMEOUT_QUEUE));
			}
		});
		return ResultPista.data("success");
	}

	private void doBurn(String account, BigDecimal reward, String orderNo) {
		Map<String, Object> formParams = new HashMap<>();
		formParams.put("orderNo", orderNo);
		formParams.put("amount", reward.stripTrailingZeros().toPlainString());
		formParams.put("accountAddress", account);
		formParams.put("tokenAddress", tokenName);
		formParams.put("sign", SignUtil.getSign(formParams, false, false, md5Key));

		// 使用HttpRequest创建请求对象
		String url = baseUrl + "/api/burn";
		//String url = baseUrl + "/api/mint";
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
		if (code.equals(200)) {
			//200才是成功
		}
	}

	private int afterOrderTask(Long orderId) {
		DestroyOrder destroyOrder = destroyOrderService.lambdaQuery()
			.eq(DestroyOrder::getId, orderId)
			.eq(DestroyOrder::getPayStatus,1)
			.eq(DestroyOrder::getBizStatus,0)
			.one();
		if(destroyOrder == null){
			return 1;
		}
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, destroyOrder.getUserId())
			.one();
		if (StrUtil.isNotBlank(userInfo.getParentChain())) {
			List<UserInfo> parentUserList = userInfoService.getParentUserList(userInfo.getUserId());
			//把自己放进去校验是否降级
			parentUserList.addFirst(userInfo);
			//查询配置
			List<W3UserLevelConfig> userLevelConfigList = w3UserLevelConfigService.lambdaQuery()
				.orderByAsc(W3UserLevelConfig::getLevel)
				.list();
			for (UserInfo parentUser : parentUserList) {
				List<UserInfo> directPushList = userInfoService.lambdaQuery()
					.eq(UserInfo::getInviteUserId, parentUser.getUserId())
					.list();

				//查询伞下
				Map<Long,List<UserInfo>> directPushMap = new HashMap<>(directPushList.size());
				for (UserInfo directUserInfo : directPushList) {
					List<UserInfo> childUserList = userInfoService.getChildUserList(directUserInfo.getUserId());
					childUserList.add(directUserInfo);
					directPushMap.put(directUserInfo.getUserId(), childUserList);
				}

				Integer initGameLevel = 0;
				for (W3UserLevelConfig w3UserLevelConfig : userLevelConfigList) {
					if(parentUser.getUmbrellaPerformance().compareTo(w3UserLevelConfig.getUmbrellaPerformance())>=0){
						if(w3UserLevelConfig.getIsUmbrellaLevel()==1){
							if(directPushList.size()<w3UserLevelConfig.getUmbrellaCount()){
								//不满足几条线几个lv用户
								break;
							}else{
								//查看伞下是否有满足
								//遍历directPushMap
								Integer umbrellaCount = 0;
								for (List<UserInfo> userList : directPushMap.values()) {
									for(UserInfo user : userList){
										if(user.getGameLevel()>=w3UserLevelConfig.getUmbrellaLevel()){
											umbrellaCount++;
											break;
										}
									}
								}
								if(umbrellaCount>=w3UserLevelConfig.getUmbrellaCount()){
									initGameLevel = w3UserLevelConfig.getLevel();
								}else{
									break;
								}
							}
						}else{
							initGameLevel = w3UserLevelConfig.getLevel();
						}
					}else{
						break;
					}
				}

				if(initGameLevel == parentUser.getGameLevel()){
					//不处理
				}else if(initGameLevel<parentUser.getGameLevel()){
					//降级
					//直接降级
					userInfoService.lambdaUpdate()
						.eq(UserInfo::getUserId, parentUser.getUserId())
						.set(UserInfo::getGameLevel, initGameLevel)
						.update();
				}else if (initGameLevel>parentUser.getGameLevel()){
					//直接升级
					userInfoService.lambdaUpdate()
						.eq(UserInfo::getUserId, parentUser.getUserId())
						.set(UserInfo::getGameLevel, initGameLevel)
						.update();
				}
			}
		}
		//更新订单为已经处理过了状态
		boolean update = destroyOrderService.lambdaUpdate()
			.eq(DestroyOrder::getId, destroyOrder.getId())
			.eq(DestroyOrder::getBizStatus, 0)
			.set(DestroyOrder::getBizStatus, 1)
			.set(DestroyOrder::getUpdateTime, new Date())
			.update();
		if(!update){
			throw new ServiceException("订单已经杯处理了");
		}
		return 1;
	}

	/**
	 * 获取币的价格 1:BOOMMAI,2:MAI
	 * @param coinType
	 * @return
	 */
	public BigDecimal getCoinPrice(Integer coinType){
		return iCoinPriceService.lambdaQuery()
			.eq(CoinPrice::getCoinType, coinType)
			.last("limit 1")
			.one().getCurrentPrice();
	}

	/**
	 * 计算订单最大可释放金额（不超过本金）
	 *
	 * @param principal 本金
	 * @param dailyRate 日利率
	 * @param runDays   订单实际运行天数
	 * @return 最大可释放金额
	 */
	public BigDecimal calculateMaxReleasable(BigDecimal principal,
											 BigDecimal dailyRate,
											 int runDays) {
		if (runDays <= 0) {
			return BigDecimal.ZERO;
		}

		// 每日收益
		BigDecimal dailyReward = principal.multiply(dailyRate);

		// 理论最大可释放 = 运行天数 × 每日收益
		BigDecimal theoreticalMax = dailyReward.multiply(new BigDecimal(runDays));

		// 如果超过本金，返回本金；否则返回理论值
		return theoreticalMax.compareTo(principal) > 0 ? principal : theoreticalMax;
	}

	@Override
	public ResultPista<List<W3MiningPackageVo>> list(Integer type) {
		List<W3MiningPackage> miningPackages = w3MiningPackageService.lambdaQuery()
			//.eq(W3MiningPackage::getStatus, SysConstant.ONE)
			.eq(W3MiningPackage::getType, type)
			.orderByAsc(W3MiningPackage::getSort)
			.list();
		if (CollectionUtil.isEmpty(miningPackages)) {
			return ResultPista.data(new ArrayList<>());
		}
		List<W3MiningPackageVo> result = new ArrayList<>(miningPackages.size());
		for (W3MiningPackage miningPackage : miningPackages) {
			W3MiningPackageVo vo = new W3MiningPackageVo();
			BeanUtil.copyProperties(miningPackage, vo);
			result.add(vo);
		}
		return ResultPista.data(result);
	}

}
