package com.xms.dao.service;

import com.xms.common.domain.AcpHPriceSnapshot;

import java.math.BigDecimal;

/**
 * XMS 代币价格服务。
 */
public interface XmsTokenPriceService {
	/**
	 * 获取 ACP 单价U。
	 *
	 * @return ACP 单价U
	 */
	BigDecimal getAcpPriceUsdt();

	/**
	 * 获取 H 单价U。
	 *
	 * @return H 单价U
	 */
	BigDecimal getHPriceUsdt();

	/**
	 * 获取 ACP/H 价格快照。
	 *
	 * @return ACP/H 价格快照
	 */
	AcpHPriceSnapshot getAcpHPriceSnapshot();
}
