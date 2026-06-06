package com.xms.dao.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.xms.common.constant.ConstantType;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.spring.SpringUtils;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.UserLevelChangeLog;
import com.xms.dao.domain.W3UserLevelConfig;
import com.xms.dao.entity.bo.ChangeLevelUserBo;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.UserMoney;
import com.xms.dao.mapper.W3UserLevelConfigMapper;
import com.xms.dao.service.IUserLevelChangeLogService;
import com.xms.dao.service.IW3UserLevelConfigService;
import com.xms.dao.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 用户等级考核配置Service业务层处理
 *
 * @author xms
 * @date 2025-04-10
 */
@Service
@Slf4j
public class W3UserLevelConfigServiceImpl extends XmsDataServiceImpl<W3UserLevelConfigMapper, W3UserLevelConfig> implements IW3UserLevelConfigService
{

	private static final String SQL_VALID_NUM3 = "UPDATE t_user_money SET update_time=?,gt_id=?,valid_num3=valid_num3+?,source_code=?,source_type=?,source_id=? WHERE id=? ";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserInfoService userInfoService;


	@Autowired
	private IUserLevelChangeLogService userLevelChangeLogService;


    /**
     * 查询用户等级考核配置列表
     *
     *
     * @param w3UserLevelConfig 用户等级考核配置
     * @return 用户等级考核配置
     */
    @Override
    public List<W3UserLevelConfig> selectW3UserLevelConfigList(W3UserLevelConfig w3UserLevelConfig)
    {
        return baseMapper.selectW3UserLevelConfigList(w3UserLevelConfig);
    }

	@Override
	public int updateRecordById(W3UserLevelConfig w3UserLevelConfig) {
		if(w3UserLevelConfig.getUmbrellaPerformance().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("团队业绩不能小于0");
		}
//
//		if(w3UserLevelConfig.getDirectPushPerformance().compareTo(BigDecimal.ZERO)<0){
//			throw new ServiceException("直推订单不能小于0");
//		}
//
//		if(w3UserLevelConfig.getUserPerformance().compareTo(BigDecimal.ZERO)<0){
//			throw new ServiceException("自己投入不能小于0");
//		}
//
//		if(w3UserLevelConfig.getDayRewardRatio().compareTo(BigDecimal.ZERO)<0){
//			throw new ServiceException("日化收益不能小于0");
//		}
//
//		if(w3UserLevelConfig.getMontyRewardRatio().compareTo(BigDecimal.ZERO)<0){
//			throw new ServiceException("月收益不能小于0");
//		}
		if(w3UserLevelConfig.getRewardRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("收益百分比不能小为0");
		}

		if(w3UserLevelConfig.getPeerRewardRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("平级奖百分比不能小为0");
		}
		boolean b = updateById(w3UserLevelConfig);
		if(!b){
			throw new ServiceException("修改失败");
		}
		return 1;
	}

	@Override
	public UserInfo tryUpdateUserLevel(Long userId, String orderNo, BigDecimal userAmount, BigDecimal userUpgradeAmount) {
		log.info("判断自己是否升降级 用户ID: {},订单号: {},用户金额: {},升级金额: {}", userId, orderNo, userAmount, userUpgradeAmount);
		UserInfo userInfo;
		Integer rowCount;
		List<W3UserLevelConfig> configList = lambdaQuery()
			.orderByAsc(W3UserLevelConfig::getLevel)
			.list();
		userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, userId)
			.one();
		ChangeLevelUserBo levelUserBo = new ChangeLevelUserBo();
		levelUserBo.setUserId(userId);
		levelUserBo.setGameLevel(userInfo.getGameLevel());
		levelUserBo.setValidSubNum(new BigDecimal(userInfo.getValidSubNum()));
		levelUserBo.setValidUmbrellaNum(new BigDecimal(userInfo.getValidUmbrellaNum()));
		levelUserBo.setUserAmount(userAmount);
		Integer calculatedLevel = calculateUserLevel(levelUserBo, configList);
		Integer currentLevel = levelUserBo.getGameLevel(); // 当前等级

