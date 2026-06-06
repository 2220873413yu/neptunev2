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
 * 卡片升级日志对象 t_card_upgrade_log
 *
 * @author xms
 * @date 2025-12-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_card_upgrade_log")
public class CardUpgradeLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 流水类型 1-购买 2-升级 */
    @Excel(name = "流水类型",sort = 1 ,dictType = "t_card_upgrade_log_flow_type")
    @ApiModelProperty(value = "流水类型 1-购买 2-升级")
    private Integer flowType;

	/** 卡片唯一标识（可选） */
	@Excel(name = "卡片唯一标识", sort=2, width = 30)
	private String cardSerialNo;
	/** 主订单号 */
	@Excel(name = "主订单号",sort=3, width = 30)
	@ApiModelProperty(value = "主订单号")
	private String masterOrderNo;

    /** 对应的卡片订单号 */
    @Excel(name = "对应的卡片订单号",sort = 4, width = 30)
    @ApiModelProperty(value = "对应的卡片订单号")
    private String cardOrderNo;

    /** 子订单号（冗余） */
    //@Excel(name = "子订单号", readConverterExp = "冗=余")
    private String orderNo;
    /** 用户ID */
    @Excel(name = "用户ID",sort = 5)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /** 变更前卡片类型（升级时填，购买可为空） */
    @Excel(name = "变更前卡片类型", sort = 6, dictType = "card_type")
    private Integer fromCardType;

    /** 变更前价格（USDT） */
    @Excel(name = "变更前价格", sort = 7)
    private BigDecimal fromPrice;
    /** 变更前算力 */
    @Excel(name = "变更前算力", sort = 8)
    @ApiModelProperty(value = "变更前算力")
    private BigDecimal fromPower;
    /** 变更前快照（购买=空，升级=原卡详情） */
    //@Excel(name = "变更前快照", readConverterExp = "购=买=空，升级=原卡详情")
    private String fromSnapshot;
    /** 变更后卡片类型 */
    @Excel(name = "变更后卡片类型", sort = 9, dictType = "card_type")
    @ApiModelProperty(value = "变更后卡片类型")
    private Integer toCardType;
    /** 变更后价格 */
    @Excel(name = "变更后价格", sort = 10)
    @ApiModelProperty(value = "变更后价格")
    private BigDecimal toPrice;
    /** 变更后算力 */
    @Excel(name = "变更后算力", sort = 11)
    @ApiModelProperty(value = "变更后算力")
    private BigDecimal toPower;
    /** 本次实际支付金额（购买=总价，升级=补差价） */
    @Excel(name = "本次实际支付金额", sort = 12)
    private BigDecimal amountDelta;
    /** 本次新增算力(购买的+补贴收益) */
    @Excel(name = "本次新增算力",sort = 13)
    private BigDecimal powerDelta;

	@TableField(exist = false)
	private Date updateTime;
	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("flowType", getFlowType())
            .append("cardOrderNo", getCardOrderNo())
            .append("cardSerialNo", getCardSerialNo())
            .append("masterOrderNo", getMasterOrderNo())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("fromCardType", getFromCardType())
            .append("fromPrice", getFromPrice())
            .append("fromPower", getFromPower())
            .append("fromSnapshot", getFromSnapshot())
            .append("toCardType", getToCardType())
            .append("toPrice", getToPrice())
            .append("toPower", getToPower())
            .append("amountDelta", getAmountDelta())
            .append("powerDelta", getPowerDelta())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
        .toString();
    }
}
