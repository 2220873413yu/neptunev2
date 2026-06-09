package com.xms.app.entity.vo;

import com.xms.common.annotation.ValidDiyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 转账vo参数
 *
 * @author xms
 * @date 2023/07/07
 */
@Data
public class TransferOrderVo {
	/**
	 * 转账金额,转账金额最多保留2位小数点
	 */
	@NotNull
	@Positive
	private BigDecimal amount;

	/**
	 * 转账币种 1:USDT,2:SMA
	 */
	@NotNull
	@ValidDiyStatus(values = {1,2}, message = "bizType error")
	private Integer coinType;

	/**
	 * 收款人用户账号
	 */
	@NotBlank
	private String toUserAccount;

	/**
	 * 谷歌验证码
	 */
	@NotBlank
	private String googleCode;

}
