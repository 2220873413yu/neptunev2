package com.xms.app.controller;

import com.xms.app.service.BybitMarketService;
import com.xms.app.service.impl.BybitMarketServiceImpl;
import com.xms.common.annotation.Anonymous;
import com.xms.common.core.domain.api.ResultPista;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Bybit 行情相关
 */
@Api(tags = "Bybit行情")
@RestController
public class BybitMarketController {


	@Autowired
	private BybitMarketService bybitMarketService;

	/**
	 * Bybit 现货K线（默认取最近7天日线）
	 * 返回数据结构说明：
	 * kline -> [timestamp, open, high, low, close, volume, turnover]
	 */
	@ApiOperation(value = "Bybit现货K线")
	@GetMapping(value = "/api/bybit/spot/kie")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.KlineResponse> bybitSpotKline() {
		return bybitMarketService.bybitSpotKline();
	}

	/**
	 * Bybit 现货最新价
	 */
	@ApiOperation(value = "Bybit现货最新价")
	@GetMapping(value = "/api/bybit/spot/pe")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.PriceResponse> bybitSpotPrice() {
		return bybitMarketService.bybitSpotPrice();
	}

	/**
	 * Bybit H 现货K线（默认取最近7天日线）
	 * 返回数据结构说明：
	 * kline -> [timestamp, open, high, low, close, volume, turnover]
	 */
	@ApiOperation(value = "Bybit H现货K线")
	@GetMapping(value = "/api/bybit/spot/h/kie")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.KlineResponse> bybitHSpotKline() {
		return bybitMarketService.bybitHSpotKline();
	}

	/**
	 * Bybit H 现货最新价
	 */
	@ApiOperation(value = "Bybit H现货最新价")
	@GetMapping(value = "/api/bybit/spot/h/pe")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.PriceResponse> bybitHSpotPrice() {
		return bybitMarketService.bybitHSpotPrice();
	}

	/**
	 * Gate ACP 现货K线（默认取最近7天日线）
	 * 返回数据结构说明：
	 * kline -> [timestamp, open, high, low, close, volume, turnover]
	 */
	@ApiOperation(value = "Gate ACP现货K线")
	@GetMapping(value = "/api/gate/spot/kie")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.KlineResponse> gateAcpSpotKline() {
		return bybitMarketService.gateAcpSpotKline();
	}

	/**
	 * Gate ACP 现货最新价
	 */
	@ApiOperation(value = "Gate ACP现货最新价")
	@GetMapping(value = "/api/gate/spot/pe")
	@Anonymous
	public ResultPista<BybitMarketServiceImpl.PriceResponse> gateAcpSpotPrice() {
		return bybitMarketService.gateAcpSpotPrice();
	}

}
