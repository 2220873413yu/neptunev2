package com.xms.app.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserWealthVaultDto {
	/** 1段 */
	private BigDecimal seg1Amount;
	/** 2段 */
	private BigDecimal seg2Amount;
	/** 3段 */
	private BigDecimal seg3Amount;
	/** 4段 */
	private BigDecimal seg4Amount;
	/** 5段 */
	private BigDecimal seg5Amount;

	/**
	 * 1段解锁价格
	 */
	private BigDecimal seg1UnlockPrice;
	/**
	 * 2段解锁价格
	 */
	private BigDecimal seg2UnlockPrice;
	/**
	 * 3段解锁价格
	 */
	private BigDecimal seg3UnlockPrice;
	/**
	 * 4段解锁价格
	 */
	private BigDecimal seg4UnlockPrice;
	/**
	 * 5段解锁价格
	 */
	private BigDecimal seg5UnlockPrice;
}
