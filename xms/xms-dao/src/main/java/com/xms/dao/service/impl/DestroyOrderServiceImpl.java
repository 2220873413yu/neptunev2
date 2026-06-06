package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.date.DateUtil;
import com.xms.dao.domain.DestroyOrder;
import com.xms.dao.entity.dto.DestroyOrderDto;
import com.xms.dao.entity.dto.TeamDestroyStatDto;
import com.xms.dao.mapper.DestroyOrderMapper;
import com.xms.dao.service.IDestroyOrderService;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 销毁记录Service业务层处理
 *
 * @author xms
 * @date 2025-11-18
 */
@Service
public class DestroyOrderServiceImpl extends XmsDataServiceImpl<DestroyOrderMapper, DestroyOrder> implements IDestroyOrderService
{


    /**
     * 查询销毁记录列表
     *
     *
     * @param destroyOrder 销毁记录
     * @return 销毁记录
     */
    @Override
    public List<DestroyOrder> selectDestroyOrderList(DestroyOrder destroyOrder)
    {
        return baseMapper.selectDestroyOrderList(destroyOrder);
    }

	@Override
	public BigDecimal sumTotalUsdtValue() {
		return baseMapper.sumTotalUsdtValue();
	}

	/**
	 * 获取今日团队销毁价值
	 * @return
	 */
	@Override
	public BigDecimal getTodayTeamUsdtValue(Long userId, Integer intDate) {
		return baseMapper.getTodayTeamUsdtValue(userId, intDate);
	}

	/**
	 * 获取当月团队销毁价值（团队月新增业绩）
	 * @param userId    团队上级用户ID
	 * @param monthStart 当月开始日期 yyyymmdd
	 * @param monthEnd   当月结束日期 yyyymmdd
	 * @return
	 */
	@Override
	public BigDecimal getMonthTeamUsdtValue(Long userId, Integer monthStart, Integer monthEnd) {
		return baseMapper.getMonthTeamUsdtValue(userId, monthStart, monthEnd);
	}

	@Override
	public TeamDestroyStatDto getTodayTeamStat(Long userId, Integer intDate) {
		return baseMapper.getTodayTeamStat(userId, intDate);
	}

	@Override
	public TeamDestroyStatDto getMonthTeamStat(Long userId, Integer monthStart, Integer monthEnd) {
		return baseMapper.getMonthTeamStat(userId, monthStart, monthEnd);
	}

	@Override
	public BigDecimal userTotalDestroyAmount(Long userId) {
		return baseMapper.userTotalDestroyAmount(userId);
	}

	@Override
	public BigDecimal totalDestroyAmount() {
		return baseMapper.totalDestroyAmount();
	}

	@Override
	public BigDecimal todayDestroyAmount() {
		return baseMapper.todayDestroyAmount(Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
	}

	@Override
	public Map<String, Object> dashboardStatistics() {
		Map<String, Object> result = new HashMap<>();

		long totalOrders = lambdaQuery().count();
		long runningOrders = lambdaQuery().eq(DestroyOrder::getPayStatus, 1).eq(DestroyOrder::getStatus, 1).count();
		long reducedOrders = lambdaQuery().eq(DestroyOrder::getPayStatus, 1).eq(DestroyOrder::getIsReduced, 1).count();
		long pendingOrders = lambdaQuery().ne(DestroyOrder::getPayStatus, 1).count();

		BigDecimal runningBoomai = lambdaQuery()
			.eq(DestroyOrder::getPayStatus, 1)
			.eq(DestroyOrder::getStatus, 1)
			.select(DestroyOrder::getValidNum1Value)
			.list()
			.stream()
			.map(DestroyOrder::getValidNum1Value)
			.filter(Objects::nonNull)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(2, RoundingMode.HALF_UP);

		Map<String, Object> summary = new HashMap<>();
		summary.put("totalOrders", totalOrders);
		summary.put("runningOrders", runningOrders);
		summary.put("reducedOrders", reducedOrders);
		summary.put("pendingOrders", pendingOrders);
		summary.put("runningDestroyAmount", runningBoomai);
		result.put("summary", summary);

		DateTimeFormatter keyFormatter = DateTimeFormatter.ofPattern("MM-dd");
		DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
		LocalDate today = LocalDate.now();
		List<String> trendKeys = new ArrayList<>();
		List<Long> trendValues = new ArrayList<>();
		for (int i = 6; i >= 0; i--) {
			LocalDate day = today.minusDays(i);
			Integer dayInt = Integer.valueOf(day.format(dateFormatter));
			Long count = lambdaQuery()
				.eq(DestroyOrder::getPayStatus, 1)
				.eq(DestroyOrder::getCreateDate, dayInt)
				.count();
			trendKeys.add(day.format(keyFormatter));
			trendValues.add(count);
		}
		Map<String, Object> trend = new HashMap<>();
		trend.put("key", trendKeys);
		trend.put("expectedData", trendValues);
		trend.put("seriesName", "新增订单数");
		result.put("trend", trend);

		long notReduced = lambdaQuery().eq(DestroyOrder::getPayStatus, 1).ne(DestroyOrder::getIsReduced, 1).count();
		List<Map<String, Object>> reduceSeries = new ArrayList<>();
		reduceSeries.add(buildPieEntry("未减产", notReduced));
		reduceSeries.add(buildPieEntry("已减产", reducedOrders));
		Map<String, Object> reduce = new HashMap<>();
		reduce.put("legend", Arrays.asList("未减产", "已减产"));
		reduce.put("seriesData", reduceSeries);
		result.put("reduceStatus", reduce);

		List<String> amountLabels = Arrays.asList("<100 U", "100-500 U", "500-1k U", "1k-5k U", ">5k U");
		List<Long> amountValues = new ArrayList<>();
		BigDecimal hundred = new BigDecimal("100");
		BigDecimal fiveHundred = new BigDecimal("500");
		BigDecimal oneThousand = new BigDecimal("1000");
		BigDecimal fiveThousand = new BigDecimal("5000");
		amountValues.add(lambdaQuery().eq(DestroyOrder::getPayStatus, 1).lt(DestroyOrder::getUsdtValue, hundred).count());
		amountValues.add(lambdaQuery().eq(DestroyOrder::getPayStatus, 1).ge(DestroyOrder::getUsdtValue, hundred).lt(DestroyOrder::getUsdtValue, fiveHundred).count());
		amountValues.add(lambdaQuery().eq(DestroyOrder::getPayStatus, 1).ge(DestroyOrder::getUsdtValue, fiveHundred).lt(DestroyOrder::getUsdtValue, oneThousand).count());
		amountValues.add(lambdaQuery().eq(DestroyOrder::getPayStatus, 1).ge(DestroyOrder::getUsdtValue, oneThousand).lt(DestroyOrder::getUsdtValue, fiveThousand).count());
		amountValues.add(lambdaQuery().eq(DestroyOrder::getPayStatus, 1).ge(DestroyOrder::getUsdtValue, fiveThousand).count());
		Map<String, Object> amount = new HashMap<>();
		amount.put("key", amountLabels);
		amount.put("expectedData", amountValues);
		amount.put("seriesName", "订单数量");
		result.put("amount", amount);

		return result;
	}

	private Map<String, Object> buildPieEntry(String name, long value) {
		Map<String, Object> item = new HashMap<>();
		item.put("name", name);
		item.put("value", value);
		return item;
	}

	@Override
	public List<DestroyOrderDto> rewardList(Long userId) {
		return baseMapper.rewardList(userId);
	}
}
