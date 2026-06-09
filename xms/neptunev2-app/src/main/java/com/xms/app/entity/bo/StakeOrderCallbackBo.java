package com.xms.app.entity.bo;

import com.xms.common.annotation.ValidDiyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 质押订单回调接口
 */
@Data
public class StakeOrderCallbackBo {

	/**
	 * 钱包地址
	 */
	@NotBlank(message = "address not null")
	private String address;

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
