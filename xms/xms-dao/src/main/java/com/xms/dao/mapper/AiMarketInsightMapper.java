package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.AiMarketInsight;

/**
 * AI分析行情Mapper接口
 *
 * @author xms
 * @date 2025-09-18
 */
public interface AiMarketInsightMapper extends XmsMapper<AiMarketInsight>
{
    /**
     * 查询AI分析行情列表
     *
     * @param aiMarketInsight AI分析行情
     * @return AI分析行情集合
     */
    public List<AiMarketInsight> selectAiMarketInsightList(AiMarketInsight aiMarketInsight);

	/**
	 * 批量删除AI分析行情
	 *
	 * @param list
	 * @return
	 */
	int deleteRecordById(List<Long> list);
}
