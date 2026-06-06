package com.xms.app.entity.resp;

import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 我的收益页面
 */
@Data
public class MyStakeIncomeResp {
	/**
	 * 日利率
	 */
	private BigDecimal drt =BigDecimal.ZERO;

	/**
	 * 当前质押总金额
	 */
	private BigDecimal tsa =BigDecimal.ZERO;

	/**
	 * 今日静态收益
	 */
	private BigDecimal tsr = BigDecimal.ZERO;

	/**
	 * 连续未提取收益达到N天触发增长
	 */
	private Integer gcd = 0;

	/** 连续未提取收益天数 */
	private Integer cnw = 0;

	/**
	 * 累计提现静态
	 */
	private BigDecimal tw2;
}
