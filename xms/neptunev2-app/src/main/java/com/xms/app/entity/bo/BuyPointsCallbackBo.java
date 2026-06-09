package com.xms.app.entity.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购买积分回调
 */
@Data
public class BuyPointsCallbackBo {
	/**
	 * 钱包地址
	 */
	@NotBlank(message = "address not null")
	private String address;

	/**
	 * 订单号
	 */
	@NotBlank(message = "orderNo not null")
	private String orderNo;

	/**
	 * hash
	 */
	@NotBlank(message = "hash not null")
	private String hash;

	/**
	 * h代币数量
	 */
	@NotNull(message = "amount not null")
	private BigDecimal amount;

	/**
	 * 签名
	 */
	@NotBlank(message = "sign not null")
	private String sign;
}
