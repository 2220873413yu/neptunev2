package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.Date;

/**
 * 分发记录对象 t_wallet_transfer_order
 *
 * @author xms
 * @date 2025-06-22
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_wallet_transfer_order")
public class WalletTransferOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单号 */
    @Excel(name = "订单号",sort = 1,width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /** 用户id */
    @Excel(name = "用户ID",sort = 2)
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 分发类型 1:TRC->BSC,2:BSC->TRC */
    @Excel(name = "分发类型",dictType = "t_wallet_transfer_order_biz_type",sort = 3)
    @ApiModelProperty(value = "分发类型 1:TRC->BSC,2:BSC->TRC")
    private Integer bizType;
    /** 跨链分发金额 */
    @Excel(name = "跨链分发金额",sort = 4)
    @ApiModelProperty(value = "跨链分发金额")
    private BigDecimal amount;
    /** 目标钱包地址1 */
    @Excel(name = "目标钱包地址1",sort = 6)
    @ApiModelProperty(value = "目标钱包地址1")
    private String targetAddress1;
    /** 目标钱包地址2 */
    @Excel(name = "目标钱包地址2",sort = 6)
    @ApiModelProperty(value = "目标钱包地址2")
    private String targetAddress2;
    /** 分发后得到的利息 */
    @Excel(name = "分发后得到的利息",sort = 5)
    @ApiModelProperty(value = "分发后得到的利息")
    private BigDecimal interest;
    /** 获得利息比例 */
    @Excel(name = "获得利息比例",sort = 7)
    @ApiModelProperty(value = "获得利息比例")
    private BigDecimal interestRatio;
    /** 格式为 20250622 */
    //@Excel(name = "格式为 20250622")
    @ApiModelProperty(value = "格式为 20250622")
    private Integer date;
    /** 结束时间,获得收益的时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间,获得收益的时间", width = 8, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /** 状态 0:未结算,1:处理中,2:已结算 */
    @Excel(name = "订单状态",sort = 9,dictType = "t_wallet_transfer_order_status")
    @ApiModelProperty(value = "状态 0:未结算,1:处理中,2:已结算")
    private Integer status;

	/** 创建者 */
	@TableField(exist = false)
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("bizType", getBizType())
            .append("amount", getAmount())
            .append("targetAddress1", getTargetAddress1())
            .append("targetAddress2", getTargetAddress2())
            .append("interest", getInterest())
            .append("interestRatio", getInterestRatio())
            .append("date", getDate())
            .append("endTime", getEndTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .append("remark", getRemark())
            .append("status", getStatus())
        .toString();
    }
}
