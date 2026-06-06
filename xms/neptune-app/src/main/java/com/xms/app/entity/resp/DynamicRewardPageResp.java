package com.xms.app.entity.resp;

import com.xms.app.entity.dto.UserWealthVaultDto;
import com.xms.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 动态奖励页面信息
 */
@Data
public class DynamicRewardPageResp {

	/**
	 * 用户等级
	 */
	private Integer gameLevel;

	/**
	 * 今日极差收益
	 */
	private BigDecimal todayDiffReward;

	/**
	 * 今日平级奖励
	 */
	private BigDecimal todayEqualReward;

	/**
	 * 今日层级将收益
	 */
	private BigDecimal todayLevelReward;

	/**
	 * 今日新增奖收益
	 */
	private BigDecimal todayNewReward;

	/**
	 * 今日节点权益分红收益
	 */
	private BigDecimal todayNodeEquityDividendIncome;

	/**
	 * 财富仓对象
	 */
	private UserWealthVaultDto wealthVaultInfo;


	/**
	 * 今日累计动态
	 */
	private BigDecimal todayTotalDynamicReward =BigDecimal.ZERO;


	/**
	 * 本轮累计提现动态
	 */
	private BigDecimal totalWithdrawDynamic =BigDecimal.ZERO;

	/**
	 * 累计提现财富仓
	 */
	private BigDecimal totalWithdrawValidNum4;

	/**
	 * 最低起购h代币数量
	 */
	private BigDecimal minBuyAmount = BigDecimal.ZERO;
	/**
	 * h代币兑换积分比例
	 */
	private BigDecimal pointsRatio = BigDecimal.ZERO;
}
