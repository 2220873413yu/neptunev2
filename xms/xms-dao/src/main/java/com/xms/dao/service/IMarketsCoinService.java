package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.MarketsCoin;

/**
 * 币种图标配置Service接口
 *
 * @author xms
 * @date 2025-08-15
 */
public interface IMarketsCoinService extends XmsDataService<MarketsCoin>
{

    /**
     * 查询币种图标配置列表
     *
     * @param marketsCoin 币种图标配置
     * @return 币种图标配置集合
     */
    public List<MarketsCoin> selectMarketsCoinList(MarketsCoin marketsCoin);

}
