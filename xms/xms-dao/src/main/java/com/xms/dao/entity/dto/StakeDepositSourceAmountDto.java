package com.xms.dao.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 质押订单入金来源金额统计
 */
@Data
public class StakeDepositSourceAmountDto {

	/**
	 * 入金来源类型
	 */
	private Integer depositSourceType;

	/**
	 * 入金总金额
	 */
	private BigDecimal totalStakeAmount = BigDecimal.ZERO;
}
