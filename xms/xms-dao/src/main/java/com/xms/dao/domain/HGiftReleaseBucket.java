package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xms.common.annotation.Excel;
import com.xms.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * H赠送释放桶对象 t_h_gift_release_bucket
 *
 * @author xms
 * @date 2026-06-07
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_h_gift_release_bucket")
public class HGiftReleaseBucket extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 释放桶编号 */
	@Excel(name = "释放桶编号", sort = 1, width = 30)
	@ApiModelProperty(value = "释放桶编号")
	private String bucketNo;

	/** 用户ID */
	@Excel(name = "用户ID", sort = 2)
	@ApiModelProperty(value = "用户ID")
	private Long userId;

	/** 用户账号/钱包地址快照 */
	@Excel(name = "用户账号", sort = 3, width = 40)
	@ApiModelProperty(value = "用户账号/钱包地址快照")
	private String account;

	/** 来源类型 1正常ACP入金 2后台手动拨 3旧系统入金 */
	@Excel(name = "来源类型", sort = 4, readConverterExp = "1=正常ACP入金,2=后台手动拨,3=旧系统入金")
	@ApiModelProperty(value = "来源类型 1正常ACP入金 2后台手动拨 3旧系统入金")
	private Integer sourceType;

	/** 来源订单号 */
	@Excel(name = "来源订单号", sort = 5, width = 30)
	@ApiModelProperty(value = "来源订单号")
	private String sourceOrderNo;

	/** 来源用户ID */
	@Excel(name = "来源用户ID", sort = 6)
	@ApiModelProperty(value = "来源用户ID")
	private Long sourceUserId;

	/** 赠送H总量 */
	@Excel(name = "赠送H总量", sort = 7)
	@ApiModelProperty(value = "赠送H总量")
	private BigDecimal totalAmount;

	/** 已释放H数量 */
	@Excel(name = "已释放H数量", sort = 8)
	@ApiModelProperty(value = "已释放H数量")
	private BigDecimal releasedAmount;

	/** 剩余待释放H数量 */
	@Excel(name = "剩余待释放H数量", sort = 9)
	@ApiModelProperty(value = "剩余待释放H数量")
	private BigDecimal remainingAmount;

	/** 每日释放H数量 */
	@Excel(name = "每日释放H数量", sort = 10)
	@ApiModelProperty(value = "每日释放H数量")
	private BigDecimal dailyReleaseAmount;

	/** 总释放天数 */
	@Excel(name = "总释放天数", sort = 11)
	@ApiModelProperty(value = "总释放天数")
	private Integer releaseDays;

	/** 已释放天数 */
	@Excel(name = "已释放天数", sort = 12)
	@ApiModelProperty(value = "已释放天数")
	private Integer releasedDays;

	/** 释放开始日期 yyyyMMdd */
	@Excel(name = "释放开始日期", sort = 13)
	@ApiModelProperty(value = "释放开始日期 yyyyMMdd")
	private Integer startDate;

	/** 最后释放日期 yyyyMMdd */
	@Excel(name = "最后释放日期", sort = 14)
	@ApiModelProperty(value = "最后释放日期 yyyyMMdd")
	private Integer lastReleaseDate;

	/** 状态 1释放中 2已完成 3冻结 */
	@Excel(name = "状态", sort = 15, readConverterExp = "1=释放中,2=已完成,3=冻结")
	@ApiModelProperty(value = "状态 1释放中 2已完成 3冻结")
	private Integer status;

	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("id", getId())
			.append("bucketNo", getBucketNo())
			.append("userId", getUserId())
			.append("account", getAccount())
			.append("sourceType", getSourceType())
			.append("sourceOrderNo", getSourceOrderNo())
			.append("sourceUserId", getSourceUserId())
			.append("totalAmount", getTotalAmount())
			.append("releasedAmount", getReleasedAmount())
			.append("remainingAmount", getRemainingAmount())
			.append("dailyReleaseAmount", getDailyReleaseAmount())
			.append("releaseDays", getReleaseDays())
			.append("releasedDays", getReleasedDays())
			.append("startDate", getStartDate())
			.append("lastReleaseDate", getLastReleaseDate())
			.append("status", getStatus())
			.append("remark", getRemark())
			.append("createTime", getCreateTime())
			.append("updateTime", getUpdateTime())
			.toString();
	}
}
