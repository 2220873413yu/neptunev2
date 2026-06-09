package com.xms.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ACP/H 价格快照。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcpHPriceSnapshot {
	/**
	 * ACP 单价U
	 */
	private BigDecimal acpPriceUsdt;

	/**
	 * H 单价U
	 */
	private BigDecimal hPriceUsdt;
}
