package com.xms.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.system.SystemUtil;
import com.xms.app.entity.bo.BuyPointsCallbackBo;
import com.xms.app.entity.bo.OldHToAcpDepositCallbackBo;
import com.xms.app.entity.bo.StakeOrderCallbackBo;
import com.xms.app.entity.req.HBalanceAcpDepositReq;
import com.xms.app.service.BizStakeService;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.*;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.domain.AcpHPriceSnapshot;
import com.xms.common.exception.ServiceException;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.common.mq.dynamic.OrderMsgDO;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.SecurityUtils;
import com.xms.common.utils.SignUtil;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.BuyHOrder;
import com.xms.dao.domain.OldHToAcpDepositRecord;
import com.xms.dao.domain.StakeOrder;
import com.xms.dao.domain.StakeRound;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 质押业务实现类
 *
 * @author xms
 * @date 2023/04/05
 */
@Slf4j
@Service
public class BizStakeServiceImpl implements BizStakeService {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementServiceImpl;

	@Autowired
	private IStakeOrderService stakeOrderServiceImpl;

	@Autowired
	private IStakeRoundService stakeRoundServiceImpl;

	@Autowired
	private IBuyHOrderService buyHOrderServiceImpl;

	@Autowired
	private IUserLevelConfigService userLevelConfigServiceImpl;

	@Autowired
	private UserWalletService userWalletServiceImpl;

	@Autowired
	private IStakeRoundLiquidationLogService stakeRoundLiquidationLogServiceImpl;

	@Autowired
	private IHGiftReleaseBucketService hGiftReleaseBucketService;

	@Autowired
	private IOldHToAcpDepositRecordService oldHToAcpDepositRecordService;

	@Autowired
	private XmsTokenPriceService xmsTokenPriceService;

	@Value("${lq.md5Key}")
	private String md5Key;

	@Value("${lq.newMd5Key}")
	private String newMd5Key;

	/**
	 * 用户H余额换ACP入金，并将换算后的ACP数量同时记入总业绩和真实业绩。
	 *
	 * <p>钱包扣减、订单创建和后置结算沿用原流程；当前用户增加真实个人业绩，全部父级增加真实团队业绩。</p>
	 *
	 * @param req H入金数量请求，单位H
	 * @return 质押订单号
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisLock(value = RedisConstant.LockConstant.XMS_STAKE_ORDER_PLAN)
	public ResultPista<String> hBalanceToAcpDeposit(HBalanceAcpDepositReq req) {
		Long userId = SecurityUtils.getLoginAppUser().getUserId();
		BigDecimal hAmount = req.getAmount().setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		if (hAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ServiceException(ResponseCode.CODE_115);
		}

		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, userId)
			.one();
		if (userInfo == null) {
			throw new ServiceException(ResponseCode.CODE_1007);
		}

		AcpHPriceSnapshot priceSnapshot = xmsTokenPriceService.getAcpHPriceSnapshot();
		BigDecimal hUsdtAmount = hAmount.multiply(priceSnapshot.getHPriceUsdt())
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal acpDepositAmount = calculateAcpDepositAmount(hUsdtAmount, priceSnapshot.getAcpPriceUsdt());
		BigDecimal minStakeAmount = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_h_deposit_min_amount));
		if (hAmount.compareTo(minStakeAmount) < 0) {
			throw new ServiceException(ResponseCode.CODE_1255);
		}

		//进行中的轮次
		StakeRound stakeRound = stakeRoundServiceImpl.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.last("for update")
			.one();
		if (stakeRound == null) {
			throw new ServiceException(ResponseCode.CODE_1260);
		}

		String orderNo = IDUtils.getSnowflakeStr();
		int deductCount = userWalletServiceImpl.handerUserMoney(hAmount.negate(), orderNo, userId, userId,
			ConstantType.user_money_log_source_type.type_34, ConstantType.user_money_coin_type.type_9);
		if (deductCount != 1) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}

		boolean update = userInfoService.lambdaUpdate()
			.eq(UserInfo::getUserId, userInfo.getUserId())
			.set(UserInfo::getIsValid, 1)
			.setSql("performance = performance + " + acpDepositAmount)
			.setSql("history_performance = history_performance + " + acpDepositAmount)
			.setSql("real_performance = real_performance + " + acpDepositAmount)
			.update();
		if (!update) {
			throw new ServiceException(ResponseCode.CODE_1002);
		}

		Long belongUserId = 0L;
		boolean flag = true;
		if (userInfo.getInviteUserId() != null) {
			update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getInviteUserId())
				.setSql("sub_performance = sub_performance + " + acpDepositAmount)
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}
			update = userInfoService.lambdaUpdate()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.setSql("umbrella_performance = umbrella_performance + " + acpDepositAmount)
				.setSql("real_umbrella_performance = real_umbrella_performance + " + acpDepositAmount)
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}

			List<UserInfo> parentUserInfoList = userInfoService.lambdaQuery()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.orderByDesc(UserInfo::getUserId)
				.list().stream().peek(item -> {
					item.setGameLevel(Math.max(item.getGameLevel(), item.getMinGameLevel()));
				})
				.collect(Collectors.toList());

			for (UserInfo info : parentUserInfoList) {
				if (flag && info.getGameLevel() >= 1) {
					belongUserId = info.getUserId();
					flag = false;
				}
			}
		}

		//只有真实acp质押才会增加player_stake_total
/*		boolean updateRound = stakeRoundServiceImpl.lambdaUpdate()
			.eq(StakeRound::getId, stakeRound.getId())
			.setSql("player_stake_total = player_stake_total + " + acpDepositAmount)
			.update();
		if (!updateRound) {
			log.error("更新轮次质押总量失败");
			throw new ServiceException(ResponseCode.CODE_1002);
		}*/

