package com.xms.app.controller;

import com.xms.app.service.BybitMarketService;
import com.xms.app.service.impl.BybitMarketServiceImpl;
import com.xms.common.annotation.Anonymous;
import com.xms.common.core.domain.api.ResultPista;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Bybit 行情相关
 */
@Api(tags = "Bybit行情")
@RestController
@RequestMapping("/api/bybit")
public class BybitMarketController {


	@Autowired
	private BybitMarketService bybitMarketService;

	/**
	 * Bybit 现货K线（默认取最近7天日线）
	 * 返回数据结构说明：
	 * kline -> [timestamp, open, high, low, close, volume, turnover]
	 */
	@ApiOperation(value = "Bybit现货K线")
	@GetMapping(value = "/spot/kie")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.KlineResponse> bybitSpotKline() {
		return bybitMarketService.bybitSpotKline();
	}

	/**
	 * Bybit 现货最新价
	 */
	@ApiOperation(value = "Bybit现货最新价")
	@GetMapping(value = "/spot/pe")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.PriceResponse> bybitSpotPrice() {
		return bybitMarketService.bybitSpotPrice();
	}

}
