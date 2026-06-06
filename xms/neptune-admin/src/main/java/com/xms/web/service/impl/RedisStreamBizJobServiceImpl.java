package com.xms.web.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.*;
import com.xms.common.exception.ServiceException;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.common.mq.dynamic.OrderMsgDO;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.spring.SpringUtils;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.*;
import com.xms.dao.entity.bo.ChangeLevelUserBo;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.UserMoney;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.entity.vo.ParentUserTaskVo;
import com.xms.dao.service.*;
import com.xms.web.service.IRedisStreamBizJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: renengadePISTA
 * @createDate: 2023/8/28
 */
@Service
@AllArgsConstructor
@Slf4j
public class RedisStreamBizJobServiceImpl implements IRedisStreamBizJobService {
	private static final String SQL_VALID_NUM3 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num3=valid_num3+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM5 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num5=valid_num5+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM4 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num4=valid_num4+?,source_code=?,source_type=?,source_id=? WHERE id=? ";


	private static final String SQL_VALID_NUM2_0 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num2=0,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM3_0 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num3=0,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM6_0 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num6=0,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM7_0 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num7=0,source_code=?,source_type=?,source_id=? WHERE id=? ";
	private static final String SQL_VALID_NUM8_0 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num8=0,source_code=?,source_type=?,source_id=? WHERE id=? ";

	private static final String SQL_VALID_NUM1 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num1=valid_num1+?,source_code=?,source_type=?,source_id=? WHERE id=? ";


	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private IUserLevelConfigService userLevelConfigService;

	@Autowired
	private INodePlanService nodePlanService;

	@Autowired
	private IW3UserLevelConfigService w3UserLevelConfigService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IUserLevelChangeLogService userLevelChangeLogService;

	@Autowired
	private IDestroyOrderService destroyOrderService;

	@Autowired
	private IUserCardAssetService userCardAssetService;

	@Autowired
	private ICardMasterOrderService cardMasterOrderService;

	@Autowired
	private UserWalletService userWalletServiceImpl;

	@Autowired
	private IRewardRecordService rewardRecordService;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private IIdoOrderService iDoOrderService;

	@Autowired
	private IAirdropClaimService airdropClaimService;

	@Autowired
	private IStakeOrderService stakeOrderService;

	@Autowired
	private IStakeRoundLiquidationLogService stakeRoundLiquidationLogServiceImpl;

	@Autowired
	private AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementServiceImpl;

	@Autowired
	private IUserStakePositionService userStakePositionService;

	@Autowired
	private IUserStakePositionFlowService userStakePositionFlowService;

	@Autowired
	private IStakeRoundService stakeRoundService;

	@Autowired
	private IUserInvestLayerConfigService userInvestLayerConfigService;

	@Autowired
	private IUserYieldRateConfigService userYieldRateConfigService;

	@Autowired
	private WithdrawalService withdrawalService;

	@Autowired
	private IUserWealthVaultService userWealthVaultService;

	@Autowired
	private IUserMoneyService userMoneyService;

	@Autowired
	private IInsuranceOrderService insuranceOrderService;

	@Autowired
	private IUserInfoSnapshotService userInfoSnapshotService;

	@Autowired
	private IUserMoneySnapshotService userMoneySnapshotService;

	@Autowired
	private IUserWealthVaultSnapshotService userWealthVaultSnapshotService;

	/**
	 * 质押任务处理
	 * @param req
	 * @return
	 */
	@Override
	public Integer handlerDynamicOrderSettlement(List req) {
		List<OrderMsgDO> ids = BeanUtil.copyToList(req, OrderMsgDO.class);
		log.debug("需要处理的事件 orders:{}", ids);
		if(CollectionUtil.isNotEmpty(ids)) {
			OrderMsgDO orderMsgDO = ids.get(0);
			if(orderMsgDO.getBizType().equals(1)){
				//质押的业务处理
				SpringUtils.getBean(RedisStreamBizJobServiceImpl.class).handleBizType1(orderMsgDO);
			}else if(orderMsgDO.getBizType().equals(2)){
				//爆仓检测
				SpringUtils.getBean(RedisStreamBizJobServiceImpl.class).handleBizType2(orderMsgDO);
			}else if(orderMsgDO.getBizType().equals(3)){
				//爆仓事件处理
				SpringUtils.getBean(RedisStreamBizJobServiceImpl.class).handleBizType3(orderMsgDO);
			}

		}
		return 1;
	}

	/**
	 * 爆仓事件处理
	 * @param orderMsgDO
	 */
	@Transactional(rollbackFor = Exception.class)
	public void handleBizType3(OrderMsgDO orderMsgDO) {
		StakeRound queryStakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getId, orderMsgDO.getId())
			.eq(StakeRound::getStatus, 1)
			.eq(StakeRound::getBizStatus, 0)
			.one();
		if(queryStakeRound!=null){
			//找到这一轮的订单
			List<UserStakePosition> userStakePositionList = userStakePositionService.lambdaQuery()
				.eq(UserStakePosition::getStakeRoundId, queryStakeRound.getId())
				.eq(UserStakePosition::getInsuranceQualifyStatus,1)
				.list();
			//todo 计算爆仓亏损
			if(CollectionUtil.isNotEmpty(userStakePositionList) && queryStakeRound.getInsuranceBalance().compareTo(BigDecimal.ZERO)>0){
				BigDecimal totalPersonalLoss = BigDecimal.ZERO;
				for (UserStakePosition userStakePosition : userStakePositionList) {
					BigDecimal allReward = userStakePosition.getTotalWithdrawalStatic().add(userStakePosition.getTotalWithdrawalDynamic())
						.add(userStakePosition.getTotalWithdrawalStudioSubsidy());
					//个人亏损额
					BigDecimal personalLossAmount = userStakePosition.getTotalStakeAmount().subtract(allReward);
					if(personalLossAmount.compareTo(BigDecimal.ZERO)>0){
						totalPersonalLoss = totalPersonalLoss.add(personalLossAmount);
					}
				}

				for (UserStakePosition userStakePosition : userStakePositionList) {
					BigDecimal allReward = userStakePosition.getTotalWithdrawalStatic().add(userStakePosition.getTotalWithdrawalDynamic())
							.add(userStakePosition.getTotalWithdrawalStudioSubsidy());
					//个人亏损额
					BigDecimal personalLossAmount = userStakePosition.getTotalStakeAmount().subtract(allReward);
					if(personalLossAmount.compareTo(BigDecimal.ZERO)>0){
//						BigDecimal remainingCompensationLimit= personalLossAmount.multiply(new BigDecimal("3"))
//							.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);

						BigDecimal remainingCompensationLimit = queryStakeRound.getInsuranceBalance().multiply(personalLossAmount)
							.divide(totalPersonalLoss, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						//todo 权重
						//设置为可赔付
						userStakePositionService.lambdaUpdate()
							.eq(UserStakePosition::getId, userStakePosition.getId())
							.eq(UserStakePosition::getInsuranceQualifyStatus,1)
							.set(UserStakePosition::getInsuranceCompensationQualifyStatus, 1)
							.set(UserStakePosition::getPersonalLossAmount, personalLossAmount)
							.set(UserStakePosition::getRemainingCompensationLimit, remainingCompensationLimit)
							.set(UserStakePosition::getAllCompensationLimit, remainingCompensationLimit)
							.update();
					}
				}

				//搞一个释放订单
				if(queryStakeRound.getInsuranceBalance().compareTo(BigDecimal.ZERO)>0){
					InsuranceOrder insuranceOrder = new InsuranceOrder();
					insuranceOrder.setOrderNo(IDUtils.getSnowflakeStr());
					insuranceOrder.setDays(100);
					insuranceOrder.setHaveDays(100);
					BigDecimal dayOutReward = queryStakeRound.getInsuranceBalance()
						.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					insuranceOrder.setDayOutReward(dayOutReward);
					insuranceOrder.setInsuranceBalance(queryStakeRound.getInsuranceBalance());
					insuranceOrder.setHsaveInsuranceBalance(queryStakeRound.getInsuranceBalance());
					insuranceOrder.setStatus(0);
					insuranceOrder.setStakeRoundId(queryStakeRound.getId());
					insuranceOrder.setCreateTime(new Date());
					insuranceOrderService.save(insuranceOrder);
				}
			}



			boolean update1 = stakeRoundService.lambdaUpdate()
				.eq(StakeRound::getId, queryStakeRound.getId())
				.eq(StakeRound::getBizStatus, 0)
				.set(StakeRound::getBizStatus, 1)
				.update();
			if(!update1){
				throw new ServiceException("更新轮次信息失败");
			}
		}
	}

