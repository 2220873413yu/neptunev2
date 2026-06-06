package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.CoinPrice;

/**
 * 币种价格配置Mapper接口
 *
 * @author xms
 * @date 2025-11-19
 */
public interface CoinPriceMapper extends XmsMapper<CoinPrice>
{
    /**
     * 查询币种价格配置列表
     *
     * @param coinPrice 币种价格配置
     * @return 币种价格配置集合
     */
    public List<CoinPrice> selectCoinPriceList(CoinPrice coinPrice);

}
