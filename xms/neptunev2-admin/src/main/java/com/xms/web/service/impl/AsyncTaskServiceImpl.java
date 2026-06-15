package com.xms.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.config.redis.delayqueue.RedissonDelayHandler;
import com.xms.common.config.redis.delayqueue.RedissonDelayOrder;
import com.xms.common.config.redis.delayqueue.config.RedissonTemplate;
import com.xms.common.config.redis.stream.ReadOffsetModel;
import com.xms.common.config.redis.stream.RenegadeStreamTemplate;
import com.xms.common.constant.*;
import com.xms.common.core.domain.BaseEntity;
import com.xms.common.exception.ServiceException;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.common.mq.dynamic.OrderMsgDO;
import com.xms.common.utils.Func;
import com.xms.common.utils.spring.SpringUtils;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.*;
import com.xms.dao.entity.bo.*;
import com.xms.dao.entity.domain.*;
import com.xms.dao.entity.vo.ParentUserTaskVo;
import com.xms.dao.mapper.AsyncTaskMapper;
import com.xms.dao.service.*;
import com.xms.dao.service.UserRelationService;
import com.xms.dao.service.impl.*;
import com.xms.quartz.mapper.SysJobLogMapper;
import com.xms.system.mapper.SysLogininforMapper;
import com.xms.system.mapper.SysOperLogMapper;
import com.xms.web.domain.ReleaseMiningBo;
import com.xms.web.domain.dto.SourceType0OrderDto;
import com.xms.web.domain.dto.TodayRewardOrderDto;
import com.xms.web.service.BybitMarketService;
import com.xms.web.service.IAsyncTaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.checkerframework.checker.units.UnitsTools.g;
import static org.checkerframework.checker.units.UnitsTools.s;

/**
 * @author: renengadePISTA
 * @createDate: 2023/9/18
 */
@Service
@AllArgsConstructor
@Slf4j
public class AsyncTaskServiceImpl implements IAsyncTaskService {
	private static final String SQL_VALID_NUM1 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num1=valid_num1+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM2 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num2=valid_num2+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM4 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num4=valid_num4+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM5 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num5=valid_num5+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM3 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num3=valid_num3+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM7 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num7=valid_num7+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM8 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num8=valid_num8+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM9 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num9=valid_num9+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_H_GIFT_RELEASE_BUCKET_DAILY_RELEASE = "UPDATE t_h_gift_release_bucket SET update_time=?,released_amount=?,remaining_amount=?,released_days=?,last_release_date=?,status=? WHERE id=? AND status=1 ";
	private static final String SQL_RECALCULATE_USER_LEVEL = "UPDATE t_user_info SET update_time=?,community_performance=?,game_level=?,layer_level=? WHERE user_id=? ";
	private static final String SQL_UPDATE_AWAITING_AMOUNT = "UPDATE t_mining_package_order SET awaiting_amount = awaiting_amount + ? WHERE id = ?";
	private static final int H_GIFT_RELEASE_STATUS_RELEASING = 1;
	private static final int H_GIFT_RELEASE_STATUS_COMPLETED = 2;
	private final AsyncTaskMapper asyncTaskMapper;
	private final JdbcTemplate jdbcTemplate;
	private final RenegadeStreamTemplate redisTemplate;
	private final SysJobLogMapper jobLogMapper;
	private final SysOperLogMapper sysOperLogMapper;
	private final SysLogininforMapper sysLogininforMapper;
	private final IMqTransactionLogService mqTransactionLogServiceImpl;
	private final RedissonDelayHandler redissonDelayHandler;
	private final UserInfoService userInfoService;
	private final WithdrawalService withdrawalServiceImpl;
	private final AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementServiceImpl;
	private final IMiningPackageOrderService miningPackageOrderService;
	private final ISysParaService sysParaServiceImpl;
	private final ICoinPriceService coinPriceService;
	private final IPtbDailyPriceService ptbDailyPriceService;
	private final XmsRedis xmsRedis;
	private final RedissonTemplate redissonTemplate;
	private final IRewardRecordService rewardRecordService;
	private final UserRelationService userRelationService;
	private final IWithdrawFeeShareStatDayService withdrawFeeShareStatDayService;
	private final IInteractRewardConfigService interactRewardConfigService;
	private final IPtbDayRatioRuleService ptbDayRatioRuleService;
	private final IRewardPoolConfigService rewardPoolConfigService;
	private final IUserLevelConfigService userLevelConfigService;

	private final IInsuranceOrderService insuranceOrderService;
	private final BybitMarketService bybitMarketService;
	private final IUserWealthVaultService userWealthVaultService;
	private final IWealthVaultStageConfigService wealthVaultStageConfigService;


	private final INodePlanOrderService nodePlanOrderService;
	private final INodePlanService nodePlanService;
	private final IUserMoneyService userMoneyService;
	private final IHGiftReleaseBucketService hGiftReleaseBucketService;

	//质押相关
	private final IStakeRoundService stakeRoundService;
	private final IStakeOrderService stakeOrderService;
	private final IUserStakePositionService userStakePositionService;
	private final IUserInvestLayerConfigService userInvestLayerConfigService;
	private final IUserYieldRateConfigService userYieldRateConfigService;
	private final IStakeDailySnapshotService stakeDailySnapshotService;
	private final Environment environment;

	/**
	 * 批量更新订单可领取金额
	 *
	 * @param orderList 订单列表
	 */
	private void batchUpdateAwaitingAmount(List<SourceType0OrderDto> orderList) {
		int[] ints = jdbcTemplate.batchUpdate(SQL_UPDATE_AWAITING_AMOUNT, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setBigDecimal(1, orderList.get(i).getAwaitingAmount());
				ps.setLong(2, orderList.get(i).getId());
			}

			@Override
			public int getBatchSize() {
				return orderList.size();
			}
		});

		if (ArrayUtil.contains(ints, 0)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("订单可领取金额更新回滚");
			throw new ServiceException("订单可领取金额更新失败");
		}
	}

	@Override
	public Map<String, Object> getTask(String type, String date) {
		Map<String, Object> task = new HashMap<>();
		task.put("type", type);
		task.put("date", date);
		return asyncTaskMapper.getTask(task);
	}


	@Override
	public int addTask(String type, String date) {
		Map<String, Object> task = new HashMap<>();
		task.put("type", type);
		task.put("date", date);
		return asyncTaskMapper.addTask(task);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean transactionExcute(String customerNo, String transactionId) {
		//这里写入业务逻辑的干活
		// 以传入的transactionId作为主键或者单独使用一个字段记录该事务id需要添加唯一索引用于消息回查.必须唯一
		log.info("分布式事务业务处理成功");
		return true;

	}

	@Override
	public void dealRedisDeadMsg() {
		int size;
		while (true) {
			List<MapRecord<String, String, byte[]>> read = redisTemplate.xRead(RedisConstant.StreamMsgConstant.XMS_DEAD_MSG, 1000,
				ReadOffsetModel.START.getReadOffset().getOffset());
			if (read == null) {
				return;
			}
			size = read.size();
			if (size < 1) {
				return;
			}
			log.info("read ===========>>>>>>>>>{}", read);
			for (MapRecord<String, String, byte[]> entries : read) {
				Map<String, byte[]> recordValue = entries.getValue();
				log.debug("{} 拉取msg：{}", RedisConstant.StreamMsgConstant.XMS_DEAD_MSG, recordValue);
				for (Map.Entry<String, byte[]> entry : recordValue.entrySet()) {
					String strKey = entry.getKey();
					String[] keys = strKey.split("@");
					if (keys.length != SysConstant.TWO) {
						return;
					}
					ThreadUtil.sleep(300L);
					//往回发
					backSendMsg(keys, entry.getValue());
				}
				redisTemplate.acknowledge(RedisConstant.StreamMsgConstant.XMS_DEAD_MSG, entries);
				redisTemplate.delete(entries);
			}
		}
	}

	private void backSendMsg(String[] keys, Object messageBody) {
		while (true) {
			RecordId result = redisTemplate.send(keys[0], keys[1], messageBody);
			log.info("result 结果：{}", result.getTimestamp());
			if (Func.isNotEmpty(result.getTimestamp())) {
				return;
			}
			ThreadUtil.sleep(300L);
		}
	}

	@Override
	public void dealSysLogs(Integer days) {
		//默认60
		if (days == null) {
			days = 60;
		}
		//清空调度日志
		List<Long> ids = jobLogMapper.listSysLogsByDays(days);
		if (ids.isEmpty()) {
			return;
		}
		Long[] logIds = ids.toArray(new Long[0]);
		jobLogMapper.deleteJobLogByIds(logIds);
		//  BY RENEGADE PISTA: 2023/9/23  操作日志，登陆日志，目前不算多，可直接清楚days+30天前的数据
		ids = sysOperLogMapper.listOperLogByDays(days * 3);
		if (ids.isEmpty()) {
			return;
		}
		Long[] operLogIds = ids.toArray(new Long[0]);
		sysOperLogMapper.deleteOperLogByIds(operLogIds);
		ids = sysLogininforMapper.listLoginLogByDays(days * 3);
		if (!ids.isEmpty()) {
			Long[] idsLong = ids.toArray(new Long[0]);
			sysLogininforMapper.deleteLogininforByIds(idsLong);
		}
	}

	/**
	 * 查询没有处理的节点订单
	 * 描述:查询没有处理的节点订单
	 */
	@Override
	public void processOverdueDestroyOrders() {
		/*
		List<IdoOrder> idoOrderList = idoOrderService.lambdaQuery()
			.in(IdoOrder::getBizStatus, 1, 3)
			.list();
		if(CollectionUtil.isNotEmpty(idoOrderList)){
			for (IdoOrder idoOrder : idoOrderList) {
				if(idoOrder.getBizStatus().equals(1)){
					//没有处理的订单(可能遗漏的)
					List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
					OrderMsgDO orderMsgDO = new OrderMsgDO();
					orderMsgDO.setId(idoOrder.getId());
					orderMsgDO.setBizType(1);
					orderMsgDOList.add(orderMsgDO);
					asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);
				}else{
					//注册了但是没有处理的订单
					Long count = userInfoService.lambdaQuery()
						.eq(UserInfo::getAccount, idoOrder.getAddress())
						.count();
					if(count>0){
						List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
						OrderMsgDO orderMsgDO = new OrderMsgDO();
						orderMsgDO.setAddress(idoOrder.getAddress());
						orderMsgDO.setBizType(2);
						orderMsgDOList.add(orderMsgDO);
						asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);
					}
				}
			}
		}

		//查询超过了24小时但是没有处理的swap订单
		// 获取当前时间减去5分钟的时间点
		Date fiveMinutesBefore = DateUtil.offsetMinute(new Date(), -5);
		List<SwapOrder> swapOrderList = swapOrderService.lambdaQuery()
			.eq(SwapOrder::getBizStatus, 2)
			.eq(SwapOrder::getBizStatus1, 0)
			// 过期时间 + 5 分钟 <= 当前
			.lt(SwapOrder::getExpireTime, fiveMinutesBefore)
			.list();
		if(CollectionUtil.isNotEmpty(swapOrderList)){
			for (SwapOrder swapOrder : swapOrderList) {
				if(swapOrder.getUserId()==0L){
					continue;
				}
				//默认24小时后加提现额度
				//发送消息到延迟队列
				redissonDelayHandler.add(new RedissonDelayOrder(swapOrder.getId().toString(), 10L, SysConstant.THREE,
					null, RedisConstant.StreamMsgConstant.DELAY_ORDER_TIMEOUT_QUEUE));
			}
		}
*/
/*		boolean updated = destroyOrderService.lambdaUpdate()
			.eq(DestroyOrder::getPayStatus, 0)
			.lt(BaseEntity::getCreateTime, deadline)
			.set(DestroyOrder::getPayStatus, 2)
			.update();

		if (!updated) {
			log.warn("处理逾期订单任务：更新失败，订单IDs: {}", overdueIds);
		} else {
			log.info("处理逾期订单任务：成功关闭 {} 条逾期订单", overdueIds.size());
		}*/
	}


	@Override
	public void task103Handler() {
/*		List<DestroyOrder> destroyOrderList = destroyOrderService.lambdaQuery()
			.eq(DestroyOrder::getPayStatus, 1)
			.eq(DestroyOrder::getBizStatus, 0)
			.list();
		if (CollectionUtil.isNotEmpty(destroyOrderList)) {
			for (DestroyOrder destroyOrder : destroyOrderList) {
				List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
				OrderMsgDO orderMsgDO = new OrderMsgDO();
				orderMsgDO.setId(destroyOrder.getId());
				orderMsgDO.setBizType(1);
				orderMsgDOList.add(orderMsgDO);
				RecordId res = streamTemplate.send(RedisConstant.StreamMsgConstant.ORDER_DYNAMIC_SETTLEMENT, IdUtil.getSnowflakeNextIdStr(), JsonUtil.toJsonAsBytes(orderMsgDOList));
				if (res == null || Func.isAllEmpty(res.getTimestamp())) {
					log.error("执行");
					throw new ServiceException("挖矿订单之后的用户升级处理更新失败");
				}
			}
		}*/
	}

	@Override
	public void task102Handler() {
	}

	/**
	 * 定时拉取ido订单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void getIdoOrder() {
//		// 调用http接口获得数据
//		//获取最后一条数据当做查询条件
//		IdoOrder queryParam = idoOrderService.lambdaQuery()
//			.last("limit 1")
//			.orderByDesc(IdoOrder::getId)
//			.one();
//		String lastId="";
//		//插入数据
//		List <IdoOrder> originList = new ArrayList<>();
//		List <IdoOrder> idoOrderList = new ArrayList<>(originList.size());
//		Set<String> idoIdSet = originList.stream().map(record -> record.getIdoId()).collect(Collectors.toSet());
//		Map<String, IdoOrder> idoOrderMap = idoOrderService.lambdaQuery()
//			.in(IdoOrder::getIdoId, idoIdSet)
//			.list().stream().collect(Collectors.toMap(IdoOrder::getIdoId, Function.identity(), (k1, k2) -> k2));
//		//根据地址查询用户
//		Set<String> addressSet = originList.stream().map(record -> record.getAddress()).collect(Collectors.toSet());
//		Map<String, Long> userMap = userInfoService.lambdaQuery()
//			.in(UserInfo::getAccount, addressSet)
//			.select(UserInfo::getUserId, UserInfo::getAccount)
//			.list().stream().collect(Collectors.toMap(UserInfo::getAccount, UserInfo::getUserId, (k1, k2) -> k2));
//
//		if(CollectionUtil.isNotEmpty(originList)){
//			IdoOrder  entity = null;
//			for (IdoOrder idoOrder : originList) {
//				if(!idoOrderMap.containsKey(idoOrder.getIdoId())){
//					//不存在才插入
//					entity = new IdoOrder();
//					entity.setIdoId(idoOrder.getIdoId());
//					if(userMap.containsKey(idoOrder.getAddress())){
//						entity.setBizStatus(1);
//					}else{
//						entity.setBizStatus(0);
//					}
//					entity.setUserId(userMap.getOrDefault(idoOrder.getAddress(), 0L));
//					entity.setAddress(idoOrder.getAddress());
//					entity.setShares(idoOrder.getShares());
//					entity.setOkbPaid(idoOrder.getOkbPaid());
//					entity.setTxHash(idoOrder.getTxHash());
//					entity.setTimestamp(idoOrder.getTimestamp());
//					entity.setBlockNumber(idoOrder.getBlockNumber());
//					entity.setCreateTime(idoOrder.getCreateTime());
//
//					idoOrderList.add(entity);
//				}
//			}
//		}
//
//		if(CollectionUtil.isNotEmpty(idoOrderList)){
//			idoOrderService.saveBatch(idoOrderList);
//		}
//
//		//查询出没有处理的数据
//		List<IdoOrder> queryOrderList = idoOrderService.lambdaQuery()
//			.eq(IdoOrder::getBizStatus, 1)
//			.list();
//		//查询每个用户
//		if(CollectionUtil.isNotEmpty(queryOrderList)){
//			// 每个用户累计购买份数（getShares 为本次购买份数）
//			Map<Long, Long> userSharesMap = queryOrderList.stream()
//				.filter(order -> order.getUserId() != null && order.getUserId() > 0)
//				.collect(Collectors.groupingBy(IdoOrder::getUserId, Collectors.summingLong(IdoOrder::getShares)));
//			Set<Long> userIdSet = queryOrderList.stream().map(IdoOrder::getUserId).collect(Collectors.toSet());
//			Map<Long, UserInfo> userInfoMap = userInfoService.lambdaQuery()
//				.in(UserInfo::getUserId, userIdSet)
//				.list().stream().collect(Collectors.toMap(UserInfo::getUserId, Function.identity(), (k1, k2) -> k2));
//
//			// 示例：遍历结果，可根据业务需要做后续处理（落库/日志/结算等）
//			userSharesMap.forEach((userId, totalShares) -> {
//				log.info("userId {} totalShares {}", userId, totalShares);
//				boolean update = userInfoService.lambdaUpdate()
//					.eq(UserInfo::getUserId, userId)
//					.setSql("")
//					.update();
//				if (!update) {
//					log.error("更新用户节点信息失败：userId {}", userId);
//				}
//				UserInfo userInfo = userInfoMap.get(userId);
//				if(userInfo.getInviteUserId()!=null){
//
//				}
//
//			});
//		}
	}

	/**
	 * 补偿任务 提现信息重置 但是需要把之前的withdrawal_balance和team_withdraw_balance重置为0
	 */
	private void extracted() {
		List<Withdrawal> withdrawalList = withdrawalServiceImpl.lambdaQuery()
			.eq(Withdrawal::getStatus, 3)
			.list();
		if (CollectionUtil.isNotEmpty(withdrawalList)) {
			for (Withdrawal withdrawal : withdrawalList) {
				UserInfo userInfo = userInfoService.lambdaQuery()
					.eq(UserInfo::getUserId, withdrawal.getUserId())
					.one();
				if (userInfo != null) {
					userInfoService.lambdaUpdate()
						.eq(UserInfo::getUserId, userInfo.getUserId())
						.setSql("withdrawal_balance = withdrawal_balance + " + withdrawal.getChangeBalance())
						.update();
					List<Long> parentIds = userInfo.getParentIds();
					if (com.xms.common.utils.CollectionUtil.isNotEmpty(parentIds)) {
						userInfoService.lambdaUpdate()
							.in(UserInfo::getUserId, parentIds)
							.setSql("team_withdraw_balance = team_withdraw_balance + " + withdrawal.getChangeBalance())
							.update();
					}
				}
			}
		}
	}

	/**
	 * 任务类型102 v9节点均分提现手续费分红任务
	 */
	@Transactional(rollbackFor = Exception.class)
	public void distributePtbInterest102(Integer parDate) {
		int targetDate = resolveStatDate(parDate);
		long currentDate = Long.parseLong(DateUtil.format(DateUtil.date(), "yyyyMMdd"));
		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_102, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型102 v9节点均分提现手续费分红任务已存在跳过");
			return;
		}

		WithdrawFeeShareStatDay statDay = initWithdrawFeeShareStatDay();
		StringBuilder failReason = new StringBuilder();

		List<Withdrawal> withdrawalList = withdrawalServiceImpl.lambdaQuery()
			.eq(Withdrawal::getChainId, targetDate)
			.select(Withdrawal::getFeeBalance)
			.list();

		if (CollectionUtil.isEmpty(withdrawalList)) {
			failReason.append("没有可分红手续费记录,执行结束");
		} else {
			BigDecimal totalFee = withdrawalList.stream()
				.map(Withdrawal::getFeeBalance)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			statDay.setTotalFee(totalFee);

			if (totalFee.compareTo(BigDecimal.ZERO) <= 0) {
				failReason.append("分红手续费为0,执行结束");
			} else {
				distributeV9Rewards(totalFee, statDay, failReason);
				burnPlatformIncome(totalFee, statDay, failReason);
			}
		}

		statDay.setFailReason(failReason.length() == 0 ? null : failReason.toString());
		withdrawFeeShareStatDayService.save(statDay);
//		int i = addTask(SysConstant.TSK_TYPE_102, currentDate + "");
//		if (i != 1) {
//			throw new RuntimeException("添加任务类型102 v9节点均分提现手续费分红任务失败");
//		}
	}

	private int resolveStatDate(Integer parDate) {
		if (parDate == null || parDate == 0) {
			return Integer.parseInt(DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
		}
		return parDate;
	}

	private WithdrawFeeShareStatDay initWithdrawFeeShareStatDay() {
		Long statDate = Long.valueOf(DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
		WithdrawFeeShareStatDay statDay = new WithdrawFeeShareStatDay();
		statDay.setOrderNo(IDUtils.getSnowflakeStr());
		statDay.setStatDate(statDate);
		statDay.setTotalFee(BigDecimal.ZERO);
		statDay.setDistributedFee(BigDecimal.ZERO);
		statDay.setUserCount(0L);
		statDay.setPerUserAmount(BigDecimal.ZERO);
		statDay.setShareUserSnapshot(StrUtil.EMPTY);
		return statDay;
	}

	private void distributeV9Rewards(BigDecimal totalFee, WithdrawFeeShareStatDay statDay, StringBuilder failReason) {
		List<UserInfo> userLevel9List = userInfoService.lambdaQuery()
			.eq(UserInfo::getGameLevel, 9)
			.list();
		if (CollectionUtil.isEmpty(userLevel9List)) {
			failReason.append("没有v9用户;");
			log.debug("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 没有v9用户");
			return;
		}

		statDay.setUserCount((long) userLevel9List.size());
		BigDecimal rewardFee = calcV9RewardFee(totalFee, failReason);
		if (rewardFee == null || rewardFee.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}

		BigDecimal eachFee = rewardFee.divide(new BigDecimal(userLevel9List.size()),
			ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		if (eachFee.compareTo(BigDecimal.ZERO) <= 0) {
			failReason.append("提现手续费金额过小,不够分;");
			log.info("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 提现手续费金额过小,不够分");
			return;
		}

		statDay.setPerUserAmount(eachFee);
		statDay.setDistributedFee(eachFee.multiply(new BigDecimal(userLevel9List.size()))
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		String snapshot = userLevel9List.stream()
			.map(userInfo -> userInfo.getUserId() + "#" + StrUtil.blankToDefault(userInfo.getAccount(), ""))
			.collect(Collectors.joining(","));
		statDay.setShareUserSnapshot(snapshot);

		distributeRewardToUsers(userLevel9List, eachFee);
	}

	private BigDecimal calcV9RewardFee(BigDecimal totalFee, StringBuilder failReason) {
		BigDecimal rewardRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_withdrawal_fee_v9_reward_ratio))
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal rewardFee = totalFee.multiply(rewardRatio)
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		if (rewardFee.compareTo(BigDecimal.ZERO) <= 0) {
			failReason.append("v9分红奖励为0,分红比例:").append(rewardRatio).append("%;");
			log.info("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 提现手续费金额过小,不够分");
			return null;
		}
		return rewardFee;
	}

	private void distributeRewardToUsers(List<UserInfo> userLevel9List, BigDecimal eachFee) {
		List<RewardRecord> rewardRecordList = new ArrayList<>(userLevel9List.size());
		List<UserMoney> userMoneyValidNum1List = new ArrayList<>(userLevel9List.size());
		int batchSize = 1000;
		int stakeCount1 = 0;
		String orderCode = IDUtils.getSnowflakeStr();

		for (UserInfo userInfo : userLevel9List) {
			userMoneyValidNum1List.add(buildUserMoney(eachFee, orderCode, userInfo.getUserId()));
			stakeCount1++;
			if (stakeCount1 >= batchSize) {
				bachUpdateMoneyValid1(userMoneyValidNum1List);
				userMoneyValidNum1List.clear();
				log.info("更新成功");
				stakeCount1 = 0;
			}
			rewardRecordList.add(buildRewardRecord(eachFee, orderCode, userInfo.getUserId()));
		}

		if (CollectionUtil.isNotEmpty(userMoneyValidNum1List)) {
			bachUpdateMoneyValid1(userMoneyValidNum1List);
		}

		if (CollectionUtil.isNotEmpty(rewardRecordList)) {
			rewardRecordService.saveBatch(rewardRecordList);
		}
	}

	private UserMoney buildUserMoney(BigDecimal eachFee, String orderCode, Long userId) {
		UserMoney entity = new UserMoney();
		entity.setId(userId);
		entity.setValidNum1(eachFee);
		entity.setGtId(IDUtils.getSnowflakeStr());
		entity.setSourceCode(orderCode);
		entity.setSourceId(userId);
		entity.setSourceType(ConstantType.user_money_log_source_type.type_10);
		entity.setUpdateTime(new Date());
		return entity;
	}

	private RewardRecord buildRewardRecord(BigDecimal eachFee, String orderCode, Long userId) {
		RewardRecord rewardRecord = new RewardRecord();
		rewardRecord.setUserId(userId);
		rewardRecord.setAmount(eachFee);
		rewardRecord.setBusinessType(ConstantType.xms_reward_record_business_type.type_5);
		//rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_14);
		rewardRecord.setSourceOrderCode(orderCode);
		rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
		rewardRecord.setSourceUserId(userId);
		rewardRecord.setCreateTime(new Date());
		return rewardRecord;
	}

	private void burnPlatformIncome(BigDecimal totalFee, WithdrawFeeShareStatDay statDay, StringBuilder failReason) {
		BigDecimal platformRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_withdrawal_fee_platform_income_ratio))
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal platformReward = platformRatio.multiply(totalFee)
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		if (platformReward.compareTo(BigDecimal.ZERO) <= 0) {
			failReason.append("打到项目方钱包余额为0,比例:").append(platformRatio).append("%;");
			log.debug("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 提现手续费为0");
			return;
		}
		SpringUtils.getBean(WithdrawalServiceImpl.class)
			.doBurn(platformReward, statDay.getOrderNo());
	}

	public void distributePtbInterest101(Integer parDate) {
		if (parDate == null || parDate == 0) {
			parDate = Integer.valueOf(DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
		}
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		//long类型日期
		long currentDate = Long.parseLong(strDate);
		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_102, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型102 v9节点均分提现手续费分红任务已存在跳过");
			return;
		}
		//查询有没有v9用户
		List<UserInfo> userLevel9List = userInfoService.lambdaQuery()
			.eq(UserInfo::getGameLevel, 9)
			.list();
		Long statDate = Long.valueOf(DateUtil.format(DateUtil.yesterday(), "yyyyMMdd"));
		WithdrawFeeShareStatDay statDay = new WithdrawFeeShareStatDay();
		statDay.setOrderNo(IDUtils.getSnowflakeStr());
		statDay.setStatDate(statDate);
		statDay.setTotalFee(BigDecimal.ZERO);
		statDay.setDistributedFee(BigDecimal.ZERO);
		statDay.setUserCount(0L);
		statDay.setPerUserAmount(BigDecimal.ZERO);
		statDay.setShareUserSnapshot(StrUtil.EMPTY);

		String failReason = null;

		if (CollectionUtil.isEmpty(userLevel9List)) {
			failReason = "没有v9用户";
			log.debug("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 没有v9用户");

			List<Withdrawal> withdrawalList = withdrawalServiceImpl.lambdaQuery()
				.eq(Withdrawal::getChainId, parDate)
				.select(Withdrawal::getFeeBalance)
				.list();

			if (CollectionUtil.isEmpty(withdrawalList)) {

			}
		} else {
			statDay.setUserCount((long) userLevel9List.size());

			List<Withdrawal> withdrawalList = withdrawalServiceImpl.lambdaQuery()
				.eq(Withdrawal::getChainId, parDate)
				.select(Withdrawal::getFeeBalance)
				.list();

			if (CollectionUtil.isEmpty(withdrawalList)) {
				failReason = "没有提现手续费";
				log.debug("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 没有提现手续费");
			} else {
				BigDecimal totalFee = withdrawalList.stream()
					.map(Withdrawal::getFeeBalance)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				statDay.setTotalFee(totalFee);
				if (totalFee.compareTo(BigDecimal.ZERO) <= 0) {
					failReason = "提现手续费为0";
					log.debug("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 提现手续费为0");
				} else {
					//看下分红比例
					BigDecimal eachFeeRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_withdrawal_fee_v9_reward_ratio))
						.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);

					//看下打到项目方钱包要多少
					BigDecimal platformRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_withdrawal_fee_platform_income_ratio))
						.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					BigDecimal platformReward = platformRatio.multiply(totalFee)
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (platformReward.compareTo(BigDecimal.ZERO) <= 0) {
						failReason += "打到项目方钱包余额为0,比例:" + platformRatio + "%";
						log.debug("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 提现手续费为0");
					} else {
						//给项目方打币调用合约
						//statDay.getOrderNo();
						SpringUtils.getBean(WithdrawalServiceImpl.class)
							.doBurn(platformReward, statDay.getOrderNo());
					}

					BigDecimal rewardFee = totalFee.multiply(eachFeeRatio)
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (rewardFee.compareTo(BigDecimal.ZERO) <= 0) {
						failReason += "v9分红奖励为0,分红比例:" + eachFeeRatio + "%";
						log.info("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 提现手续费金额过小,不够分");
					} else {
						BigDecimal eachFee = rewardFee.divide(new BigDecimal(userLevel9List.size()),
							ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						if (eachFee.compareTo(BigDecimal.ZERO) <= 0) {
							failReason = "提现手续费金额过小,不够分";
							log.info("任务类型102 v9节点均分提现手续费分红任务,无法分奖励 提现手续费金额过小,不够分");
						} else {
							statDay.setPerUserAmount(eachFee);
							statDay.setDistributedFee(eachFee.multiply(new BigDecimal(userLevel9List.size()))
								.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
							String snapshot = userLevel9List.stream()
								.map(userInfo -> userInfo.getUserId() + "#" + StrUtil.blankToDefault(userInfo.getAccount(), ""))
								.collect(Collectors.joining(","));
							statDay.setShareUserSnapshot(snapshot);

							List<RewardRecord> rewardRecordList = new ArrayList<>(userLevel9List.size());
							List<UserMoney> userMoneyValidNum1List = new ArrayList<>(userLevel9List.size());
							int batchSize = 1000;
							int stakeCount1 = 0;
							String orderCode = IDUtils.getSnowflakeStr();
							for (UserInfo userInfo : userLevel9List) {


								UserMoney entity = new UserMoney();
								entity.setId(userInfo.getUserId());
								entity.setValidNum1(eachFee);
								entity.setGtId(IDUtils.getSnowflakeStr());
								entity.setSourceCode(orderCode);
								entity.setSourceId(userInfo.getUserId());
								entity.setSourceType(ConstantType.user_money_log_source_type.type_10);
								entity.setUpdateTime(new Date());
								userMoneyValidNum1List.add(entity);
								stakeCount1++;
								if (stakeCount1 >= batchSize) {
									bachUpdateMoneyValid1(userMoneyValidNum1List);
									userMoneyValidNum1List.clear();
									log.info("更新成功");
									stakeCount1 = 0;
								}

								RewardRecord rewardRecord = new RewardRecord();
								rewardRecord.setUserId(userInfo.getUserId());
								rewardRecord.setAmount(eachFee);
								rewardRecord.setBusinessType(ConstantType.xms_reward_record_business_type.type_5);
								//rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_14);
								rewardRecord.setSourceOrderCode(orderCode);
								rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
								rewardRecord.setSourceUserId(userInfo.getUserId());
								rewardRecord.setCreateTime(new Date());
								rewardRecordList.add(rewardRecord);
							}

							if (CollectionUtil.isNotEmpty(userMoneyValidNum1List)) {
								bachUpdateMoneyValid1(userMoneyValidNum1List);
							}

							if (CollectionUtil.isNotEmpty(rewardRecordList)) {
								rewardRecordService.saveBatch(rewardRecordList);
							}
						}

					}
				}
			}
		}

		statDay.setFailReason(failReason);
		withdrawFeeShareStatDayService.save(statDay);
