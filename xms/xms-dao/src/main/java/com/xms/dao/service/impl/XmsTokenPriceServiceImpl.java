package com.xms.dao.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.RedisConstant;
import com.xms.common.domain.AcpHPriceSnapshot;
import com.xms.common.exception.ServiceException;
import com.xms.dao.service.XmsTokenPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * XMS 代币价格服务实现。
 *
 * <p>当前价格统一从 HTTP 接口读取，并写入 Redis 5 秒缓存。</p>
 */
@Slf4j
@Service
public class XmsTokenPriceServiceImpl implements XmsTokenPriceService {
	private static final String PRICE_FAIL_MESSAGE = "获取价格失败";
	private static final String PRICE_API_PATH = "/api/v1/price?tokenName=";
	private static final int PRICE_REQUEST_TIMEOUT = 3000;
	private static final long PRICE_CACHE_SECONDS = 5L;

	@Autowired
	private XmsRedis xmsRedis;

	@Value("${lq.baseUrl}")
	private String baseUrl;

	@Override
	public BigDecimal getAcpPriceUsdt() {
		return getTokenPrice(RedisConstant.XMS_TOKEN_PRICE_ACP_USDT, "acp");
	}

	@Override
	public BigDecimal getHPriceUsdt() {
		return getTokenPrice(RedisConstant.XMS_TOKEN_PRICE_H_USDT, "h");
	}

	@Override
	public AcpHPriceSnapshot getAcpHPriceSnapshot() {
		return AcpHPriceSnapshot.builder()
			.acpPriceUsdt(getAcpPriceUsdt())
			.hPriceUsdt(getHPriceUsdt())
			.build();
	}

	/**
	 * 获取代币价格，优先读取 Redis 缓存；缓存未命中时请求价格接口，成功后缓存 5 秒。
	 */
	private BigDecimal getTokenPrice(String cacheKey, String tokenName) {
		Object cacheValue = xmsRedis.get(cacheKey);
		if (cacheValue != null && StrUtil.isNotBlank(String.valueOf(cacheValue))) {
			return parsePrice(String.valueOf(cacheValue));
		}
		BigDecimal price = requestTokenPrice(buildPriceUrl(tokenName));
		xmsRedis.set(cacheKey, price.toPlainString(), PRICE_CACHE_SECONDS, TimeUnit.SECONDS);
		return price;
	}

	private String buildPriceUrl(String tokenName) {
		if (StrUtil.isBlank(baseUrl)) {
			throw new ServiceException(PRICE_FAIL_MESSAGE);
		}
		return StrUtil.removeSuffix(baseUrl, "/") + PRICE_API_PATH + tokenName;
	}

	private BigDecimal requestTokenPrice(String url) {
		HttpResponse response;
		try {
			response = HttpRequest.get(url)
				.timeout(PRICE_REQUEST_TIMEOUT)
				.execute();
		} catch (IORuntimeException ex) {
			log.error("获取代币价格接口超时或请求失败, url:{}", url, ex);
			throw new ServiceException(PRICE_FAIL_MESSAGE);
		}

		int status = response.getStatus();
		String body = response.body();
		if (status < 200 || status >= 300 || StrUtil.isBlank(body)) {
			log.error("获取代币价格接口响应异常, url:{}, status:{}, body:{}", url, status, body);
			throw new ServiceException(PRICE_FAIL_MESSAGE);
		}
		return parsePrice(body);
	}

	private BigDecimal parsePrice(String body) {
		try {
			String priceValue = StrUtil.trim(body);
			if (!isNumeric(priceValue)) {
				JSONObject jsonObject = JSONUtil.parseObj(priceValue);
				Object code = jsonObject.get("code");
				if (!"200".equals(StrUtil.trim(String.valueOf(code)))) {
					throw new ServiceException(PRICE_FAIL_MESSAGE);
				}
				Object price = jsonObject.get("price");
				if (price == null) {
					price = jsonObject.getByPath("data.price");
				}
				priceValue = price == null ? null : String.valueOf(price);
			}
			if (StrUtil.isBlank(priceValue)) {
				throw new ServiceException(PRICE_FAIL_MESSAGE);
			}
			BigDecimal price = new BigDecimal(priceValue.trim())
				.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
			if (price.compareTo(BigDecimal.ZERO) <= 0) {
				throw new ServiceException(PRICE_FAIL_MESSAGE);
			}
			return price;
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("解析代币价格失败, body:{}", body, ex);
			throw new ServiceException(PRICE_FAIL_MESSAGE);
		}
	}

	private boolean isNumeric(String value) {
		if (StrUtil.isBlank(value)) {
			return false;
		}
		try {
			new BigDecimal(value.trim());
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
