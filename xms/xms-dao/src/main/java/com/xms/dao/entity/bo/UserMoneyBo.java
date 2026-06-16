package com.xms.dao.entity.bo;

import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @createDate: 2023/7/26 15:18
 */
@Data
public class UserMoneyBo{
	/**
	 * 节点收益
	 */
	private BigDecimal validNum1;

	/**
	 * 静态
	 */
	private BigDecimal validNum2;

	/**
	 * 动态
	 */
	private BigDecimal validNum3;

	/**
	 * 财富
	 */
	private BigDecimal validNum4;

	/**
	 * 保险仓(魔盒收益/手续费)
	 */
	private BigDecimal validNum5;

	/**
	 * 工作室收益
	 */
	private BigDecimal validNum6;

	/**
	 * 贡献分
	 */
	private BigDecimal validNum7;

	/**
	 * 今日总动态收益
	 */
	private BigDecimal validNum8;

	/**
	 * H代币
	 */
	private BigDecimal validNum9;
}