/*		int i = addTask(SysConstant.TSK_TYPE_102, currentDate + "");
		if (i != 1) {
			throw new RuntimeException("添加任务类型102 v9节点均分提现手续费分红任务失败");
		}*/
	}

	@Override
	public void taskMsgCycle() {
		//获取当前时间时间的减去29L
		long epochMilli = Instant.now().toEpochMilli() - 300000L;
		List<MqTransactionLog> tMqTransactionLogs = mqTransactionLogServiceImpl.lambdaQuery().lt(MqTransactionLog::getCreateTime, epochMilli).list();
		if (tMqTransactionLogs.isEmpty()) {
			return;
		}
		for (MqTransactionLog tMqTransactionLog : tMqTransactionLogs) {
			byte[] contens = tMqTransactionLog.getLog();
			MqMsgDO sendMqDo = JsonUtil.readValue(contens, MqMsgDO.class);
			sendMqDo.setTransactionId(tMqTransactionLog.getId().toString());
			redisTemplate.send(sendMqDo.getTopic(), sendMqDo.getTransactionId(), JsonUtil.toJsonAsBytes(sendMqDo.getBody()));
			log.warn("{} 重新发送 发送订单ok ", sendMqDo.getBody());

		}
	}


	/**
	 * 发放互动奖
	 *
	 * @param userStaticRewardMap 每个用户的静态收益
	 * @param allUserMap          所有用户信息（用于判断父级、有效性）
	 * @return 互动奖励 map：userId -> 互动奖总额
	 */
	private Map<Long, BigDecimal> sendInteractionReward(Map<Long, BigDecimal> userStaticRewardMap, Map<Long, UserInfo> allUserMap,
														BigDecimal boomaiPrice) {
		// 一次性查询所有 1 代关系
		List<UserRelation> relationList = userRelationService.lambdaQuery()
			.eq(UserRelation::getActiveFlag, 1)
			.eq(UserRelation::getDistance, 1)
			.list();

		// 构建：userId -> 直推列表（只包含有效用户）
		Map<Long, List<Long>> level1ChildrenMap = new HashMap<>();
		for (UserRelation rel : relationList) {
			// 只统计伞下有效用户
			if (rel.getPosUserId() == null) {
				continue;
			}
			UserInfo child = allUserMap.get(rel.getPosUserId());
			if (child == null || child.getIsValid() == null || !child.getIsValid().equals(1)) {
				continue;
			}

			if (rel.getDistance() != null && rel.getDistance() == 1) {
				level1ChildrenMap
					.computeIfAbsent(rel.getParUserId(), k -> new ArrayList<>())
					.add(rel.getPosUserId());
			}
		}

		// 互动奖励结果：每个用户能拿到多少互动奖励
		Map<Long, BigDecimal> interactRewardMap = new HashMap<>();
		List<RewardRecord> parentRewardRecords = new ArrayList<>();
		List<RewardRecord> level1RewardRecords = new ArrayList<>();
		List<RewardRecord> level2RewardRecords = new ArrayList<>();
		Map<Integer, BigDecimal> configRewardMap = interactRewardConfigService.lambdaQuery()
			.list().stream().collect(Collectors.toMap(InteractRewardConfig::getLevel, InteractRewardConfig::getRewardRatio));
		BigDecimal configUp = configRewardMap.get(1).divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal configlvl1 = configRewardMap.get(2).divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal configlvl2 = configRewardMap.get(3).divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		for (Map.Entry<Long, BigDecimal> entry : userStaticRewardMap.entrySet()) {
			//b:500 静态奖励
			//a<-b(下一层)
			//d<-a<-b(下二层)
			//b->c(上一层)
			Long centerUserId = entry.getKey();           // 当前作为“中心”的用户
			BigDecimal staticReward = entry.getValue();   // 他的静态收益 S
			if (staticReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			// 上一层 10%，下一层 10%，下二层 5%
			BigDecimal upPool = staticReward.multiply(configUp);
			BigDecimal lvl1Pool = staticReward.multiply(configlvl1);
			BigDecimal lvl2Pool = staticReward.multiply(configlvl2);

			// 下一层：父级拿 10%
			UserInfo centerUser = allUserMap.get(centerUserId);
			UserInfo parentInfo = null;
			if (centerUser != null && centerUser.getInviteUserId() != null) {
				Long parentId = centerUser.getInviteUserId();
				parentInfo = allUserMap.get(parentId);
				if (parentInfo != null && parentInfo.getIsValid() != null && parentInfo.getIsValid().equals(1)
					&& upPool.compareTo(BigDecimal.ZERO) > 0) {
					interactRewardMap.merge(parentId, upPool, BigDecimal::add);
					addInteractRewardRecord(parentRewardRecords, parentId, centerUserId, upPool,
						ConstantType.xms_reward_record_source_type.type_7, boomaiPrice);
				}
			}

			// 上一层：1代平分 10%
			List<Long> level1List = level1ChildrenMap.getOrDefault(centerUserId, Collections.emptyList());
			if (!level1List.isEmpty() && lvl1Pool.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal per = lvl1Pool.divide(new BigDecimal(level1List.size()),
					ConstantStatic.newScale, ConstantStatic.roundingModeNew);
				if (per.compareTo(BigDecimal.ZERO) > 0) {
					for (Long childId : level1List) {
						interactRewardMap.merge(childId, per, BigDecimal::add);
						addInteractRewardRecord(level1RewardRecords, childId, centerUserId, per,
							ConstantType.xms_reward_record_source_type.type_8, boomaiPrice);
					}
				}
			}


			// 下二层：父级的父级
			if (parentInfo != null && parentInfo.getInviteUserId() != null && lvl2Pool.compareTo(BigDecimal.ZERO) > 0) {
				Long grandParentId = parentInfo.getInviteUserId();
				UserInfo grandParentInfo = allUserMap.get(grandParentId);
				if (grandParentInfo != null && grandParentInfo.getIsValid() != null && grandParentInfo.getIsValid().equals(1)) {
					interactRewardMap.merge(grandParentId, lvl2Pool, BigDecimal::add);
					addInteractRewardRecord(level2RewardRecords, grandParentId, centerUserId, lvl2Pool,
						ConstantType.xms_reward_record_source_type.type_9, boomaiPrice);
				}
			}

		}

		saveInteractRewardRecords(parentRewardRecords);
		saveInteractRewardRecords(level1RewardRecords);
		saveInteractRewardRecords(level2RewardRecords);
		return interactRewardMap;
	}

	private void addInteractRewardRecord(List<RewardRecord> recordList, Long receiveUserId, Long sourceUserId,
										 BigDecimal reward, int sourceType, BigDecimal boomaiPrice) {
		if (reward == null || reward.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		RewardRecord rr = new RewardRecord();
		rr.setUserId(receiveUserId);
		rr.setAmount(reward);
		rr.setBusinessType(ConstantType.xms_reward_record_business_type.type_2);
		rr.setSourceType(sourceType);
		rr.setSourceOrderCode(IDUtils.getSnowflakeStr());
		rr.setOrderCode(IDUtils.getSnowflakeStr());
		rr.setSourceUserId(sourceUserId);
		rr.setRealTimePrice(boomaiPrice);
		rr.setCreateTime(new Date());
		recordList.add(rr);
	}

	private void saveInteractRewardRecords(List<RewardRecord> recordList) {
		if (CollectionUtil.isEmpty(recordList)) {
			return;
		}
		rewardRecordService.saveBatch(recordList);
	}

	/**
	 * 获取日利率
	 *
	 * @return
	 */
	private BigDecimal getDayRatio() {
		PtbDayRatioRule rule = ptbDayRatioRuleService.lambdaQuery()
			.eq(PtbDayRatioRule::getCoinType, 1L)
			.last("limit 1")
			.one();
		if (rule == null || rule.getBaseRatio() == null) {
			return BigDecimal.ONE;
		}
		return rule.getBaseRatio();
	}


	/**
	 * 任务类型103 每日统计平台币价格
	 */
	@Override
	public void dailyPlatformCoinPriceRecord103() {
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		//long类型日期
		long currentDate = Long.parseLong(strDate);
		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_103, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型103 每日统计平台币价格任务已存在跳过");
			return;
		}

		String toDayStr = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		Long dayLong = Long.valueOf(toDayStr);

		// 获取上一价格用于涨跌幅：BOOMMAI(1)、MAI(2)
		BigDecimal lastBoomaiPrice = getLastCoinPrice(1);
		BigDecimal lastMaiPrice = getLastCoinPrice(2);

		// 当前价格
		BigDecimal boommai = getCurrentCoinPrice(1);
		BigDecimal mai = getCurrentCoinPrice(2);

		// 统计 BOOMMAI
		BigDecimal boomChange = upsertDailyPriceRecord(dayLong, 1, boommai, lastBoomaiPrice);
		adjustBoomaiDayRatio(boomChange);
		// 统计 MAI
		upsertDailyPriceRecord(dayLong, 2, mai, lastMaiPrice);

	/*	int i = addTask(SysConstant.TSK_TYPE_103, currentDate + "");
		if (i != 1) {
			throw new RuntimeException("添加任务类型103 每日统计平台币价格失败");
		}*/
		xmsRedis.del(RedisConstant.PTB_PRICE_KEY);
		redissonTemplate.sendCleanCacheWithDelay(RedisConstant.PTB_PRICE_KEY);
	}

	/**
	 * 获取某个币种最新一条价格（上一价格）
	 */
	private BigDecimal getLastCoinPrice(int coinType) {
		PtbDailyPrice ptbDailyPrice = ptbDailyPriceService.lambdaQuery()
			.eq(PtbDailyPrice::getCoinType, coinType)
			.orderByDesc(PtbDailyPrice::getId)
			.last("limit 1")
			.one();
		return ptbDailyPrice == null ? BigDecimal.ZERO : ptbDailyPrice.getPrice();
	}

	/**
	 * 获取某个币种当前价格
	 */
	private BigDecimal getCurrentCoinPrice(int coinType) {
		CoinPrice record = coinPriceService.lambdaQuery()
			.eq(CoinPrice::getCoinType, coinType)
			.last("limit 1")
			.one();
		return record == null ? BigDecimal.ZERO : record.getCurrentPrice();
	}

	/**
	 * 插入或更新每日价格记录，并计算涨跌幅
	 */
	private BigDecimal upsertDailyPriceRecord(Long day, int coinType,
											  BigDecimal currentPrice, BigDecimal lastPrice) {
		PtbDailyPrice record = ptbDailyPriceService.lambdaQuery()
			.eq(PtbDailyPrice::getDate, day)
			.eq(PtbDailyPrice::getCoinType, coinType)
			.last("limit 1")
			.one();

		BigDecimal change = BigDecimal.ZERO;
		if (lastPrice != null && lastPrice.compareTo(java.math.BigDecimal.ZERO) > 0
			&& currentPrice != null) {
			change = currentPrice
				.subtract(lastPrice)
				.divide(lastPrice, ConstantStatic.newScale, ConstantStatic.roundingModeNew)
				.multiply(new BigDecimal("100"));
		}

		if (record != null) {
			record.setPrice(currentPrice);
			record.setChangeRate(change);
			ptbDailyPriceService.updateById(record);
		} else {
			PtbDailyPrice entity = new PtbDailyPrice();
			entity.setDate(day);
			entity.setPrice(currentPrice);
			entity.setCoinType(coinType);
			entity.setChangeRate(change);
			entity.setCreateTime(new Date());
			ptbDailyPriceService.save(entity);
		}
		return change;
	}

	/**
	 * 根据涨跌幅调整 BOOMAI 日利率
	 */
	private void adjustBoomaiDayRatio(BigDecimal changeRate) {
		if (changeRate == null) {
			return;
		}
		PtbDayRatioRule dayRatioRule = ptbDayRatioRuleService.lambdaQuery()
			.eq(PtbDayRatioRule::getCoinType, 1L)
			.last("limit 1")
			.one();
		if (dayRatioRule == null || (dayRatioRule.getEnabled() != null && dayRatioRule.getEnabled() == 0)) {
			return;
		}
		BigDecimal threshold = dayRatioRule.getTriggerThreshold() == null ? new BigDecimal("10") : dayRatioRule.getTriggerThreshold();
		BigDecimal absRate = changeRate.abs();
		if (absRate.compareTo(threshold) <= 0) {
			return;
		}
		BigDecimal extra = absRate.subtract(threshold);
		if (extra.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		BigDecimal currentRatio = dayRatioRule.getBaseRatio() == null ? BigDecimal.ONE : dayRatioRule.getBaseRatio();
		BigDecimal step = dayRatioRule.getStepPerc() == null ? new BigDecimal("0.01") : dayRatioRule.getStepPerc();
		BigDecimal delta = extra.setScale(0, RoundingMode.DOWN).multiply(step);
		if (delta.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		if (changeRate.compareTo(BigDecimal.ZERO) > 0) {
			currentRatio = currentRatio.add(delta);
		} else {
			currentRatio = currentRatio.subtract(delta);
		}
		BigDecimal min = dayRatioRule.getMinRatio() == null ? new BigDecimal("0.5") : dayRatioRule.getMinRatio();
		BigDecimal max = dayRatioRule.getMaxRatio() == null ? new BigDecimal("3") : dayRatioRule.getMaxRatio();
		if (currentRatio.compareTo(min) < 0) {
			currentRatio = min;
		} else if (currentRatio.compareTo(max) > 0) {
			currentRatio = max;
		}
		ptbDayRatioRuleService.lambdaUpdate()
			.eq(PtbDayRatioRule::getId, dayRatioRule.getId())
			.set(PtbDayRatioRule::getBaseRatio, currentRatio)
			.update();
	}

	/**
	 * handelStakeOrder105 补偿任务
	 */
	@Override
	public void handelStakeOrder105() {
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		//long类型日期
		long currentDate = Long.parseLong(strDate);
		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_105, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型105 每日快照数据任务已存在跳过");
			return;
		}
		StakeDailySnapshot entity = new StakeDailySnapshot();
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		if(stakeRound !=null){
			entity.setInsuranceBalance(stakeRound.getInsuranceBalance());
			entity.setPlayerStakeTotal(stakeRound.getPlayerStakeTotal());
			entity.setStudioSubsidyTotal(stakeRound.getStudioSubsidyTotal());
			entity.setWithdrawRewardTotalFull(stakeRound.getWithdrawRewardTotalFull());
			entity.setBuyPointTotal(stakeRound.getBuyPointTotal());
			entity.setStakeRoundId(stakeRound.getId());
			// 对所有用户 valid_num4 求和
			BigDecimal totalValidNum4 = userMoneyService.getBaseMapper()
				.selectObjs(new QueryWrapper<UserMoney>()
					.select("IFNULL(SUM(valid_num4), 0)"))
				.stream()
				.findFirst()
				.map(v -> v instanceof BigDecimal ? (BigDecimal) v : new BigDecimal(v.toString()))
				.orElse(BigDecimal.ZERO);
			entity.setTotalValidNum4(totalValidNum4);

			BigDecimal totalSegAmount = userWealthVaultService.getBaseMapper()
				.selectMaps(new QueryWrapper<UserWealthVault>()
					.select("IFNULL(SUM(seg1_amount),0) + IFNULL(SUM(seg2_amount),0) + IFNULL(SUM(seg3_amount),0) + IFNULL(SUM(seg4_amount),0) + IFNULL(SUM(seg5_amount),0) AS totalSegAmount"))
				.stream()
				.findFirst()
				.map(m -> m.get("totalSegAmount"))
				.map(v -> v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()))
				.orElse(BigDecimal.ZERO);
			entity.setLockedValidNum4(totalSegAmount);
			entity.setWithdrawContractBalance(SpringUtils.getBean(WithdrawalServiceImpl.class).getWithdrawalCaBalance());
			stakeDailySnapshotService.save(entity);
		}

		int i = addTask(SysConstant.TSK_TYPE_105, currentDate + "");
		if (i != 1) {
			throw new RuntimeException("任务类型101 每日快照数据任务失败");
		}
	}

	/**
	 * 补偿任务：旧业绩归零后重新计算用户等级。
	 *
	 * <p>停服清理 old_* 旧业绩后执行，用当前质押后置结算同一口径重算
	 * community_performance、game_level、layer_level。不清 old_*，不改真实业绩，
	 * 不写钱包和奖励流水。</p>
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handelHGiftRelease() {
		log.info("旧业绩归零后用户等级补偿重算开始");

		// 1. 一次性收集用户和等级配置，后续在内存里按父级链深度倒序计算，避免循环中反复查库。
		List<UserInfo> userInfoList = userInfoService.lambdaQuery()
			.select(UserInfo::getUserId, UserInfo::getInviteUserId, UserInfo::getParentChain,
				UserInfo::getIsValid, UserInfo::getPerformance, UserInfo::getOldPerformance,
				UserInfo::getHistoryPerformance, UserInfo::getOldHistoryPerformance,
				UserInfo::getUmbrellaPerformance, UserInfo::getOldUmbrellaPerformance,
				UserInfo::getCommunityPerformance, UserInfo::getGameLevel, UserInfo::getLayerLevel)
			.list();
		if (CollectionUtil.isEmpty(userInfoList)) {
			log.info("旧业绩归零后用户等级补偿重算无用户数据");
			return;
		}
		List<UserLevelConfig> userLevelConfigList = userLevelConfigService.lambdaQuery()
			.orderByAsc(UserLevelConfig::getLevel)
			.list();
		List<UserInvestLayerConfig> layerConfigList = userInvestLayerConfigService.lambdaQuery()
			.orderByAsc(UserInvestLayerConfig::getMinInvest)
			.list();
		Map<Long, List<UserInfo>> directUserMap = userInfoList.stream()
			.filter(item -> item.getInviteUserId() != null)
			.collect(Collectors.groupingBy(UserInfo::getInviteUserId));

		// 2. 叶子节点先算、上级后算。当前小区业绩依赖直推线有效业绩，倒序也方便后续扩展团队重算。
		List<UserInfo> sortUserInfoList = userInfoList.stream()
			.sorted((item1, item2) -> {
				int depthCompare = Integer.compare(getUserParentDepth(item2), getUserParentDepth(item1));
				if (depthCompare != 0) {
					return depthCompare;
				}
				return Long.compare(item2.getUserId(), item1.getUserId());
			})
			.collect(Collectors.toList());

		// 3. 构造补偿更新对象，只更新小区业绩、用户等级和层级等级，按批量统一落库。
		Date now = new Date();
		int batchSize = 1000;
		List<UserInfo> updateUserInfoList = new ArrayList<>(Math.min(sortUserInfoList.size(), batchSize));
		for (UserInfo userInfo : sortUserInfoList) {
			BigDecimal communityPerformance = calculateCompensateCommunityPerformance(userInfo, directUserMap);
			Integer gameLevel = calculateCompensateGameLevel(userInfo, communityPerformance, userLevelConfigList);
			Integer layerLevel = calculateCompensateLayerLevel(userInfo.getEffectiveHistoryPerformance(), layerConfigList);

			UserInfo updateUserInfo = new UserInfo();
			updateUserInfo.setUserId(userInfo.getUserId());
			updateUserInfo.setCommunityPerformance(communityPerformance);
			updateUserInfo.setGameLevel(gameLevel);
			updateUserInfo.setLayerLevel(layerLevel);
			updateUserInfo.setUpdateTime(now);
			updateUserInfoList.add(updateUserInfo);
			if (updateUserInfoList.size() >= batchSize) {
				batchUpdateUserLevelCompensate(updateUserInfoList);
				updateUserInfoList.clear();
			}
		}
		if (CollectionUtil.isNotEmpty(updateUserInfoList)) {
			batchUpdateUserLevelCompensate(updateUserInfoList);
		}

		log.info("旧业绩归零后用户等级补偿重算完成, userCount:{}", userInfoList.size());
	}

	private BigDecimal calculateCompensateCommunityPerformance(UserInfo userInfo, Map<Long, List<UserInfo>> directUserMap) {
		List<UserInfo> children = directUserMap.get(userInfo.getUserId());
		if (CollectionUtil.isEmpty(children) || children.size() <= 1) {
			return BigDecimal.ZERO;
		}
		BigDecimal totalChildPerformance = BigDecimal.ZERO;
		BigDecimal maxChildPerformance = BigDecimal.ZERO;
		for (UserInfo child : children) {
			BigDecimal childLinePerformance = child.getEffectiveUmbrellaPerformance()
				.add(child.getEffectivePerformance());
			totalChildPerformance = totalChildPerformance.add(childLinePerformance);
			if (childLinePerformance.compareTo(maxChildPerformance) > 0) {
				maxChildPerformance = childLinePerformance;
			}
		}
		BigDecimal communityPerformance = totalChildPerformance.subtract(maxChildPerformance);
		return communityPerformance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : communityPerformance;
	}

	private Integer calculateCompensateGameLevel(UserInfo userInfo, BigDecimal communityPerformance,
												List<UserLevelConfig> userLevelConfigList) {
		if (userInfo.getIsValid() == null || userInfo.getIsValid().equals(0)) {
			return userInfo.getGameLevel() == null ? 0 : userInfo.getGameLevel();
		}
		Integer gameLevel = 0;
		if (CollectionUtil.isNotEmpty(userLevelConfigList)) {
			for (UserLevelConfig userLevelConfig : userLevelConfigList) {
				if (communityPerformance.compareTo(userLevelConfig.getUmbrellaPerformance()) >= 0) {
					gameLevel = userLevelConfig.getLevel();
				}
			}
		}
		return gameLevel;
	}

	private Integer calculateCompensateLayerLevel(BigDecimal historyPerformance,
												 List<UserInvestLayerConfig> layerConfigList) {
		Integer layerLevel = 0;
		if (CollectionUtil.isNotEmpty(layerConfigList)) {
			for (UserInvestLayerConfig config : layerConfigList) {
				if (historyPerformance.compareTo(config.getMinInvest()) >= 0) {
					layerLevel = config.getLevel();
				} else {
					break;
				}
			}
		}
		return layerLevel;
	}

	private int getUserParentDepth(UserInfo userInfo) {
		if (StrUtil.isBlank(userInfo.getParentChain())) {
			return 0;
		}
		int depth = 0;
		for (String parentId : userInfo.getParentChain().split(",")) {
			if (StrUtil.isNotBlank(parentId)) {
				depth++;
			}
		}
		return depth;
	}

	private void batchUpdateUserLevelCompensate(List<UserInfo> userInfoList) {
		int[] ints = jdbcTemplate.batchUpdate(SQL_RECALCULATE_USER_LEVEL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserInfo userInfo = userInfoList.get(i);
				ps.setTimestamp(1, new java.sql.Timestamp(userInfo.getUpdateTime().getTime()));
				ps.setBigDecimal(2, userInfo.getCommunityPerformance());
				ps.setInt(3, userInfo.getGameLevel());
				ps.setInt(4, userInfo.getLayerLevel());
				ps.setLong(5, userInfo.getUserId());
			}

			@Override
			public int getBatchSize() {
				return userInfoList.size();
			}
		});
		if (ArrayUtil.contains(ints, 0)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("旧业绩归零后用户等级补偿重算回滚");
			throw new ServiceException("旧业绩归零后用户等级补偿重算失败");
		}
	}

	/**
	 * 任务类型106：H赠送释放每日释放。
	 *
	 * <p>每日释放以 H赠送释放桶为来源，释放事实写入 {@code xms_reward_record}，
	 * H 钱包余额写入 {@code t_user_money.valid_num9}，钱包流水由 Canal 监听钱包表变更后自动生成。
	 * 生产环境通过 {@code t_async_task(type=106,date=yyyyMMdd)} 控制每天只执行一次；非生产环境不写任务标识，方便反复联调。</p>
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handelHGiftRelease106() {
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		long currentDate = Long.parseLong(strDate);

		// 1. 生产环境按任务类型和自然日做幂等，已有执行标识则直接跳过。
		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_106, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型106 H赠送释放每日释放任务已存在跳过");
			return;
		}

		// 2. 读取当前运行环境。生产环境会限制 start_date 必须小于当天，测试环境允许当天数据反复验证。
		String profile = environment.getProperty(Constants.ACTIVE_PROFILES_PROPERTY);

		// 3. 批量释放：生成奖金记录、批量增加 H 余额、更新释放桶进度。
		handleHGiftReleaseDaily(profile, Integer.valueOf(strDate), new Date());

		// 4. 生产环境在同一事务末尾写入任务标识，防止当天重复执行。
		if (Constants.ACTIVE_PROPERTY_PROD.equalsIgnoreCase(profile)) {
			addTask(SysConstant.TSK_TYPE_106, currentDate + "");
		}
	}

	/**
	 * 执行 H赠送释放每日批量结算。
	 *
	 * <p>生产环境只释放 start_date 小于当天的释放桶，避免 00:00 到任务执行时间之间新建的桶当天被释放。
	 * 释放后写奖金记录，更新 H 钱包余额，并推进释放桶进度；钱包流水不在这里手写，由 Canal 自动生成。</p>
	 */
	private void handleHGiftReleaseDaily(String profile, Integer releaseDay, Date now) {
		// 1. 收集当天可释放的释放桶。冻结、已完成、剩余为 0 的释放桶不参与本次结算。
		List<HGiftReleaseBucket> bucketList = hGiftReleaseBucketService.lambdaQuery()
			.eq(HGiftReleaseBucket::getStatus, H_GIFT_RELEASE_STATUS_RELEASING)
			.lt(Constants.ACTIVE_PROPERTY_PROD.equalsIgnoreCase(profile), HGiftReleaseBucket::getStartDate, releaseDay)
			.gt(HGiftReleaseBucket::getRemainingAmount, BigDecimal.ZERO)
			.list();
		if (CollectionUtil.isEmpty(bucketList)) {
			log.info("H赠送释放每日释放无可释放数据, releaseDay:{}", releaseDay);
			return;
		}

		int batchSize = 1000;
		List<RewardRecord> rewardRecordList = new ArrayList<>(Math.min(bucketList.size(), batchSize));
		List<UserMoney> userMoneyValidNum9List = new ArrayList<>(Math.min(bucketList.size(), batchSize));
		List<HGiftReleaseBucket> updateBucketList = new ArrayList<>(Math.min(bucketList.size(), batchSize));
		int count = 0;

		for (HGiftReleaseBucket bucket : bucketList) {
			// 2. 过滤异常桶；生产环境再次兜底 start_date，防止查询后数据变化导致当天新桶被释放。
			if (bucket.getId() == null || bucket.getUserId() == null || bucket.getStartDate() == null
				|| bucket.getRemainingAmount() == null || bucket.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			if (Constants.ACTIVE_PROPERTY_PROD.equalsIgnoreCase(profile) && bucket.getStartDate() >= releaseDay) {
				continue;
			}

			BigDecimal releaseAmount = calculateHGiftReleaseAmount(bucket);
			if (releaseAmount.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			String gtId = IDUtils.getSnowflakeStr();

			// 3. 奖金记录作为释放事实，后台收益记录按 type_11=H赠送释放 查询。
			RewardRecord rewardRecord = new RewardRecord();
			rewardRecord.setUserId(bucket.getUserId());
			rewardRecord.setAmount(releaseAmount);
			rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_11);
			rewardRecord.setSourceOrderCode(bucket.getBucketNo());
			rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
			rewardRecord.setSourceUserId(bucket.getUserId());
			rewardRecord.setCreateTime(now);
			rewardRecordList.add(rewardRecord);

			// 4. 批量增加 H 余额，gtId/source 信息供 Canal 生成钱包流水。
			UserMoney userMoney = new UserMoney();
			userMoney.setId(bucket.getUserId());
			userMoney.setValidNum9(releaseAmount);
			userMoney.setGtId(gtId);
			userMoney.setSourceCode(bucket.getBucketNo());
			userMoney.setSourceId(bucket.getId());
			userMoney.setSourceType(ConstantType.user_money_log_source_type.type_33);
			userMoney.setUpdateTime(now);
			userMoneyValidNum9List.add(userMoney);

			HGiftReleaseBucket updateBucket = buildHGiftReleaseBucketUpdate(bucket, releaseAmount, releaseDay, now);
			updateBucketList.add(updateBucket);

			// 5. 按固定批量落库，避免一次性堆积过多对象和 SQL 参数。
			count++;
			if (count >= batchSize) {
				batchSettleHGiftRelease(rewardRecordList, userMoneyValidNum9List, updateBucketList);
				rewardRecordList.clear();
				userMoneyValidNum9List.clear();
				updateBucketList.clear();
				count = 0;
			}
		}

		batchSettleHGiftRelease(rewardRecordList, userMoneyValidNum9List, updateBucketList);
		log.info("H赠送释放每日释放完成, releaseDay:{}, bucketCount:{}", releaseDay, bucketList.size());
	}

	/**
	 * 计算单个释放桶当天应释放的 H 数量。
	 *
	 * <p>释放桶由系统创建，金额和天数字段按表默认值与创建逻辑保证有值。普通释放日按
	 * daily_release_amount 释放；最后一天或剩余数量小于每日释放量时释放全部剩余，避免小数尾差残留。</p>
	 */
	private BigDecimal calculateHGiftReleaseAmount(HGiftReleaseBucket bucket) {
		BigDecimal remainingAmount = bucket.getRemainingAmount();
		BigDecimal dailyReleaseAmount = bucket.getDailyReleaseAmount();
		Integer releasedDays = bucket.getReleasedDays();
		Integer releaseDays = bucket.getReleaseDays();
		// 最后一天或尾差小于每日释放量时，释放全部剩余，避免小数尾差残留。
		if (releasedDays + 1 >= releaseDays || remainingAmount.compareTo(dailyReleaseAmount) <= 0) {
			return remainingAmount;
		}
		return dailyReleaseAmount;
	}

	/**
	 * 构造释放后的释放桶更新对象。
	 *
	 * <p>只组装需要批量更新的字段，不直接写库。释放后同步推进已释放数量、剩余数量、释放天数、
	 * 最后释放日期和状态；当达到总释放天数或剩余数量归零时，释放桶状态改为已完成。</p>
	 */
	private HGiftReleaseBucket buildHGiftReleaseBucketUpdate(HGiftReleaseBucket bucket, BigDecimal releaseAmount, Integer releaseDay, Date now) {
		// 1. 计算释放后的累计释放量和剩余量。
		BigDecimal releasedAmount = bucket.getReleasedAmount().add(releaseAmount);
		BigDecimal remainingAmount = bucket.getRemainingAmount().subtract(releaseAmount);
		// 2. 推进释放天数；release_days 由创建释放桶时统一写入 100 天。
		Integer releasedDays = bucket.getReleasedDays() + 1;
		Integer releaseDays = bucket.getReleaseDays();

		// 3. 构造批量更新对象；达到总天数或剩余为 0 时标记完成。
		HGiftReleaseBucket updateBucket = new HGiftReleaseBucket();
		updateBucket.setId(bucket.getId());
		updateBucket.setReleasedAmount(releasedAmount);
		updateBucket.setRemainingAmount(remainingAmount);
		updateBucket.setReleasedDays(releasedDays);
		updateBucket.setLastReleaseDate(releaseDay);
		updateBucket.setStatus(releasedDays >= releaseDays || remainingAmount.compareTo(BigDecimal.ZERO) <= 0
			? H_GIFT_RELEASE_STATUS_COMPLETED : H_GIFT_RELEASE_STATUS_RELEASING);
		updateBucket.setUpdateTime(now);
		return updateBucket;
	}

	private void batchSettleHGiftRelease(List<RewardRecord> rewardRecordList, List<UserMoney> userMoneyValidNum9List,
										 List<HGiftReleaseBucket> updateBucketList) {
		if (CollectionUtil.isEmpty(rewardRecordList)) {
			return;
		}
		if (!rewardRecordService.saveBatch(rewardRecordList)) {
			throw new ServiceException("保存H赠送释放奖励记录失败");
		}
		bachUpdateMoneyValid9(userMoneyValidNum9List);
		batchUpdateHGiftReleaseBucket(updateBucketList);
	}


	/**
	 * 任务类型104 财富仓收益解锁
	 */
	@Override
	public void handelStakeOrder104() {
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		//long类型日期
		long currentDate = Long.parseLong(strDate);

		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_104, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型104 财富仓收益解锁任务已存在跳过");
			return;
		}
		//改造成获取成昨日最高价
		BigDecimal spotPrice = new BigDecimal(bybitMarketService.bybitSpotYesterdayHighPrice().getData().getHighPrice());
		List<WealthVaultStageConfig> list = wealthVaultStageConfigService.lambdaQuery()
			.orderByAsc(WealthVaultStageConfig::getStageNo)
			.list();
		if(CollectionUtil.isEmpty(list)){
			return;
		}

		//找到现在价格在哪个阶段
		Integer stageNo = 0;
		for (WealthVaultStageConfig config : list) {
			if(config.getStageNo() == null || config.getUnlockPrice() == null){
				continue;
			}
			if(spotPrice.compareTo(config.getUnlockPrice()) >= 0){
				stageNo = config.getStageNo();
			}
		}
		if(stageNo>0){
			final Integer currentStageNo = stageNo;
			List<UserWealthVault> unlockUserList = userWealthVaultService.lambdaQuery()
				.and(wrapper -> {
					wrapper.gt(UserWealthVault::getSeg1Amount, BigDecimal.ZERO);
					if(currentStageNo >= 2){
						wrapper.or().gt(UserWealthVault::getSeg2Amount, BigDecimal.ZERO);
					}
					if(currentStageNo >= 3){
						wrapper.or().gt(UserWealthVault::getSeg3Amount, BigDecimal.ZERO);
					}
					if(currentStageNo >= 4){
						wrapper.or().gt(UserWealthVault::getSeg4Amount, BigDecimal.ZERO);
					}
					if(currentStageNo >= 5){
						wrapper.or().gt(UserWealthVault::getSeg5Amount, BigDecimal.ZERO);
					}
				})
				.list();
			if(CollectionUtil.isNotEmpty(unlockUserList)){
				List<RewardRecord> rewardRecordList = new ArrayList<>(unlockUserList.size());
				List<UserMoney> userMoneyValidNum4List = new ArrayList<>(unlockUserList.size()>1000?unlockUserList.size():1000);
				int batchSize = 1000;
				int stakeCount1 = 0;
				String sourceCode = IDUtils.getSnowflakeStr();
				for (UserWealthVault userWealthVault : unlockUserList) {
					//算出扣了多少财富仓金额
					BigDecimal deductAmount = BigDecimal.ZERO;
					if(currentStageNo>=1){
						deductAmount = deductAmount.add(userWealthVault.getSeg1Amount());
					}
					if(currentStageNo>=2){
						deductAmount = deductAmount.add(userWealthVault.getSeg2Amount());
					}
					if(currentStageNo>=3){
						deductAmount = deductAmount.add(userWealthVault.getSeg3Amount());
					}
					if(currentStageNo>=4){
						deductAmount = deductAmount.add(userWealthVault.getSeg4Amount());
					}
					if(currentStageNo>=5){
						deductAmount = deductAmount.add(userWealthVault.getSeg5Amount());
					}
					if(deductAmount.compareTo(BigDecimal.ZERO) <= 0){
						continue;
					}
					userWealthVaultService.lambdaUpdate()
						.eq(UserWealthVault::getId, userWealthVault.getId())
						.setSql(currentStageNo>=1,"seg1_amount = seg1_amount - "+userWealthVault.getSeg1Amount() )
						.setSql(currentStageNo>=2,"seg2_amount = seg2_amount - "+userWealthVault.getSeg2Amount() )
						.setSql(currentStageNo>=3,"seg3_amount = seg3_amount - "+userWealthVault.getSeg3Amount() )
						.setSql(currentStageNo>=4,"seg4_amount = seg4_amount - "+userWealthVault.getSeg4Amount() )
						.setSql(currentStageNo>=5,"seg5_amount = seg5_amount - "+userWealthVault.getSeg5Amount() )
						.set(UserWealthVault::getUpdateTime, new Date())
						.update();

					UserMoney entity = new UserMoney();
					entity.setId(userWealthVault.getId());
					entity.setValidNum4(deductAmount);
					entity.setGtId(IDUtils.getSnowflakeStr());
					entity.setSourceCode(sourceCode);
					entity.setSourceId(userWealthVault.getId());
					entity.setSourceType(ConstantType.user_money_log_source_type.type_14);
					entity.setUpdateTime(new Date());
					userMoneyValidNum4List.add(entity);
					stakeCount1++;
					if (stakeCount1 >= batchSize) {
						bachUpdateMoneyValid4(userMoneyValidNum4List);
						userMoneyValidNum4List.clear();
						log.info("更新成功");
						stakeCount1 = 0;
					}

					RewardRecord rewardRecord = new RewardRecord();
					rewardRecord.setUserId(userWealthVault.getId());
					rewardRecord.setAmount(deductAmount);
					rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_10);
					rewardRecord.setSourceOrderCode(sourceCode);
					rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
					rewardRecord.setSourceUserId(userWealthVault.getId());
					rewardRecord.setCreateTime(new Date());
					rewardRecordList.add(rewardRecord);
				}

				if (CollectionUtil.isNotEmpty(userMoneyValidNum4List)) {
					bachUpdateMoneyValid4(userMoneyValidNum4List);
				}

				if (CollectionUtil.isNotEmpty(rewardRecordList)) {
					rewardRecordService.saveBatch(rewardRecordList);
				}
			}
		}

		int i = addTask(SysConstant.TSK_TYPE_104, currentDate + "");
		if (i != 1) {
			throw new RuntimeException("任务类型104 财富仓收益解锁任务失败");
		}
	}

	/**
	 * 任务类型103 每日计算日质押的日利率
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handelStakeOrder103() {
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		//long类型日期
		long currentDate = Long.parseLong(strDate);

		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_103, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型103 每日计算日质押的日利率任务已存在跳过");
			return;
		}
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		if(stakeRound != null){
			userStakePositionService.lambdaUpdate()
				.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
				.setSql("continuous_no_withdraw_days = continuous_no_withdraw_days +1")
				.update();

			List<UserStakePosition> userStakePositionList = userStakePositionService.lambdaQuery()
				.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
				.list();
			if(CollectionUtil.isNotEmpty(userStakePositionList)){
				UserYieldRateConfig rateConfig = userYieldRateConfigService.lambdaQuery()
					.last("limit 1")
					.one();
				if(rateConfig != null
					&& rateConfig.getGrowthRateStep() != null
					&& rateConfig.getGrowthConsecutiveDays() != null
					&& rateConfig.getGrowthConsecutiveDays() > 0
					&& rateConfig.getMaxDailyRate() != null){
					Date now = new Date();
					List<UserStakePosition> updateList = new ArrayList<>();
					for (UserStakePosition userStakePosition : userStakePositionList) {
						Integer continuousNoWithdrawDays = userStakePosition.getContinuousNoWithdrawDays();
						if(continuousNoWithdrawDays == null || continuousNoWithdrawDays <= 0){
							continue;
						}
						// 连续未提现天数达到配置天数的倍数时，日利率增加一次，但不超过上限
						if(continuousNoWithdrawDays % rateConfig.getGrowthConsecutiveDays() != 0){
							continue;
						}
						BigDecimal currentDayRate = userStakePosition.getCurrentDayRate();
						if(currentDayRate == null){
							continue;
						}
						BigDecimal newDayRate = currentDayRate.add(rateConfig.getGrowthRateStep());
						if(newDayRate.compareTo(rateConfig.getMaxDailyRate()) > 0){
							newDayRate = rateConfig.getMaxDailyRate();
						}
						if(newDayRate.compareTo(currentDayRate) == 0){
							continue;
						}
						userStakePosition.setCurrentDayRate(newDayRate);
						userStakePosition.setUpdateTime(now);
						updateList.add(userStakePosition);
					}
					if(CollectionUtil.isNotEmpty(updateList)){
						userStakePositionService.updateBatchById(updateList);
					}
				}
			}
		}

		int i = addTask(SysConstant.TSK_TYPE_103, currentDate + "");
		if (i != 1) {
			throw new RuntimeException("任务类型103 每日计算日质押的日利率任务失败");
		}
	}

	/**
	 * 任务类型102 每日释放保险仓利润
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handelStakeOrder102() {
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		//long类型日期
		long currentDate = Long.parseLong(strDate);

		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_102, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型102 每日释放保险仓利润任务已存在跳过");
			return;
		}
		insuranceOrderService.lambdaUpdate()
			.eq(InsuranceOrder::getStatus, 0)
			.setSql("have_days = have_days -1")
			.set(InsuranceOrder::getUpdateTime,new Date())
			.update();

		List<InsuranceOrder> orderList = insuranceOrderService.lambdaQuery()
			.eq(InsuranceOrder::getStatus, 0)
			.list();
		if(CollectionUtil.isNotEmpty(orderList)){
			for (InsuranceOrder insuranceOrder : orderList) {

				List<UserStakePosition> userStakePositionList = userStakePositionService.lambdaQuery()
					.eq(UserStakePosition::getStakeRoundId, insuranceOrder.getStakeRoundId())
					.eq(UserStakePosition::getInsuranceCompensationQualifyStatus, 1)
					.gt(UserStakePosition::getRemainingCompensationLimit, 0)
					.list();

				BigDecimal releaseAmount = insuranceOrder.getDayOutReward();
				if(insuranceOrder.getHaveDays() == 0
					&& insuranceOrder.getHsaveInsuranceBalance().compareTo(releaseAmount) > 0){
					// 最后一天如果剩余待释放余额大于日产出，则直接把剩余待释放余额全部释放
					releaseAmount = insuranceOrder.getHsaveInsuranceBalance();
				}
				if(releaseAmount != null && releaseAmount.compareTo(BigDecimal.ZERO)>0){
					// 当日释放总金额，按个人亏损额加权分配，且每人不超过剩余可赔付
					insuranceOrderService.lambdaUpdate()
						.eq(InsuranceOrder::getId, insuranceOrder.getId())
						.setSql("hsave_insurance_balance = hsave_insurance_balance - " + releaseAmount)
						.update();
					if(CollectionUtil.isNotEmpty(userStakePositionList)){
						// 总权重 = 所有合格用户的个人亏损额之和
						BigDecimal totalPersonalLoss = userStakePositionList.stream()
							.map(UserStakePosition::getPersonalLossAmount)
							.filter(a -> a != null && a.compareTo(BigDecimal.ZERO) > 0)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
						Date now = new Date();
						int batchSize = 1000;
						int compensationCount = 0;
						List<UserMoney> compensationMoneyList = new ArrayList<>(Math.min(userStakePositionList.size(), batchSize));
						List<RewardRecord> compensationRecordList = new ArrayList<>(Math.min(userStakePositionList.size(), batchSize));
						List<UserStakePosition> positionUpdateList = new ArrayList<>(Math.min(userStakePositionList.size(), batchSize));
						for (UserStakePosition record : userStakePositionList) {
							BigDecimal personalLoss = record.getPersonalLossAmount();
							if(personalLoss == null || personalLoss.compareTo(BigDecimal.ZERO) <= 0){
								continue;
							}
							// 按亏损额加权：应得 = 日释放 * (个人亏损额 / 总亏损额)
							BigDecimal rawShare = releaseAmount.multiply(personalLoss)
								.divide(totalPersonalLoss, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
							// 实际发放不超过剩余可赔付
							BigDecimal actualCompensation = rawShare.min(record.getRemainingCompensationLimit());
							if(actualCompensation.compareTo(BigDecimal.ZERO) <= 0){
								continue;
							}
							// 发放到用户保险仓余额 validNum5
							UserMoney entity = new UserMoney();
							entity.setId(record.getUserId());
							entity.setValidNum5(actualCompensation);
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(insuranceOrder.getOrderNo());
							entity.setSourceId(record.getUserId());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_13);
							entity.setUpdateTime(now);
							compensationMoneyList.add(entity);

							// 奖金记录
							RewardRecord rewardRecord = new RewardRecord();
							rewardRecord.setUserId(record.getUserId());
							rewardRecord.setAmount(actualCompensation);
							rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_9);
							rewardRecord.setSourceOrderCode(insuranceOrder.getOrderNo());
							rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_5);
							rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
							rewardRecord.setSourceUserId(record.getUserId());
							rewardRecord.setCreateTime(now);
							compensationRecordList.add(rewardRecord);
							// 扣减剩余可赔付
							record.setRemainingCompensationLimit(
								record.getRemainingCompensationLimit().subtract(actualCompensation).setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
							record.setUpdateTime(now);
							positionUpdateList.add(record);

							compensationCount++;
							if(compensationCount >= batchSize){
								bachUpdateMoneyValid5(compensationMoneyList);
								compensationMoneyList.clear();
								rewardRecordService.saveBatch(compensationRecordList);
								compensationRecordList.clear();
								userStakePositionService.updateBatchById(positionUpdateList);
								positionUpdateList.clear();
								compensationCount = 0;
							}
						}

						if(CollectionUtil.isNotEmpty(compensationMoneyList)){
							bachUpdateMoneyValid5(compensationMoneyList);
						}
						if(CollectionUtil.isNotEmpty(compensationRecordList)){
							rewardRecordService.saveBatch(compensationRecordList);
						}
						if(CollectionUtil.isNotEmpty(positionUpdateList)){
							userStakePositionService.updateBatchById(positionUpdateList);
						}
					}
				}

			}
		}
		insuranceOrderService.lambdaUpdate()
			.eq(InsuranceOrder::getStatus, 0)
			.le(InsuranceOrder::getHaveDays,0 )
			.set(InsuranceOrder::getStatus,1)
			.update();

		int i = addTask(SysConstant.TSK_TYPE_102, currentDate + "");
		if (i != 1) {
			throw new RuntimeException("任务类型102 每日释放保险仓利润任务失败");
		}

	}

	/**
	 * 任务类型101 处理质押订单收益
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handelStakeOrder() {
		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		//long类型日期
		long currentDate = Long.parseLong(strDate);

		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_101, currentDate + "");
		if (!CollectionUtil.isEmpty(task)) {
			log.debug("任务类型101 处理质押订单收益任务已存在跳过");
			return;
		}

		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();

		if (stakeRound != null) {
			List<UserMoney> userMoney7List = userMoneyService.lambdaQuery()
				.list();
			Map<Long, BigDecimal> userMoney7Map = userMoney7List.stream().collect(Collectors.toMap(UserMoney::getId, UserMoney::getValidNum7, (k1, k2) -> k2));


			//处理质押订单收益任务
			List<UserStakePosition> userStakePositionList = userStakePositionService.lambdaQuery()
				.eq(UserStakePosition::getStatus, 1)
				.gt(UserStakePosition::getTotalStakeAmount,BigDecimal.ZERO)
				.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
				.list();
			if (CollectionUtil.isNotEmpty(userStakePositionList)) {
				//静态
				List<UserMoney> userMoneyValidNum2List = new ArrayList<>(userStakePositionList.size());
				List<RewardRecord> staticRewardRecordList = new ArrayList<>(userStakePositionList.size());
				int batchSize = 1000;
				int stakeCount1 = 0;
				Date now = new Date();
				UserMoney entity = null;
				RewardRecord rewardRecord = null;
				for (UserStakePosition userStakePosition : userStakePositionList) {
					if (userStakePosition.getCurrentDayRate().compareTo(BigDecimal.ZERO) <= 0 ||
						userStakePosition.getTotalStakeAmount().compareTo(BigDecimal.ZERO) <= 0) {
						log.debug("质押订单静态收益率异常", userStakePosition);
						continue;
					}
					BigDecimal staticReward = userStakePosition.getCurrentDayRate()
						.multiply(userStakePosition.getTotalStakeAmount())
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
						.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (staticReward.compareTo(BigDecimal.ZERO) <= 0) {
						log.debug("质押订单静态收益异常", userStakePosition);
						continue;
					}

					//更新日收益
					userStakePositionService.lambdaUpdate()
						.eq(UserStakePosition::getId, userStakePosition.getId())
						.setSql("total_reward = total_reward + " + staticReward)
						.set(UserStakePosition::getTodayReward,staticReward)
						.update();

					stakeCount1++;
					entity = new UserMoney();
					entity.setId(userStakePosition.getUserId());
					entity.setValidNum2(staticReward);
					entity.setGtId(IDUtils.getSnowflakeStr());
					entity.setSourceCode(userStakePosition.getOrderNo());
					entity.setSourceId(userStakePosition.getUserId());
					entity.setSourceType(ConstantType.user_money_log_source_type.type_3);
					entity.setUpdateTime(new Date());
					userMoneyValidNum2List.add(entity);

					if (stakeCount1 >= batchSize) {
						bachUpdateMoneyValid2(userMoneyValidNum2List);
						userMoneyValidNum2List.clear();
						log.info("更新成功");
						stakeCount1 = 0;
					}

					rewardRecord = new RewardRecord();
					rewardRecord.setUserId(userStakePosition.getUserId());
					rewardRecord.setAmount(staticReward);
					rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_2);
					rewardRecord.setSourceOrderCode(userStakePosition.getOrderNo());
					rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
					rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
					rewardRecord.setSourceUserId(userStakePosition.getUserId());
					rewardRecord.setCreateTime(now);
					staticRewardRecordList.add(rewardRecord);
				}

				if (CollectionUtil.isNotEmpty(userMoneyValidNum2List)) {
					bachUpdateMoneyValid2(userMoneyValidNum2List);
				}
				if (CollectionUtil.isNotEmpty(staticRewardRecordList)) {
					//插入奖金记录
					rewardRecordService.saveBatch(staticRewardRecordList);
					Map<Long, BigDecimal> userMoney3Map = new HashMap<>();
					Map<Integer, UserInvestLayerConfig> layerConfigMap = userInvestLayerConfigService.lambdaQuery()
						.gt(UserInvestLayerConfig::getLevel,0)
						.orderByDesc(UserInvestLayerConfig::getLevel)
						.list()
						.stream().map(record -> {
							if (record.getRewardRatio() != null) {
								record.setRewardRatio(record.getRewardRatio().divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew));
							}
							return record;
						})
						.collect(Collectors.toMap(UserInvestLayerConfig::getLevel, Function.identity()));
					BigDecimal layerRewardRatio = layerConfigMap.get(1).getRewardRatio();
					Map<Integer, UserLevelConfig> levelConfigMap = userLevelConfigService.lambdaQuery()
						.orderByAsc(UserLevelConfig::getLevel)
						.list().stream()
						.map(record -> {
							record.setRewardRatio(record.getRewardRatio().divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew));
							record.setPeerRewardRatio(record.getPeerRewardRatio().divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew));
							return record;
						}).collect(Collectors.toMap(UserLevelConfig::getLevel, Function.identity(), (o1, o2) -> o1));

					Map<Long, List<ParentUserTaskVo>> parentUserTaskVoMap = new HashMap<>();
					for (RewardRecord record : staticRewardRecordList) {
						List<ParentUserTaskVo> parentUserTaskVo = userInfoService.getParentUserTaskVo(record.getUserId())
							.stream().peek(
								item->{
									item.setGameLevel(Math.max(item.getGameLevel(), item.getMinGameLevel()));
									item.setNodeLevel(Math.max(item.getNodeLevel(), item.getMinNodeLevel()));
									item.setLayerLevel(Math.max(item.getLayerLevel(), item.getMinLayerLevel()));
								}
							)
							.collect(Collectors.toList())
							;
						parentUserTaskVoMap.put(record.getUserId(), parentUserTaskVo);
					}

					RewardRecord dynamicRewardRecordEntity = null;
					List<RewardRecord> dynamicRewardRecordList = new ArrayList<>(20);
					List<UserMoney> userMoneyValidNum3List = new ArrayList<>(20);
					List<UserMoney> userMoneyValidNum7List = new ArrayList<>(20);
					stakeCount1 = 0;
					int[] rewardBatchCountHolder = new int[]{stakeCount1};
					//发放层级奖、极差奖
					for (RewardRecord record : staticRewardRecordList) {
						if (parentUserTaskVoMap.containsKey(record.getUserId())) {
							List<ParentUserTaskVo> parentUserTaskVos = parentUserTaskVoMap.get(record.getUserId());
							if (CollectionUtil.isNotEmpty(parentUserTaskVos)) {

								// 上一个拿到级差奖的用户
								ParentUserTaskVo lastRewardUser = null;
								// 上一个级差奖金额
								BigDecimal lastRewardAmount = BigDecimal.ZERO;
								// 上一个拿奖用户的等级
								Integer lastRewardLevel = null;
								// 已发放的累积比例
								BigDecimal initRewardRatio = BigDecimal.ZERO;
								// 上一次发奖等级 & 金额（用于超越奖）
								Integer beforeLevel = 0;
								BigDecimal beforeReward = BigDecimal.ZERO;
								// 是否已触发超越奖
								boolean exceedAwardFlag = true;
								// 必须先拿一次极差，才允许再拿一次平级；平级发完后需等待新的极差再次开启资格
								boolean canPeerReward = false;
								Integer layerLevel = 1;
								for (ParentUserTaskVo parentEntity : parentUserTaskVos) {
									if (parentEntity.getIsValid() == 0) continue;
									parentEntity.setLayerLevel(parentEntity.getLayerLevel()>parentEntity.getMinLayerLevel()?parentEntity.getLayerLevel():parentEntity.getMinLayerLevel());
									if(layerLevel>=10){
										continue;
									}
									if(parentEntity.getLayerLevel()>=layerLevel){
										BigDecimal layerReward = record.getAmount().multiply(layerRewardRatio)
											.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
										BigDecimal userValidNum7 = userMoney7Map.get(parentEntity.getUserId());
										userValidNum7 = userValidNum7 == null ? BigDecimal.ZERO : userValidNum7;
										BigDecimal actualLayerReward = layerReward.min(userValidNum7);

										if (actualLayerReward.compareTo(BigDecimal.ZERO) > 0) {
											dynamicRewardRecordEntity = new RewardRecord();
											dynamicRewardRecordEntity.setOrderCode(IDUtils.getSnowflakeStr());
											dynamicRewardRecordEntity.setUserId(parentEntity.getUserId());
											dynamicRewardRecordEntity.setAmount(actualLayerReward);
											dynamicRewardRecordEntity.setCoinType(ConstantType.user_money_coin_type.type_3);
											dynamicRewardRecordEntity.setSourceType(ConstantType.xms_reward_record_source_type.type_5);
											dynamicRewardRecordEntity.setSourceUserId(record.getSourceUserId());
											dynamicRewardRecordEntity.setSourceOrderCode(record.getSourceOrderCode());
											dynamicRewardRecordEntity.setGtId(IDUtils.getSnowflakeStr());
											dynamicRewardRecordList.add(dynamicRewardRecordEntity);

											entity = new UserMoney();
											entity.setId(parentEntity.getUserId());
											entity.setValidNum3(actualLayerReward);
											entity.setGtId(IDUtils.getSnowflakeStr());
											entity.setSourceCode(record.getSourceOrderCode());
											entity.setSourceId(record.getSourceUserId());
											entity.setSourceType(ConstantType.user_money_log_source_type.type_6);
											entity.setUpdateTime(new Date());
											userMoneyValidNum3List.add(entity);
											stakeCount1++;
											if (stakeCount1 >= batchSize) {
												bachUpdateMoneyValid3(userMoneyValidNum3List);
												userMoneyValidNum3List.clear();
												log.info("更新成功");
												stakeCount1 = 0;
											}

											UserMoney deductValidNum7 = new UserMoney();
											deductValidNum7.setId(parentEntity.getUserId());
											deductValidNum7.setValidNum7(actualLayerReward.negate());
											deductValidNum7.setGtId(IDUtils.getSnowflakeStr());
											deductValidNum7.setSourceCode(record.getSourceOrderCode());
											deductValidNum7.setSourceId(record.getSourceUserId());
											deductValidNum7.setSourceType(ConstantType.user_money_log_source_type.type_17);
											deductValidNum7.setUpdateTime(new Date());
											userMoneyValidNum7List.add(deductValidNum7);
											userMoney7Map.put(parentEntity.getUserId(), userValidNum7.subtract(actualLayerReward));
											userMoney3Map.merge(parentEntity.getUserId(), actualLayerReward, BigDecimal::add);
										}
										layerLevel++;
									}
								}

								for (ParentUserTaskVo p : parentUserTaskVos) {
									// 无效用户不参与
									if (p.getIsValid() == 0) {
										continue;
									}

									int gameLevel = p.getGameLevel() > p.getMinGameLevel() ? p.getGameLevel() : p.getMinGameLevel();
									UserLevelConfig cfg = levelConfigMap.get(gameLevel);

									if (cfg == null || cfg.getRewardRatio() == null
										|| cfg.getRewardRatio().compareTo(BigDecimal.ZERO) <= 0) {
										continue;
									}

									BigDecimal finalRewardRatio = cfg.getRewardRatio().subtract(initRewardRatio);


									// 1) 极差奖
									if (finalRewardRatio.compareTo(BigDecimal.ZERO) > 0) {
										BigDecimal teamReward = record.getAmount().multiply(finalRewardRatio)
											.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
										BigDecimal actualTeamReward = issueDiffOrPeerReward(
											p.getUserId(),
											teamReward,
											ConstantType.xms_reward_record_source_type.type_3,
											ConstantType.user_money_log_source_type.type_4,
											record,
											dynamicRewardRecordList,
											userMoneyValidNum3List,
											userMoneyValidNum7List,
											userMoney7Map,
											userMoney3Map,
											rewardBatchCountHolder,
											batchSize);
										if (actualTeamReward.compareTo(BigDecimal.ZERO) > 0) {
											// 更新极差状态
											initRewardRatio = cfg.getRewardRatio();
											lastRewardUser = p;
											lastRewardAmount = actualTeamReward;
											lastRewardLevel = gameLevel;
											canPeerReward = true;
											if (gameLevel > 0) {
												beforeLevel = gameLevel;
											}
										}

										continue;
									}

									// 2) 平级奖（仅在没有新增极差时触发，且必须先有一次极差成功发放）
										if (canPeerReward
											&& lastRewardLevel != null
											&& gameLevel <= lastRewardLevel
											&& cfg.getPeerRewardRatio().compareTo(BigDecimal.ZERO) > 0) {
											BigDecimal peerReward = lastRewardAmount.multiply(cfg.getPeerRewardRatio())
												.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
											BigDecimal actualPeerReward = issueDiffOrPeerReward(
												p.getUserId(),
												peerReward,
												ConstantType.xms_reward_record_source_type.type_4,
												ConstantType.user_money_log_source_type.type_5,
												record,
												dynamicRewardRecordList,
												userMoneyValidNum3List,
												userMoneyValidNum7List,
												userMoney7Map,
												userMoney3Map,
												rewardBatchCountHolder,
												batchSize);
											if (actualPeerReward.compareTo(BigDecimal.ZERO) > 0) {
												canPeerReward = false;
											}

										}

									// 3) 超越奖（finalRewardRatio < 0）
//									if (finalRewardRatio.compareTo(BigDecimal.ZERO) < 0 && exceedAwardFlag) {
//										if (gameLevel < beforeLevel && beforeReward.compareTo(BigDecimal.ZERO) > 0
//											&& exceedAwardRatio != null && exceedAwardRatio.compareTo(BigDecimal.ZERO) > 0) {
//
//										}
//									}

								}
							}
						}
					}

					stakeCount1 = rewardBatchCountHolder[0];
					//插入钱包流水
					if (CollectionUtil.isNotEmpty(userMoneyValidNum3List)) {
						bachUpdateMoneyValid3(userMoneyValidNum3List);
					}
					if (CollectionUtil.isNotEmpty(userMoneyValidNum7List)) {
						bachUpdateMoneyValid7(userMoneyValidNum7List);
					}

					//插入奖金记录
					if(CollectionUtil.isNotEmpty(dynamicRewardRecordList)){
						rewardRecordService.saveBatch(dynamicRewardRecordList);
					}

					BigDecimal totalStaticReward = staticRewardRecordList.stream()
						.map(RewardRecord::getAmount)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
					if(totalStaticReward.compareTo(BigDecimal.ZERO) > 0){
						String sourceCode = IDUtils.getSnowflakeStr();
						//1.发送节点权益分红
						sendNodeUserReward(totalStaticReward,sourceCode, userMoney3Map);
						//2.新增奖励
						sendStakeNewReward(totalStaticReward,sourceCode, userMoney3Map);
					}
					//遍历userMoney3Map
					if(CollectionUtil.isNotEmpty(userMoney3Map)){
						for(Map.Entry<Long, BigDecimal> entry : userMoney3Map.entrySet()){
							userStakePositionService.lambdaUpdate()
								.eq(UserStakePosition::getUserId, entry.getKey())
								.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
								.setSql("dynamic_reward = dynamic_reward + " + entry.getValue())
								.update();
						}
					}
				}
			}

			int i = addTask(SysConstant.TSK_TYPE_101, currentDate + "");
			if (i != 1) {
				throw new RuntimeException("任务类型101 处理质押订单收益任务失败");
			}
		}
	}

	/**
	 * 新增奖励
	 * @param totalStaticReward
	 */
	private void sendStakeNewReward(BigDecimal totalStaticReward,String sourceCode, Map<Long, BigDecimal> userMoney3Map) {
		BigDecimal stakeNewRewardPoolRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_stake_new_reward_pool_ratio));
		if(stakeNewRewardPoolRatio.compareTo(BigDecimal.ZERO)>0){
			stakeNewRewardPoolRatio = stakeNewRewardPoolRatio.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);

			//总的奖励
			BigDecimal  stakeNewReward = totalStaticReward.multiply(stakeNewRewardPoolRatio)
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if(stakeNewReward.compareTo(BigDecimal.ZERO) <= 0){
				return;
			}
			List<StakeOrder> yesterDayOrderList = stakeOrderService.lambdaQuery()
				.eq(StakeOrder::getCreateDay, Integer.valueOf(DateUtil.format(DateUtil.yesterday(), "yyyyMMdd")))
				.gt(StakeOrder::getBelongUserId, 0)
				.list();

			if(CollectionUtil.isNotEmpty(yesterDayOrderList)){
				//昨日新增总业绩
				BigDecimal yesterDayStakeAmount = yesterDayOrderList.stream()
					.map(order -> order.getStakeAmount() == null ? BigDecimal.ZERO : order.getStakeAmount())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				if(yesterDayStakeAmount.compareTo(BigDecimal.ZERO) <= 0){
					return;
				}
				// 按归属上级分组，汇总每个用户昨日新增业绩。
				Map<Long, BigDecimal> userStakeAmountMap = new HashMap<>();
				for (StakeOrder order : yesterDayOrderList) {
					if(order.getBelongUserId() == null){
						continue;
					}
					BigDecimal stakeAmount = order.getStakeAmount() == null ? BigDecimal.ZERO : order.getStakeAmount();
					userStakeAmountMap.merge(order.getBelongUserId(), stakeAmount, BigDecimal::add);
				}
				List<UserMoney> userMoneyValidNum3List = new ArrayList<>(userStakeAmountMap.size() > 1000 ? 1000 : userStakeAmountMap.size());
				List<UserMoney> userMoneyValidNum7List = new ArrayList<>(userStakeAmountMap.size() > 1000 ? 1000 : userStakeAmountMap.size());
				List<RewardRecord> dynamicRewardRecordList = new ArrayList<>(userStakeAmountMap.size() > 1000 ? 1000 : userStakeAmountMap.size());
				Map<Long, BigDecimal> userMoney7Map = userMoneyService.lambdaQuery()
					.list()
					.stream()
					.collect(Collectors.toMap(UserMoney::getId,
						item -> item.getValidNum7() == null ? BigDecimal.ZERO : item.getValidNum7(), (k1, k2) -> k2));
				int batchSize = 1000;
				int rewardCount = 0;
				Date now = new Date();
				Integer moneySourceType = ConstantType.user_money_log_source_type.type_9;
				Integer rewardSourceType =  ConstantType.xms_reward_record_source_type.type_8;
				// 昨日每个人的新增业绩占比 * 新增奖励池 = 该用户新增奖励
				for (Map.Entry<Long, BigDecimal> entry : userStakeAmountMap.entrySet()) {
					BigDecimal userStakeAmount = entry.getValue() == null ? BigDecimal.ZERO : entry.getValue();
					if(userStakeAmount.compareTo(BigDecimal.ZERO) <= 0){
						continue;
					}

					BigDecimal rewardAmount = userStakeAmount
						.divide(yesterDayStakeAmount, ConstantStatic.newScale, ConstantStatic.roundingModeNew)
						.multiply(stakeNewReward)
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					log.info("新增奖励 总的奖励:{},yesterDayStakeAmount:{},userStakeAmount:{},rewardAmount:{},userId:{}",
						stakeNewReward
						,yesterDayStakeAmount
					,userStakeAmount,rewardAmount,entry.getKey());
					BigDecimal userValidNum7 = userMoney7Map.get(entry.getKey());
					userValidNum7 = userValidNum7 == null ? BigDecimal.ZERO : userValidNum7;
					BigDecimal actualRewardAmount = rewardAmount.min(userValidNum7);
					if(actualRewardAmount.compareTo(BigDecimal.ZERO) <= 0){
						continue;
					}

					UserMoney entity = new UserMoney();
					entity.setId(entry.getKey());
					entity.setValidNum3(actualRewardAmount);
					entity.setGtId(IDUtils.getSnowflakeStr());
					entity.setSourceCode(sourceCode);
					entity.setSourceId(entry.getKey());
					entity.setSourceType(moneySourceType);
					entity.setUpdateTime(now);
					userMoneyValidNum3List.add(entity);

					UserMoney deductValidNum7 = new UserMoney();
					deductValidNum7.setId(entry.getKey());
					deductValidNum7.setValidNum7(actualRewardAmount.negate());
					deductValidNum7.setGtId(IDUtils.getSnowflakeStr());
					deductValidNum7.setSourceCode(sourceCode);
					deductValidNum7.setSourceId(entry.getKey());
					deductValidNum7.setSourceType(ConstantType.user_money_log_source_type.type_17);
					deductValidNum7.setUpdateTime(now);
					userMoneyValidNum7List.add(deductValidNum7);

					RewardRecord rewardRecord = new RewardRecord();
					rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
					rewardRecord.setUserId(entry.getKey());
					rewardRecord.setAmount(actualRewardAmount);
					rewardRecord.setBusinessType(ConstantType.xms_reward_record_business_type.type_2);
					rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_3);
					rewardRecord.setSourceType(rewardSourceType);
					rewardRecord.setSourceOrderCode(sourceCode);
					rewardRecord.setSourceUserId(entry.getKey());
					rewardRecord.setCreateTime(now);
					dynamicRewardRecordList.add(rewardRecord);
					userMoney7Map.put(entry.getKey(), userValidNum7.subtract(actualRewardAmount));
					userMoney3Map.merge(entry.getKey(), actualRewardAmount, BigDecimal::add);

					rewardCount++;
					if(rewardCount >= batchSize){
						bachUpdateMoneyValid3(userMoneyValidNum3List);
						bachUpdateMoneyValid7(userMoneyValidNum7List);
						userMoneyValidNum3List.clear();
						userMoneyValidNum7List.clear();
						rewardRecordService.saveBatch(dynamicRewardRecordList);
						dynamicRewardRecordList.clear();
						rewardCount = 0;
					}
				}
				if(CollectionUtil.isNotEmpty(userMoneyValidNum3List)){
					bachUpdateMoneyValid3(userMoneyValidNum3List);
				}
				if(CollectionUtil.isNotEmpty(userMoneyValidNum7List)){
					bachUpdateMoneyValid7(userMoneyValidNum7List);
				}
				if(CollectionUtil.isNotEmpty(dynamicRewardRecordList)){
					rewardRecordService.saveBatch(dynamicRewardRecordList);
				}

			}
		}
	}

	private BigDecimal issueDiffOrPeerReward(Long userId, BigDecimal rewardAmount, Integer rewardSourceType,
											 Integer moneySourceType, RewardRecord sourceRecord,
											 List<RewardRecord> dynamicRewardRecordList,
											 List<UserMoney> userMoneyValidNum3List,
											 List<UserMoney> userMoneyValidNum7List,
											 Map<Long, BigDecimal> userMoney7Map,
											 Map<Long, BigDecimal> userMoney3Map,
											 int[] rewardBatchCountHolder, int batchSize) {
		BigDecimal userValidNum7 = userMoney7Map.get(userId);
		userValidNum7 = userValidNum7 == null ? BigDecimal.ZERO : userValidNum7;
		BigDecimal actualReward = rewardAmount.min(userValidNum7);
		if (actualReward.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		}

		Date now = new Date();

		RewardRecord dynamicRewardRecordEntity = new RewardRecord();
		dynamicRewardRecordEntity.setOrderCode(IDUtils.getSnowflakeStr());
		dynamicRewardRecordEntity.setUserId(userId);
		dynamicRewardRecordEntity.setAmount(actualReward);
		dynamicRewardRecordEntity.setCoinType(ConstantType.user_money_coin_type.type_3);
		dynamicRewardRecordEntity.setSourceType(rewardSourceType);
		dynamicRewardRecordEntity.setSourceUserId(sourceRecord.getSourceUserId());
		dynamicRewardRecordEntity.setSourceOrderCode(sourceRecord.getSourceOrderCode());
		dynamicRewardRecordEntity.setGtId(IDUtils.getSnowflakeStr());
		dynamicRewardRecordList.add(dynamicRewardRecordEntity);

		UserMoney entity = new UserMoney();
		entity.setId(userId);
		entity.setValidNum3(actualReward);
		entity.setGtId(IDUtils.getSnowflakeStr());
		entity.setSourceCode(sourceRecord.getSourceOrderCode());
		entity.setSourceId(sourceRecord.getSourceUserId());
		entity.setSourceType(moneySourceType);
		entity.setUpdateTime(now);
		userMoneyValidNum3List.add(entity);
		rewardBatchCountHolder[0]++;
		if (rewardBatchCountHolder[0] >= batchSize) {
			bachUpdateMoneyValid3(userMoneyValidNum3List);
			userMoneyValidNum3List.clear();
			log.info("更新成功");
			rewardBatchCountHolder[0] = 0;
		}

		UserMoney deductValidNum7 = new UserMoney();
		deductValidNum7.setId(userId);
		deductValidNum7.setValidNum7(actualReward.negate());
		deductValidNum7.setGtId(IDUtils.getSnowflakeStr());
		deductValidNum7.setSourceCode(sourceRecord.getSourceOrderCode());
		deductValidNum7.setSourceId(sourceRecord.getSourceUserId());
		deductValidNum7.setSourceType(ConstantType.user_money_log_source_type.type_17);
		deductValidNum7.setUpdateTime(now);
		userMoneyValidNum7List.add(deductValidNum7);

		userMoney7Map.put(userId, userValidNum7.subtract(actualReward));
		userMoney3Map.merge(userId, actualReward, BigDecimal::add);
		return actualReward;
	}

	private void sendNodeUserReward(BigDecimal totalStaticReward,String sourceCode, Map<Long, BigDecimal> userMoney3Map) {
		//节点用户全网分红
		BigDecimal ratio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_global_static_income_ratio))
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal globalStaticReward = totalStaticReward.multiply(ratio)
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		//查询
		List<UserInfo> userInfos = userInfoService.lambdaQuery()
			.and(wrapper -> wrapper.gt(UserInfo::getNodeLevel, 0)
				.or()
				.gt(UserInfo::getMinNodeLevel, 0))
			.select(UserInfo::getGameLevel, UserInfo::getUserId, UserInfo::getNodeLevel,UserInfo::getMinNodeLevel)
			.list();

			if(CollectionUtil.isNotEmpty(userInfos)){
			Map<Integer, NodePlan> nodePlanMap = nodePlanService.lambdaQuery()
				.list()
				.stream().collect(Collectors.toMap(NodePlan::getNodeLevel, Function.identity(), (o1, o2) -> o1));

			//求出每个用户的最大投资额度
			List<NodePlanOrder> nodeplanOrderList = nodePlanOrderService.lambdaQuery()
				.in(NodePlanOrder::getBizStatus, 1, 2)
				.select(NodePlanOrder::getUserId,NodePlanOrder::getRemark)
				.list();
			Map<Long, BigDecimal> userMaxInvestmentMap = new HashMap<>();
			for (NodePlanOrder nodePlanOrder : nodeplanOrderList) {
				BigDecimal investmentAmount =  new BigDecimal(nodePlanOrder.getRemark());
				userMaxInvestmentMap.merge(nodePlanOrder.getUserId(), investmentAmount, BigDecimal::max);
			}

			// 个人节点权重 = 每个用户最大投资金额 * 节点权重系数
			Map<Long, BigDecimal> userNodeWeightMap = new HashMap<>();
			for (UserInfo userInfo : userInfos) {
				int nodeLevel = Math.max(
					userInfo.getNodeLevel() == null ? 0 : userInfo.getNodeLevel(),
					userInfo.getMinNodeLevel() == null ? 0 : userInfo.getMinNodeLevel());
				NodePlan nodePlan = nodePlanMap.get(nodeLevel);
				if(nodePlan == null || nodePlan.getWeightCoefficient() == null){
					continue;
				}

				// 虚拟等级可能没有真实购买记录，此时回退到节点配置里的默认认购额度
				BigDecimal maxInvestmentAmount = userMaxInvestmentMap.get(userInfo.getUserId());

				//拨付了虚拟节点但是也买了节点。虚拟节点比真实节点等级高的情况
				if(maxInvestmentAmount!=null && userInfo.getMinNodeLevel()>userInfo.getNodeLevel()){
					maxInvestmentAmount = nodePlan.getPurchaseAmount();
				}

				if(maxInvestmentAmount == null || maxInvestmentAmount.compareTo(BigDecimal.ZERO) <= 0){
					maxInvestmentAmount = nodePlan.getPurchaseAmount();
				}
				if(maxInvestmentAmount == null || maxInvestmentAmount.compareTo(BigDecimal.ZERO) <= 0){
					continue;
				}
				BigDecimal userNodeWeight = maxInvestmentAmount.multiply(nodePlan.getWeightCoefficient())
					.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
				userNodeWeightMap.put(userInfo.getUserId(), userNodeWeight);
			}

			// 全网节点权重 = 所有节点用户个人节点权重之和
			BigDecimal totalNodeWeight = userNodeWeightMap.values().stream()
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			log.info("节点分红权重计算完成, userCount:{}, totalNodeWeight:{}, globalStaticReward:{}",
				userNodeWeightMap.size(), totalNodeWeight, globalStaticReward);
			//计算每个用户的收益
			distributeGlobalNodeReward(globalStaticReward, userNodeWeightMap, totalNodeWeight,sourceCode, userMoney3Map);

		}
	}

	/**
		 * 任务类型100 认购节点订单每日天数减1,每减少30天释放一次奖励
		 */

		@Transactional(rollbackFor = Exception.class)
		@Override
		public void handelNodePlanOrder() {
			String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
			//long类型日期
			long currentDate = Long.parseLong(strDate);

			Map<String, Object> task = getTask(SysConstant.TSK_TYPE_100, currentDate + "");
			if (!CollectionUtil.isEmpty(task)) {
				log.debug("任务类型100 认购节点订单每日天数减1,每减少30天释放一次奖励任务已存在跳过");
				return;
			}

			nodePlanOrderService.lambdaUpdate()
				.ge(NodePlanOrder::getHaveDay, 1)
				.eq(NodePlanOrder::getBizStatus, 1)
				.setSql("have_day = have_day -1")
				.update();

			List<NodePlanOrder> nodeOrderList = nodePlanOrderService.lambdaQuery()
				.eq(NodePlanOrder::getBizStatus, 1)
				.ge(NodePlanOrder::getHaveDay, 0)
				.list();
			if (CollectionUtil.isNotEmpty(nodeOrderList)) {
				// 标记要发奖励的用户：每减少30天一次，天数=0也需要标记，且过滤掉异常 >= 360 的情况
				List<Long> rewardUserIds = nodeOrderList.stream()
					.filter(order -> {
						Integer haveDay = order.getHaveDay();
						return haveDay != null && haveDay >= 0 && haveDay < 360 && haveDay % 30 == 0;
					})
					.map(NodePlanOrder::getUserId)
					.filter(Objects::nonNull)
					.distinct()
					.collect(Collectors.toList());
				if (CollectionUtil.isNotEmpty(rewardUserIds)) {
					log.info("任务类型100 标记需发奖励用户数:{}, userIds:{}", rewardUserIds.size(), rewardUserIds);
				}

				List<NodePlanOrder> handlerUserList = nodeOrderList.stream()
					.filter(order -> {
						Integer haveDay = order.getHaveDay();
						return haveDay != null && haveDay >= 0 && haveDay % 30 == 0;
					}).collect(Collectors.toList());
				if (CollectionUtil.isNotEmpty(handlerUserList)) {
					Date now = new Date();
					//需要关单的订单
					//List<Long> orderIds = new ArrayList<>(500);
					List<UserMoney> userMoneyValidNum1List = new ArrayList<>(handlerUserList.size());
					List<RewardRecord> dynamicRewardRecordList = new ArrayList<>(handlerUserList.size());
					int batchSize = 1000;
					int stakeCount1 = 0;
					UserMoney entity = null;
					BigDecimal divide12 = new BigDecimal("12");
					for (NodePlanOrder nodePlanOrder : handlerUserList) {
						stakeCount1++;
						//年华收益
						BigDecimal yearAnnual = BigDecimal.ZERO;
						if(nodePlanOrder.getAnnualRate().compareTo(BigDecimal.ZERO)>0){
							 yearAnnual = nodePlanOrder.getAnnualRate().multiply(nodePlanOrder.getAmount())
								.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew)
								.divide(divide12, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						}

						if (nodePlanOrder.getHaveDay() == 0) {
							//关单
							boolean update = nodePlanOrderService.lambdaUpdate()
								.eq(NodePlanOrder::getId, nodePlanOrder.getId())
								.eq(NodePlanOrder::getBizStatus, 1)
								.set(NodePlanOrder::getBizStatus, 2)
								.set(NodePlanOrder::getHaveDay, 0)
								.set(NodePlanOrder::getHaveAmount, 0)
								.setSql("total_annual = total_annual + " + yearAnnual)
								.update();
							if (!update) {
								throw new ServiceException("更新节点认购订单状态失败");
							}
							//添加收益记录
							if (nodePlanOrder.getHaveAmount().compareTo(BigDecimal.ZERO) > 0) {
								entity = new UserMoney();
								entity.setId(nodePlanOrder.getUserId());
								entity.setValidNum1(nodePlanOrder.getHaveAmount().add(yearAnnual));


								entity.setGtId(IDUtils.getSnowflakeStr());
								entity.setSourceCode(nodePlanOrder.getOrderNo());
								entity.setSourceId(nodePlanOrder.getUserId());
								entity.setSourceType(ConstantType.user_money_log_source_type.type_2);
								entity.setUpdateTime(new Date());
								userMoneyValidNum1List.add(entity);

								RewardRecord rewardRecord = new RewardRecord();
								rewardRecord.setUserId(nodePlanOrder.getUserId());
								rewardRecord.setAmount(nodePlanOrder.getHaveAmount().add(yearAnnual));
								rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_1);
								rewardRecord.setSourceOrderCode(nodePlanOrder.getOrderNo());
								rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_1);
								rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
								rewardRecord.setSourceUserId(nodePlanOrder.getUserId());
								rewardRecord.setCreateTime(now);
								dynamicRewardRecordList.add(rewardRecord);
							}

						} else {
							//发放质押收益
							BigDecimal reward = nodePlanOrder.getTotalAmount().divide(divide12, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
							//更新订单
							boolean update = nodePlanOrderService.lambdaUpdate()
								.eq(NodePlanOrder::getId, nodePlanOrder.getId())
								.setSql("have_amount = have_amount - " + reward)
								.setSql("total_annual = total_annual + " + yearAnnual)
								.update();
							if (!update) {
								throw new ServiceException("更新节点认购订单状态失败");
							}

							//添加收益记录
							entity = new UserMoney();
							entity.setId(nodePlanOrder.getUserId());
							//年华收益额外的一部分
							entity.setValidNum1(reward.add(yearAnnual));
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(nodePlanOrder.getOrderNo());
							entity.setSourceId(nodePlanOrder.getUserId());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_2);
							entity.setUpdateTime(new Date());
							userMoneyValidNum1List.add(entity);

							RewardRecord rewardRecord = new RewardRecord();
							rewardRecord.setUserId(nodePlanOrder.getUserId());
							//年华收益额外的一部分
							rewardRecord.setAmount(reward.add(yearAnnual));
							rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_1);
							rewardRecord.setSourceOrderCode(nodePlanOrder.getOrderNo());
							rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_1);
							rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
							rewardRecord.setSourceUserId(nodePlanOrder.getUserId());
							rewardRecord.setCreateTime(now);
							dynamicRewardRecordList.add(rewardRecord);
						}


						if (stakeCount1 >= batchSize) {
							bachUpdateMoneyValid1(userMoneyValidNum1List);
							userMoneyValidNum1List.clear();
							log.info("更新成功");
							stakeCount1 = 0;
						}
					}


					if (CollectionUtil.isNotEmpty(userMoneyValidNum1List)) {
						bachUpdateMoneyValid1(userMoneyValidNum1List);
					}
					if (CollectionUtil.isNotEmpty(dynamicRewardRecordList)) {
						rewardRecordService.saveBatch(dynamicRewardRecordList);
					}
				}
			}

			//消费分红池分红任务
			int i = addTask(SysConstant.TSK_TYPE_100, currentDate + "");
			if (i != 1) {
				throw new RuntimeException("任务类型100 认购节点订单每日天数减1,每减少30天释放一次奖励任务失败");
			}
		}

	/**
	 * 节点全网分红，按个人节点权重占比分配到动态收益
	 */
	private void distributeGlobalNodeReward(BigDecimal globalStaticReward, Map<Long, BigDecimal> userNodeWeightMap,
		BigDecimal totalNodeWeight,String sourceCode, Map<Long, BigDecimal> userMoney3Map) {
		if (globalStaticReward == null || globalStaticReward.compareTo(BigDecimal.ZERO) <= 0
			|| CollectionUtil.isEmpty(userNodeWeightMap)
			|| totalNodeWeight == null || totalNodeWeight.compareTo(BigDecimal.ZERO) <= 0) {
			log.info("节点全网分红跳过, globalStaticReward:{}, userCount:{}, totalNodeWeight:{}",
				globalStaticReward, userNodeWeightMap == null ? 0 : userNodeWeightMap.size(), totalNodeWeight);
			return;
		}
		Date now = new Date();
		List<UserMoney> userMoneyValidNum3List = new ArrayList<>(userNodeWeightMap.size() > 1000 ? 1000 : userNodeWeightMap.size());
		List<UserMoney> userMoneyValidNum7List = new ArrayList<>(userNodeWeightMap.size() > 1000 ? 1000 : userNodeWeightMap.size());
		List<RewardRecord> dynamicRewardRecordList = new ArrayList<>(userNodeWeightMap.size());
		Map<Long, BigDecimal> userMoney7Map = userMoneyService.lambdaQuery()
			.list()
			.stream()
			.collect(Collectors.toMap(UserMoney::getId,
				item -> item.getValidNum7() == null ? BigDecimal.ZERO : item.getValidNum7(), (k1, k2) -> k2));
		int batchSize = 1000;
		int stakeCount1 = 0;
		for (Map.Entry<Long, BigDecimal> entry : userNodeWeightMap.entrySet()) {
			Long userId = entry.getKey();
			BigDecimal userNodeWeight = entry.getValue();
			if (userId == null || userNodeWeight == null || userNodeWeight.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			BigDecimal reward = globalStaticReward.multiply(userNodeWeight)
				.divide(totalNodeWeight, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			BigDecimal userValidNum7 = userMoney7Map.get(userId);
			userValidNum7 = userValidNum7 == null ? BigDecimal.ZERO : userValidNum7;
			BigDecimal actualReward = reward.min(userValidNum7);
			if (actualReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			UserMoney entity = new UserMoney();
			entity.setId(userId);
			entity.setValidNum3(actualReward);
			entity.setGtId(IDUtils.getSnowflakeStr());
			entity.setSourceCode(sourceCode);
			entity.setSourceId(userId);
			entity.setSourceType(ConstantType.user_money_log_source_type.type_8);
			entity.setUpdateTime(now);
			userMoneyValidNum3List.add(entity);

			UserMoney deductValidNum7 = new UserMoney();
			deductValidNum7.setId(userId);
			deductValidNum7.setValidNum7(actualReward.negate());
			deductValidNum7.setGtId(IDUtils.getSnowflakeStr());
			deductValidNum7.setSourceCode(sourceCode);
			deductValidNum7.setSourceId(userId);
			deductValidNum7.setSourceType(ConstantType.user_money_log_source_type.type_17);
			deductValidNum7.setUpdateTime(now);
			userMoneyValidNum7List.add(deductValidNum7);

			RewardRecord rewardRecord = new RewardRecord();
			rewardRecord.setUserId(userId);
			rewardRecord.setAmount(actualReward);
			rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_7);
			rewardRecord.setSourceOrderCode(sourceCode);
			rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_3);
			rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
			rewardRecord.setSourceUserId(userId);
			rewardRecord.setCreateTime(now);
			dynamicRewardRecordList.add(rewardRecord);
			userMoney7Map.put(userId, userValidNum7.subtract(actualReward));
			userMoney3Map.merge(userId, actualReward, BigDecimal::add);

			stakeCount1++;
			if (stakeCount1 >= batchSize) {
				bachUpdateMoneyValid3(userMoneyValidNum3List);
				bachUpdateMoneyValid7(userMoneyValidNum7List);
				userMoneyValidNum3List.clear();
				userMoneyValidNum7List.clear();
				log.info("节点全网分红批量更新成功");
				stakeCount1 = 0;
			}
		}

		if (CollectionUtil.isNotEmpty(userMoneyValidNum3List)) {
			bachUpdateMoneyValid3(userMoneyValidNum3List);
		}
		if (CollectionUtil.isNotEmpty(userMoneyValidNum7List)) {
			bachUpdateMoneyValid7(userMoneyValidNum7List);
		}
		if (CollectionUtil.isNotEmpty(dynamicRewardRecordList)) {
			rewardRecordService.saveBatch(dynamicRewardRecordList);
		}
	}

//	/**
//	 * 每日矿池分配
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void distributePtbInterest100() {
//		String strDate = DateUtil.format(DateUtil.date(), "yyyyMMdd");
//		//long类型日期
//		long currentDate = Long.parseLong(strDate);
//		long currentDateInt = Long.parseLong(strDate);
//
//		Map<String, Object> task = getTask(SysConstant.TSK_TYPE_100, currentDate + "");
//		if (!CollectionUtil.isEmpty(task)) {
//			log.debug("每日矿池分配任务已存在跳过");
//			return;
//		}
//		//添加日志
//		//rewardPoolConfigService.save();
//		//Integer todayInt = Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd"));
//		//用户等级
//		List<UserLevelBo> userLevelBoList = userInfoService.getUserLevelList();
//		//用户算力
//		List<UserComputingPowerBo> userComputingPowerBos = userInfoService.userComputingPower();
//
//		List<UserLevelConfig> userLevelConfigList = userLevelConfigService.lambdaQuery()
//			.orderByDesc(UserLevelConfig::getLevel)
//			.list().stream()
//			.map(record->{
//				record.setRewardRatio(record.getRewardRatio().divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew));
//				return record;
//			}).collect(Collectors.toList());
//		BigDecimal totalComputingPower = userInfoService.userTotalComputingPower();
//		RewardPoolConfig miningConfig = rewardPoolConfigService.lambdaQuery()
//			.eq(RewardPoolConfig::getPoolType, 1)
//			.one();
//		//挖矿静态分红任务
//		miningPoolTask(miningConfig, userLevelBoList,userComputingPowerBos,totalComputingPower,userLevelConfigList);
//
//		RewardPoolConfig consumptionConfig = rewardPoolConfigService.lambdaQuery()
//			.eq(RewardPoolConfig::getPoolType, 2)
//			.one();
//		//商城消费分红
//		consumptionTask(consumptionConfig, userComputingPowerBos, totalComputingPower, userLevelBoList,userLevelConfigList);
//
//		RewardPoolConfig uCardFeeConfig = rewardPoolConfigService.lambdaQuery()
//			.eq(RewardPoolConfig::getPoolType, 3)
//			.one();
//		//u卡分红的流水类型和奖金记录类型有问题
//		//uCardFeeTask(uCardFeeConfig, userComputingPowerBos, totalComputingPower, userLevelBoList,userLevelConfigList);
//
//		//更新运行天数
//		String sysEnabled = sysParaServiceImpl.getValue(ConstantSys.biz_sys_enabled);
//		if(sysEnabled.equals("1")){
//			SysPara sysPara = sysParaServiceImpl.lambdaQuery()
//				.eq(SysPara::getParaCode, ConstantSys.biz_sys_run_days)
//				.one();
//			Integer i = Integer.valueOf(sysPara.getParaValue());
//			i =i+1;
//			sysPara.setParaValue(i+"");
//			sysParaServiceImpl.updateSysPara(sysPara);
//		}
//
//		//消费分红池分红任务
///*		int i = addTask(SysConstant.TSK_TYPE_100, currentDate + "");
//		if (i != 1) {
//			throw new RuntimeException("添加任务类型100:每日释放线性订单失败");
//		}*/
//		//延迟删除
//		redissonTemplate.sendCleanCacheWithDelay(RedisConstant.XMS_PARAM + ConstantSys.biz_sys_run_days);
//	}

	/**
	 * u卡分红订单
	 */
	private void uCardFeeTask(RewardPoolConfig uCardFeeConfig, List<UserComputingPowerBo> userComputingPowerBos,
							  BigDecimal totalComputingPower, List<UserLevelBo> userLevelBoList,
							  List<UserLevelConfig> userLevelConfigList) {
		if (uCardFeeConfig == null || uCardFeeConfig.getDailyOutput().compareTo(BigDecimal.ZERO) <= 0) {
			log.info("U卡分红池今日无可分配产出");
			return;
		}
		String orderCode = IDUtils.getSnowflakeStr();
		BigDecimal uCardStaticReward = uCardFeeConfig.getDailyOutput()
			.multiply(uCardFeeConfig.getStaticRatio())
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		if (uCardStaticReward.compareTo(BigDecimal.ZERO) > 0) {
			distributeUCardStatic(uCardStaticReward, userComputingPowerBos, totalComputingPower,
				orderCode);
		} else {
			log.info("U卡分红池静态收益不足，无法分配");
		}

		BigDecimal uCardDynamicReward = uCardFeeConfig.getDailyOutput()
			.multiply(uCardFeeConfig.getDynamicRatio())
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		if (uCardDynamicReward.compareTo(BigDecimal.ZERO) > 0) {
			distributeUCardDynamic(uCardDynamicReward, userLevelBoList, orderCode,userLevelConfigList);
		} else {
			log.info("U卡分红池动态收益不足，无法分配");
		}
	}

	/*private void consumptionTask(RewardPoolConfig consumptionConfig, List<UserComputingPowerBo> userComputingPowerBos,
								 BigDecimal totalComputingPower, List<UserLevelBo> userLevelBoList,
								 List<UserLevelConfig> userLevelConfigList) {
		if (consumptionConfig.getDailyOutput().compareTo(BigDecimal.ZERO) > 0) {
			String orderCode = IDUtils.getSnowflakeStr();
			BigDecimal consumptionStaticReward = consumptionConfig.getDailyOutput()
				.multiply(consumptionConfig.getStaticRatio())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
				.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (consumptionStaticReward.compareTo(BigDecimal.ZERO) > 0) {
				distributeConsumptionStatic(consumptionStaticReward, userComputingPowerBos, totalComputingPower,
					orderCode);
			} else {
				log.info("消费分红池静态收益不足，无法分配");
			}
			BigDecimal consumptionDynamicReward = consumptionConfig.getDailyOutput()
				.multiply(consumptionConfig.getDynamicRatio())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
				.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (consumptionDynamicReward.compareTo(BigDecimal.ZERO) > 0) {
				distributeConsumptionDynamic(consumptionDynamicReward, userLevelBoList,
					orderCode,userLevelConfigList);
			} else {
				log.info("消费分红池动态收益不足，无法分配");
			}
		} else {
			log.info("消费分红池今日无可分配产出");
		}
	}*/

/*
	private void miningPoolTask(RewardPoolConfig miningConfig, List<UserLevelBo> userLevelBoList,
								List<UserComputingPowerBo> userComputingPowerList,BigDecimal totalComputingPower,
								List<UserLevelConfig> userLevelConfigList) {
		String orderCode = IDUtils.getSnowflakeStr();
		if (miningConfig.getDailyOutput().compareTo(BigDecimal.ZERO) <= 0) {
			//不分,做记录
		} else {
			//1.计算动态收益
			BigDecimal dynamicReward = miningConfig.getDailyOutput().multiply(miningConfig.getDynamicRatio())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
				.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (dynamicReward.compareTo(BigDecimal.ZERO) <= 0) {
				//不够分、做记录
			} else {
				Map<Integer, List<UserLevelBo>> eligibleCache = new HashMap<>();
				List<UserMoney> dynamicMoneyList = new ArrayList<>();
				List<RewardRecord> dynamicRewardRecordList = new ArrayList<>();
				int dynamicBatchSize = 1000;
				int dynamicStakeCount = 0;
				Date now = new Date();

				for (UserLevelConfig userLevelConfig : userLevelConfigList) {
					if (userLevelConfig.getLevel().equals(0)) {
						continue;
					}
					if (userLevelConfig.getRewardRatio() == null ||
						userLevelConfig.getRewardRatio().compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}
					BigDecimal levelTotalReward = dynamicReward.multiply(userLevelConfig.getRewardRatio())
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (levelTotalReward.compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}
					List<UserLevelBo> eligibleUsers = eligibleCache.computeIfAbsent(userLevelConfig.getLevel(), level ->
						userLevelBoList.stream()
							.filter(user -> user.getLevel() != null && user.getLevel() >= level)
							.collect(Collectors.toList())
					);
					if (CollectionUtil.isEmpty(eligibleUsers)) {
						log.info("动态分红：等级{} 无符合条件用户", userLevelConfig.getLevel());
						continue;
					}
					BigDecimal perUserReward = levelTotalReward.divide(new BigDecimal(eligibleUsers.size()),
						ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (perUserReward.compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}
					for (UserLevelBo userLevelBo : eligibleUsers) {
						UserMoney money = new UserMoney();
						money.setId(userLevelBo.getUserId());
						money.setValidNum2(perUserReward);
						money.setGtId(IDUtils.getSnowflakeStr());
						money.setSourceCode(orderCode);
						money.setSourceId(userLevelBo.getUserId());
						money.setSourceType(getMoneySourceTypeByMiningLevel(userLevelConfig.getLevel()));
						money.setUpdateTime(now);
						dynamicMoneyList.add(money);
						dynamicStakeCount++;
						if (dynamicStakeCount >= dynamicBatchSize) {
							bachUpdateMoneyValid2(dynamicMoneyList);
							dynamicMoneyList.clear();
							dynamicStakeCount = 0;
						}

						RewardRecord rewardRecord = new RewardRecord();
						rewardRecord.setUserId(userLevelBo.getUserId());
						rewardRecord.setAmount(perUserReward);
						//rewardRecord.setBusinessType(ConstantType.xms_reward_record_business_type.type_2);
						rewardRecord.setSourceType(getRewardSourceTypeByMiningLevel(userLevelConfig.getLevel()));
						rewardRecord.setSourceOrderCode(orderCode);
						rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
						rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
						rewardRecord.setSourceUserId(userLevelBo.getUserId());
						rewardRecord.setCreateTime(now);
						dynamicRewardRecordList.add(rewardRecord);
					}
				}
				if (CollectionUtil.isNotEmpty(dynamicMoneyList)) {
					bachUpdateMoneyValid2(dynamicMoneyList);
				}
				if (CollectionUtil.isNotEmpty(dynamicRewardRecordList)) {
					rewardRecordService.saveBatch(dynamicRewardRecordList);
				}
			}

			//2.计算静态收益
			BigDecimal miningStaticReward = miningConfig.getDailyOutput().multiply(miningConfig.getStaticRatio())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
				.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (miningStaticReward.compareTo(BigDecimal.ZERO) <= 0) {
				//不够分、做记录
			} else {

				if (totalComputingPower.compareTo(BigDecimal.ZERO) <= 0) {
					//没有算力,不分 做记录
				} else {
					List<RewardRecord> rewardRecordList = new ArrayList<>(userComputingPowerList.size());
					List<UserMoney> userMoneyValidNum1List = new ArrayList<>(userComputingPowerList.size());
					int batchSize = 1000;
					int stakeCount1 = 0;

					UserMoney entity = null;
					RewardRecord rewardRecord = null;
					for (UserComputingPowerBo userComputingPowerBo : userComputingPowerList) {
						BigDecimal eachReward = userComputingPowerBo.getComputingPower()
							.divide(totalComputingPower, ConstantStatic.newScale, ConstantStatic.roundingModeNew)
							.multiply(miningStaticReward);
						if (eachReward.compareTo(BigDecimal.ZERO) > 0) {

							entity = new UserMoney();
							entity.setId(userComputingPowerBo.getUserId());
							entity.setValidNum2(eachReward);
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(orderCode);
							entity.setSourceId(userComputingPowerBo.getUserId());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_5);
							entity.setUpdateTime(new Date());

							userMoneyValidNum1List.add(entity);
							stakeCount1++;
							if (stakeCount1 >= batchSize) {
								bachUpdateMoneyValid2(userMoneyValidNum1List);
								userMoneyValidNum1List.clear();
								log.info("更新成功");
								stakeCount1 = 0;
							}

							//奖金记录
							rewardRecord = new RewardRecord();
							rewardRecord.setUserId(userComputingPowerBo.getUserId());
							rewardRecord.setAmount(eachReward);
							//rewardRecord.setBusinessType(ConstantType.xms_reward_record_business_type.type_5);
							rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_1);
							rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
							rewardRecord.setSourceOrderCode(orderCode);
							rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
							rewardRecord.setSourceUserId(userComputingPowerBo.getUserId());
							rewardRecord.setCreateTime(new Date());
							rewardRecordList.add(rewardRecord);
						}
					}

					if (CollectionUtil.isNotEmpty(userMoneyValidNum1List)) {
						bachUpdateMoneyValid2(userMoneyValidNum1List);
					}

					if (CollectionUtil.isNotEmpty(rewardRecordList)) {
						rewardRecordService.saveBatch(rewardRecordList);
					}
				}
			}
		}
	}
*/



	/**
	 * 商城消费分红的时候。根据等级获取奖金记录 sourceType值
	 *
	 * @param level
	 * @return
	 */
//	private Integer getRewardSourceTypeByConsumptionStatic(Integer level) {
//		switch (level) {
//			case 1:
//				return ConstantType.xms_reward_record_source_type.type_11;
//			case 2:
//				return ConstantType.xms_reward_record_source_type.type_12;
//			case 3:
//				return ConstantType.xms_reward_record_source_type.type_13;
//			case 4:
//				return ConstantType.xms_reward_record_source_type.type_14;
//			case 5:
//				return ConstantType.xms_reward_record_source_type.type_15;
//			case 6:
//				return ConstantType.xms_reward_record_source_type.type_16;
//			case 7:
//				return ConstantType.xms_reward_record_source_type.type_17;
//			case 8:
//				return ConstantType.xms_reward_record_source_type.type_18;
//			case 9:
//				return ConstantType.xms_reward_record_source_type.type_19;
//			default:
//				throw new ServiceException("等级异常");
//		}
//	}

	/**
	 * 矿池动态分红的时候。根据等级获取t_user_money_log sourceType值
	 *
	 * @param level
	 * @return
	 */
	private Integer getMoneySourceTypeByMiningLevel(Integer level) {
		switch (level) {
			case 1:
				return ConstantType.user_money_log_source_type.type_6;
			case 2:
				return ConstantType.user_money_log_source_type.type_7;
			case 3:
				return ConstantType.user_money_log_source_type.type_8;
			case 4:
				return ConstantType.user_money_log_source_type.type_9;
			case 5:
				return ConstantType.user_money_log_source_type.type_10;
			case 6:
				return ConstantType.user_money_log_source_type.type_11;
			case 7:
				return ConstantType.user_money_log_source_type.type_12;
			case 8:
				return ConstantType.user_money_log_source_type.type_13;
			case 9:
				return ConstantType.user_money_log_source_type.type_14;
			default:
				throw new ServiceException("等级异常");
		}
	}

	/**
	 * 商城消费分红的时候。根据等级获取t_user_money_log sourceType值
	 *
	 * @param level
	 * @return
	 */
	private Integer getMoneySourceTypeByConsumptionStatic(Integer level) {
		switch (level) {
			case 1:
				return ConstantType.user_money_log_source_type.type_15;
			case 2:
				return ConstantType.user_money_log_source_type.type_16;
			case 3:
				return ConstantType.user_money_log_source_type.type_17;
			case 4:
				return ConstantType.user_money_log_source_type.type_18;
			case 5:
				return ConstantType.user_money_log_source_type.type_19;
			case 6:
				return ConstantType.user_money_log_source_type.type_20;
			case 7:
				return ConstantType.user_money_log_source_type.type_21;
			case 8:
				return ConstantType.user_money_log_source_type.type_22;
			case 9:
				return ConstantType.user_money_log_source_type.type_23;
			default:
				throw new ServiceException("等级异常");
		}
	}


	private void distributeConsumptionStatic(BigDecimal consumptionStaticReward,
											 List<UserComputingPowerBo> userComputingPowerList,
											 BigDecimal totalComputingPower,
											 String orderCode) {
		if (totalComputingPower.compareTo(BigDecimal.ZERO) <= 0) {
			log.info("消费分红池静态：全网算力为0，无法分配");
			return;
		}
		List<RewardRecord> rewardRecordList = new ArrayList<>(userComputingPowerList.size());
		List<UserMoney> userMoneyValidNum1List = new ArrayList<>(userComputingPowerList.size());
		int batchSize = 1000;
		int stakeCount = 0;
		Date now = new Date();
		for (UserComputingPowerBo userComputingPowerBo : userComputingPowerList) {
			BigDecimal eachReward = userComputingPowerBo.getComputingPower()
				.divide(totalComputingPower, ConstantStatic.newScale, ConstantStatic.roundingModeNew)
				.multiply(consumptionStaticReward);
			if (eachReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			UserMoney entity = new UserMoney();
			entity.setId(userComputingPowerBo.getUserId());
			entity.setValidNum2(eachReward);
			entity.setGtId(IDUtils.getSnowflakeStr());
			entity.setSourceCode(orderCode);
			entity.setSourceId(userComputingPowerBo.getUserId());
			entity.setSourceType(ConstantType.user_money_log_source_type.type_24);
			entity.setUpdateTime(now);
			userMoneyValidNum1List.add(entity);
			stakeCount++;
			if (stakeCount >= batchSize) {
				bachUpdateMoneyValid2(userMoneyValidNum1List);
				userMoneyValidNum1List.clear();
				stakeCount = 0;
			}

			RewardRecord rewardRecord = new RewardRecord();
			rewardRecord.setUserId(userComputingPowerBo.getUserId());
			rewardRecord.setAmount(eachReward);
			rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_20);
			rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
			rewardRecord.setSourceOrderCode(orderCode);
			rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
			rewardRecord.setSourceUserId(userComputingPowerBo.getUserId());
			rewardRecord.setCreateTime(now);
			rewardRecordList.add(rewardRecord);
		}
		if (CollectionUtil.isNotEmpty(userMoneyValidNum1List)) {
			bachUpdateMoneyValid2(userMoneyValidNum1List);
		}
		if (CollectionUtil.isNotEmpty(rewardRecordList)) {
			rewardRecordService.saveBatch(rewardRecordList);
		}
	}

	/*private void distributeConsumptionDynamic(BigDecimal consumptionDynamicReward,
											  List<UserLevelBo> userLevelBoList,
											  String orderCode,List<UserLevelConfig> userLevelConfigList) {
		if (consumptionDynamicReward.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		Map<Integer, List<UserLevelBo>> eligibleCache = new HashMap<>();
		List<UserMoney> dynamicMoneyList = new ArrayList<>();
		List<RewardRecord> dynamicRewardRecordList = new ArrayList<>();
		int batchSize = 1000;
		int stakeCount = 0;
		Date now = new Date();
		for (UserLevelConfig userLevelConfig : userLevelConfigList) {
			if (userLevelConfig.getLevel().equals(0)) {
				continue;
			}
			if (userLevelConfig.getRewardRatio() == null ||
				userLevelConfig.getRewardRatio().compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			BigDecimal levelTotalReward = consumptionDynamicReward.multiply(userLevelConfig.getRewardRatio())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (levelTotalReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			List<UserLevelBo> eligibleUsers = eligibleCache.computeIfAbsent(userLevelConfig.getLevel(), level ->
				userLevelBoList.stream()
					.filter(user -> user.getLevel() != null && user.getLevel() >= level)
					.collect(Collectors.toList())
			);
			if (CollectionUtil.isEmpty(eligibleUsers)) {
				log.info("消费分红动态：等级{} 无符合条件用户", userLevelConfig.getLevel());
				continue;
			}
			BigDecimal perUserReward = levelTotalReward.divide(new BigDecimal(eligibleUsers.size()),
				ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (perUserReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			for (UserLevelBo userLevelBo : eligibleUsers) {
				UserMoney entity = new UserMoney();
				entity.setId(userLevelBo.getUserId());
				entity.setValidNum2(perUserReward);
				entity.setGtId(IDUtils.getSnowflakeStr());
				entity.setSourceCode(orderCode);
				entity.setSourceId(userLevelBo.getUserId());
				//entity.setSourceType(ConstantType.user_money_log_source_type.type_43);
				entity.setSourceType((getMoneySourceTypeByConsumptionStatic(userLevelConfig.getLevel())));
				entity.setUpdateTime(now);
				dynamicMoneyList.add(entity);
				stakeCount++;
				if (stakeCount >= batchSize) {
					bachUpdateMoneyValid2(dynamicMoneyList);
					dynamicMoneyList.clear();
					stakeCount = 0;
				}

				RewardRecord rewardRecord = new RewardRecord();
				rewardRecord.setUserId(userLevelBo.getUserId());
				rewardRecord.setAmount(perUserReward);
				//rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_17);
				rewardRecord.setSourceType((getRewardSourceTypeByConsumptionStatic(userLevelConfig.getLevel())));
				rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
				rewardRecord.setSourceOrderCode(orderCode);
				rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
				rewardRecord.setSourceUserId(userLevelBo.getUserId());
				rewardRecord.setCreateTime(now);
				dynamicRewardRecordList.add(rewardRecord);
			}
		}
		if (CollectionUtil.isNotEmpty(dynamicMoneyList)) {
			bachUpdateMoneyValid2(dynamicMoneyList);
		}
		if (CollectionUtil.isNotEmpty(dynamicRewardRecordList)) {
			rewardRecordService.saveBatch(dynamicRewardRecordList);
		}
	}*/

	private void distributeUCardStatic(BigDecimal reward, List<UserComputingPowerBo> userComputingPowerList,
									   BigDecimal totalComputingPower, String orderCode) {
		if (totalComputingPower.compareTo(BigDecimal.ZERO) <= 0) {
			log.info("U卡分红池静态：全网算力为0，无法分配");
			return;
		}
		List<RewardRecord> rewardRecordList = new ArrayList<>(userComputingPowerList.size());
		List<UserMoney> userMoneyValidNum1List = new ArrayList<>(userComputingPowerList.size());
		int batchSize = 1000;
		int stakeCount = 0;
		Date now = new Date();
		for (UserComputingPowerBo userComputingPowerBo : userComputingPowerList) {
			BigDecimal eachReward = userComputingPowerBo.getComputingPower()
				.divide(totalComputingPower, ConstantStatic.newScale, ConstantStatic.roundingModeNew)
				.multiply(reward);
			if (eachReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			UserMoney entity = new UserMoney();
			entity.setId(userComputingPowerBo.getUserId());
			entity.setValidNum2(eachReward);
			entity.setGtId(IDUtils.getSnowflakeStr());
			entity.setSourceCode(orderCode);
			entity.setSourceId(userComputingPowerBo.getUserId());
			entity.setSourceType(ConstantType.user_money_log_source_type.type_44);
			entity.setUpdateTime(now);
			userMoneyValidNum1List.add(entity);
			stakeCount++;
			if (stakeCount >= batchSize) {
				bachUpdateMoneyValid2(userMoneyValidNum1List);
				userMoneyValidNum1List.clear();
				stakeCount = 0;
			}

			RewardRecord rewardRecord = new RewardRecord();
			rewardRecord.setUserId(userComputingPowerBo.getUserId());
			rewardRecord.setAmount(eachReward);
			//rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_18);
			rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
			rewardRecord.setSourceOrderCode(orderCode);
			rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
			rewardRecord.setSourceUserId(userComputingPowerBo.getUserId());
			rewardRecord.setCreateTime(now);
			rewardRecordList.add(rewardRecord);
		}
		if (CollectionUtil.isNotEmpty(userMoneyValidNum1List)) {
			bachUpdateMoneyValid2(userMoneyValidNum1List);
		}
		if (CollectionUtil.isNotEmpty(rewardRecordList)) {
			rewardRecordService.saveBatch(rewardRecordList);
		}
	}

	private void distributeUCardDynamic(BigDecimal reward, List<UserLevelBo> userLevelBoList, String orderCode,
										List<UserLevelConfig> userLevelConfigList) {
		if (reward.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		Map<Integer, List<UserLevelBo>> eligibleCache = new HashMap<>();
		List<UserMoney> dynamicMoneyList = new ArrayList<>();
		List<RewardRecord> dynamicRewardRecordList = new ArrayList<>();
		int batchSize = 1000;
		int stakeCount = 0;
		Date now = new Date();
		for (UserLevelConfig userLevelConfig : userLevelConfigList) {
			if (userLevelConfig.getLevel().equals(0)) {
				continue;
			}
			if (userLevelConfig.getRewardRatio() == null ||
				userLevelConfig.getRewardRatio().compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			BigDecimal levelTotalReward = reward.multiply(userLevelConfig.getRewardRatio())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (levelTotalReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			List<UserLevelBo> eligibleUsers = eligibleCache.computeIfAbsent(userLevelConfig.getLevel(), level ->
				userLevelBoList.stream()
					.filter(user -> user.getLevel() != null && user.getLevel() >= level)
					.collect(Collectors.toList())
			);
			if (CollectionUtil.isEmpty(eligibleUsers)) {
				log.info("U卡分红动态：等级{} 无符合条件用户", userLevelConfig.getLevel());
				continue;
			}
			BigDecimal perUserReward = levelTotalReward.divide(new BigDecimal(eligibleUsers.size()),
				ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (perUserReward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			for (UserLevelBo userLevelBo : eligibleUsers) {
				UserMoney entity = new UserMoney();
				entity.setId(userLevelBo.getUserId());
				entity.setValidNum2(perUserReward);
				entity.setGtId(IDUtils.getSnowflakeStr());
				entity.setSourceCode(orderCode);
				entity.setSourceId(userLevelBo.getUserId());
				//entity.setSourceType(ConstantType.user_money_log_source_type.type_45);
				entity.setUpdateTime(now);
				dynamicMoneyList.add(entity);
				stakeCount++;
				if (stakeCount >= batchSize) {
					bachUpdateMoneyValid2(dynamicMoneyList);
					dynamicMoneyList.clear();
					stakeCount = 0;
				}

				RewardRecord rewardRecord = new RewardRecord();
				rewardRecord.setUserId(userLevelBo.getUserId());
				rewardRecord.setAmount(perUserReward);
				//rewardRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_25);
				rewardRecord.setCoinType(ConstantType.user_money_coin_type.type_2);
				rewardRecord.setSourceOrderCode(orderCode);
				rewardRecord.setOrderCode(IDUtils.getSnowflakeStr());
				rewardRecord.setSourceUserId(userLevelBo.getUserId());
				rewardRecord.setCreateTime(now);
				dynamicRewardRecordList.add(rewardRecord);
			}
		}
		if (CollectionUtil.isNotEmpty(dynamicMoneyList)) {
			bachUpdateMoneyValid2(dynamicMoneyList);
		}
		if (CollectionUtil.isNotEmpty(dynamicRewardRecordList)) {
			rewardRecordService.saveBatch(dynamicRewardRecordList);
		}
	}


/**
 * 计算第N天应释放的金额
 *
 * @param principal 本金
 * @param dailyRate 日利率
 * @param runDays   实际运行天数（产生了多少笔收益）
 * @param dayN      当前是第几天
 * @return 第N天释放金额
 */
public BigDecimal calculateDayNRelease(BigDecimal principal,
									   BigDecimal dailyRate,
									   int runDays,
									   int dayN) {
	if (dayN <= 1) {
		return BigDecimal.ZERO;
	}

	// 每日产生收益
	BigDecimal dailyReward = principal.multiply(dailyRate);

	// 每日从单笔收益中释放的金额
	BigDecimal dailyReleasePerReward = dailyReward.divide(
		new BigDecimal(100), 8, RoundingMode.HALF_UP);

	// 计算当天应该释放几笔收益
	int releaseCount = 0;
	for (int i = 1; i <= runDays; i++) {
		// 第i天产生的收益：
		// - 从第i+1天开始释放
		// - 释放100天，所以结束日期是 i+1+99 = i+100
		int startDay = i + 1;
		int endDay = i + 100;

		if (dayN >= startDay && dayN <= endDay) {
			releaseCount++;
		}
	}

	return dailyReleasePerReward.multiply(new BigDecimal(releaseCount));
}


/**
 * 批量更新活期矿机 可领取利息和累计产出金额
 */
private void w3Order0UpdateReward(List<w30OrderBo> w30OrderBoList) {
	// SQL语句：更新total_reward和unclaimed_reward字段
	String sql = "UPDATE t_w3_mining_package_order SET day_reward = ?, " +
		"total_reward = total_reward + ? WHERE id = ? and status = 0";

	int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			w30OrderBo stake = w30OrderBoList.get(i);
			ps.setBigDecimal(1, stake.getReward()); // total_reward增加的值
			ps.setBigDecimal(2, stake.getReward()); // unclaimed_reward增加的值
			ps.setLong(3, stake.getId()); // 记录ID
		}

		@Override
		public int getBatchSize() {
			return w30OrderBoList.size();
		}
	});

	// 检查更新结果
	if (ArrayUtil.contains(results, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("批量更新质押记录奖励失败 w30OrderBoList:{}", w30OrderBoList);
		throw new ServiceException("批量更新质押记录奖励失败");
	}
}

/**
 * 批量更新固定矿机 可领取利息和剩余产出金额
 */
private void w3Order1UpdateReward(List<W3MiningPackageOrderBo> w3MiningPackageOrderBos) {
	// SQL语句：更新total_reward和unclaimed_reward字段
	String sql = "UPDATE t_w3_mining_package_order SET day_reward = ?, " +
		"have_fsn_multiplied_value = have_fsn_multiplied_value + ?, status = ?  WHERE id = ?";

	int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			W3MiningPackageOrderBo stake = w3MiningPackageOrderBos.get(i);
			ps.setBigDecimal(1, stake.getHaveFsnMultipliedValue()); // total_reward增加的值
			//剩余可领取金额
			ps.setBigDecimal(2, stake.getHaveFsnMultipliedValue().negate()); // unclaimed_reward增加的值
			ps.setLong(3, stake.getStatus()); // 记录ID
			ps.setLong(4, stake.getId()); // 记录ID
		}

		@Override
		public int getBatchSize() {
			return w3MiningPackageOrderBos.size();
		}
	});

	// 检查更新结果
	if (ArrayUtil.contains(results, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("批量更新质押记录奖励失败");
		throw new ServiceException("批量更新质押记录奖励失败");
	}
}

/**
 * 补偿基金订单赎回本期的时候.t+1时间到了但是还没有执行发放本金任务
 */
@Override
@Transactional(rollbackFor = Exception.class)
public void compensateUnpaidPrincipalOrders() {
	List<MiningPackageOrder> packageOrderList = miningPackageOrderService.lambdaQuery()
		.eq(MiningPackageOrder::getSourceType, 0)
		.eq(MiningPackageOrder::getReturnedBizStatus, 2)
		.ge(MiningPackageOrder::getReturnedTime, DateUtil.offsetMinute(new Date(), 15))
		.list();
	if (CollectionUtil.isNotEmpty(packageOrderList)) {
		for (MiningPackageOrder miningPackageOrder : packageOrderList) {
			RedissonDelayOrder delayOrder = new RedissonDelayOrder(miningPackageOrder.getOrderNo(), null, SysConstant.TWO,
				null, RedisConstant.StreamMsgConstant.DELAY_ORDER_TIMEOUT_QUEUE);
			SpringUtils.getBean(StoreOrderAutoServiceImpl.class).hanlerOrder(delayOrder);
			ThreadUtil.sleep(2000);
		}
	}
}


/**
 * 批量关闭矿机订单
 */
private void batchCloseMiningOrder(List<ReleaseMiningBo> releaseMiningBoList) {
	// SQL语句：更新total_reward和unclaimed_reward字段
	String sql = "UPDATE t_mining_package_order SET available_amount = available_amount + released_amount + ?, " +
		" total_released_amount = total_released_amount + ?, status = 2, released_amount = 0, close_amount = ?  WHERE id = ? and status =0 ";

	int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			ReleaseMiningBo stake = releaseMiningBoList.get(i);
			ps.setBigDecimal(1, stake.getReward()); // 记录reward
			ps.setBigDecimal(2, stake.getReward()); // 记录reward
			ps.setBigDecimal(3, stake.getClosePrice()); // 记录reward
			ps.setLong(4, stake.getId()); // 记录ID
		}

		@Override
		public int getBatchSize() {
			return releaseMiningBoList.size();
		}
	});

	// 检查更新结果
	if (ArrayUtil.contains(results, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("批量更新已经释放奖励的记录失败");
		throw new ServiceException("批量更新已经释放奖励的记录失败");
	}
}

/**
 * 批量更新可领取奖励的记录
 */
private void batchUpdateAvailableAmount(List<ReleaseMiningBo> releaseMiningBoList) {
	// SQL语句：更新total_reward和unclaimed_reward字段
	String sql = "UPDATE t_mining_package_order SET available_amount = available_amount + released_amount + ?, " +
		" total_released_amount = total_released_amount + ?, released_amount = 0, status = 1, close_amount = ?  WHERE id = ? and status = 0 ";

	int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			ReleaseMiningBo stake = releaseMiningBoList.get(i);
			ps.setBigDecimal(1, stake.getReward()); // 记录reward
			ps.setBigDecimal(2, stake.getReward()); // 记录reward
			ps.setBigDecimal(3, stake.getClosePrice()); // 记录reward
			ps.setLong(4, stake.getId()); // 记录ID
		}

		@Override
		public int getBatchSize() {
			return releaseMiningBoList.size();
		}
	});

	// 检查更新结果
	if (ArrayUtil.contains(results, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("批量更新已经释放奖励的记录失败");
		throw new ServiceException("批量更新已经释放奖励的记录失败");
	}
}

