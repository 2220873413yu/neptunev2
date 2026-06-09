package com.xms.dao.service.impl;

import cn.hutool.core.date.DateUtil;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.exception.ServiceException;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.HGiftReleaseBucket;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.mapper.HGiftReleaseBucketMapper;
import com.xms.dao.service.IHGiftReleaseBucketService;
import com.xms.dao.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * H赠送释放桶Service业务层处理
 *
 * @author xms
 * @date 2026-06-07
 */
@Service
@Slf4j
public class HGiftReleaseBucketServiceImpl extends XmsDataServiceImpl<HGiftReleaseBucketMapper, HGiftReleaseBucket> implements IHGiftReleaseBucketService {
	private static final int SOURCE_TYPE_ACP_DEPOSIT = 1;
	private static final int SOURCE_TYPE_MANUAL = 2;
	private static final int SOURCE_TYPE_OLD_H_TO_ACP_DEPOSIT = 3;
	private static final int STATUS_RELEASING = 1;
	private static final int STATUS_FROZEN = 3;

	private final UserInfoService userInfoService;

	public HGiftReleaseBucketServiceImpl(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	/**
	 * 查询H赠送释放桶列表
	 *
	 * @param hGiftReleaseBucket H赠送释放桶
	 * @return H赠送释放桶
	 */
	@Override
	public List<HGiftReleaseBucket> selectHGiftReleaseBucketList(HGiftReleaseBucket hGiftReleaseBucket) {
		return baseMapper.selectHGiftReleaseBucketList(hGiftReleaseBucket);
	}

	/**
	 * 后台手动创建H赠送释放桶
	 *
	 * @param hGiftReleaseBucket H赠送释放桶
	 * @return 是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createManualBucket(HGiftReleaseBucket hGiftReleaseBucket) {
		if (hGiftReleaseBucket == null) {
			throw new ServiceException("参数不能为空");
		}
		BigDecimal totalAmount = hGiftReleaseBucket.getTotalAmount();
		if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ServiceException("赠送H总量必须大于0");
		}
		if (StringUtils.isBlank(hGiftReleaseBucket.getAccount())) {
			throw new ServiceException("钱包地址不能为空");
		}

		UserInfo userInfo = findUser(hGiftReleaseBucket);
		Date now = new Date();
		Integer startDate = Integer.valueOf(DateUtil.format(now, "yyyyMMdd"));
		Integer releaseDays = ConstantStatic.H_GIFT_RELEASE_DAYS;

		String orderNo = IDUtils.getSnowflakeStr();
		totalAmount = totalAmount.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal dailyReleaseAmount = totalAmount
			.divide(new BigDecimal(releaseDays), ConstantStatic.newScale, ConstantStatic.roundingModeNew);

		HGiftReleaseBucket entity = new HGiftReleaseBucket();
		entity.setBucketNo(orderNo);
		entity.setUserId(userInfo.getUserId());
		entity.setAccount(userInfo.getAccount());
		entity.setSourceType(SOURCE_TYPE_MANUAL);
		entity.setSourceOrderNo(orderNo);
		entity.setSourceUserId(userInfo.getUserId());
		entity.setTotalAmount(totalAmount);
		entity.setReleasedAmount(BigDecimal.ZERO.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		entity.setRemainingAmount(totalAmount);
		entity.setDailyReleaseAmount(dailyReleaseAmount);
		entity.setReleaseDays(releaseDays);
		entity.setReleasedDays(0);
		entity.setStartDate(startDate);
		entity.setLastReleaseDate(null);
		entity.setStatus(STATUS_RELEASING);
		entity.setCreateTime(now);
		entity.setUpdateTime(now);
		return save(entity);
	}

	/**
	 * 正常 ACP 入金后创建 H赠送释放桶。
	 *
	 * <p>本方法只创建释放计划，不直接写钱包、不写奖励流水。赠送H数量来自 t_stake_order
	 * 的订单快照，不在释放桶创建时重新读取价格计算。</p>
	 *
	 * @param userId 用户ID
	 * @param account 用户钱包地址
	 * @param sourceOrderNo 来源订单号
	 * @param giftHAmount 本单应赠送H总量
	 * @return 是否创建成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createAcpDepositBucket(Long userId, String account, String sourceOrderNo, BigDecimal giftHAmount) {
		return createDepositGiftBucket(userId, account, SOURCE_TYPE_ACP_DEPOSIT, sourceOrderNo, giftHAmount);
	}

	/**
	 * 旧系统H换ACP入金后创建 H赠送释放桶。
	 *
	 * <p>本方法只创建释放计划，不直接写钱包、不写奖励流水。赠送H数量来自 t_stake_order
	 * 的订单快照，不在释放桶创建时重新读取价格计算。</p>
	 *
	 * @param userId 用户ID
	 * @param account 用户钱包地址
	 * @param sourceOrderNo 来源订单号
	 * @param giftHAmount 本单应赠送H总量
	 * @return 是否创建成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createOldHToAcpDepositBucket(Long userId, String account, String sourceOrderNo, BigDecimal giftHAmount) {
		return createDepositGiftBucket(userId, account, SOURCE_TYPE_OLD_H_TO_ACP_DEPOSIT, sourceOrderNo, giftHAmount);
	}

	private boolean createDepositGiftBucket(Long userId, String account, Integer sourceType, String sourceOrderNo,
											BigDecimal giftHAmount) {
		if (userId == null || StringUtils.isBlank(account) || StringUtils.isBlank(sourceOrderNo)) {
			throw new ServiceException("创建H赠送释放桶参数不能为空");
		}
		if (giftHAmount == null || giftHAmount.compareTo(BigDecimal.ZERO) <= 0) {
			log.info("H赠送释放桶赠送H数量为0, sourceType:{}, sourceOrderNo:{}, giftHAmount:{}",
				sourceType, sourceOrderNo, giftHAmount);
			return false;
		}

		// 1. 同一来源订单只创建一次释放桶，重复回调时直接跳过。
		Long exists = lambdaQuery()
			.eq(HGiftReleaseBucket::getSourceType, sourceType)
			.eq(HGiftReleaseBucket::getSourceOrderNo, sourceOrderNo)
			.count();
		if (exists > 0) {
			log.info("H赠送释放桶已存在, sourceType:{}, sourceOrderNo:{}", sourceType, sourceOrderNo);
			return false;
		}

		// 2. 创建对应来源类型的释放桶，后续由任务106每日释放。
		String bucketNo = IDUtils.getSnowflakeStr();
		HGiftReleaseBucket entity = buildBucket(bucketNo, userId, account, sourceType,
			sourceOrderNo, userId, giftHAmount, new Date());
		return save(entity);
	}

	/**
	 * 冻结释放桶
	 *
	 * @param id 主键
	 * @return 是否成功
	 */
	@Override
	public boolean freeze(Long id) {
		if (id == null) {
			throw new ServiceException("释放桶ID不能为空");
		}
		boolean update = lambdaUpdate()
			.eq(HGiftReleaseBucket::getId, id)
			.eq(HGiftReleaseBucket::getStatus, STATUS_RELEASING)
			.set(HGiftReleaseBucket::getStatus, STATUS_FROZEN)
			.set(HGiftReleaseBucket::getUpdateTime, new Date())
			.update();
		if (!update) {
			throw new ServiceException("只有释放中的释放桶可以冻结");
		}
		return true;
	}

	/**
	 * 解冻释放桶
	 *
	 * @param id 主键
	 * @return 是否成功
	 */
	@Override
	public boolean unfreeze(Long id) {
		if (id == null) {
			throw new ServiceException("释放桶ID不能为空");
		}
		boolean update = lambdaUpdate()
			.eq(HGiftReleaseBucket::getId, id)
			.eq(HGiftReleaseBucket::getStatus, STATUS_FROZEN)
			.set(HGiftReleaseBucket::getStatus, STATUS_RELEASING)
			.set(HGiftReleaseBucket::getUpdateTime, new Date())
			.update();
		if (!update) {
			throw new ServiceException("只有冻结的释放桶可以解冻");
		}
		return true;
	}

	private UserInfo findUser(HGiftReleaseBucket hGiftReleaseBucket) {
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getAccount, hGiftReleaseBucket.getAccount())
			.one();
		if (userInfo == null) {
			throw new ServiceException("用户不存在");
		}
		return userInfo;
	}

