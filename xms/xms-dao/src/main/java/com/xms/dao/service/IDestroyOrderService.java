package com.xms.dao.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.xms.dao.domain.DestroyOrder;
import com.xms.dao.entity.dto.DestroyOrderDto;
import com.xms.dao.entity.dto.TeamDestroyStatDto;
import com.xms.dao.service.XmsDataService;

/**
 * 销毁记录Service接口
 *
 * @author xms
 * @date 2025-11-18
 */
public interface IDestroyOrderService extends XmsDataService<DestroyOrder>
{

    /**
     * 查询销毁记录列表
     *
     * @param destroyOrder 销毁记录
     * @return 销毁记录集合
     */
    public List<DestroyOrder> selectDestroyOrderList(DestroyOrder destroyOrder);

	/**
	 * 获取销毁订单总金额
	 * @return
	 */
	BigDecimal sumTotalUsdtValue();

	/**
	 * 获取今日团队销毁价值
	 * @return
	 */
	public BigDecimal getTodayTeamUsdtValue(Long userId, Integer intDate);

	/**
	 * 获取当月团队销毁价值（团队月新增业绩）
	 * @param userId    团队上级用户ID
	 * @param monthStart 当月开始日期 yyyymmdd
	 * @param monthEnd   当月结束日期 yyyymmdd
	 * @return
	 */
	public BigDecimal getMonthTeamUsdtValue(Long userId, Integer monthStart, Integer monthEnd);

	/**
	 * 获取今日团队销毁统计（USDT + BOOMAI）
	 */
	TeamDestroyStatDto getTodayTeamStat(Long userId, Integer intDate);

	/**
	 * 获取当月团队销毁统计（USDT + BOOMAI）
	 */
	TeamDestroyStatDto getMonthTeamStat(Long userId, Integer monthStart, Integer monthEnd);

	/**
	 * 获取用户销毁总额
	 * @param userId
	 * @return
	 */
	BigDecimal userTotalDestroyAmount(Long userId);

	/**
	 * 获取用户销毁总额
	 * @return
	 */
	public BigDecimal totalDestroyAmount();

	/**
	 * 获取今日销毁总额
	 * @return
	 */
	BigDecimal todayDestroyAmount();

	/**
	 * 销毁订单看板统计
	 * @return
	 */
	Map<String, Object> dashboardStatistics();

	/**
	 * 获取奖励列表
	 * @param userId
	 * @return
	 */
	List<DestroyOrderDto> rewardList(Long userId);
}
