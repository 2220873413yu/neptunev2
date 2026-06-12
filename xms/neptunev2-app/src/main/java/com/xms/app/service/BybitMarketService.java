package com.xms.app.service;

import com.xms.app.service.impl.BybitMarketServiceImpl;
import com.xms.common.core.domain.api.ResultPista;

public interface BybitMarketService {
	ResultPista<BybitMarketServiceImpl.KlineResponse> bybitSpotKline();

	ResultPista<BybitMarketServiceImpl.PriceResponse> bybitSpotPrice();

	ResultPista<BybitMarketServiceImpl.KlineResponse> bybitHSpotKline();

	ResultPista<BybitMarketServiceImpl.PriceResponse> bybitHSpotPrice();

	ResultPista<BybitMarketServiceImpl.KlineResponse> gateAcpSpotKline();

	ResultPista<BybitMarketServiceImpl.PriceResponse> gateAcpSpotPrice();
}