/**
 * 批量更新已经释放奖励的记录
 */
private void batchUpdateReleaseMining(List<ReleaseMiningBo> releaseMiningBoList) {
	// SQL语句：更新total_reward和unclaimed_reward字段
	String sql = "UPDATE t_mining_package_order SET released_amount = released_amount + ?, " +
		" total_released_amount = total_released_amount + ?, close_amount = ?  WHERE id = ?";

	int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			ReleaseMiningBo stake = releaseMiningBoList.get(i);
			ps.setBigDecimal(1, stake.getReward()); // 记录reward
			ps.setBigDecimal(2, stake.getReward()); // 记录reward
			ps.setBigDecimal(3, stake.getClosePrice()); // 记录reward
			ps.setLong(4, stake.getId()); // 记录ID
		}

		@Override
		public int getBatchSize() {
			return releaseMiningBoList.size();
		}
	});

	// 检查更新结果
	if (ArrayUtil.contains(results, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("批量更新已经释放奖励的记录失败");
		throw new ServiceException("批量更新已经释放奖励的记录失败");
	}
}


/**
 * 对usdt资产增加
 *
 * @param userMoneyList
 */
private void bachUpdateMoneyValid1(List<UserMoney> userMoneyList) {
	int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM1, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {

			ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
			ps.setString(2, userMoneyList.get(i).getGtId());
			ps.setBigDecimal(3, userMoneyList.get(i).getValidNum1());
			ps.setString(4, userMoneyList.get(i).getSourceCode());
			ps.setInt(5, userMoneyList.get(i).getSourceType());
			ps.setLong(6, userMoneyList.get(i).getSourceId());
			ps.setLong(7, userMoneyList.get(i).getId());
		}

		@Override
		public int getBatchSize() {
			return userMoneyList.size();
		}
	});
	if (ArrayUtil.contains(ints, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("结算更新回滚了");
		throw new ServiceException("更新资产结算更新回滚了");
	}
}


	/**
	 * 对usdt资产增加
	 *
	 * @param userMoneyList
	 */
	private void bachUpdateMoneyValid8(List<UserMoney> userMoneyList) {
		int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM8, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
				ps.setString(2, userMoneyList.get(i).getGtId());
				ps.setBigDecimal(3, userMoneyList.get(i).getValidNum8());
				ps.setString(4, userMoneyList.get(i).getSourceCode());
				ps.setInt(5, userMoneyList.get(i).getSourceType());
				ps.setLong(6, userMoneyList.get(i).getSourceId());
				ps.setLong(7, userMoneyList.get(i).getId());
			}

			@Override
			public int getBatchSize() {
				return userMoneyList.size();
			}
		});
		if (ArrayUtil.contains(ints, 0)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("结算更新回滚了");
			throw new ServiceException("更新资产结算更新回滚了");
		}
	}

	/**
	 * 对保险仓余额增加
	 *
	 * @param userMoneyList
	 */
	private void bachUpdateMoneyValid5(List<UserMoney> userMoneyList) {
		int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM5, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
				ps.setString(2, userMoneyList.get(i).getGtId());
				ps.setBigDecimal(3, userMoneyList.get(i).getValidNum5());
				ps.setString(4, userMoneyList.get(i).getSourceCode());
				ps.setInt(5, userMoneyList.get(i).getSourceType());
				ps.setLong(6, userMoneyList.get(i).getSourceId());
				ps.setLong(7, userMoneyList.get(i).getId());
			}

			@Override
			public int getBatchSize() {
				return userMoneyList.size();
			}
		});
		if (ArrayUtil.contains(ints, 0)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("结算更新回滚了");
			throw new ServiceException("更新资产结算更新回滚了");
		}
	}

	/**
	 * 对财富仓余额增加
	 *
	 * @param userMoneyList
	 */
	private void bachUpdateMoneyValid4(List<UserMoney> userMoneyList) {
		int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM4, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
				ps.setString(2, userMoneyList.get(i).getGtId());
				ps.setBigDecimal(3, userMoneyList.get(i).getValidNum4());
				ps.setString(4, userMoneyList.get(i).getSourceCode());
				ps.setInt(5, userMoneyList.get(i).getSourceType());
				ps.setLong(6, userMoneyList.get(i).getSourceId());
				ps.setLong(7, userMoneyList.get(i).getId());
			}

			@Override
			public int getBatchSize() {
				return userMoneyList.size();
			}
		});
		if (ArrayUtil.contains(ints, 0)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("结算更新回滚了");
			throw new ServiceException("更新资产结算更新回滚了");
		}
	}

