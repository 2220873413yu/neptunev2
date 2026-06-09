package com.xms.web.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.RedisConstant;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.web.service.BybitMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BybitMarketServiceImpl implements BybitMarketService {
	@Autowired
	private XmsRedis xmsRedis;


	@Override
	public ResultPista<PriceResponse> bybitSpotPrice() {
		try {
			PriceResponse data = xmsRedis.get(
				RedisConstant.BYBIT_SPOT_PRICE + "HUSDT",
				() -> fetchPriceData(),
				8L,
				TimeUnit.SECONDS
			);
			if (data == null) {
				return ResultPista.fail("Bybit 最新价获取失败");
			}
			return ResultPista.data(data);
		} catch (IORuntimeException ex) {
			return ResultPista.fail("Bybit 请求失败: " + ex.getMessage());
		} catch (JSONException ex) {
			return ResultPista.fail("Bybit 解析失败: " + ex.getMessage());
		}
	}

	@Override
	public ResultPista<YesterdayHighPriceResponse> bybitSpotYesterdayHighPrice() {
		try {
			YesterdayHighPriceResponse data = xmsRedis.get(
				RedisConstant.BYBIT_SPOT_KLINE + "HUSDT:D:yesterdayHigh",
				this::fetchYesterdayHighPriceData,
				60L,
				TimeUnit.SECONDS
			);
			if (data == null) {
				return ResultPista.fail("Bybit 昨日最高价获取失败");
			}
			return ResultPista.data(data);
		} catch (IORuntimeException ex) {
			return ResultPista.fail("Bybit 请求失败: " + ex.getMessage());
		} catch (JSONException ex) {
			return ResultPista.fail("Bybit 解析失败: " + ex.getMessage());
		}
	}

	private PriceResponse fetchPriceData() {
		String normalizedSymbol = "HUSDT";
		String tickerUrl = "https://api.bybit.com/v5/market/tickers?category=spot&symbol=" + normalizedSymbol;

		String tickerBody = HttpRequest.get(tickerUrl).timeout(5000).execute().body();
		JSONObject tickerJson = JSONUtil.parseObj(tickerBody);

		Integer tickerCode = tickerJson.getInt("retCode");
		if (tickerCode != null && tickerCode != 0) {
			return null;
		}

		PriceResponse data = new PriceResponse();
		data.setSymbol(normalizedSymbol);
		data.setLatestPrice(String.valueOf(tickerJson.getByPath("result.list.0.lastPrice")));
		return data;
	}

	private YesterdayHighPriceResponse fetchYesterdayHighPriceData() {
		String normalizedSymbol = "HUSDT";
		String interval = "D";
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		long start = yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long end = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

		String klineUrl = "https://api.bybit.com/v5/market/kline?category=spot&symbol="
			+ normalizedSymbol + "&interval=" + interval + "&start=" + start + "&end=" + end + "&limit=2";

		String klineBody = HttpRequest.get(klineUrl).timeout(5000).execute().body();
		JSONObject klineJson = JSONUtil.parseObj(klineBody);

		Integer klineCode = klineJson.getInt("retCode");
		if (klineCode != null && klineCode != 0) {
			return null;
		}

		JSONArray rawKline = JSONUtil.parseArray(klineJson.getByPath("result.list"));
		if (rawKline == null || rawKline.isEmpty()) {
			return null;
		}
		JSONArray yesterdayRow = null;
		for (Object item : rawKline) {
			JSONArray row = JSONUtil.parseArray(item);
			long ts = Long.parseLong(row.getStr(0));
			if (ts == start) {
				yesterdayRow = row;
				break;
			}
		}
		if (yesterdayRow == null) {
			yesterdayRow = JSONUtil.parseArray(rawKline.get(0));
		}

		YesterdayHighPriceResponse data = new YesterdayHighPriceResponse();
		data.setSymbol(normalizedSymbol);
		data.setStart(start);
		data.setEnd(end);
		data.setHighPrice(yesterdayRow.getStr(2));
		return data;
	}

	public static class KlineResponse {
		/**
		 * 合约名称（symbol）
		 */
		private String symbol;
		/**
		 * 时间粒度（interval）：1,3,5,15,30,60,120,240,360,720,D,M,W
		 */
		private String interval;
		/**
		 * 开始时间戳（毫秒）
		 */
		private long start;
		/**
		 * 结束时间戳（毫秒）
		 */
		private long end;
		/**
		 * K线数组，每条为：
		 * [timestamp, open, high, low, close, volume, turnover]
		 * 其中：
		 * timestamp: 蜡烛开始时间戳（毫秒）
		 * open: 开盘价
		 * high: 最高价
		 * low: 最低价
		 * close: 收盘价（若蜡烛未结束，为最新成交价）
		 * volume: 成交量（现货为 base coin 数量）
		 * turnover: 成交额（现货为 quote coin 数量，例如 USDT）
		 */
		private List<Object> kline;

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public String getInterval() {
			return interval;
		}

		public void setInterval(String interval) {
			this.interval = interval;
		}

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getEnd() {
			return end;
		}

		public void setEnd(long end) {
			this.end = end;
		}

		public List<Object> getKline() {
			return kline;
		}

		public void setKline(List<Object> kline) {
			this.kline = kline;
		}
	}

	public static class PriceResponse {
		private String symbol;
		private String latestPrice;

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public String getLatestPrice() {
			return latestPrice;
		}

		public void setLatestPrice(String latestPrice) {
			this.latestPrice = latestPrice;
		}
	}

	public static class YesterdayHighPriceResponse {
		private String symbol;
		private long start;
		private long end;
		private String highPrice;

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getEnd() {
			return end;
		}

		public void setEnd(long end) {
			this.end = end;
		}

		public String getHighPrice() {
			return highPrice;
		}

		public void setHighPrice(String highPrice) {
			this.highPrice = highPrice;
		}
	}

	@Override
	public ResultPista<KlineResponse> bybitSpotKline() {
		try {
			KlineResponse data = xmsRedis.get(
				RedisConstant.BYBIT_SPOT_KLINE + "HUSDT:D:7d",
				() -> fetchKlineData(),
				60L,
				TimeUnit.SECONDS
			);
			if (data == null) {
				return ResultPista.fail("Bybit K线获取失败");
			}
			return ResultPista.data(data);
		} catch (IORuntimeException ex) {
			return ResultPista.fail("Bybit 请求失败: " + ex.getMessage());
		} catch (JSONException ex) {
			return ResultPista.fail("Bybit 解析失败: " + ex.getMessage());
		}
	}

	private KlineResponse fetchKlineData() {
		String normalizedSymbol = "HUSDT";
		String interval = "D";
		int safeDays = 7;
		long end = System.currentTimeMillis();
		long start = end - safeDays * 24L * 60L * 60L * 1000L;

		String klineUrl = "https://api.bybit.com/v5/market/kline?category=spot&symbol="
			+ normalizedSymbol + "&interval=" + interval + "&start=" + start + "&end=" + end;

		String klineBody = HttpRequest.get(klineUrl).timeout(5000).execute().body();
		JSONObject klineJson = JSONUtil.parseObj(klineBody);

		Integer klineCode = klineJson.getInt("retCode");
		if (klineCode != null && klineCode != 0) {
			return null;
		}

		KlineResponse data = new KlineResponse();
		data.setSymbol(normalizedSymbol);
		data.setInterval(interval);
		data.setStart(start);
		data.setEnd(end);

		JSONArray rawKline = JSONUtil.parseArray(klineJson.getByPath("result.list"));
		List<Object> filtered = new ArrayList<>();
		for (Object item : rawKline) {
			JSONArray row = JSONUtil.parseArray(item);
			long ts = Long.parseLong(row.getStr(0));
			if (ts >= start && ts <= end) {
				filtered.add(row);
			}
		}
		Collections.reverse(filtered); // Bybit默认倒序，反转后按时间升序

		data.setKline(filtered);
		return data;
	}
}
