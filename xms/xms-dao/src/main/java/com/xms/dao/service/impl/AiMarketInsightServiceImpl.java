package com.xms.dao.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.AiMarketInsightMapper;
import com.xms.dao.domain.AiMarketInsight;
import com.xms.dao.service.IAiMarketInsightService;

/**
 * AI分析行情Service业务层处理
 *
 * @author xms
 * @date 2025-09-18
 */
@Service
public class AiMarketInsightServiceImpl extends XmsDataServiceImpl<AiMarketInsightMapper, AiMarketInsight> implements IAiMarketInsightService
{


    /**
     * 查询AI分析行情列表
     *
     *
     * @param aiMarketInsight AI分析行情
     * @return AI分析行情
     */
    @Override
    public List<AiMarketInsight> selectAiMarketInsightList(AiMarketInsight aiMarketInsight)
    {
        return baseMapper.selectAiMarketInsightList(aiMarketInsight);
    }

	@Override
	public int deleteRecordById(List<Long> list) {
		if(CollectionUtil.isEmpty(list)){
			return 1;
		}
		return baseMapper.deleteRecordById(list);
	}
}
