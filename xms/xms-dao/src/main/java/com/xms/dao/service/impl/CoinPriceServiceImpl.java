package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.CoinPriceMapper;
import com.xms.dao.domain.CoinPrice;
import com.xms.dao.service.ICoinPriceService;

/**
 * 币种价格配置Service业务层处理
 *
 * @author xms
 * @date 2025-11-19
 */
@Service
public class CoinPriceServiceImpl extends XmsDataServiceImpl<CoinPriceMapper, CoinPrice> implements ICoinPriceService
{


    /**
     * 查询币种价格配置列表
     *
     *
     * @param coinPrice 币种价格配置
     * @return 币种价格配置
     */
    @Override
    public List<CoinPrice> selectCoinPriceList(CoinPrice coinPrice)
    {
        return baseMapper.selectCoinPriceList(coinPrice);
    }

	/**
	 * todo 待实现 后需要用交易所价格
	 * 获取mai价格
	 * @return
	 */
	@Override
	public BigDecimal getMaiPrice() {
		return lambdaQuery()
			.eq(CoinPrice::getCoinType,2)
			.one().getCurrentPrice();
	}
}
