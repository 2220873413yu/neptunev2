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
		data.setSn("hu");
		data.setLp(String.valueOf(tickerJson.getByPath("result.list.0.lastPrice")));
		return data;
	}

	public static class KlineResponse {
		/** 原字段 symbol，交易对标识，例如 HUSDT */
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
		/** 原字段 symbol，交易对标识，例如 HUSDT */
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
		data.setSn(normalizedSymbol);
		data.setIv(interval);
		data.setSt(start);
		data.setEt(end);

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

		data.setKl(filtered);
		return data;
	}
}
