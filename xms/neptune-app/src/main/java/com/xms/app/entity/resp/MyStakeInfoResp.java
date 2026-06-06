package com.xms.app.entity.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 我的质押信息
 */
@Data
public class MyStakeInfoResp {
	/**
	 * 日利率
	 */
	private BigDecimal drt;

	/**
	 * 当前质押总金额
	 */
	private BigDecimal tsa =BigDecimal.ZERO;
}
