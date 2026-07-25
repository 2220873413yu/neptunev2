package com.xms.dao.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户质押订单按入金来源聚合的业绩数据。
 */
@Data
public class UserStakePerformanceSourceStatDto {

	/** 用户ID */
	private Long userId;

	/** 入金来源类型：1正常ACP，3旧系统H换ACP，4用户H余额换ACP */
	private Integer depositSourceType;

	/** 成功订单ACP入金数量合计 */
	private BigDecimal totalStakeAmount = BigDecimal.ZERO;
}
