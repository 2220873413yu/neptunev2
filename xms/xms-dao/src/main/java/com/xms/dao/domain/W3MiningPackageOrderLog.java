package com.xms.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.math.BigDecimal;
import java.util.Date;

import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 矿机订单余额宝变更记录对象 t_w3_mining_package_order_log
 *
 * @author xms
 * @date 2025-04-14
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_mining_package_order_log")
public class W3MiningPackageOrderLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单ID */
    @Excel(name = "订单ID")
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    /** 用户ID */
    @Excel(name = "用户ID")
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /** 变更前数量 */
    @Excel(name = "变更前数量")
    @ApiModelProperty(value = "变更前数量")
    private BigDecimal beforeAmount;
    /** 变更数量 */
    @Excel(name = "变更数量")
    @ApiModelProperty(value = "变更数量")
    private BigDecimal changeAmount;
    /** 变更后数量 */
    @Excel(name = "变更后数量")
    @ApiModelProperty(value = "变更后数量")
    private BigDecimal afterAmount;

	@TableField(exist = false)
	private String remark;
	/** 创建者 */
	@JsonIgnore
	@TableField(exist = false)
	private String createBy;

	/** 更新者 */
	@JsonIgnore
	@TableField(exist = false)
	private String updateBy;
	@JsonIgnore
	@TableField(exist = false)
	private Integer deleted;
	@JsonIgnore
	@TableField(exist = false)
	private Date updateTime;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("beforeAmount", getBeforeAmount())
            .append("changeAmount", getChangeAmount())
            .append("afterAmount", getAfterAmount())
            .append("createTime", getCreateTime())
        .toString();
    }
}
