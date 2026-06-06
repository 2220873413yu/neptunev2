package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.MarketsCoinMapper;
import com.xms.dao.domain.MarketsCoin;
import com.xms.dao.service.IMarketsCoinService;

/**
 * 币种图标配置Service业务层处理
 *
 * @author xms
 * @date 2025-08-15
 */
@Service
public class MarketsCoinServiceImpl extends XmsDataServiceImpl<MarketsCoinMapper, MarketsCoin> implements IMarketsCoinService
{


    /**
     * 查询币种图标配置列表
     *
     *
     * @param marketsCoin 币种图标配置
     * @return 币种图标配置
     */
    @Override
    public List<MarketsCoin> selectMarketsCoinList(MarketsCoin marketsCoin)
    {
        return baseMapper.selectMarketsCoinList(marketsCoin);
    }

}
