package com.xms.dao.service;

import java.math.BigDecimal;
import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.PtbDayRatioRule;

/**
 * BOOMAI日利率调节规则Service接口
 *
 * @author xms
 * @date 2025-11-26
 */
public interface IPtbDayRatioRuleService extends XmsDataService<PtbDayRatioRule>
{

    /**
     * 查询BOOMAI日利率调节规则列表
     *
     * @param ptbDayRatioRule BOOMAI日利率调节规则
     * @return BOOMAI日利率调节规则集合
     */
    public List<PtbDayRatioRule> selectPtbDayRatioRuleList(PtbDayRatioRule ptbDayRatioRule);

	/**
	 * 获取日利率
	 * @return
	 */
	BigDecimal getDayRatio();
}