	/**
	 * 处理用户之前没有在dapp注册但是购买了节点的数据
	 * @param orderMsgDO
	 */
	@Transactional(rollbackFor = Exception.class)
	@RedisLock(value = RedisConstant.LockConstant.XMS_STAKE_ORDER_PLAN)
	public void handleBizType2(OrderMsgDO orderMsgDO) {
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getId,orderMsgDO.getId())
			.eq(StakeRound::getStatus, 0)
			.one();
		if(stakeRound!=null){
			//(玩家参与的总量小于+购买贡献分总额)<发放的工作室收益+累计提现总额
			BigDecimal t1= stakeRound.getPlayerStakeTotal().add(stakeRound.getBuyPointTotal());
			BigDecimal t2 = stakeRound.getStudioSubsidyTotal().add(stakeRound.getWithdrawRewardTotalFull());
			if(t1.compareTo(t2)<=0){
				Date snapshotTime = new Date();

				// 爆仓前先保存全量用户信息快照，便于后续审计和对账
				List<UserInfo> userInfoList = userInfoService.lambdaQuery().list();
				saveUserInfoSnapshots(stakeRound.getId(), snapshotTime, userInfoList);

				//更新所有用户的业绩
				userInfoService.lambdaUpdate()
//					.set(UserInfo::getGameLevel,0)
//					.set(UserInfo::getIsValid,0)
//					.set(UserInfo::getHasStudioSubsidyEligible,0)
//					.set(UserInfo::getPerformance,0)
//					.set(UserInfo::getSubPerformance,0)
//					.set(UserInfo::getCommunityPerformance,0)
					.set(UserInfo::getHistoryPerformance,0)
					.set(UserInfo::getLayerLevel,0)
					.update();

				//更新状态
				boolean update1 = stakeRoundService.lambdaUpdate()
					.eq(StakeRound::getId, stakeRound.getId())
					.eq(StakeRound::getStatus, 0)
					.set(StakeRound::getStatus, 1)
					.set(StakeRound::getLiquidationTime, new Date())
					.update();
				if(!update1){
					throw new ServiceException("更新轮次信息失败");
				}

				//爆仓正在提现的订单
				withdrawalService.lambdaUpdate()
					.eq(Withdrawal::getStakeRoundId, stakeRound.getId())
					.eq(Withdrawal::getStatus, 0)
					.set(Withdrawal::getStatus, 5)
					.update();

				//把所有的财富仓金额都释放
				List<UserWealthVault> userWealthVaultList = userWealthVaultService.lambdaQuery()
					.list();
				// 爆仓前先保存全量财富仓快照，避免后续释放和清零后无法追溯
				saveUserWealthVaultSnapshots(stakeRound.getId(), snapshotTime, userWealthVaultList);
				//更新财富仓余额为0
				userWealthVaultService.lambdaUpdate()
					.set(UserWealthVault::getSeg1Amount,0)
					.set(UserWealthVault::getSeg2Amount,0)
					.set(UserWealthVault::getSeg3Amount,0)
					.set(UserWealthVault::getSeg4Amount,0)
					.set(UserWealthVault::getSeg5Amount,0)
					.update();

				if(CollectionUtil.isNotEmpty(userWealthVaultList)){
					List<UserMoney> userMoneyValidNum4List = new ArrayList<>(userWealthVaultList.size()>=1000?1000:userWealthVaultList.size());
					int batchSize = 1000;
					int stakeCount4 = 0;
					UserMoney entity = null;
					for (UserWealthVault userWealthVault : userWealthVaultList) {
						BigDecimal segAmount = userWealthVault.getSeg1Amount().add(userWealthVault.getSeg2Amount()).add(userWealthVault.getSeg3Amount()).add(userWealthVault.getSeg4Amount())
							.add(userWealthVault.getSeg5Amount());
						if(segAmount.compareTo(BigDecimal.ZERO)>0){
							entity = new UserMoney();
							entity.setId(userWealthVault.getId());
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(stakeRound.getId().toString());
							entity.setSourceId(userWealthVault.getId());
							entity.setValidNum4(segAmount);
							entity.setSourceType(ConstantType.user_money_log_source_type.type_15);
							entity.setUpdateTime(new Date());
							userMoneyValidNum4List.add(entity);
							stakeCount4++;
							if (stakeCount4 >= batchSize) {
								bachUpdateMoneyValid4(userMoneyValidNum4List);
								userMoneyValidNum4List.clear();
								log.info("更新成功");
								stakeCount4 = 0;
							}
						}
					}

					//爆仓释放财富仓余额
					if(CollectionUtil.isNotEmpty(userMoneyValidNum4List)){
						bachUpdateMoneyValid4(userMoneyValidNum4List);
					}
				}

				//扣减:动态、静态、工作室余额
				List<UserStakePosition> userStakePositionList = userStakePositionService.lambdaQuery()
					.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
					.select(UserStakePosition::getUserId)
					.list();
				if(CollectionUtil.isNotEmpty(userStakePositionList)){
					Set<Long> userIds = userStakePositionList.stream().map(
						UserStakePosition::getUserId
					).collect(Collectors.toSet());

					// 爆仓前保存全量钱包快照，后续即使只清当前轮次用户，也能完整对账全网资产
					List<UserMoney> allUserMoneyList = userMoneyService.lambdaQuery().list();
					saveUserMoneySnapshots(stakeRound.getId(), snapshotTime, allUserMoneyList);

					// 这里只查询当前轮次相关用户的钱包，用于后续实际清零处理
					List<UserMoney> userMoneyList = userMoneyService.lambdaQuery()
						.in(UserMoney::getId,userIds)
						.select(UserMoney::getId, UserMoney::getValidNum2, UserMoney::getValidNum3, UserMoney::getValidNum6,
							UserMoney::getValidNum7,UserMoney::getValidNum8)
						.list();

					List<UserMoney> userMoneyValidNum2List = new ArrayList<>(userMoneyList.size()>=1000?1000:userMoneyList.size());
					List<UserMoney> userMoneyValidNum3List = new ArrayList<>(userMoneyList.size()>=1000?1000:userMoneyList.size());
					List<UserMoney> userMoneyValidNum6List = new ArrayList<>(userMoneyList.size()>=1000?1000:userMoneyList.size());
					List<UserMoney> userMoneyValidNum7List = new ArrayList<>(userMoneyList.size()>=1000?1000:userMoneyList.size());
					List<UserMoney> userMoneyValidNum8List = new ArrayList<>(userMoneyList.size()>=1000?1000:userMoneyList.size());
					int batchSize = 1000;
					int stakeCount2 = 0;
					int stakeCount3 = 0;
					int stakeCount6 = 0;
					int stakeCount7 = 0;
					int stakeCount8 = 0;
					UserMoney entity = null;
					for (UserMoney userMoney : userMoneyList) {
						if(userMoney.getValidNum2().compareTo(BigDecimal.ZERO)>0){
							entity = new UserMoney();
							entity.setId(userMoney.getId());
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(stakeRound.getId().toString());
							entity.setSourceId(userMoney.getId());
							entity.setValidNum2(userMoney.getValidNum2().negate());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_12);
							entity.setUpdateTime(new Date());
							userMoneyValidNum2List.add(entity);
							stakeCount2++;
							if (stakeCount2 >= batchSize) {
								bachUpdateMoneyValid_0(userMoneyValidNum2List,2);
								userMoneyValidNum2List.clear();
								log.info("更新成功");
								stakeCount2 = 0;
							}
						}

						if(userMoney.getValidNum3().compareTo(BigDecimal.ZERO)>0){
							entity = new UserMoney();
							entity.setId(userMoney.getId());
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(stakeRound.getId().toString());
							entity.setSourceId(userMoney.getId());
							entity.setValidNum3(userMoney.getValidNum3().negate());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_12);
							entity.setUpdateTime(new Date());
							userMoneyValidNum3List.add(entity);
							stakeCount3++;
							if (stakeCount3 >= batchSize) {
								bachUpdateMoneyValid_0(userMoneyValidNum3List,3);
								userMoneyValidNum3List.clear();
								log.info("更新成功");
								stakeCount3 = 0;
							}
						}

						if(userMoney.getValidNum6().compareTo(BigDecimal.ZERO)>0){
							entity = new UserMoney();
							entity.setId(userMoney.getId());
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(stakeRound.getId().toString());
							entity.setValidNum6(userMoney.getValidNum6().negate());
							entity.setSourceId(userMoney.getId());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_12);
							entity.setUpdateTime(new Date());
							userMoneyValidNum6List.add(entity);
							stakeCount6++;
							if (stakeCount6 >= batchSize) {
								bachUpdateMoneyValid_0(userMoneyValidNum6List,6);
								userMoneyValidNum6List.clear();
								log.info("更新成功");
								stakeCount6 = 0;
							}
						}

						if(userMoney.getValidNum7().compareTo(BigDecimal.ZERO)>0){
							entity = new UserMoney();
							entity.setId(userMoney.getId());
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(stakeRound.getId().toString());
							entity.setValidNum7(userMoney.getValidNum7().negate());
							entity.setSourceId(userMoney.getId());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_12);
							entity.setUpdateTime(new Date());
							userMoneyValidNum7List.add(entity);
							stakeCount7++;
							if (stakeCount7 >= batchSize) {
								bachUpdateMoneyValid_0(userMoneyValidNum7List,7);
								userMoneyValidNum7List.clear();
								log.info("更新成功");
								stakeCount7 = 0;
							}
						}

