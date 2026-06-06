package com.xms.dao.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.xms.dao.entity.dto.DestroyOrderDto;
import com.xms.dao.entity.dto.TeamDestroyStatDto;
import com.xms.dao.mapper.XmsMapper;
import com.xms.dao.domain.DestroyOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 销毁记录Mapper接口
 *
 * @author xms
 * @date 2025-11-18
 */
public interface DestroyOrderMapper extends XmsMapper<DestroyOrder>
{
    /**
     * 查询销毁记录列表
     *
     * @param destroyOrder 销毁记录
     * @return 销毁记录集合
     */
    public List<DestroyOrder> selectDestroyOrderList(DestroyOrder destroyOrder);

	@Select("SELECT SUM(usdt_value) FROM t_destroy_order WHERE status in(1,2)")
	BigDecimal sumTotalUsdtValue();


	/**
	 * 获取今日团队销毁价值
	 * @return
	 */
	BigDecimal getTodayTeamUsdtValue(@Param("userId") Long userId, @Param("intDate") Integer intDate);

	/**
	 * 获取当月团队销毁价值（团队月新增业绩）
	 * @param userId    团队上级用户ID
	 * @param monthStart 当月开始日期 yyyymmdd
	 * @param monthEnd   当月结束日期 yyyymmdd
	 * @return
	 */
	BigDecimal getMonthTeamUsdtValue(@Param("userId") Long userId,
									 @Param("monthStart") Integer monthStart,
									 @Param("monthEnd") Integer monthEnd);

	/**
	 * 获取今日团队销毁统计（同时返回 USDT 和 BOOMAI 数量）
	 */
	TeamDestroyStatDto getTodayTeamStat(@Param("userId") Long userId, @Param("intDate") Integer intDate);

	/**
	 * 获取当月团队销毁统计（同时返回 USDT 和 BOOMAI 数量）
	 */
	TeamDestroyStatDto getMonthTeamStat(@Param("userId") Long userId,
										@Param("monthStart") Integer monthStart,
										@Param("monthEnd") Integer monthEnd);

	/**
	 * 获取用户销毁总额
	 */
	BigDecimal userTotalDestroyAmount(@Param("userId") Long userId);

	/**
	 * 获取总销毁总额
	 */
	BigDecimal totalDestroyAmount();

	/**
	 * 获取今日销毁总额
	 */
	BigDecimal todayDestroyAmount(@Param("today") Integer today);

	/**
	 * 获取销毁订单列表
	 */
    List<DestroyOrderDto> rewardList(@Param("userId") Long userId);
}
