package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.MarketTradeConfig;

/**
 * 交易产品行情数据管理Mapper接口
 *
 * @author xms
 * @date 2025-08-12
 */
public interface MarketTradeConfigMapper extends XmsMapper<MarketTradeConfig>
{
    /**
     * 查询交易产品行情数据管理列表
     *
     * @param marketTradeConfig 交易产品行情数据管理
     * @return 交易产品行情数据管理集合
     */
    public List<MarketTradeConfig> selectMarketTradeConfigList(MarketTradeConfig marketTradeConfig);

}