						if(userMoney.getValidNum8().compareTo(BigDecimal.ZERO)>0){
							entity = new UserMoney();
							entity.setId(userMoney.getId());
							entity.setGtId(IDUtils.getSnowflakeStr());
							entity.setSourceCode(stakeRound.getId().toString());
							entity.setValidNum8(userMoney.getValidNum8().negate());
							entity.setSourceId(userMoney.getId());
							entity.setSourceType(ConstantType.user_money_log_source_type.type_12);
							entity.setUpdateTime(new Date());
							userMoneyValidNum8List.add(entity);
							stakeCount8++;
							if (stakeCount8 >= batchSize) {
								bachUpdateMoneyValid_0(userMoneyValidNum8List,8);
								userMoneyValidNum8List.clear();
								log.info("更新成功");
								stakeCount8 = 0;
							}
						}
					}

					//插入流水记录
					if(CollectionUtil.isNotEmpty(userMoneyValidNum2List)){
						bachUpdateMoneyValid_0(userMoneyValidNum2List,2);
					}

					if(CollectionUtil.isNotEmpty(userMoneyValidNum3List)){
						bachUpdateMoneyValid_0(userMoneyValidNum3List,3);
					}

					if(CollectionUtil.isNotEmpty(userMoneyValidNum6List)){
						bachUpdateMoneyValid_0(userMoneyValidNum6List,6);
					}

					if(CollectionUtil.isNotEmpty(userMoneyValidNum7List)){
						bachUpdateMoneyValid_0(userMoneyValidNum7List,7);
					}
					if(CollectionUtil.isNotEmpty(userMoneyValidNum8List)){
						bachUpdateMoneyValid_0(userMoneyValidNum8List,8);
					}
				}
				//质押定单为爆仓
				userStakePositionService.lambdaUpdate()
					.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
					.eq(UserStakePosition::getStatus, 1)
					.set(UserStakePosition::getStatus, 2)
					.update();

				//创建新的轮次
				StakeRound insertRound = new StakeRound();
				insertRound.setStatus(0);
				insertRound.setBizStatus(0);
				insertRound.setStartTime(new Date());
				insertRound.setCreateTime(insertRound.getStartTime());
				//玩家参与量
				insertRound.setPlayerStakeTotal(BigDecimal.ZERO);
				insertRound.setStudioSubsidyTotal(BigDecimal.ZERO);
				insertRound.setWithdrawRewardTotalFull(BigDecimal.ZERO);
				insertRound.setInsuranceBalance(BigDecimal.ZERO);
				boolean save = stakeRoundService.save(insertRound);
				if(!save){
					throw new ServiceException("创建新的轮次信息失败");
				}

				//处理爆仓事件
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
						OrderMsgDO orderMsgDO = new OrderMsgDO();
						orderMsgDO.setId(stakeRound.getId());
						orderMsgDO.setBizType(3);
						orderMsgDOList.add(orderMsgDO);
						asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);

					}
				});
			}
		}
	}

	private void saveUserInfoSnapshots(Long stakeRoundId, Date snapshotTime, List<UserInfo> userInfoList) {
		if (CollectionUtil.isEmpty(userInfoList)) {
			return;
		}
		List<UserInfoSnapshot> snapshotList = new ArrayList<>(userInfoList.size());
		for (UserInfo userInfo : userInfoList) {
			UserInfoSnapshot snapshot = new UserInfoSnapshot();
			BeanUtil.copyProperties(userInfo, snapshot);
			snapshot.setSnapshotId(null);
			snapshot.setStakeRoundId(stakeRoundId);
			snapshot.setSnapshotTime(snapshotTime);
			snapshotList.add(snapshot);
		}
		boolean save = userInfoSnapshotService.saveBatch(snapshotList, 1000);
		if (!save) {
			throw new ServiceException("保存用户信息快照失败");
		}
	}

	private void saveUserMoneySnapshots(Long stakeRoundId, Date snapshotTime, List<UserMoney> userMoneyList) {
		if (CollectionUtil.isEmpty(userMoneyList)) {
			return;
		}
		List<UserMoneySnapshot> snapshotList = new ArrayList<>(userMoneyList.size());
		for (UserMoney userMoney : userMoneyList) {
			UserMoneySnapshot snapshot = new UserMoneySnapshot();
			BeanUtil.copyProperties(userMoney, snapshot);
			snapshot.setSnapshotId(null);
			snapshot.setStakeRoundId(stakeRoundId);
			snapshot.setSnapshotTime(snapshotTime);
			snapshotList.add(snapshot);
		}
		boolean save = userMoneySnapshotService.saveBatch(snapshotList, 1000);
		if (!save) {
			throw new ServiceException("保存用户余额快照失败");
		}
	}

	private void saveUserWealthVaultSnapshots(Long stakeRoundId, Date snapshotTime, List<UserWealthVault> userWealthVaultList) {
		if (CollectionUtil.isEmpty(userWealthVaultList)) {
			return;
		}
		List<UserWealthVaultSnapshot> snapshotList = new ArrayList<>(userWealthVaultList.size());
		for (UserWealthVault userWealthVault : userWealthVaultList) {
			UserWealthVaultSnapshot snapshot = new UserWealthVaultSnapshot();
			BeanUtil.copyProperties(userWealthVault, snapshot);
			snapshot.setSnapshotId(null);
			snapshot.setStakeRoundId(stakeRoundId);
			snapshot.setSnapshotTime(snapshotTime);
			snapshotList.add(snapshot);
		}
		boolean save = userWealthVaultSnapshotService.saveBatch(snapshotList, 1000);
		if (!save) {
			throw new ServiceException("保存用户财富仓快照失败");
		}
	}

	/**
	 * 处理推送合约购买的节点数据
	 * @param orderMsgDO
	 * @return
	 */
	@Nullable
	@Transactional(rollbackFor = Exception.class)
	public Integer handleBizType1(OrderMsgDO orderMsgDO) {

		StakeOrder stakeOrder = stakeOrderService.lambdaQuery()
			.eq(StakeOrder::getId, orderMsgDO.getId())
			.eq(StakeOrder::getBizStatus, 0)
			.one();
		if(stakeOrder==null){
			log.info("质押订单已经处理 订单id:{}", orderMsgDO.getId());
			return 1;
		}

		//增加、添加流水
		UserStakePosition userStakePosition = userStakePositionService.lambdaQuery()
			.eq(UserStakePosition::getUserId, stakeOrder.getUserId())
			.eq(UserStakePosition::getStakeRoundId, stakeOrder.getStakeRoundId())
			.one();
		UserStakePositionFlow flowLog = new UserStakePositionFlow();
		flowLog.setFlowNo(IDUtils.getSnowflakeStr());
		flowLog.setUserId(stakeOrder.getUserId());
		flowLog.setBizOrderNo(stakeOrder.getOrderNo());
		flowLog.setChangeAmount(stakeOrder.getStakeAmount());
		flowLog.setStakeRoundId(stakeOrder.getStakeRoundId());

		if(userStakePosition == null){
			//初始化日利率
			UserYieldRateConfig rateConfig = userYieldRateConfigService.lambdaQuery()
				.one();

			flowLog.setBeforeTotalStake(BigDecimal.ZERO);
			flowLog.setAfterTotalStake(stakeOrder.getStakeAmount());
			//插入一个
			userStakePosition = new UserStakePosition();

			userStakePosition.setUserId(stakeOrder.getUserId());
			userStakePosition.setTotalStakeAmount(stakeOrder.getStakeAmount());

			userStakePosition.setCurrentDayRate(rateConfig.getInitialDailyRate());
			userStakePosition.setContinuousNoWithdrawDays(0);
			userStakePosition.setTotalReward(BigDecimal.ZERO);
			userStakePosition.setTodayReward(BigDecimal.ZERO);
			userStakePosition.setStatus(1);
			userStakePosition.setOrderNo(IDUtils.getSnowflakeStr());
			userStakePosition.setStakeRoundId(stakeOrder.getStakeRoundId());
			userStakePosition.setCreateTime(new Date());

			userStakePositionService.save(userStakePosition);
		}else{
			flowLog.setBeforeTotalStake(userStakePosition.getTotalStakeAmount());
			//更新后
			flowLog.setAfterTotalStake(userStakePosition.getTotalStakeAmount().add(stakeOrder.getStakeAmount()));
			//更新
			boolean update = userStakePositionService.lambdaUpdate()
				.eq(UserStakePosition::getId, userStakePosition.getId())
				.setSql("total_stake_amount = total_stake_amount + " + stakeOrder.getStakeAmount())
				.set(UserStakePosition::getUpdateTime, new Date())
				.update();
			if(!update){
				throw new ServiceException("更新用户质押信息失败");
			}
		}

		//添加流水
		userStakePositionFlowService.save(flowLog);

		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, stakeOrder.getUserId())
			.one();
		List<Long> parentIds = userInfo.getParentIds();

		List<UserLevelConfig> userLevelConfigList = userLevelConfigService.lambdaQuery()
			.orderByAsc(UserLevelConfig::getLevel)
			.list();

		//发放工作室收益
		if(CollectionUtil.isNotEmpty(userInfo.getParentIds())){
			sendStudioSubsidyReward(userInfo, userLevelConfigList, stakeOrder);
		}

		parentIds.add(userInfo.getUserId());
		//计算小区业绩
		calculateCommunityPerformance(parentIds);
		//计算等级
		calculateParentUserLevel(userInfo,userLevelConfigList);

		//计算用户的层级
		calculateLayerLevel(userInfo.getHistoryPerformance(), userInfo);

		boolean update = stakeOrderService.lambdaUpdate()
			.eq(StakeOrder::getId, stakeOrder.getId())
			.eq(StakeOrder::getStatus, 1)
			.set(StakeOrder::getBizStatus, 1)
			.set(StakeOrder::getUpdateTime, new Date())
			.update();
		if(!update){
			throw new ServiceException("更新质押订单信息失败");
		}
