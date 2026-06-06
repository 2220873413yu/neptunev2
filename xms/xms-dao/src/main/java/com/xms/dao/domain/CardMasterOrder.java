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
import java.util.Date;

import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 购买记录对象 t_card_master_order
 *
 * @author xms
 * @date 2025-12-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_card_master_order")
public class CardMasterOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 主订单号（一次下单生成的批次编号，用于关联同一批购买的多张卡片） */
    @Excel(name = "主订单号",sort = 1, width = 30)
    private String masterOrderNo;
    /** 用户ID */
    @Excel(name = "用户ID",sort = 2)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /** 本次实际支付金额（USDT） */
    @Excel(name = "本次支付金额",sort = 3)
    private BigDecimal payAmount;
    /** 本次订单购买套餐基础算力 */
    @Excel(name = "本次订单购买套餐基础算力", sort = 4)
    @ApiModelProperty(value = "本次订单购买套餐基础算力")
    private BigDecimal computingPower;
	/** 本地订单购买额外赠送算力 */
    @Excel(name = "本地订单购买额外赠送算力",sort = 5)
    private BigDecimal extraComputingPower;
    /** 卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡 */
    @Excel(name = "卡片类型", sort = 6, dictType = "card_type")
    @ApiModelProperty(value = "卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡")
    private Integer cardType;

	/**
	 * 1:购买,2:升级
	 */
	@Excel(name = "来源类型", sort = 6, dictType = "card_order_type")
	private Integer sourceType;

	/**
	 * 升级前卡片类型 升级的时候填写(购买会空)
	 */
	private Integer beforeCardType;
    /** 购买数量 */
    @Excel(name = "购买数量",sort = 7)
    @ApiModelProperty(value = "购买数量")
    private Integer buyNum;
    /** 购买的时候快照 json */
    //@Excel(name = "购买的时候快照 json")
    @ApiModelProperty(value = "购买的时候快照 json")
    private String snapshotJson;

	/**
	 * 本次购买套餐的算力
	 */
	private BigDecimal packagePower;

	/**
	 * 业务状态 0:未处理,1:已经处理
	 */
	private Integer bizStatus;






	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String remark;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("masterOrderNo", getMasterOrderNo())
            .append("userId", getUserId())
            .append("payAmount", getPayAmount())
            .append("computingPower", getComputingPower())
            .append("cardType", getCardType())
            .append("buyNum", getBuyNum())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("snapshotJson", getSnapshotJson())
        .toString();
    }
}
