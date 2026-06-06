package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.MarketsCoin;

/**
 * 币种图标配置Mapper接口
 *
 * @author xms
 * @date 2025-08-15
 */
public interface MarketsCoinMapper extends XmsMapper<MarketsCoin>
{
    /**
     * 查询币种图标配置列表
     *
     * @param marketsCoin 币种图标配置
     * @return 币种图标配置集合
     */
    public List<MarketsCoin> selectMarketsCoinList(MarketsCoin marketsCoin);

}