/*		if(!b){
			//检测是否爆仓
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
					OrderMsgDO orderMsgDO = new OrderMsgDO();
					orderMsgDO.setId(stakeOrder.getStakeRoundId());
					orderMsgDO.setBizType(2);
					orderMsgDOList.add(orderMsgDO);
					asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);

				}
			});
		}*/
		return null;
	}

	private void calculateLayerLevel(BigDecimal historyPerformance, UserInfo userInfo) {
		List<UserInvestLayerConfig> list = userInvestLayerConfigService.lambdaQuery()
			.orderByAsc(UserInvestLayerConfig::getMinInvest)
			.list();
		Integer layerLevel = 0;
		if(CollectionUtil.isNotEmpty(list)){
			for (UserInvestLayerConfig config : list) {
				if(historyPerformance.compareTo(config.getMinInvest()) >= 0){
					layerLevel = config.getLevel();
				}else{
					break;
				}
			}
		}

		// 按历史业绩匹配当前可享受的层级等级
		if(!userInfo.getLayerLevel().equals(layerLevel)){
			boolean updateLayerLevel = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getUserId())
				.set(UserInfo::getLayerLevel, layerLevel)
				.set(UserInfo::getUpdateTime, new Date())
				.update();
			if(!updateLayerLevel){
				throw new ServiceException("更新用户层级等级失败");
			}
		}
	}

	private void sendStudioSubsidyReward(UserInfo userInfo, List<UserLevelConfig> userLevelConfigList, StakeOrder stakeOrder) {
		List<UserInfo> parentUserInfoList = userInfoService.lambdaQuery()
			.in(UserInfo::getUserId, userInfo.getParentIds())
			.select(UserInfo::getPerformance, UserInfo::getGameLevel, UserInfo::getMinGameLevel,
				UserInfo::getUserId, UserInfo::getCommunityPerformance, UserInfo::getNodeLevel,
				UserInfo::getMinNodeLevel)
			.orderByDesc(UserInfo::getUserId)
			.list().stream().peek(item -> {
				item.setGameLevel(Math.max(item.getGameLevel(), item.getMinGameLevel()));
				item.setNodeLevel(Math.max(item.getNodeLevel(), item.getMinNodeLevel()));
			})
			.collect(Collectors.toList());
		Map<Integer, UserLevelConfig> levelConfigMap = userLevelConfigList.stream()
			.collect(Collectors.toMap(UserLevelConfig::getLevel, Function.identity(), (o1, o2) -> o1));
		Map<Integer, BigDecimal> nodeMap = nodePlanService.lambdaQuery()
			.list()
			.stream()
			.peek(item -> {
				item.setStudioSubsidyRatio(
					item.getStudioSubsidyRatio()
						.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew));
			})
			.collect(Collectors.toMap(NodePlan::getNodeLevel, NodePlan::getStudioSubsidyRatio, (k1, k2) -> k2));
		BigDecimal issuedRewardRatio = BigDecimal.ZERO;
		Integer lastRewardNodeLevel = 0;
		for (UserInfo parentUserInfo : parentUserInfoList) {
			if (parentUserInfo.getGameLevel() <= 0 || parentUserInfo.getNodeLevel() <= 0) {
				continue;
			}
			UserLevelConfig userLevelConfig = levelConfigMap.get(parentUserInfo.getGameLevel());
			if (userLevelConfig.getHasStudioSubsidy() != 1) {
				continue;
			}
			BigDecimal rewardRatio = nodeMap.get(parentUserInfo.getNodeLevel());
			if (rewardRatio.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			BigDecimal finalRewardRatio = rewardRatio.subtract(issuedRewardRatio);
			if (parentUserInfo.getNodeLevel() <= lastRewardNodeLevel
				|| finalRewardRatio.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			BigDecimal reward = finalRewardRatio.multiply(stakeOrder.getStakeAmount())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (reward.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			int count = userWalletServiceImpl.handerUserMoney(reward, stakeOrder.getOrderNo(),
				parentUserInfo.getUserId(), userInfo.getUserId(), ConstantType.user_money_log_source_type.type_7,
				ConstantType.user_money_coin_type.type_6);
			if (count != 1) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}

			RewardRecord selfRecord1 = new RewardRecord();
			selfRecord1.setOrderCode(IDUtils.getSnowflakeStr());
			selfRecord1.setUserId(parentUserInfo.getUserId());
			selfRecord1.setAmount(reward);
			selfRecord1.setCoinType(ConstantType.user_money_coin_type.type_6);
			selfRecord1.setSourceType(ConstantType.xms_reward_record_source_type.type_6);
			selfRecord1.setSourceUserId(userInfo.getUserId());
			selfRecord1.setSourceOrderCode(stakeOrder.getOrderNo());
			boolean save = rewardRecordService.save(selfRecord1);
			if(!save){
				throw new ServiceException("保存用户收益记录失败");
			}

			//累计工作室收益
			userStakePositionService.lambdaUpdate()
				.eq(UserStakePosition::getUserId,parentUserInfo.getUserId())
				.eq(UserStakePosition::getStakeRoundId,stakeOrder.getStakeRoundId())
				.setSql("studio_subsidy = studio_subsidy + " + reward)
				.update();

			issuedRewardRatio = rewardRatio;
			lastRewardNodeLevel = parentUserInfo.getNodeLevel();
		}
	}


	private Integer handleBizType11(OrderMsgDO orderMsgDO) {
		//正常购买回调处理
		IdoOrder queryIdoOrder = iDoOrderService.lambdaQuery()
			.eq(IdoOrder::getId, orderMsgDO.getId())
			.eq(IdoOrder::getBizStatus, 1)
			.one();
		if(queryIdoOrder==null){
			log.info("节点订单已经处理 订单id:{}", orderMsgDO.getId());
			return 1;
		}
		//查询对应用户 加业绩、加直推、加团队
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, queryIdoOrder.getUserId())
			.one();
		boolean update = userInfoService.lambdaUpdate()
			.eq(UserInfo::getUserId, userInfo.getUserId())
			.setSql("performance = performance + "+queryIdoOrder.getShares())
			.update();
		if(!update){
			throw new ServiceException("更新用户节点信息失败");
		}
		if(userInfo.getInviteUserId()!=null){
			update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getInviteUserId())
				.setSql("sub_performance = sub_performance + "+queryIdoOrder.getShares())
				.update();
			if(!update){
				throw new ServiceException("更新直推用户节点信息失败");
			}
			//更新团队信息
			update = userInfoService.lambdaUpdate()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.setSql("umbrella_performance = umbrella_performance + "+queryIdoOrder.getShares())
				.update();
			if(!update){
				throw new ServiceException("更新直推用户节点信息失败");
			}
		}

		update = iDoOrderService.lambdaUpdate()
			.eq(IdoOrder::getId, orderMsgDO.getId())
			.eq(IdoOrder::getBizStatus, 1)
			.set(IdoOrder::getBizStatus, 2)
			.set(IdoOrder::getUpdateTime, new Date())
			.update();
		if(!update){
			throw new ServiceException("更新订单信息失败");
		}
		return null;
	}
	/**
	 * 计算等级
	 * @param airdropClaim
	 */
	private void callUserLevel(AirdropClaim airdropClaim,List<UserLevelConfig> levelConfigList) {
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, airdropClaim.getUserId())
			.one();
		for (UserLevelConfig userLevelConfig : levelConfigList) {
			userLevelConfig.setIntUmbrellaPerformance(userLevelConfig.getUmbrellaPerformance().intValue());
		}
		List<UserInfo> parentUserList = userInfoService.getParentUserList(userInfo.getUserId());
		if(CollectionUtil.isEmpty(parentUserList)){
			parentUserList = new ArrayList<>(8);
		}
		//把自己放进去校验是否降级
		parentUserList.addFirst(userInfo);
		for (UserInfo parentUser : parentUserList) {
			log.info("校验是否降级 userInfo:{}",parentUser);
			//必须自身有效才能参与等级
			if(parentUser.getIsValid().equals(0)){
				continue;
			}
			Integer initGameLevel = 0;
			for (UserLevelConfig w3UserLevelConfig : levelConfigList) {
				//团队有效地址数量要大于配置
				if(parentUser.getValidUmbrellaNum() >= w3UserLevelConfig.getIntUmbrellaPerformance()){
					initGameLevel = w3UserLevelConfig.getLevel();
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

	/**
	 * 发放极差奖励
	 * @param airdropClaim
	 */
	private void sendReward(AirdropClaim airdropClaim,List<UserLevelConfig> levelConfigList) {
		Map<Integer, UserLevelConfig> levelConfigMap = levelConfigList.stream()
			.collect(Collectors.toMap(UserLevelConfig::getLevel, Function.identity(), (o1, o2) -> o1));

		BigDecimal tokenAmount = airdropClaim.getTokenAmount();
		List<ParentUserTaskVo> parentUserTaskVo = userInfoService.getParentUserTaskVo(airdropClaim.getUserId());
		if(CollectionUtil.isNotEmpty(parentUserTaskVo)){

			// 已发放的累积比例
			BigDecimal initRewardRatio = BigDecimal.ZERO;

			RewardRecord rewardRecordEntity = null;
			List<RewardRecord> teamRewardRecordList = new ArrayList<>(20);
			List<UserMoney> userMoneyValidNum1List = new ArrayList<>(20);
			UserMoney entity = null;

			for (ParentUserTaskVo p : parentUserTaskVo) {
				// 无效用户不参与
				if (p.getIsValid() == null || p.getIsValid() == 0) {
					continue;
				}
				rewardRecordEntity = new  RewardRecord();
				// 真实等级 = max(gameLevel, minGameLevel)
				int gameLevel =p.getGameLevel()>p.getMinGameLevel()?p.getGameLevel():p.getMinGameLevel();

				UserLevelConfig cfg = levelConfigMap.get(gameLevel);
				if (cfg == null || cfg.getRewardRatio() == null || cfg.getRewardRatio().compareTo(BigDecimal.ZERO) <= 0) {
					continue;
				}

				BigDecimal finalRewardRatio = cfg.getRewardRatio().subtract(initRewardRatio);

				// 1) 极差奖
				if (finalRewardRatio.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal teamReward = tokenAmount.multiply(finalRewardRatio)
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (teamReward.compareTo(BigDecimal.ZERO) > 0) {
						//插入奖金明细
						rewardRecordEntity.setOrderCode(IDUtils.getSnowflakeStr());
						rewardRecordEntity.setUserId(p.getUserId());
						rewardRecordEntity.setAmount(teamReward);
						rewardRecordEntity.setSourceType(ConstantType.xms_reward_record_source_type.type_1);
						rewardRecordEntity.setSourceUserId(airdropClaim.getUserId());
						rewardRecordEntity.setSourceOrderCode(airdropClaim.getClaimNo());
						rewardRecordEntity.setGtId(IDUtils.getSnowflakeStr());
						teamRewardRecordList.add(rewardRecordEntity);
						// 更新极差状态
						initRewardRatio = cfg.getRewardRatio();

						//发放钱明细
						//批量加钱
						entity = new UserMoney();
						entity.setId(p.getUserId());
						entity.setValidNum1(teamReward);
						entity.setGtId(IDUtils.getSnowflakeStr());
						entity.setSourceCode(airdropClaim.getClaimNo());
						entity.setSourceId(airdropClaim.getUserId());
						entity.setSourceType(ConstantType.user_money_log_source_type.type_4);
						entity.setUpdateTime(new Date());
						userMoneyValidNum1List.add(entity);
					}
				}
			}

			//插入v1资产
			if (CollectionUtil.isNotEmpty(userMoneyValidNum1List)) {
				bachUpdateMoneyValid1(userMoneyValidNum1List);
			}
		}
	}

	public void processCardOrderBiz(CardMasterOrder masterOrder) {
		String masterOrderNo = masterOrder.getMasterOrderNo();
		List<RewardRecord> rewardRecordList = new ArrayList<>();
		try {
			Long userId = masterOrder.getUserId();
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getUserId, userId)
				.one();
			if (userInfo == null) {
				throw new ServiceException("用户不存在，无法处理订单");
			}
			BigDecimal totalComputingPower = masterOrder.getPackagePower();
			BigDecimal payPrice = masterOrder.getPayAmount();
			// 自身获得算力奖励记录
			RewardRecord selfRecord = new RewardRecord();
			selfRecord.setOrderCode(masterOrderNo);
			selfRecord.setUserId(userId);
			selfRecord.setAmount(totalComputingPower);
			selfRecord.setCoinType(ConstantType.reward_record_coin_type.type_1);
			if(masterOrder.getSourceType().equals(1)){
				selfRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_22);
			}else{
				selfRecord.setSourceType(ConstantType.xms_reward_record_source_type.type_21);
			}

			selfRecord.setSourceUserId(userId);
			selfRecord.setSourceOrderCode(masterOrder.getMasterOrderNo());
			rewardRecordList.add(selfRecord);

			//补贴算力
			if(masterOrder.getExtraComputingPower().compareTo(BigDecimal.ZERO)>0){
				RewardRecord selfRecord1 = new RewardRecord();
				selfRecord1.setOrderCode(masterOrderNo);
				selfRecord1.setUserId(userId);
				selfRecord1.setAmount(masterOrder.getExtraComputingPower());
				selfRecord1.setCoinType(ConstantType.reward_record_coin_type.type_1);
				selfRecord1.setSourceType(ConstantType.xms_reward_record_source_type.type_26);
				selfRecord1.setSourceUserId(userId);
				selfRecord1.setSourceOrderCode(masterOrder.getMasterOrderNo());
				rewardRecordList.add(selfRecord1);
			}


			if (StrUtil.isNotBlank(userInfo.getParentChain())) {
				// 直推业绩
				if (userInfo.getInviteUserId() != null) {
					userInfoService.lambdaUpdate()
						.eq(UserInfo::getUserId, userInfo.getInviteUserId())
						.setSql("sub_performance = sub_performance + " + payPrice)
						.update();
				}
				List<Long> parentIds = userInfo.getParentIds();
				if (CollectionUtil.isNotEmpty(parentIds)) {
					userInfoService.lambdaUpdate()
						.in(UserInfo::getUserId, parentIds)
						.setSql("umbrella_performance = umbrella_performance + " + payPrice)
						.update();
				}

				//计算小区业绩
				calculateCommunityPerformance(parentIds);

				// 直推、间推奖励（USDT + 算力）
				if (userInfo.getInviteUserId() != null) {

					UserInfo inviteUserInfo = userInfoService.lambdaQuery()
						.eq(UserInfo::getUserId, userInfo.getInviteUserId())
						.one();

					if(inviteUserInfo.getIsValid()==1){
						//必须得有效用户
						BigDecimal directURatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_direct_xls_ratio))
							.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						BigDecimal directUReward = payPrice.multiply(directURatio)
							.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						if (directUReward.compareTo(BigDecimal.ZERO) > 0) {
							int count = userWalletServiceImpl.handerUserMoney(directUReward, masterOrderNo,
								userInfo.getInviteUserId(), userInfo.getUserId(), ConstantType.user_money_log_source_type.type_2,
								ConstantType.user_money_coin_type.type_1);
							if (count != 1) {
								throw new ServiceException(ResponseCode.CODE_1015);
							}
						}

						BigDecimal directCpRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_direct_computing_power_ratio))
							.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						BigDecimal directCpReward = totalComputingPower.multiply(directCpRatio)
							.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						if (directCpReward.compareTo(BigDecimal.ZERO) > 0) {
						}
					}


					//间推用户
					if(inviteUserInfo.getInviteUserId()!=null){
						// 间推奖励
						UserInfo directUser = userInfoService.lambdaQuery()
							.eq(UserInfo::getUserId, inviteUserInfo.getInviteUserId())
							.one();
						if (directUser != null && directUser.getIsValid() == 1) {
							BigDecimal indirectURatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_indirect_xls_ratio))
								.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
							BigDecimal indirectUReward = payPrice.multiply(indirectURatio)
								.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
							if (indirectUReward.compareTo(BigDecimal.ZERO) > 0) {
								int count = userWalletServiceImpl.handerUserMoney(indirectUReward, masterOrderNo,
									directUser.getUserId(), userInfo.getUserId(), ConstantType.user_money_log_source_type.type_3,
									ConstantType.user_money_coin_type.type_1);
								if (count != 1) {
									throw new ServiceException(ResponseCode.CODE_1015);
								}
							}

							BigDecimal indirectCpRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_indirect_computing_power_ratio))
								.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
							BigDecimal indirectCpReward = totalComputingPower.multiply(indirectCpRatio)
								.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
							if (indirectCpReward.compareTo(BigDecimal.ZERO) > 0) {

							}
						}
					}

				}

				// 等级计算
				List<UserLevelConfig> userLevelConfigList = userLevelConfigService.lambdaQuery()
					.orderByAsc(UserLevelConfig::getLevel)
					.list();
				calculateParentUserLevel(userInfo,userLevelConfigList);
			}

			if (CollectionUtil.isNotEmpty(rewardRecordList)) {
				rewardRecordService.saveBatch(rewardRecordList);
			}
		} catch (Exception ex) {
			log.error("processCardOrderBiz error, orderNo={}", masterOrderNo, ex);
			throw ex;
		}
	}

	/**
	 * 计算等级
	 *
	 * @param userInfo
	 */
	private void calculateParentUserLevel(UserInfo userInfo,List<UserLevelConfig> userLevelConfigList) {
		List<UserInfo> parentUserList = userInfoService.getParentUserList(userInfo.getUserId());
		parentUserList.add(0, userInfo);

		for (UserInfo parentUser : parentUserList) {
			if (parentUser.getIsValid() == null || parentUser.getIsValid().equals(0)) {
				continue;
			}
			Integer initGameLevel = 0;
			for (UserLevelConfig userLevelConfig : userLevelConfigList) {
				if (parentUser.getCommunityPerformance()
					.compareTo(userLevelConfig.getUmbrellaPerformance()) >= 0) {
					initGameLevel = userLevelConfig.getLevel();
				}
			}
			if (!initGameLevel.equals(parentUser.getGameLevel())) {
				userInfoService.lambdaUpdate()
					.eq(UserInfo::getUserId, parentUser.getUserId())
					.set(UserInfo::getGameLevel, initGameLevel)
					.update();
			}
		}
	}

	private void tempCalculateParentUserLevel(UserInfo userInfo) {
//		List<UserInfo> parentUserList = userInfoService.getParentUserList(userInfo.getUserId());
//		parentUserList.add(0, userInfo);
//		Set<Long> userIds = parentUserList.stream().map(UserInfo::getUserId).collect(Collectors.toSet());
//		Map<Long, UserCardAsset> userCardAssetMap = userCardAssetService.lambdaQuery()
//			.in(UserCardAsset::getId, userIds)
//			.list().stream().collect(Collectors.toMap(UserCardAsset::getId, Function.identity()));
//		List<UserLevelConfig> userLevelConfigList = userLevelConfigService.lambdaQuery()
//			.orderByAsc(UserLevelConfig::getLevel)
//			.list();
//		for (UserInfo parentUser : parentUserList) {
//			if (parentUser.getIsValid() == null || parentUser.getIsValid().equals(0)) {
//				continue;
//			}
//			Integer initGameLevel = 0;
//			for (UserLevelConfig userLevelConfig : userLevelConfigList) {
//				if (userLevelConfig.getIsTwoChooseOne().equals(1)) {
//					if (parentUser.getCommunityPerformance()
//						.compareTo(userLevelConfig.getUmbrellaPerformance()) >= 0) {
//						initGameLevel = userLevelConfig.getLevel();
//					}
//					UserCardAsset userCardAsset = userCardAssetMap.get(parentUser.getUserId());
//					if (userCardAsset == null) {
//						continue;
//					}
//					if (userLevelConfig.getCardType() == 1 &&
//						(userCardAsset.getCardLevel1() != null && userCardAsset.getCardLevel1() > 0
//							|| userCardAsset.getCardLevel2() != null && userCardAsset.getCardLevel2() > 0
//							|| userCardAsset.getCardLevel3() != null && userCardAsset.getCardLevel3() > 0
//							|| userCardAsset.getCardLevel4() != null && userCardAsset.getCardLevel4() > 0)) {
//						initGameLevel = userLevelConfig.getLevel();
//					}
//					if (userLevelConfig.getCardType() == 2 &&
//						(userCardAsset.getCardLevel2() != null && userCardAsset.getCardLevel2() > 0
//							|| userCardAsset.getCardLevel3() != null && userCardAsset.getCardLevel3() > 0
//							|| userCardAsset.getCardLevel4() != null && userCardAsset.getCardLevel4() > 0)) {
//						initGameLevel = userLevelConfig.getLevel();
//					}
//					if (userLevelConfig.getCardType() == 3 &&
//						(userCardAsset.getCardLevel3() != null && userCardAsset.getCardLevel3() > 0
//							|| userCardAsset.getCardLevel4() != null && userCardAsset.getCardLevel4() > 0)) {
//						initGameLevel = userLevelConfig.getLevel();
//					}
//					if (userLevelConfig.getCardType() == 4 &&
//						userCardAsset.getCardLevel4() != null && userCardAsset.getCardLevel4() > 0) {
//						initGameLevel = userLevelConfig.getLevel();
//					}
//				} else {
//					if (parentUser.getCommunityPerformance()
//						.compareTo(userLevelConfig.getUmbrellaPerformance()) >= 0) {
//						initGameLevel = userLevelConfig.getLevel();
//					}
//				}
//			}
//			if (!initGameLevel.equals(parentUser.getGameLevel())) {
//				userInfoService.lambdaUpdate()
//					.eq(UserInfo::getUserId, parentUser.getUserId())
//					.set(UserInfo::getGameLevel, initGameLevel)
//					.update();
//			}
//		}
	}

	/**
	 * 计算小区业绩
	 *
	 * @param parentIds
	 */
	private void calculateCommunityPerformance(List<Long> parentIds) {
		BigDecimal minPerformance = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_studio_subsidy_min_umbrella_performance));
		// 小区业绩（对所有上级计算：去掉最大直推线）
		if (CollectionUtil.isNotEmpty(parentIds)) {
			for (Long parentId : parentIds) {
				List<UserInfo> children = userInfoService.lambdaQuery()
					.eq(UserInfo::getInviteUserId, parentId)
					.list();
				if (CollectionUtil.isEmpty(children) || children.size() <= 1) {
					userInfoService.lambdaUpdate()
						.eq(UserInfo::getUserId, parentId)
						.set(UserInfo::getCommunityPerformance, BigDecimal.ZERO)
						.update();
					continue;
				}
				BigDecimal totalChildPerformance = BigDecimal.ZERO;
				BigDecimal maxChildPerformance = BigDecimal.ZERO;
				for (UserInfo child : children) {
					BigDecimal childUmbrella =  child.getUmbrellaPerformance();
					BigDecimal performance = child.getPerformance();
					childUmbrella = childUmbrella.add(performance);

					totalChildPerformance = totalChildPerformance.add(childUmbrella);
					if (childUmbrella.compareTo(maxChildPerformance) > 0) {
						maxChildPerformance = childUmbrella;
					}
				}
				BigDecimal communityPerformance = totalChildPerformance.subtract(maxChildPerformance);
				if (communityPerformance.compareTo(BigDecimal.ZERO) < 0) {
					communityPerformance = BigDecimal.ZERO;
				}

				userInfoService.lambdaUpdate()
					.eq(UserInfo::getUserId, parentId)
					.set(communityPerformance.compareTo(minPerformance)>=0,UserInfo::getHasStudioSubsidyEligible,1)
					.set(UserInfo::getCommunityPerformance, communityPerformance)
					.update();
			}
		}
	}


	/**
	 * 计算小区业绩
	 * @param parentIds
	 */
	private void tempCalculateCommunityPerformance(List<Long> parentIds) {
		// 小区业绩（对所有上级计算：去掉最大直推线）
		if (CollectionUtil.isNotEmpty(parentIds)) {
			for (Long parentId : parentIds) {
				List<UserInfo> children = userInfoService.lambdaQuery()
					.eq(UserInfo::getInviteUserId, parentId)
					.list();
				if (CollectionUtil.isEmpty(children) || children.size() <= 1) {
					userInfoService.lambdaUpdate()
						.eq(UserInfo::getUserId, parentId)
						.set(UserInfo::getCommunityPerformance, BigDecimal.ZERO)
						.update();
					continue;
				}
				BigDecimal totalChildPerformance = BigDecimal.ZERO;
				BigDecimal maxChildPerformance = BigDecimal.ZERO;
				for (UserInfo child : children) {
					BigDecimal childUmbrella = child.getUmbrellaPerformance() == null ? BigDecimal.ZERO : child.getUmbrellaPerformance();
					BigDecimal performance = child.getPerformance() == null ? BigDecimal.ZERO : child.getPerformance();
					childUmbrella = childUmbrella.add(performance);

					totalChildPerformance = totalChildPerformance.add(childUmbrella);
					if (childUmbrella.compareTo(maxChildPerformance) > 0) {
						maxChildPerformance = childUmbrella;
					}
				}
				BigDecimal communityPerformance = totalChildPerformance.subtract(maxChildPerformance);
				if (communityPerformance.compareTo(BigDecimal.ZERO) < 0) {
					communityPerformance = BigDecimal.ZERO;
				}
				userInfoService.lambdaUpdate()
					.eq(UserInfo::getUserId, parentId)
					.set(UserInfo::getCommunityPerformance, communityPerformance)
					.update();
			}
		}
	}

	public Integer handlerDynamicOrderSettlement1(List req) {
		List<OrderMsgDO> ids = BeanUtil.copyToList(req, OrderMsgDO.class);
		log.debug("需要处理的业绩订单 orders:{}", ids);
		if(CollectionUtil.isNotEmpty(ids)) {

			OrderMsgDO orderMsgDO = ids.get(0);


			DestroyOrder destroyOrder = destroyOrderService.lambdaQuery()
				.eq(DestroyOrder::getId, orderMsgDO.getId())
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
					//必须自身有效才能参与等级
					if(parentUser.getIsValid().equals(0)){
						continue;
					}
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
						//个人业绩
						if(parentUser.getPerformance().compareTo(w3UserLevelConfig.getPerformance())>=0){
							//团队业绩
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
		}
		return 1;
	}

	/**
	 * 返回相差几秒，如果当前时间晚于结束时间则返回固定的10秒
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
	 * 检查用户等级变更并打印结果
	 *
	 * @param changeLevelUserBo 需要判断等级变化的用户列表
	 * @param sortedConfigs     已按等级从低到高排序的配置列表
	 */
	public void checkUserLevelChanges(List<ChangeLevelUserBo> changeLevelUserBo, List<W3UserLevelConfig> sortedConfigs,
									  String sourceOrderNo,Long sourceUserId,BigDecimal upUserAmountLimit) {
		if (CollUtil.isEmpty(changeLevelUserBo)) {
			System.out.println("没有需要检查的用户");
			return;
		}

		System.out.println("===========用户等级变更检查开始===========");
		List<UserMoney> updateMoneyList = new ArrayList<>(100);
		List<UserLevelChangeLog> userLevelChangeLogList = new ArrayList<>(changeLevelUserBo.size() * 2);
		UserMoney entity = null;
		UserLevelChangeLog changeLog = null;
		for (ChangeLevelUserBo levelUserBo : changeLevelUserBo) {
			Long userId = levelUserBo.getUserId();
			Integer currentLevel = levelUserBo.getGameLevel(); // 当前等级
			// 根据业务规则计算应该的等级
			Integer calculatedLevel = calculateUserLevel(levelUserBo, sortedConfigs,upUserAmountLimit);
			if (currentLevel.equals(calculatedLevel)) {
				// 等级不变，无需处理
				System.out.println(String.format("用户ID: %d,  等级保持不变: %d级",
					userId, currentLevel));
			} else if (calculatedLevel > currentLevel) {
				// 需要升级
				System.out.println(String.format("用户ID: %d,等级升级: %d级 -> %d级",
					userId, currentLevel, calculatedLevel));
				System.out.println("升级原因: 直推业绩:" + levelUserBo.getValidSubNum() +
					", 团队业绩:" + levelUserBo.getValidUmbrellaNum());

				// 打印每个等级的升级奖励明细
				System.out.println("===== 升级奖励明细 =====");
				BigDecimal totalReward = BigDecimal.ZERO;

				// 从当前等级开始，逐级升级，每级对应一条奖励记录
				for (int level = currentLevel; level < calculatedLevel; level++) {
					int nextLevel = level + 1;
					BigDecimal currentLevelReward = getLevelReward(level, sortedConfigs);
					BigDecimal nextLevelReward = getLevelReward(nextLevel, sortedConfigs);
					BigDecimal diffReward = nextLevelReward.subtract(currentLevelReward);

					System.out.println(String.format("  等级 %d->%d 奖励: %s (差额: %s)",
						level, nextLevel, nextLevelReward, diffReward));

					// 创建每一级的等级变更日志
					changeLog = new UserLevelChangeLog();
					changeLog.setUserId(userId);
					changeLog.setOldLevel((long) level);
					changeLog.setNewLevel((long) nextLevel);
					changeLog.setOrderNo(sourceOrderNo);
					changeLog.setChangeType(1L); // 1表示升级

					// 判断是否应该发放奖励
					Integer historyMaxLevel = levelUserBo.getMaxGameLevel();
					if (historyMaxLevel == null) {
						historyMaxLevel = currentLevel;
					}
					changeLog.setHistoryMaxLevel((long) historyMaxLevel);
					boolean shouldReward = nextLevel > historyMaxLevel;

					totalReward = totalReward.add(diffReward);
					if (diffReward.compareTo(BigDecimal.ZERO) > 0 && shouldReward) {
						// 符合奖励条件
						changeLog.setTotalReward(diffReward);
						changeLog.setHasReward(1L);

						entity = new UserMoney();
						entity.setId(levelUserBo.getUserId());
						entity.setValidNum3(diffReward);
						entity.setGtId(IDUtils.getSnowflakeStr());
						entity.setSourceCode(sourceOrderNo);
						entity.setSourceId(sourceUserId);
						entity.setSourceType(ConstantType.user_money_log_source_type.type_8);
						entity.setUpdateTime(new Date());
						updateMoneyList.add(entity);
					} else {
						// 不符合奖励条件
						changeLog.setTotalReward(BigDecimal.ZERO);
						changeLog.setHasReward(0L);

						if (!shouldReward) {
							System.out.println(String.format("用户已达到过等级%d，不再发放奖励", nextLevel));
						} else {
							log.error("分发订单升级失败 用户id:{},等级:{},奖励:{},差额:{}", levelUserBo.getUserId(),
								level, nextLevelReward, diffReward);
						}
					}

					// 将日志添加到列表
					userLevelChangeLogList.add(changeLog);
				}

				System.out.println(String.format("===== 总奖励: %s =====", totalReward));
				//
				Integer maxGameLevel = levelUserBo.getMaxGameLevel()<calculatedLevel ? calculatedLevel : levelUserBo.getMaxGameLevel();
				boolean update = userInfoService.update()
					.eq("user_id", levelUserBo.getUserId())
					.eq("game_level", currentLevel)
					.set("max_game_level", maxGameLevel)
					.set("game_level", calculatedLevel)
					.update();
				if (!update) {
					log.error("分发订单升级失败 用户id:{}", levelUserBo.getUserId());
					throw new ServiceException("分发订单升级失败");
				}
			} else {
				// 需要降级
				System.out.println(String.format("用户ID: %d, 等级降级: %d级 -> %d级",
					userId, currentLevel, calculatedLevel));
				System.out.println("降级原因: 直推业绩:" + levelUserBo.getValidSubNum() +
					", 要求:" + getRequiredPerformance(calculatedLevel + 1, sortedConfigs, "direct") +
					", 团队业绩:" + levelUserBo.getValidUmbrellaNum() +
					", 要求:" + getRequiredPerformance(calculatedLevel + 1, sortedConfigs, "umbrella"));

				// 创建降级日志
				changeLog = new UserLevelChangeLog();
				changeLog.setUserId(userId);
				changeLog.setOldLevel((long) currentLevel);
				changeLog.setNewLevel((long) calculatedLevel);
				changeLog.setOrderNo(sourceOrderNo);
				changeLog.setChangeType(2L); // 2表示降级
				changeLog.setHistoryMaxLevel((long) (levelUserBo.getMaxGameLevel() == null ? currentLevel : levelUserBo.getMaxGameLevel()));
				changeLog.setTotalReward(BigDecimal.ZERO); // 降级不发放奖励
				changeLog.setHasReward(0L);
				userLevelChangeLogList.add(changeLog);

				boolean update = userInfoService.update()
					.eq("user_id", levelUserBo.getUserId())
					.eq("game_level", currentLevel)
					.set("game_level", calculatedLevel)
					.update();
				if (!update) {
					log.error("分发订单降级失败 用户id:{}", levelUserBo.getUserId());
					throw new ServiceException("分发订单降级失败");
				}
			}
		}

		//批量更新升降级日志
		if (CollectionUtil.isNotEmpty(userLevelChangeLogList)) {
			boolean b = userLevelChangeLogService.saveBatch(userLevelChangeLogList);
			if (!b) {
				throw new ServiceException("批量更新用户等级失败");
			}
		}

		// 批量更新钱包获得奖励
		if (CollectionUtil.isNotEmpty(updateMoneyList)) {
			bachUpdateMoneyValid3(updateMoneyList);
		}
		System.out.println("===========用户等级变更检查结束===========");
	}


	/**
	 * 获取指定等级的业绩要求
	 *
	 * @param level         等级
	 * @param sortedConfigs 已排序的配置列表
	 * @param type          类型：direct-直推，umbrella-团队
	 * @return 业绩要求
	 */
	private BigDecimal getRequiredPerformance(Integer level, List<W3UserLevelConfig> sortedConfigs, String type) {
		Optional<W3UserLevelConfig> config = sortedConfigs.stream()
			.filter(c -> c.getLevel().equals(level))
			.findFirst();

//		if (config.isPresent()) {
//			return "direct".equals(type) ?
//				config.get().getDirectPushPerformance() :
//				config.get().getUmbrellaPerformance();
//		}
		return BigDecimal.ZERO;
	}

	/**
	 * 获取指定等级的奖励金额
	 *
	 * @param level         等级
	 * @param sortedConfigs 已排序的配置列表
	 * @return 该等级的奖励金额
	 */
	private BigDecimal getLevelReward(Integer level, List<W3UserLevelConfig> sortedConfigs) {
		Optional<W3UserLevelConfig> config = sortedConfigs.stream()
			.filter(c -> c.getLevel().equals(level))
			.findFirst();

//		if (config.isPresent()) {
//			return config.get().getUpgradeReward();
//		}
		return BigDecimal.ZERO;
	}

	/**
	 * 对佣金钱包资产增加
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
	 * 对币种2、3、6、7余额变为0
	 *
	 * @param userMoneyList
	 */
	private void bachUpdateMoneyValid_0(List<UserMoney> userMoneyList,Integer coinType) {
		String updateSql =SQL_VALID_NUM2_0;
		if(coinType == 3){
			updateSql = SQL_VALID_NUM3_0;
		}else if(coinType == 6){
			updateSql = SQL_VALID_NUM6_0;
		}else if(coinType == 7){
			updateSql = SQL_VALID_NUM7_0;
		}else if(coinType == 8){
			updateSql = SQL_VALID_NUM8_0;
		}
		int[] ints = jdbcTemplate.batchUpdate(updateSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ps.setTimestamp(1, new java.sql.Timestamp(userMoneyList.get(i).getUpdateTime().getTime()));
				ps.setString(2, userMoneyList.get(i).getGtId());
				ps.setString(3, userMoneyList.get(i).getSourceCode());
				ps.setInt(4, userMoneyList.get(i).getSourceType());
				ps.setLong(5, userMoneyList.get(i).getSourceId());
				ps.setLong(6, userMoneyList.get(i).getId());
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
	 * 根据用户数据计算应该的等级
	 *
	 * @param userBo        用户数据
	 * @param sortedConfigs 已按等级从低到高排序的配置列表
	 * @return 计算出的用户等级
	 */
	private Integer calculateUserLevel(ChangeLevelUserBo userBo, List<W3UserLevelConfig> sortedConfigs,
									   BigDecimal upUserAmountLimit) {
		//直推用户数
		BigDecimal directPerformance = userBo.getValidSubNum() == null ? BigDecimal.ZERO : userBo.getValidSubNum();
		//团队有效用户数
		BigDecimal umbrellaPerformance = userBo.getValidUmbrellaNum() == null ? BigDecimal.ZERO : userBo.getValidUmbrellaNum();

		// 默认为最低等级0
		Integer matchLevel = 0;

		// 从低到高检查每个等级
//		for (W3UserLevelConfig config : sortedConfigs) {
//			// 如果同时满足直推业绩和团队业绩要求，更新匹配等级
//			//还需要有这么多钱
//			if (directPerformance.compareTo(config.getDirectPushPerformance()) >= 0 &&
//				umbrellaPerformance.compareTo(config.getUmbrellaPerformance()) >= 0 &&
//				userBo.getUserAmount().compareTo(upUserAmountLimit)>=0) {
//				matchLevel = config.getLevel();
//			} else {
//				// 一旦不满足某个等级，就退出循环
//				break;
//			}
//		}

		return matchLevel;
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
	 * 对usdt资产增加
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
}
