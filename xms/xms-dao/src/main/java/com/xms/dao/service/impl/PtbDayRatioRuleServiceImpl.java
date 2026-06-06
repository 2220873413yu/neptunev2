package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.PtbDayRatioRuleMapper;
import com.xms.dao.domain.PtbDayRatioRule;
import com.xms.dao.service.IPtbDayRatioRuleService;

/**
 * BOOMAI日利率调节规则Service业务层处理
 *
 * @author xms
 * @date 2025-11-26
 */
@Service
public class PtbDayRatioRuleServiceImpl extends XmsDataServiceImpl<PtbDayRatioRuleMapper, PtbDayRatioRule> implements IPtbDayRatioRuleService
{


    /**
     * 查询BOOMAI日利率调节规则列表
     *
     *
     * @param ptbDayRatioRule BOOMAI日利率调节规则
     * @return BOOMAI日利率调节规则
     */
    @Override
    public List<PtbDayRatioRule> selectPtbDayRatioRuleList(PtbDayRatioRule ptbDayRatioRule)
    {
        return baseMapper.selectPtbDayRatioRuleList(ptbDayRatioRule);
    }

	@Override
	public BigDecimal getDayRatio() {
		return lambdaQuery()
			.eq(PtbDayRatioRule::getCoinType,1)
			.one().getBaseRatio();
	}
}
