package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.AiMarketInsight;

/**
 * AI分析行情Service接口
 *
 * @author xms
 * @date 2025-09-18
 */
public interface IAiMarketInsightService extends XmsDataService<AiMarketInsight>
{

    /**
     * 查询AI分析行情列表
     *
     * @param aiMarketInsight AI分析行情
     * @return AI分析行情集合
     */
    public List<AiMarketInsight> selectAiMarketInsightList(AiMarketInsight aiMarketInsight);

	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	int deleteRecordById(List<Long> list);
}
