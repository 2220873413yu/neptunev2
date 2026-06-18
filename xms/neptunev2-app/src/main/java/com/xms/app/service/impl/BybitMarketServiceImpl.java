package com.xms.app.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xms.app.service.BybitMarketService;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.RedisConstant;
import com.xms.common.core.domain.api.ResultPista;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BybitMarketServiceImpl implements BybitMarketService {
	private static final String GATE_ACP_USDT = "ACP_USDT";
	private static final String GATE_H_USDT = "H_USDT";

	@Autowired
	private XmsRedis xmsRedis;


	@Override
	public ResultPista<PriceResponse> bybitSpotPrice() {
		try {
			PriceResponse data = xmsRedis.get(
				RedisConstant.GATE_MARKET_SPOT_PRICE + GATE_H_USDT,
				() -> fetchPriceData(),
				8L,
				TimeUnit.SECONDS
			);
			if (data == null) {
				return ResultPista.fail("Gate H 最新价获取失败");
			}
			return ResultPista.data(data);
		} catch (IORuntimeException ex) {
			return ResultPista.fail("Gate 请求失败: " + ex.getMessage());
		} catch (JSONException ex) {
			return ResultPista.fail("Gate 解析失败: " + ex.getMessage());
		}
	}

	private PriceResponse fetchPriceData() {
		String tickerUrl = "https://api.gateio.ws/api/v4/spot/tickers?currency_pair=" + GATE_H_USDT;

		String tickerBody = HttpRequest.get(tickerUrl).timeout(5000).execute().body();
		JSONArray tickerArray = JSONUtil.parseArray(tickerBody);
		if (tickerArray.isEmpty()) {
			return null;
		}

		JSONObject ticker = tickerArray.getJSONObject(0);
		String lastPrice = ticker.getStr("last");
		if (lastPrice == null) {
			return null;
		}

		PriceResponse data = new PriceResponse();
		data.setSn(GATE_H_USDT);
		data.setLp(lastPrice);
		return data;
	}

	@Override
	public ResultPista<PriceResponse> bybitHSpotPrice() {
		return bybitSpotPrice();
	}

	@Override
	public ResultPista<PriceResponse> gateAcpSpotPrice() {
		try {
			PriceResponse data = xmsRedis.get(
				RedisConstant.GATE_MARKET_SPOT_PRICE + GATE_ACP_USDT,
				() -> fetchGateAcpPriceData(),
				8L,
				TimeUnit.SECONDS
			);
			if (data == null) {
				return ResultPista.fail("Gate ACP 最新价获取失败");
			}
			return ResultPista.data(data);
		} catch (IORuntimeException ex) {
			return ResultPista.fail("Gate 请求失败: " + ex.getMessage());
		} catch (JSONException ex) {
			return ResultPista.fail("Gate 解析失败: " + ex.getMessage());
		}
	}

	private PriceResponse fetchGateAcpPriceData() {
		String tickerUrl = "https://api.gateio.ws/api/v4/spot/tickers?currency_pair=" + GATE_ACP_USDT;

		String tickerBody = HttpRequest.get(tickerUrl).timeout(5000).execute().body();
		JSONArray tickerArray = JSONUtil.parseArray(tickerBody);
		if (tickerArray.isEmpty()) {
			return null;
		}

		JSONObject ticker = tickerArray.getJSONObject(0);
		String lastPrice = ticker.getStr("last");
		if (lastPrice == null) {
			return null;
		}

		PriceResponse data = new PriceResponse();
		data.setSn(GATE_ACP_USDT);
		data.setLp(lastPrice);
		return data;
	}

	public static class KlineResponse {
		/** 原字段 symbol，交易对标识，例如 H_USDT */
		private String sn;
		/** 原字段 interval，K 线周期 */
		private String iv;
		/** 原字段 start，开始时间戳（毫秒） */
		private long st;
		/** 原字段 end，结束时间戳（毫秒） */
		private long et;
		/** 原字段 kline，K 线数据列表 */
		private List<Object> kl;

		public String getSn() {
			return sn;
		}

		public void setSn(String sn) {
			this.sn = sn;
		}

		public String getIv() {
			return iv;
		}

		public void setIv(String iv) {
			this.iv = iv;
		}

		public long getSt() {
			return st;
		}

		public void setSt(long st) {
			this.st = st;
		}

		public long getEt() {
			return et;
		}

		public void setEt(long et) {
			this.et = et;
		}

		public List<Object> getKl() {
			return kl;
		}

		public void setKl(List<Object> kl) {
			this.kl = kl;
		}
	}

	public static class PriceResponse {
		/** 原字段 symbol，交易对标识，例如 H_USDT */
		private String sn;
		/** 原字段 latestPrice，最新价格 */
		private String lp;

		public String getSn() {
			return sn;
		}

		public void setSn(String sn) {
			this.sn = sn;
		}

		public String getLp() {
			return lp;
		}

		public void setLp(String lp) {
			this.lp = lp;
		}
	}

	@Override
	public ResultPista<KlineResponse> bybitSpotKline() {
		try {
			KlineResponse data = xmsRedis.get(
				RedisConstant.GATE_SPOT_KLINE + GATE_H_USDT + ":1d:7d",
				() -> fetchKlineData(),
				60L,
				TimeUnit.SECONDS
			);
			if (data == null) {
				return ResultPista.fail("Gate H K线获取失败");
			}
			return ResultPista.data(data);
		} catch (IORuntimeException ex) {
			return ResultPista.fail("Gate 请求失败: " + ex.getMessage());
		} catch (JSONException ex) {
			return ResultPista.fail("Gate 解析失败: " + ex.getMessage());
		}
	}


	private KlineResponse fetchKlineData() {
		String normalizedSymbol = "H_USDT";
		int safeDays = 7;
		long endSeconds = System.currentTimeMillis() / 1000;
		long startSeconds = endSeconds - safeDays * 24L * 60L * 60L;
		long startMillis = startSeconds * 1000;
		long endMillis = endSeconds * 1000;

		String klineUrl = "https://api.gateio.ws/api/v4/spot/candlesticks?currency_pair="
			+ normalizedSymbol + "&interval=1d&from=" + startSeconds + "&to=" + endSeconds;

		String klineBody = HttpRequest.get(klineUrl).timeout(5000).execute().body();
		JSONArray rawKline = JSONUtil.parseArray(klineBody);
		if (rawKline.isEmpty()) {
			return null;
		}

		KlineResponse data = new KlineResponse();
		data.setSn(normalizedSymbol);
		data.setIv("D");
		data.setSt(startMillis);
		data.setEt(endMillis);

		List<Object> filtered = new ArrayList<>();
		for (Object item : rawKline) {
			JSONArray row = JSONUtil.parseArray(item);
			long tsMillis = Long.parseLong(row.getStr(0)) * 1000;
			if (tsMillis >= startMillis && tsMillis <= endMillis) {
				JSONArray normalizedRow = new JSONArray();
				normalizedRow.add(tsMillis);
				normalizedRow.add(row.getStr(5));
				normalizedRow.add(row.getStr(3));
				normalizedRow.add(row.getStr(4));
				normalizedRow.add(row.getStr(2));
				normalizedRow.add(row.getStr(1));
				normalizedRow.add(row.getStr(6));
				filtered.add(normalizedRow);
			}
		}

		data.setKl(filtered);
		return data;
	}

	@Override
	public ResultPista<KlineResponse> bybitHSpotKline() {
		return bybitSpotKline();
	}

	@Override
	public ResultPista<KlineResponse> gateAcpSpotKline() {
		try {
			KlineResponse data = xmsRedis.get(
				RedisConstant.GATE_SPOT_KLINE + GATE_ACP_USDT + ":1d:7d",
				() -> fetchGateAcpKlineData(),
				60L,
				TimeUnit.SECONDS
			);
			if (data == null) {
				return ResultPista.fail("Gate ACP K线获取失败");
			}
			return ResultPista.data(data);
		} catch (IORuntimeException ex) {
			return ResultPista.fail("Gate 请求失败: " + ex.getMessage());
		} catch (JSONException ex) {
			return ResultPista.fail("Gate 解析失败: " + ex.getMessage());
		}
	}

	private KlineResponse fetchGateAcpKlineData() {
		String interval = "D";
		String gateInterval = "1d";
		String klineUrl = "https://api.gateio.ws/api/v4/spot/candlesticks?currency_pair="
			+ GATE_ACP_USDT + "&interval=" + gateInterval + "&limit=7";

		String klineBody = HttpRequest.get(klineUrl).timeout(5000).execute().body();
		JSONArray rawKline = JSONUtil.parseArray(klineBody);
		if (rawKline.isEmpty()) {
			return null;
		}

		List<JSONArray> converted = new ArrayList<>();
		for (Object item : rawKline) {
			JSONArray row = JSONUtil.parseArray(item);
			if (row.size() < 7) {
				continue;
			}
			JSONArray targetRow = new JSONArray();
			targetRow.add(Long.parseLong(row.getStr(0)) * 1000L);
			targetRow.add(row.getStr(5));
			targetRow.add(row.getStr(3));
			targetRow.add(row.getStr(4));
			targetRow.add(row.getStr(2));
			targetRow.add(row.getStr(6));
			targetRow.add(row.getStr(1));
			converted.add(targetRow);
		}

		if (converted.isEmpty()) {
			return null;
		}

		converted.sort(Comparator.comparingLong(row -> Long.parseLong(String.valueOf(row.get(0)))));

		long start = Long.parseLong(String.valueOf(converted.get(0).get(0)));
		long end = Long.parseLong(String.valueOf(converted.get(converted.size() - 1).get(0)));
		List<Object> filtered = new ArrayList<>(converted);

		KlineResponse data = new KlineResponse();
		data.setSn(GATE_ACP_USDT);
		data.setIv(interval);
		data.setSt(start);
		data.setEt(end);
		data.setKl(filtered);
		return data;
	}
}
