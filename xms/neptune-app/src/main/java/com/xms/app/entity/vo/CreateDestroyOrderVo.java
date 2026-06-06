package com.xms.app.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销毁boomai订单
 */
@Data
public class CreateDestroyOrderVo {
	/**
	 * 销毁数量 以u为单位
	 */
	@NotNull
	private BigDecimal destroyAmount;

	/**
	 * 签名
	 */
	@ApiModelProperty(value = "签名")
	@NotBlank
	private String signature;

	/**
	 * 随机数不能为空
	 */
	@NotBlank(message = "随机数不能为空")
	private String randomNum;
}
