package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.PtbDayRatioRule;

/**
 * BOOMAI日利率调节规则Mapper接口
 *
 * @author xms
 * @date 2025-11-26
 */
public interface PtbDayRatioRuleMapper extends XmsMapper<PtbDayRatioRule>
{
    /**
     * 查询BOOMAI日利率调节规则列表
     *
     * @param ptbDayRatioRule BOOMAI日利率调节规则
     * @return BOOMAI日利率调节规则集合
     */
    public List<PtbDayRatioRule> selectPtbDayRatioRuleList(PtbDayRatioRule ptbDayRatioRule);

}