		BigDecimal giftRatioSnapshot = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_acp_h_gift_ratio))
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		StakeOrder stakeOrder = new StakeOrder();
		stakeOrder.setOrderNo(orderNo);
		stakeOrder.setStakeAmount(acpDepositAmount);
		stakeOrder.setOldHAmount(hAmount);
		stakeOrder.setDepositSourceType(ConstantType.stake_order_deposit_source_type.type_4);
		fillAcpDepositStakeSnapshot(stakeOrder, priceSnapshot, hUsdtAmount, giftRatioSnapshot);
		stakeOrder.setUserId(userId);
		stakeOrder.setStakeRoundId(stakeRound.getId());
		stakeOrder.setStatus(1);
		stakeOrder.setCreateDay(Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
		stakeOrder.setBelongUserId(belongUserId);
		stakeOrder.setBizStatus(0);
		stakeOrder.setTxHash(orderNo);
		stakeOrder.setCreateTime(new Date());
		boolean save = stakeOrderServiceImpl.save(stakeOrder);
		if (!save) {
			log.error("保存质押订单失败");
			throw new ServiceException(ResponseCode.CODE_1002);
		}

		//不赠送
//		if (stakeOrder.getGiftHAmount().compareTo(BigDecimal.ZERO) > 0) {
//			hGiftReleaseBucketService.createWalletHToAcpDepositBucket(userInfo.getUserId(), userInfo.getAccount(),
//				stakeOrder.getOrderNo(), stakeOrder.getGiftHAmount());
//		}
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
				OrderMsgDO orderMsgDO = new OrderMsgDO();
				orderMsgDO.setId(stakeOrder.getId());
				orderMsgDO.setBizType(1);
				orderMsgDOList.add(orderMsgDO);
				asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);
			}
		});
		return ResultPista.data(orderNo);
	}


	/**
	 * 处理旧系统H换ACP入金回调，并将换算后的ACP数量同时记入总业绩和映射业绩。
	 *
	 * <p>当前用户增加映射个人业绩，全部父级增加映射团队业绩；订单、赠送H和后置结算逻辑保持不变。</p>
	 *
	 * @param req 旧系统H入金回调参数，amount单位H
	 * @return 回调处理结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<String> oldHToAcpDepositCallback(OldHToAcpDepositCallbackBo req) {

		log.info("旧系统H换ACP入金回调事件 req:{}", req);
		Map<String, Object> map = BeanUtil.beanToMap(req);

		String sign = SignUtil.getSign(map, false, false, newMd5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}


		req.setAmount(req.getAmount().setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		BigDecimal oldHAmount = req.getAmount();
		if (oldHAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ServiceException(ResponseCode.CODE_1258);
		}
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getAccount, req.getAddress())
			.one();
		if(userInfo == null){
			throw new ServiceException(ResponseCode.CODE_1007);
		}

		//旧系统回调没有链上hash，tx_hash 字段存旧系统订单号/请求单号用于幂等。
		Long count = stakeOrderServiceImpl.lambdaQuery()
			.eq(StakeOrder::getTxHash, req.getOrderNo())
			.count();
		if (count > 0) {
			log.info("订单已存在");
			return ResultPista.data("success");
		}

		//进行中的轮次
		StakeRound stakeRound = stakeRoundServiceImpl.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.last("for update")
			.one();

		if(stakeRound == null){
			throw new ServiceException(ResponseCode.CODE_1260);
		}

		AcpHPriceSnapshot priceSnapshot = xmsTokenPriceService.getGateAcpHPriceSnapshot();
		//入金赠送比例
		BigDecimal giftRatioSnapshot = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_acp_h_gift_ratio))
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		//价值多少usdt
		BigDecimal oldHUsdtAmount = oldHAmount.multiply(priceSnapshot.getHPriceUsdt())
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal acpDepositAmount = calculateAcpDepositAmount(oldHUsdtAmount, priceSnapshot.getAcpPriceUsdt());

		Long userId = 0L;
		Integer bizStatus = 0;
		//status是有效订单,如果status=2说明要么用户没有注册.要么该笔订单小于最低质押量
		Integer status = 1;
		if (userInfo == null) {
			status = 2;
		} else {
			userId = userInfo.getUserId();
		}


		if (userInfo != null && status == 1) {
			//个人业绩
			boolean update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getUserId())
				.set(UserInfo::getIsValid, 1)
				.setSql("performance = performance + " + acpDepositAmount)
				.setSql("history_performance = history_performance + " + acpDepositAmount)
				.setSql("mapping_performance = mapping_performance + " + acpDepositAmount)
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}
		}

		//业绩归属上级用户id
		Long belongUserId = 0L;
		boolean flag =true;
		if (status == 1 && userInfo != null && userInfo.getInviteUserId() != null) {
			//加直推业绩
			boolean update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getInviteUserId())
				.setSql("sub_performance = sub_performance + " + acpDepositAmount)
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}
			//团队业绩
			update = userInfoService.lambdaUpdate()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.setSql("umbrella_performance = umbrella_performance + " + acpDepositAmount)
				.setSql("mapping_umbrella_performance = mapping_umbrella_performance + " + acpDepositAmount)
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}

			//找到该业绩归属于谁
			List<UserInfo> parentUserInfoList = userInfoService.lambdaQuery()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.orderByDesc(UserInfo::getUserId)
				.list().stream().peek(item -> {
					item.setGameLevel(Math.max(item.getGameLevel(), item.getMinGameLevel()));
				})
				.collect(Collectors.toList());

			for (UserInfo info : parentUserInfoList) {
				if (flag && info.getGameLevel() >= 1) {
					belongUserId = info.getUserId();
					flag = false;
				}
			}
		}

		//只有真实acp质押才会增加player_stake_total
//		boolean update1 = stakeRoundServiceImpl.lambdaUpdate()
//			.eq(StakeRound::getId, stakeRound.getId())
//			.setSql("player_stake_total = player_stake_total + " + acpDepositAmount)
//			.update();
//		if (!update1) {
//			log.error("更新轮次质押总量失败");
//			throw new ServiceException(ResponseCode.CODE_1002);
//		}

		String orderNo = IDUtils.getSnowflakeStr();
		StakeOrder stakeOrder = new StakeOrder();
		stakeOrder.setOrderNo(orderNo);
		stakeOrder.setStakeAmount(acpDepositAmount);
		stakeOrder.setOldHAmount(oldHAmount);
		stakeOrder.setDepositSourceType(ConstantType.stake_order_deposit_source_type.type_3);
		fillAcpDepositStakeSnapshot(stakeOrder, priceSnapshot, oldHUsdtAmount, giftRatioSnapshot);
		stakeOrder.setUserId(userId);
		stakeOrder.setStakeRoundId(stakeRound.getId());
		stakeOrder.setStatus(status);
		stakeOrder.setCreateDay(Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
		stakeOrder.setBelongUserId(belongUserId);
		stakeOrder.setBizStatus(bizStatus);
		stakeOrder.setTxHash(req.getOrderNo());
		stakeOrder.setCreateTime(new Date());
		boolean save = stakeOrderServiceImpl.save(stakeOrder);
		if (!save) {
			log.error("保存质押订单失败");
			throw new ServiceException(ResponseCode.CODE_1002);
		}

		//旧系统入金不会增对应的30%h代币
		saveOldHToAcpDepositRecord(req, userInfo, oldHAmount, oldHUsdtAmount, acpDepositAmount, priceSnapshot, stakeOrder);

		if (status == 1) {
//			if (stakeOrder.getGiftHAmount().compareTo(BigDecimal.ZERO) > 0) {
//				hGiftReleaseBucketService.createOldHToAcpDepositBucket(userInfo.getUserId(), userInfo.getAccount(),
//					stakeOrder.getOrderNo(), stakeOrder.getGiftHAmount());
//			}
			//需要处理质押之后的任务
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
					OrderMsgDO orderMsgDO = new OrderMsgDO();
					orderMsgDO.setId(stakeOrder.getId());
					orderMsgDO.setBizType(1);
					orderMsgDOList.add(orderMsgDO);
					asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);
				}
			});
		}
		return ResultPista.success();
	}

	/**
	 * 保存旧系统H换ACP入金回调审计记录。
	 */
	private void saveOldHToAcpDepositRecord(OldHToAcpDepositCallbackBo req, UserInfo userInfo, BigDecimal oldHAmount,
											BigDecimal oldHUsdtAmount, BigDecimal acpDepositAmount,
											AcpHPriceSnapshot priceSnapshot, StakeOrder stakeOrder) {
		Date now = new Date();
		OldHToAcpDepositRecord record = new OldHToAcpDepositRecord();
		record.setRecordNo(IDUtils.getSnowflakeStr());
		record.setOldOrderNo(req.getOrderNo());
		record.setUserId(userInfo.getUserId());
		record.setAccount(userInfo.getAccount());
		record.setOldHAmount(oldHAmount);
		record.setHPriceUsdtSnapshot(priceSnapshot.getHPriceUsdt());
		record.setOldHUsdtAmount(oldHUsdtAmount);
		record.setAcpPriceUsdtSnapshot(priceSnapshot.getAcpPriceUsdt());
		record.setAcpDepositAmount(acpDepositAmount);
		record.setStakeOrderId(stakeOrder.getId());
		record.setStakeOrderNo(stakeOrder.getOrderNo());
		record.setDepositSourceType(ConstantType.stake_order_deposit_source_type.type_3);
		record.setStatus(1);
		record.setCreateTime(now);
		record.setUpdateTime(now);
		boolean save = oldHToAcpDepositRecordService.save(record);
		if (!save) {
			throw new ServiceException("保存旧系统H换ACP入金记录失败");
		}
	}

	/**
	 * 购买积分回调
	 *
	 * @param req
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<String> buyPointsCallback(BuyPointsCallbackBo req) {
		log.info("购买积分回调事件 req:{}", req);
		Map<String, Object> map = BeanUtil.beanToMap(req);

		String sign = SignUtil.getSign(map, false, false, md5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}
		req.setAmount(req.getAmount().setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		if (req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ServiceException("花费h代币数量不能小于等于0");
		}

		Long count = buyHOrderServiceImpl.lambdaQuery()
			.eq(BuyHOrder::getPayHash, req.getHash())
			.count();
		if (count > 0) {
			log.info("订单已存在");
			return ResultPista.data("success");
		}
		BuyHOrder hOrder = buyHOrderServiceImpl.lambdaQuery()
			.eq(BuyHOrder::getOrderNo, req.getOrderNo())
			.eq(BuyHOrder::getStatus, 0)
			.one();
		if (hOrder == null) {
			log.info("订单不存在");
			return ResultPista.data("success");
		}

		boolean update = buyHOrderServiceImpl.lambdaUpdate()
			.eq(BuyHOrder::getId, hOrder.getId())
			.eq(BuyHOrder::getStatus, 0)
			.set(BuyHOrder::getStatus, 1)
			.set(BuyHOrder::getPayHash, req.getHash())
			.set(BuyHOrder::getUpdateTime, new Date())
			.update();
		if (!update) {
			throw new ServiceException(ResponseCode.CODE_1002);
		}

		Integer count1 = userWalletServiceImpl.handerUserMoney(hOrder.getPointsAmount(), hOrder.getOrderNo(),
			hOrder.getUserId(), hOrder.getUserId(), ConstantType.user_money_log_source_type.type_10,
			ConstantType.user_money_coin_type.type_7);
		if (count1 != 1) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}

		stakeRoundServiceImpl.lambdaUpdate()
			.eq(StakeRound::getId,hOrder.getStakeRoundId())
			.setSql("buy_point_total = buy_point_total + " + hOrder.getPayHAmount())
			.update();

		//爆仓检测
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				sendLiquidationCheckMessageIfEnabled(hOrder.getStakeRoundId(), "购买贡献分");
			}
		});


		return ResultPista.success();
	}

	/**
	 * 当前轮次开启爆仓检测时投递 bizType=2 消息。
	 *
	 * <p>该方法在主事务提交后调用，会重新读取最新轮次开关；如果管理员已关闭开关，则跳过投递，
	 * 保证关闭期间不检测、不爆仓。</p>
	 *
	 * @param stakeRoundId 需要检测的质押轮次ID
	 * @param scene 触发场景，用于日志定位
	 */
	private void sendLiquidationCheckMessageIfEnabled(Long stakeRoundId, String scene) {
		StakeRound stakeRound = stakeRoundServiceImpl.lambdaQuery()
			.eq(StakeRound::getId, stakeRoundId)
			.eq(StakeRound::getStatus, 0)
			.select(StakeRound::getId, StakeRound::getStatus, StakeRound::getLiquidationCheckEnabled)
			.one();
		if (stakeRound == null) {
			log.info("跳过爆仓检测投递，轮次不存在或已非进行中 scene:{} stakeRoundId:{}", scene, stakeRoundId);
			return;
		}
		if (!Integer.valueOf(1).equals(stakeRound.getLiquidationCheckEnabled())) {
			log.info("跳过爆仓检测投递，轮次爆仓检测开关关闭 scene:{} stakeRoundId:{}", scene, stakeRoundId);
			return;
		}

		List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
		OrderMsgDO orderMsgDO = new OrderMsgDO();
		orderMsgDO.setId(stakeRoundId);
		orderMsgDO.setBizType(2);
		orderMsgDOList.add(orderMsgDO);
		asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);
	}

	/**
	 * 处理正常ACP链上入金回调，并将成功入金数量同时记入总业绩和真实业绩。
	 *
	 * <p>当前用户增加真实个人业绩，全部父级增加真实团队业绩；无效或低于最低数量的订单不计业绩。</p>
	 *
	 * @param req ACP入金回调参数，amount单位ACP
	 * @return 回调处理结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisLock(value = RedisConstant.LockConstant.XMS_STAKE_ORDER_PLAN)
	public ResultPista<String> stakeOrderCallback(StakeOrderCallbackBo req) {
		log.info("质押订单回调事件 req:{}", req);
		Map<String, Object> map = BeanUtil.beanToMap(req);

		String sign = SignUtil.getSign(map, false, false, md5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}


		req.setAmount(req.getAmount().setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getAccount, req.getAddress())
			.one();
		Long count = stakeOrderServiceImpl.lambdaQuery()
			.eq(StakeOrder::getTxHash, req.getHash())
			.count();
		if (count > 0) {
			log.info("订单已存在");
			return ResultPista.data("success");
		}

		//进行中的轮次
		StakeRound stakeRound = stakeRoundServiceImpl.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.last("for update")
			.one();

		if(stakeRound == null){
			throw new ServiceException(ResponseCode.CODE_1260);
		}

		Long userId = 0L;
		Integer bizStatus = 0;
		//status是有效订单,如果status=2说明要么用户没有注册.要么该笔订单小于最低质押量
		Integer status = 1;
		if (userInfo == null) {
			status = 2;
		} else {
			userId = userInfo.getUserId();
		}
		BigDecimal minStakeAmount = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_min_stake_amount));
		if (req.getAmount().compareTo(minStakeAmount) < 0) {
			status = 2;
		}

		if (userInfo != null && status == 1) {
			//个人业绩
			boolean update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getUserId())
				.set(UserInfo::getIsValid, 1)
				.setSql("performance = performance + " + req.getAmount())
				.setSql("history_performance = history_performance + " + req.getAmount())
				.setSql("real_performance = real_performance + " + req.getAmount())
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}
		}

		//业绩归属上级用户id
		Long belongUserId = 0L;
		boolean flag =true;
		if (status == 1 && userInfo != null && userInfo.getInviteUserId() != null) {
			//加直推业绩
			boolean update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getInviteUserId())
				.setSql("sub_performance = sub_performance + " + req.getAmount())
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}
			//团队业绩
			update = userInfoService.lambdaUpdate()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.setSql("umbrella_performance = umbrella_performance + " + req.getAmount())
				.setSql("real_umbrella_performance = real_umbrella_performance + " + req.getAmount())
				.update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}

			//找到该业绩归属于谁
			List<UserInfo> parentUserInfoList = userInfoService.lambdaQuery()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.orderByDesc(UserInfo::getUserId)
				.list().stream().peek(item -> {
					item.setGameLevel(Math.max(item.getGameLevel(), item.getMinGameLevel()));
				})
				.collect(Collectors.toList());

			for (UserInfo info : parentUserInfoList) {
				if (flag && info.getGameLevel() >= 1) {
					belongUserId = info.getUserId();
					flag = false;
				}
			}
		}

		//更新本轮的质押量
		boolean update1 = stakeRoundServiceImpl.lambdaUpdate()
			.eq(StakeRound::getId, stakeRound.getId())
			.setSql("player_stake_total = player_stake_total + " + req.getAmount())
			.update();
		if (!update1) {
			log.error("更新轮次质押总量失败");
			return ResultPista.fail("更新轮次质押总量失败");
		}

		String orderNo = IDUtils.getSnowflakeStr();
		AcpHPriceSnapshot priceSnapshot = xmsTokenPriceService.getAcpHPriceSnapshot();
		BigDecimal giftRatioSnapshot = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_acp_h_gift_ratio))
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal depositUsdtAmount = req.getAmount().multiply(priceSnapshot.getAcpPriceUsdt())
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);

		StakeOrder stakeOrder = new StakeOrder();
		stakeOrder.setOrderNo(orderNo);
		stakeOrder.setStakeAmount(req.getAmount());
		stakeOrder.setOldHAmount(BigDecimal.ZERO);
		stakeOrder.setDepositSourceType(ConstantType.stake_order_deposit_source_type.type_1);
		fillAcpDepositStakeSnapshot(stakeOrder, priceSnapshot, depositUsdtAmount, giftRatioSnapshot);
		stakeOrder.setUserId(userId);
		stakeOrder.setStakeRoundId(stakeRound.getId());
		stakeOrder.setStatus(status);
		stakeOrder.setCreateDay(Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
		stakeOrder.setBelongUserId(belongUserId);
		stakeOrder.setBizStatus(bizStatus);
		stakeOrder.setTxHash(req.getHash());
		stakeOrder.setCreateTime(new Date());
		boolean save = stakeOrderServiceImpl.save(stakeOrder);
		if (!save) {
			log.error("保存质押订单失败");
			return ResultPista.fail("保存质押订单失败");
		}

		if (status == 1) {
			if (stakeOrder.getGiftHAmount().compareTo(BigDecimal.ZERO) > 0) {
				hGiftReleaseBucketService.createAcpDepositBucket(userInfo.getUserId(), userInfo.getAccount(),
					stakeOrder.getOrderNo(), stakeOrder.getGiftHAmount());
			}
			//需要处理质押之后的任务
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
					OrderMsgDO orderMsgDO = new OrderMsgDO();
					orderMsgDO.setId(stakeOrder.getId());
					orderMsgDO.setBizType(1);
					orderMsgDOList.add(orderMsgDO);
					asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);
				}
			});
		}
		return ResultPista.success();
	}

	/**
	 * 填充 ACP 入金订单价格和赠送快照。
	 */
	private void fillAcpDepositStakeSnapshot(StakeOrder stakeOrder, AcpHPriceSnapshot priceSnapshot,
											 BigDecimal depositUsdtAmount, BigDecimal giftRatioSnapshot) {
		stakeOrder.setAcpPriceUsdtSnapshot(priceSnapshot.getAcpPriceUsdt());
		stakeOrder.setHPriceUsdtSnapshot(priceSnapshot.getHPriceUsdt());
		stakeOrder.setDepositUsdtAmount(depositUsdtAmount);
		stakeOrder.setGiftRatioSnapshot(giftRatioSnapshot);
		stakeOrder.setGiftHAmount(calculateGiftHAmount(depositUsdtAmount, giftRatioSnapshot, priceSnapshot.getHPriceUsdt()));
	}

	/**
	 * 按旧系统H折U价值和ACP价格快照计算新系统 ACP 入金数量。
	 */
	private BigDecimal calculateAcpDepositAmount(BigDecimal oldHUsdtAmount, BigDecimal acpPriceUsdtSnapshot) {
		return oldHUsdtAmount.divide(acpPriceUsdtSnapshot, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
	}

	/**
	 * 按订单折U价值、赠送比例快照和H价格快照计算本单赠送H总量。
	 */
	private BigDecimal calculateGiftHAmount(BigDecimal depositUsdtAmount, BigDecimal giftRatioSnapshot,
											BigDecimal hPriceUsdtSnapshot) {
		BigDecimal giftUsdtAmount = depositUsdtAmount.multiply(giftRatioSnapshot)
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		return giftUsdtAmount.divide(hPriceUsdtSnapshot, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
	}
}
