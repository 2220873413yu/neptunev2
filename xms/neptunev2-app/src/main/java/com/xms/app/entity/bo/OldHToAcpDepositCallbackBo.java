package com.xms.app.entity.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 旧系统H入金回调请求参数
 */
@Data
public class OldHToAcpDepositCallbackBo {

	/**
	 * 钱包地址
	 */
	@NotBlank(message = "address not null")
	private String address;

	/**
	 * orderNo
	 */
	@NotBlank(message = "orderNo not null")
	private String orderNo;

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