		if (currentLevel.equals(calculatedLevel)) {
			// 等级不变，无需处理
		} else if (calculatedLevel > currentLevel) {
			// 需要升级
			System.out.println(String.format("用户ID: %d,等级升级: %d级 -> %d级",
				userId, currentLevel, calculatedLevel));
			System.out.println("升级原因: 直推业绩:" + levelUserBo.getValidSubNum() +
				", 团队业绩:" + levelUserBo.getValidUmbrellaNum());
			// 打印每个等级的升级奖励明细
			System.out.println("===== 升级奖励明细 =====");
			BigDecimal totalReward = BigDecimal.ZERO;
			UserLevelChangeLog changeLog = null;
			// 从当前等级开始，逐级升级，每级对应一条奖励记录
			for (int level = currentLevel; level < calculatedLevel; level++) {
				int nextLevel = level + 1;
				BigDecimal currentLevelReward = getLevelReward(level, configList);
				BigDecimal nextLevelReward = getLevelReward(nextLevel, configList);
				BigDecimal diffReward = nextLevelReward.subtract(currentLevelReward);

				System.out.println(String.format("  等级 %d->%d 奖励: %s (差额: %s)",
					level, nextLevel, nextLevelReward, diffReward));

				// 创建每一级的等级变更日志
				changeLog = new UserLevelChangeLog();
				changeLog.setUserId(userId);
				changeLog.setOldLevel((long) level);
				changeLog.setNewLevel((long) nextLevel);
				changeLog.setOrderNo(orderNo);
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
					rowCount = SpringUtils.getBean(UserWalletServiceImpl.class).handerUserMoney(diffReward, orderNo, userId, userId,
						ConstantType.user_money_log_source_type.type_8,
						ConstantType.user_money_coin_type.type_3);
					if (rowCount != 1) {
						throw new ServiceException(ResponseCode.CODE_1002);
					}
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
			}

			boolean update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, levelUserBo.getUserId())
				.eq(UserInfo::getGameLevel, currentLevel)
				.set(UserInfo::getGameLevel, calculatedLevel)
				.update();
			if (!update) {
				log.error("分发订单升级失败 用户id:{}", levelUserBo.getUserId());
				throw new ServiceException("分发订单升级失败");
			}
		} else {
			UserLevelChangeLog changeLog = new UserLevelChangeLog();
			changeLog.setUserId(userId);
			changeLog.setOldLevel((long) currentLevel);
			changeLog.setNewLevel((long) calculatedLevel);
			changeLog.setOrderNo(orderNo);
			changeLog.setChangeType(2L); // 2表示降级
			changeLog.setHistoryMaxLevel((long) (levelUserBo.getMaxGameLevel() == null ? currentLevel : levelUserBo.getMaxGameLevel()));
			changeLog.setTotalReward(BigDecimal.ZERO); // 降级不发放奖励
			changeLog.setHasReward(0L);
			userLevelChangeLogService.save(changeLog);
			boolean update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, levelUserBo.getUserId())
				.eq(UserInfo::getGameLevel, currentLevel)
				.set(UserInfo::getGameLevel, calculatedLevel)
				.update();
			if (!update) {
				log.error("分发订单降级失败 用户id:{}", levelUserBo.getUserId());
				throw new ServiceException("分发订单降级失败");
			}
		}
		return userInfo;
	}


	/**
	 * 检查用户等级变更并打印结果
	 *
	 * @param changeLevelUserBo 需要判断等级变化的用户列表
	 * @param sortedConfigs     已按等级从低到高排序的配置列表
	 */
	public void checkUserLevelChanges(List<ChangeLevelUserBo> changeLevelUserBo, List<W3UserLevelConfig> sortedConfigs,
									  String sourceOrderNo,Long sourceUserId) {
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
			Integer calculatedLevel = calculateUserLevel(levelUserBo, sortedConfigs);
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
				boolean update = userInfoService.lambdaUpdate()
					.eq(UserInfo::getUserId, levelUserBo.getUserId())
					.eq(UserInfo::getGameLevel, currentLevel)
					.set(UserInfo::getGameLevel, calculatedLevel)
					.update();
				if (!update) {
					log.error("分发订单升级失败 用户id:{}", levelUserBo.getUserId());
					throw new ServiceException("分发订单升级失败");
				}
			} else {
				boolean update = userInfoService.lambdaUpdate()
					.eq(UserInfo::getUserId, levelUserBo.getUserId())
					.eq(UserInfo::getGameLevel, currentLevel)
					.set(UserInfo::getGameLevel, calculatedLevel)
					.update();
				if (!update) {
					log.error("分发订单降级失败 用户id:{}", levelUserBo.getUserId());
					throw new ServiceException("分发订单降级失败");
				}


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
	 * 根据用户数据计算应该的等级
	 *
	 * @param userBo        用户数据
	 * @param sortedConfigs 已按等级从低到高排序的配置列表
	 * @return 计算出的用户等级
	 */
	private Integer calculateUserLevel(ChangeLevelUserBo userBo, List<W3UserLevelConfig> sortedConfigs) {
		// 获取用户的业绩数据
		BigDecimal directPerformance = userBo.getValidSubNum() == null ? BigDecimal.ZERO : userBo.getValidSubNum();
		BigDecimal umbrellaPerformance = userBo.getValidUmbrellaNum() == null ? BigDecimal.ZERO : userBo.getValidUmbrellaNum();

		// 默认为最低等级0
		Integer matchLevel = 0;

		// 从低到高检查每个等级
//		for (W3UserLevelConfig config : sortedConfigs) {
//			// 如果同时满足直推业绩和团队业绩要求，更新匹配等级
//			if (directPerformance.compareTo(config.getDirectPushPerformance()) >= 0 &&
//				umbrellaPerformance.compareTo(config.getUmbrellaPerformance()) >= 0 &&
//				userBo.getUserAmount().compareTo(config.getUserPerformance())>=0) {
//				matchLevel = config.getLevel();
//			} else {
//				// 一旦不满足某个等级，就退出循环
//				break;
//			}
//		}

		return matchLevel;
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
}