/**
 * 对fsn资产增加
 *
 * @param userMoneyList
 */
private void bachUpdateMoneyValid2(List<UserMoney> userMoneyList) {
	int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM2, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {

			ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
			ps.setString(2, userMoneyList.get(i).getGtId());
			ps.setBigDecimal(3, userMoneyList.get(i).getValidNum2());
			ps.setString(4, userMoneyList.get(i).getSourceCode());
			ps.setInt(5, userMoneyList.get(i).getSourceType());
			ps.setLong(6, userMoneyList.get(i).getSourceId());
			ps.setLong(7, userMoneyList.get(i).getId());
		}

		@Override
		public int getBatchSize() {
			return userMoneyList.size();
		}
	});
	if (ArrayUtil.contains(ints, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("结算更新回滚了");
		throw new ServiceException("更新资产结算更新回滚了");
	}
}

/**
 * 对动态资产资产增加
 *
 * @param userMoneyList
 */
private void bachUpdateMoneyValid3(List<UserMoney> userMoneyList) {
	int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM3, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {

			ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
			ps.setString(2, userMoneyList.get(i).getGtId());
			ps.setBigDecimal(3, userMoneyList.get(i).getValidNum3());
			ps.setString(4, userMoneyList.get(i).getSourceCode());
			ps.setObject(5, userMoneyList.get(i).getSourceType());
			ps.setObject(6, userMoneyList.get(i).getSourceId());
			ps.setLong(7, userMoneyList.get(i).getId());
		}

		@Override
		public int getBatchSize() {
			return userMoneyList.size();
		}
	});
	if (ArrayUtil.contains(ints, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("结算更新回滚了");
		throw new ServiceException("更新资产结算更新回滚了");
	}
}

