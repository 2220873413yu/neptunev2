package com.xms.dao.service;

import java.math.BigDecimal;
import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.CoinPrice;

/**
 * 币种价格配置Service接口
 *
 * @author xms
 * @date 2025-11-19
 */
public interface ICoinPriceService extends XmsDataService<CoinPrice>
{

    /**
     * 查询币种价格配置列表
     *
     * @param coinPrice 币种价格配置
     * @return 币种价格配置集合
     */
    public List<CoinPrice> selectCoinPriceList(CoinPrice coinPrice);

	/**
	 * 获取mai价格
	 * @return
	 */
	BigDecimal getMaiPrice();
}
