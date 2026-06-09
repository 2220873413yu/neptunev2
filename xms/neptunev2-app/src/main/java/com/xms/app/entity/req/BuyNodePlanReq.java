package com.xms.app.entity.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 认购节点配置请求参数
 *
 * @author xms
 * @date 2026-01-16
 */
@Data
public class BuyNodePlanReq {

	/**
	 * 钱包地址
	 */
	@NotBlank
	private String adr;

	/**
	 * 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点
	 */
	@NotNull
	private Integer nll;

	/**
	 * 钱包余额
	 */
	@NotNull
	private BigDecimal amt;
}
