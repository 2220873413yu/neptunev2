package com.xms.common.utils;

import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.ConstantSys;
import com.xms.common.domain.AcpHPriceSnapshot;
import com.xms.common.exception.ServiceException;
import com.xms.common.utils.spring.SpringUtils;

import java.math.BigDecimal;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * XMS 代币价格读取工具。
 *
 * <p>当前价格从系统参数读取，后续如果改为 HTTP 行情接口调用，只替换本工具类内部实现。</p>
 */
public class XmsTokenPriceUtils {
	private static final String SYS_PARA_SERVICE_BEAN_NAME = "sysParaServiceImpl";

	private XmsTokenPriceUtils() {
	}

	/**
	 * 获取 ACP 单价U。
	 *
	 * @return ACP 单价U
	 */
	public static BigDecimal getAcpPriceUsdt() {
		return getSysParaAmount(ConstantSys.biz_acp_price_usdt);
	}

	/**
	 * 获取 H 单价U。
	 *
	 * @return H 单价U
	 */
	public static BigDecimal getHPriceUsdt() {
		return getSysParaAmount(ConstantSys.biz_h_price_usdt);
	}

	/**
	 * 获取 ACP/H 价格快照。
	 *
	 * @return ACP/H 价格快照
	 */
	public static AcpHPriceSnapshot getAcpHPriceSnapshot() {
		return AcpHPriceSnapshot.builder()
			.acpPriceUsdt(getAcpPriceUsdt())
			.hPriceUsdt(getHPriceUsdt())
			.build();
	}

	private static BigDecimal getSysParaAmount(String code) {
		String value = getSysParaValue(code);
		return new BigDecimal(value).setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
	}

	private static String getSysParaValue(String code) {
		Object sysParaService = SpringUtils.getBean(SYS_PARA_SERVICE_BEAN_NAME);
		try {
			Method method = sysParaService.getClass().getMethod("getValue", String.class);
			return (String) method.invoke(sysParaService, code);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new ServiceException("获取系统参数失败：" + code);
		}
	}
}