private void bachUpdateMoneyValid7(List<UserMoney> userMoneyList) {
	int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM7, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
			ps.setString(2, userMoneyList.get(i).getGtId());
			ps.setBigDecimal(3, userMoneyList.get(i).getValidNum7());
			ps.setString(4, userMoneyList.get(i).getSourceCode());
			ps.setObject(5, userMoneyList.get(i).getSourceType());
			ps.setObject(6, userMoneyList.get(i).getSourceId());
			ps.setLong(7, userMoneyList.get(i).getId());
		}

		@Override
		public int getBatchSize() {
			return userMoneyList.size();
		}
	});
	if (ArrayUtil.contains(ints, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("结算更新回滚了");
		throw new ServiceException("更新资产结算更新回滚了");
	}
}

	private void bachUpdateMoneyValid9(List<UserMoney> userMoneyList) {
		int[] ints = jdbcTemplate.batchUpdate(SQL_VALID_NUM9, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
				ps.setString(2, userMoneyList.get(i).getGtId());
				ps.setBigDecimal(3, userMoneyList.get(i).getValidNum9());
				ps.setString(4, userMoneyList.get(i).getSourceCode());
				ps.setObject(5, userMoneyList.get(i).getSourceType());
				ps.setObject(6, userMoneyList.get(i).getSourceId());
				ps.setLong(7, userMoneyList.get(i).getId());
			}

			@Override
			public int getBatchSize() {
				return userMoneyList.size();
			}
		});
		if (ArrayUtil.contains(ints, 0)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("H赠送释放更新H余额回滚");
			throw new ServiceException("H赠送释放更新H余额失败");
		}
	}

	private void batchUpdateHGiftReleaseBucket(List<HGiftReleaseBucket> bucketList) {
		int[] ints = jdbcTemplate.batchUpdate(SQL_H_GIFT_RELEASE_BUCKET_DAILY_RELEASE, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				HGiftReleaseBucket bucket = bucketList.get(i);
				ps.setTimestamp(1, new java.sql.Timestamp(bucket.getUpdateTime().getTime()));
				ps.setBigDecimal(2, bucket.getReleasedAmount());
				ps.setBigDecimal(3, bucket.getRemainingAmount());
				ps.setInt(4, bucket.getReleasedDays());
				ps.setInt(5, bucket.getLastReleaseDate());
				ps.setInt(6, bucket.getStatus());
				ps.setLong(7, bucket.getId());
			}

			@Override
			public int getBatchSize() {
				return bucketList.size();
			}
		});
		if (ArrayUtil.contains(ints, 0)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("H赠送释放批量更新释放桶回滚");
			throw new ServiceException("H赠送释放批量更新释放桶失败");
		}
	}

