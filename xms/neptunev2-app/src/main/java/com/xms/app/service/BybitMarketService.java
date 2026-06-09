package com.xms.app.service;

import com.xms.app.controller.BybitMarketController;
import com.xms.app.service.impl.BybitMarketServiceImpl;
import com.xms.common.core.domain.api.ResultPista;

public interface BybitMarketService {
	ResultPista<BybitMarketServiceImpl.KlineResponse> bybitSpotKline();

	ResultPista<BybitMarketServiceImpl.PriceResponse> bybitSpotPrice();
}
