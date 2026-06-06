package com.xms.dao.entity.bo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xms.common.annotation.Excel;
import com.xms.dao.entity.domain.UserMoneyLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @createDate: 2023/7/26 15:18
 */
@Data
public class UserMoneyLogBo{

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 更新时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


	@ApiModelProperty(value = "主键id")
	private Integer id;

	/**
	 * 用户UID
	 */
	private Long userId;

	/**
	 * 币种 原:coinType
	 */
	private Integer cte;

	/**
	 * 变动额度 原:changeBalance
	 */
	private BigDecimal cbe;

	/**
	 * 变动前余额 原:beforeBalance
	 */
	private BigDecimal bbe;

	/**
	 * 变动后余额 原:afterBalance
	 */
	private BigDecimal abe;

	/**
	 * 流水号
	 */
	private String serialCode;

	/**
	 * 来源订单
	 */
	private String sourceCode;

	/**
	 * 来源类型
	 */
	private Integer sourceType;

	private String remark;

	private Integer activeFlag;
	/**
	 * binlog对应唯一ID 必填
	 */
	private String gtId;
	/**
	 * 来源用户ID
	 */
	private Long sourceId;
}
