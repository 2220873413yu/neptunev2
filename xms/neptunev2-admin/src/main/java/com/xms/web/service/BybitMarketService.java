package com.xms.web.service;

import com.xms.common.core.domain.api.ResultPista;
import com.xms.web.service.impl.BybitMarketServiceImpl;

public interface BybitMarketService {
	ResultPista<BybitMarketServiceImpl.KlineResponse> bybitSpotKline();

	ResultPista<BybitMarketServiceImpl.PriceResponse> bybitSpotPrice();

	ResultPista<BybitMarketServiceImpl.YesterdayHighPriceResponse> bybitSpotYesterdayHighPrice();
}
