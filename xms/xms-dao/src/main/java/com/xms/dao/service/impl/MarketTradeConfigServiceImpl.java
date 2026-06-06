package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.MarketTradeConfigMapper;
import com.xms.dao.domain.MarketTradeConfig;
import com.xms.dao.service.IMarketTradeConfigService;

/**
 * 交易产品行情数据管理Service业务层处理
 *
 * @author xms
 * @date 2025-08-12
 */
@Service
public class MarketTradeConfigServiceImpl extends XmsDataServiceImpl<MarketTradeConfigMapper, MarketTradeConfig> implements IMarketTradeConfigService
{


    /**
     * 查询交易产品行情数据管理列表
     *
     *
     * @param marketTradeConfig 交易产品行情数据管理
     * @return 交易产品行情数据管理
     */
    @Override
    public List<MarketTradeConfig> selectMarketTradeConfigList(MarketTradeConfig marketTradeConfig)
    {
        return baseMapper.selectMarketTradeConfigList(marketTradeConfig);
    }

}