private void batchHandleValidNum8Transfer(List<UserMoney> transferValidNum8List,
										  List<UserMoney> transferValidNum3List,
										  List<UserMoney> transferValidNum7List,
										  List<UserMoney> burnValidNum8List) {
	if (CollectionUtil.isNotEmpty(transferValidNum8List)) {
		bachUpdateMoneyValid8(transferValidNum8List);
	}
	if (CollectionUtil.isNotEmpty(transferValidNum7List)) {
		bachUpdateMoneyValid7(transferValidNum7List);
	}
	if (CollectionUtil.isNotEmpty(transferValidNum3List)) {
		bachUpdateMoneyValid3(transferValidNum3List);
	}
	if (CollectionUtil.isNotEmpty(burnValidNum8List)) {
		bachUpdateMoneyValid8(burnValidNum8List);
	}
}

/**
 * 返回相差几秒，如果当前时间晚于结束时间则返回固定的10秒
 *
 * @param current 当前时间
 * @param endTime 结束时间
 * @return 相差的秒数
 */
public Long getEndTime(Date current, Date endTime) {
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

/**
 * 批量更新用户收益汇总信息（优化版：只更新非空字段）
 *
 * @param userIncomeSummaryList 用户收益汇总列表
 */
private void batchUpdateUserIncomeSummary(List<UserIncomeSummary> userIncomeSummaryList) {
	if (CollectionUtil.isEmpty(userIncomeSummaryList)) {
		return;
	}

	// 按照更新字段类型分组，避免动态SQL的复杂性
	Map<String, List<UserIncomeSummary>> groupedByFields = userIncomeSummaryList.stream()
		.collect(Collectors.groupingBy(this::generateUpdateFieldKey));

	// 对每种字段组合执行批量更新
	for (Map.Entry<String, List<UserIncomeSummary>> entry : groupedByFields.entrySet()) {
		String fieldKey = entry.getKey();
		List<UserIncomeSummary> summaries = entry.getValue();
		String sql = buildDynamicUpdateSql(fieldKey);
		executeBatchUpdate(sql, summaries, fieldKey);
	}

	log.info("批量更新用户收益汇总成功，总更新记录数: {}", userIncomeSummaryList.size());
}

/**
 * 生成更新字段的键值，用于分组
 */
private String generateUpdateFieldKey(UserIncomeSummary summary) {
	StringBuilder key = new StringBuilder();
	if (summary.getSourceType21Balance0() != null) key.append("0");
	if (summary.getSourceType21Balance1() != null) key.append("1");
	if (summary.getSourceType23Balance() != null) key.append("3");
	if (summary.getSourceType24Balance() != null) key.append("4");
	if (summary.getSourceType25Balance() != null) key.append("5");
	return key.toString();
}

/**
 * 根据字段键值构建动态SQL
 */
private String buildDynamicUpdateSql(String fieldKey) {
	StringBuilder sql = new StringBuilder("UPDATE t_user_income_summary SET ");
	boolean hasField = false;

	if (fieldKey.contains("0")) {
		sql.append("source_type21_balance0 = source_type21_balance0 + ?");
		hasField = true;
	}
	if (fieldKey.contains("1")) {
		if (hasField) sql.append(", ");
		sql.append("source_type21_balance1 = source_type21_balance1 + ?");
		hasField = true;
	}
	if (fieldKey.contains("3")) {
		if (hasField) sql.append(", ");
		sql.append("source_type23_balance = source_type23_balance + ?");
		hasField = true;
	}
	if (fieldKey.contains("4")) {
		if (hasField) sql.append(", ");
		sql.append("source_type24_balance = source_type24_balance + ?");
		hasField = true;
	}
	if (fieldKey.contains("5")) {
		if (hasField) sql.append(", ");
		sql.append("source_type25_balance = source_type25_balance + ?");
		hasField = true;
	}

	sql.append(" WHERE user_id = ?");
	return sql.toString();
}

/**
 * 执行批量更新
 */
private void executeBatchUpdate(String sql, List<UserIncomeSummary> summaries, String fieldKey) {
	int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			UserIncomeSummary summary = summaries.get(i);
			int paramIndex = 1;

			// 只设置非空字段的参数
			if (fieldKey.contains("0")) {
				ps.setBigDecimal(paramIndex++, summary.getSourceType21Balance0());
			}
			if (fieldKey.contains("1")) {
				ps.setBigDecimal(paramIndex++, summary.getSourceType21Balance1());
			}
			if (fieldKey.contains("3")) {
				ps.setBigDecimal(paramIndex++, summary.getSourceType23Balance());
			}
			if (fieldKey.contains("4")) {
				ps.setBigDecimal(paramIndex++, summary.getSourceType24Balance());
			}
			if (fieldKey.contains("5")) {
				ps.setBigDecimal(paramIndex++, summary.getSourceType25Balance());
			}

			// 最后设置user_id
			ps.setLong(paramIndex, summary.getUserId());
		}

		@Override
		public int getBatchSize() {
			return summaries.size();
		}
	});

	// 检查更新结果
	if (ArrayUtil.contains(results, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("批量更新用户收益汇总失败，字段组合: {}, 失败记录数: {}", fieldKey, summaries.size());
		throw new ServiceException("批量更新用户收益汇总失败");
	}

	log.debug("成功更新字段组合 [{}]，记录数: {}", fieldKey, summaries.size());
}

