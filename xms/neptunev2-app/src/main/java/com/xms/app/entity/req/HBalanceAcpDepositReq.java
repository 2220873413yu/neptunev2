package com.xms.app.entity.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * H余额换ACP入金请求
 */
@Data
public class HBalanceAcpDepositReq {
	/**
	 * 扣减的H代币数量
	 */
	@NotNull
	@Positive
	private BigDecimal amount;

	@ApiModelProperty(value = "签名")
	@NotBlank
	private String sig;

	/**
	 * 随机数不能为空
	 */
	@NotBlank(message = "随机数不能为空")
	private String rdn;
}
