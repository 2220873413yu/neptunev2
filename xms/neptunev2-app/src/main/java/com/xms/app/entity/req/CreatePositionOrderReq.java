package com.xms.app.entity.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建购买贡献分订单
 */
@Data
public class CreatePositionOrderReq {
	/**
	 * 花费h代币余额
	 */
	@NotNull
	@Positive
	private BigDecimal amt;
}

