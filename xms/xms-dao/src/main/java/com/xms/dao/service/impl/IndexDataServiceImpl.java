package com.xms.dao.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.RedisConstant;
import com.xms.common.constant.SysConstant;
import com.xms.dao.domain.*;
import com.xms.dao.entity.bo.UserMoneySumDTO;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.entity.vo.IndexDataPanelVo;
import com.xms.dao.mapper.SysParaMapper;
import com.xms.dao.mapper.UserInfoMapper;
import com.xms.dao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IndexDataServiceImpl implements IndexDataService {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private XmsRedis xmsRedis;

	@Autowired
	private SysParaMapper sysParaMapper;

	@Resource(name = "asyncExecutor")
	private Executor asyncExecutor;

	@Autowired
	private WithdrawalService withdrawalService;

	@Autowired
	private IStakeOrderService stakeOrderService;

	@Autowired
	private IRewardRecordService rewardRecordService;

	@Autowired
	private IBuyHOrderService buyHOrderService;

	@Autowired
	private INodePlanOrderService nodePlanOrderService;

	@Autowired
	private IUserStakePositionService userStakePositionService;

	@Autowired
	private IStakeRoundService stakeRoundService;

	@Autowired
	private  IUserWealthVaultService userWealthVaultService;
	/**
	 * 计算跌幅百分比
	 *
	 * @param previousPrice 前一天的价格
	 * @param currentPrice  今天的价格
	 * @return 跌幅百分比，如果前一天价格为零则返回 null
	 */
	public static BigDecimal calculateDeclinePercentage(BigDecimal previousPrice, BigDecimal currentPrice) {
		if (previousPrice.compareTo(BigDecimal.ZERO) == 0) {
			// 返回 null 表示无法计算
			return BigDecimal.ZERO;
		}
		BigDecimal difference = previousPrice.subtract(currentPrice);
		BigDecimal declinePercentage = difference.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew).multiply(SysConstant.BAIFENBI).setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		return declinePercentage;
	}

	@Override
	public IndexDataPanelVo getIndexDataPanelVo() {
		IndexDataPanelVo indexDataPanelVo = new IndexDataPanelVo();

		//获取用户总节点数量
		StakeRound stakeRound1 = stakeRoundService
			.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		BigDecimal v7 = BigDecimal.ZERO;
		if(stakeRound1!=null){
			v7 =stakeRound1.getPlayerStakeTotal()
				.add(stakeRound1.getBuyPointTotal())
				.subtract(stakeRound1.getStudioSubsidyTotal())
				.subtract(stakeRound1.getWithdrawRewardTotalFull());
		}
		indexDataPanelVo.setV7(v7);

		//v6=节点收益,v8=静态,v9=动态,v10=财富,v11=魔盒,v12=工作室收益,v13=贡献分
		UserMoneySumDTO userMoneySumDTO = userInfoMapper.queryUserMoneySum();
		indexDataPanelVo.setV6(userMoneySumDTO.getTotalValidNum1());
		indexDataPanelVo.setV8(userMoneySumDTO.getTotalValidNum2());
		indexDataPanelVo.setV9(userMoneySumDTO.getTotalValidNum3());
		indexDataPanelVo.setV10(userMoneySumDTO.getTotalValidNum4());
		indexDataPanelVo.setV11(userMoneySumDTO.getTotalValidNum5());
		indexDataPanelVo.setV12(userMoneySumDTO.getTotalValidNum6());
		indexDataPanelVo.setV13(userMoneySumDTO.getTotalValidNum7());

		CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
			BigDecimal v35 = BigDecimal.ZERO;
			BigDecimal v36 = BigDecimal.ZERO;
			BigDecimal v37 = BigDecimal.ZERO;
			BigDecimal v38 = BigDecimal.ZERO;
			BigDecimal v39 = BigDecimal.ZERO;
			BigDecimal v40 = BigDecimal.ZERO;
			List<Withdrawal> withdrawalList = withdrawalService.lambdaQuery()
				.in(Withdrawal::getStatus,0,1)
				.select(Withdrawal::getCoinType, Withdrawal::getChangeBalance)
				.list();
			if (CollectionUtil.isNotEmpty(withdrawalList)) {
				for (Withdrawal withdrawal : withdrawalList) {
					if (withdrawal.getCoinType() == null || withdrawal.getChangeBalance() == null) {
						continue;
					}
					if (withdrawal.getCoinType().equals(1)) {
						v35 = v35.add(withdrawal.getChangeBalance());
					} else if (withdrawal.getCoinType().equals(2)) {
						v36 = v36.add(withdrawal.getChangeBalance());
					} else if (withdrawal.getCoinType().equals(3)) {
						v37 = v37.add(withdrawal.getChangeBalance());
					} else if (withdrawal.getCoinType().equals(4)) {
						v38 = v38.add(withdrawal.getChangeBalance());
					} else if (withdrawal.getCoinType().equals(5)) {
						v39 = v39.add(withdrawal.getChangeBalance());
					} else if (withdrawal.getCoinType().equals(6)) {
						v40 = v40.add(withdrawal.getChangeBalance());
					}
				}
			}
			indexDataPanelVo.setV35(v35);
			indexDataPanelVo.setV36(v36);
			indexDataPanelVo.setV37(v37);
			indexDataPanelVo.setV38(v38);
			indexDataPanelVo.setV39(v39);
			indexDataPanelVo.setV40(v40);
		}, asyncExecutor);

		CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
			//今日新增社区业绩、今日新增社区奖励
			List<StakeOrder> stakeOrderList = stakeOrderService.lambdaQuery()
				.eq(StakeOrder::getCreateDay, Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")))
				.list();
			//今日新增业绩
			BigDecimal todayAddStakeAmount = stakeOrderList.stream()
				.filter(item -> item.getStakeAmount() != null)
				.map(StakeOrder::getStakeAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			//今日工作室补贴
			BigDecimal todayAmount6 = rewardRecordService.lambdaQuery()
				.eq(RewardRecord::getSourceType,6)
				.apply("create_time >= CURDATE()")
				.select(RewardRecord::getAmount)
				.list().stream().map(RewardRecord::getAmount)
				.filter(item -> item != null)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

			indexDataPanelVo.setV29(todayAddStakeAmount);
			indexDataPanelVo.setV30(todayAmount6);

			BigDecimal v31 = BigDecimal.ZERO;
			BigDecimal v32 = BigDecimal.ZERO;
			BigDecimal v33 = BigDecimal.ZERO;
			BigDecimal v34 = BigDecimal.ZERO;



			indexDataPanelVo.setV31(v31);
			indexDataPanelVo.setV32(v32);
			indexDataPanelVo.setV33(v33);
			indexDataPanelVo.setV34(v34);
		}, asyncExecutor);

		CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> {
			//今日动态提现、
			List<Withdrawal> withdrawalList = withdrawalService.lambdaQuery()
				.in(Withdrawal::getCoinType, 2, 3)
				.eq(Withdrawal::getStatus, 3)
				.apply("create_time >= CURDATE()")
				.select(Withdrawal::getChangeBalance,Withdrawal::getCoinType)
				.list();
			//coinType 2:静态,3:动态
			BigDecimal v41 = BigDecimal.ZERO;
			BigDecimal v42 = BigDecimal.ZERO;
			if (CollectionUtil.isNotEmpty(withdrawalList)) {
				for (Withdrawal withdrawal : withdrawalList) {
					if (withdrawal.getCoinType() == null || withdrawal.getChangeBalance() == null) {
						continue;
					}
					if (withdrawal.getCoinType().equals(3)) {
						v41 = v41.add(withdrawal.getChangeBalance());
					} else if (withdrawal.getCoinType().equals(2)) {
						v42 = v42.add(withdrawal.getChangeBalance());
					}
				}
			}
			indexDataPanelVo.setV41(v41);
			indexDataPanelVo.setV42(v42);
			//今日静态提现总额
			//今日购买花费h
			BigDecimal v43= buyHOrderService.lambdaQuery()
				.apply("create_time >= CURDATE()")
				.select(BuyHOrder::getPayHAmount)
				.list().stream()
				.map(BuyHOrder::getPayHAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			indexDataPanelVo.setV43(v43);

			BigDecimal v44= buyHOrderService.lambdaQuery()
				.select(BuyHOrder::getPayHAmount)
				.eq(BuyHOrder::getStatus,1)
				.list().stream()
				.map(BuyHOrder::getPayHAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			indexDataPanelVo.setV44(v44);

			BigDecimal v45= nodePlanOrderService.lambdaQuery()
				.in(NodePlanOrder::getBizStatus, 1,2)
				.select(NodePlanOrder::getAmount)
				.list().stream()
				.map(NodePlanOrder::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			indexDataPanelVo.setV45(v45);

		}, asyncExecutor);


		CompletableFuture<Void> future5 = CompletableFuture.runAsync(() -> {
			userStakePositionService.lambdaQuery()
				.select(UserStakePosition::getTotalStakeAmount)
				.list().stream()
				.map(UserStakePosition::getTotalStakeAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

			StakeRound stakeRound = stakeRoundService.lambdaQuery()
				.eq(StakeRound::getStatus, 0)
				.one();
			if(stakeRound !=null){
				List<UserStakePosition> userStakePositionList = userStakePositionService.lambdaQuery()
					.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
					.select(UserStakePosition::getTotalReward,UserStakePosition::getStudioSubsidy,
						UserStakePosition::getDynamicReward,UserStakePosition::getTotalStakeAmount)
					.list();

				BigDecimal v47 = BigDecimal.ZERO;
				BigDecimal v46 = BigDecimal.ZERO;
				BigDecimal v48 = BigDecimal.ZERO;
				BigDecimal v50 = BigDecimal.ZERO;
				if(CollectionUtil.isNotEmpty(userStakePositionList)){
					for (UserStakePosition userStakePosition : userStakePositionList) {
						v47 = v47.add(userStakePosition.getTotalReward());
						v48 = v48.add(userStakePosition.getDynamicReward());
						v46 = v46.add(userStakePosition.getStudioSubsidy());
						v50 = v50.add(userStakePosition.getTotalStakeAmount());
					}
				}
				indexDataPanelVo.setV47(v47);
				indexDataPanelVo.setV48(v48);
				indexDataPanelVo.setV46(v46);
				BigDecimal v49 = userWealthVaultService.getBaseMapper()
					.selectMaps(new QueryWrapper<UserWealthVault>()
						.select("IFNULL(SUM(seg1_amount),0) + IFNULL(SUM(seg2_amount),0) + IFNULL(SUM(seg3_amount),0) + IFNULL(SUM(seg4_amount),0) + IFNULL(SUM(seg5_amount),0) AS totalSegAmount"))
					.stream()
					.findFirst()
					.map(m -> m.get("totalSegAmount"))
					.map(v -> v == null ? BigDecimal.ZERO : new BigDecimal(v.toString()))
					.orElse(BigDecimal.ZERO);
				indexDataPanelVo.setV49(v49);
				indexDataPanelVo.setV50(v50);


			}

		}, asyncExecutor);

		CompletableFuture.allOf(future2, future3, future4, future5).join();
		return indexDataPanelVo;
	}

	public String getValue(String code) {
		return xmsRedis.get(RedisConstant.XMS_PARAM + code, () -> sysParaMapper.getValue(code), 15L, TimeUnit.DAYS);
	}
}