	private HGiftReleaseBucket buildBucket(String bucketNo, Long userId, String account, Integer sourceType,
										   String sourceOrderNo, Long sourceUserId, BigDecimal totalAmount, Date now) {
		Integer releaseDays = ConstantStatic.H_GIFT_RELEASE_DAYS;
		Integer startDate = Integer.valueOf(DateUtil.format(now, "yyyyMMdd"));
		totalAmount = totalAmount.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		BigDecimal dailyReleaseAmount = totalAmount
			.divide(new BigDecimal(releaseDays), ConstantStatic.newScale, ConstantStatic.roundingModeNew);

		HGiftReleaseBucket entity = new HGiftReleaseBucket();
		entity.setBucketNo(bucketNo);
		entity.setUserId(userId);
		entity.setAccount(account);
		entity.setSourceType(sourceType);
		entity.setSourceOrderNo(sourceOrderNo);
		entity.setSourceUserId(sourceUserId);
		entity.setTotalAmount(totalAmount);
		entity.setReleasedAmount(BigDecimal.ZERO.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		entity.setRemainingAmount(totalAmount);
		entity.setDailyReleaseAmount(dailyReleaseAmount);
		entity.setReleaseDays(releaseDays);
		entity.setReleasedDays(0);
		entity.setStartDate(startDate);
		entity.setLastReleaseDate(null);
		entity.setStatus(STATUS_RELEASING);
		entity.setCreateTime(now);
		entity.setUpdateTime(now);
		return entity;
	}

}