/**
 * 批量更新订单总收益 - 分批处理，每批1000条
 *
 * @param todayRewardOrderDtoList 今日收益订单列表
 */
private void batchUpdateTotalReward(List<TodayRewardOrderDto> todayRewardOrderDtoList) {
	if (CollectionUtil.isEmpty(todayRewardOrderDtoList)) {
		return;
	}

	int batchSize = 1000; // 每批处理1000条
	int totalSize = todayRewardOrderDtoList.size();

	log.info("开始批量更新订单总收益，总记录数: {}，分批大小: {}", totalSize, batchSize);

	// 分批处理
	for (int i = 0; i < totalSize; i += batchSize) {
		int endIndex = Math.min(i + batchSize, totalSize);
		List<TodayRewardOrderDto> batch = todayRewardOrderDtoList.subList(i, endIndex);

		log.debug("处理第 {} 批，记录范围: {} - {}", (i / batchSize + 1), i + 1, endIndex);
		executeBatchUpdateTotalReward(batch);
	}

	log.info("批量更新订单总收益完成，总更新记录数: {}", totalSize);
}

/**
 * 执行批量更新订单总收益
 *
 * @param batch 批次数据
 */
private void executeBatchUpdateTotalReward(List<TodayRewardOrderDto> batch) {
	// SQL语句：批量更新订单总收益
	String sql = "UPDATE t_mining_package_order SET " +
		"total_reward = total_reward + ?, " +
		"update_time = NOW() " +
		"WHERE id = ?";

	int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			TodayRewardOrderDto orderDto = batch.get(i);

			// 设置参数：收益金额和订单ID
			ps.setBigDecimal(1, orderDto.getReward());
			ps.setLong(2, orderDto.getId());
		}

		@Override
		public int getBatchSize() {
			return batch.size();
		}
	});

	// 检查更新结果
	if (ArrayUtil.contains(results, 0)) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		log.error("批量更新订单总收益失败，批次大小: {}，部分记录未更新成功", batch.size());
		throw new ServiceException("批量更新订单总收益失败");
	}

	log.debug("批量更新订单总收益成功，本批记录数: {}", batch.size());
}

}
