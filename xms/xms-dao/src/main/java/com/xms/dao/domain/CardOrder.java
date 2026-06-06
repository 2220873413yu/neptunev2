package com.xms.dao.domain;

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
import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 卡片订单对象 t_card_order
 *
 * @author xms
 * @date 2025-12-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_card_order")
public class CardOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单号 */
    @Excel(name = "订单号", sort = 1, width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;

	/**
	 * 卡片ID
	 */
	@Excel(name = "卡片ID", sort = 2, width = 30)
	private String cardInstanceId;
	/**
	 * 主订单号（一次下单生成的批次编号，用于关联同一批购买的多张卡片）
	 */
    @Excel(name = "主订单号", sort = 3, width = 30)
    @ApiModelProperty(value = "主订单号")
    private String masterOrderNo;
    /** 用户ID */
    @Excel(name = "用户ID", sort = 4)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /** 订单类型：1-购买新卡，2-补差价升级 */
    @Excel(name = "来源类型", sort = 5, dictType = "card_order_type")
    @ApiModelProperty(value = "订单类型：1-购买新卡，2-补差价升级")
    private Integer orderType;
    /** 价格 */
    @Excel(name = "支付金额", sort = 6)
    private BigDecimal payAmount;
    /** 购买套餐的时候套餐给了多少套餐算力 */
    @Excel(name = "算力", sort = 7)
    @ApiModelProperty(value = "算力")
    private BigDecimal computingPower;

	/**
	 * 购买卡片的时候额外赠送的算力
	 */
	@Excel(name = "购买卡片的时候额外赠送的算力", sort = 8)
	private BigDecimal extraComputingPower;

	/**
	 * 本卡订单总共获得了多少算力(套餐+额外赠送的)
	 */
	@Excel(name = "本卡片赠送了多少算力", sort = 9)
	private BigDecimal currentComputingPower;

    /** 卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡 */
    @Excel(name = "卡片等级", sort = 9, dictType = "card_type")
    @ApiModelProperty(value = "卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡")
    private Integer cardType;
    /** 购买的时候快照 json(废弃) */
    private String snapshotJson;

	/**
	 * 创建时间 格式例如:yyyymmdd
	 */
	private Integer createDate;

	/**
	 * 卡片状态 0:正常,1:已升级/冻结
	 */
	@Excel(name = "卡片状态", sort = 9, dictType = "card_status")
	private Integer status;

	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;

	@TableField(exist = false)
	private String remark;

	/**
	 * 升级的时候补贴的算力
	 */
	@TableField(exist = false)
	private BigDecimal newPower = BigDecimal.ZERO;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("orderType", getOrderType())
            .append("payAmount", getPayAmount())
            .append("computingPower", getComputingPower())
            .append("cardType", getCardType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
