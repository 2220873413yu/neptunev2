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
 * w3矿机订单对象 t_w3_mining_package_order
 *
 * @author xms
 * @date 2025-04-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_mining_package_order")
public class W3MiningPackageOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
	/** 用户id */
	@Excel(name = "用户UID",sort = 1)
	private Long userId;
    /** 矿机套餐id */
    private Long miningPackageId;
    /** 订单号 */
    @Excel(name = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /** 总天数 */
    @Excel(name = "套餐天数",sort = 3)
    private Integer days;
	/** 剩余天数 */
	@Excel(name = "剩余天数",sort = 2)
	private Integer haveDays;

    /** 矿机类型 0:活期,1:固定 */
    @Excel(name = "矿机类型",sort = 4,readConverterExp="0=活期,1=定期")
    private Integer type;

	/**
	 * 状态 0:释放中,1:已经达到最大倍数,2:已结束
	 */
	@Excel(name = "状态",sort = 5, dictType = "t_mining_package_order_status")
	private Integer status;

	/**
	 * 业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务
	 */
	private Integer bizStatus;

    /** 支付usdt数量 */
    @Excel(name = "支付USDT",sort = 6)
    private BigDecimal payValidNum1;
    /** 支付FTN数量 */
    @Excel(name = "支付FTN",sort = 7)
    private BigDecimal payValidNum2;
    /** 支付fsn数量 */
    @Excel(name = "支付FSN",sort = 8)
    @ApiModelProperty(value = "支付fsn数量")
    private BigDecimal payValidNum3;
    /** 价值u数量 */
    @Excel(name = "质押业绩",sort = 9)
    @ApiModelProperty(value = "价值u数量")
    private BigDecimal usdtValue;

	/** 价值fsn数量 */
    @Excel(name = "质押本金",sort = 10)
    private BigDecimal fsnValue;

	/** 退本(可领取金额)*/
    @Excel(name = "可领取退本金额" ,sort = 11)
    private BigDecimal haveFsnValue;
    /** 日利率 */
    @Excel(name = "日利率%",sort = 12)
    @ApiModelProperty(value = "日利率")
    private BigDecimal dayRatio;

    /** 日利率(历史记录) */
    //@Excel(name = "日利率(历史记录)")
    @ApiModelProperty(value = "日利率(历史记录)")
    private BigDecimal parentDayRatio;

	/** 收益倍数 */
    @Excel(name = "收益倍数",sort = 13)
    private BigDecimal multipliedValue;
    /** 收益数量(等值的fsn数量*倍数) */
    @Excel(name = "奖励产出数量",sort = 14)
    private BigDecimal fsnMultipliedValue;

	/** 剩余产出动静态奖励 */
    @Excel(name = "剩余产出数量",sort = 15)
    private BigDecimal haveFsnMultipliedValue;
	/**
	 * 总共收益(活期矿机统计使用)
	 */
	private BigDecimal totalReward;

    /** 余额宝(如果没有领取每日收益,会转入到余额宝) 单位是fsn */
    @Excel(name = "余额宝",sort = 14)
    private BigDecimal validNum3;


	/**
	 * ftn价格(下单的时候价格)
	 */
	private BigDecimal lastFtnPrice;

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
            .append("days", getDays())
            .append("type", getType())
            .append("payValidNum1", getPayValidNum1())
            .append("payValidNum2", getPayValidNum2())
            .append("payValidNum3", getPayValidNum3())
            .append("usdtValue", getUsdtValue())
            .append("fsnValue", getFsnValue())
            .append("dayRatio", getDayRatio())
            .append("parentDayRatio", getParentDayRatio())
            .append("multipliedValue", getMultipliedValue())
            .append("fsnMultipliedValue", getFsnMultipliedValue())
            .append("haveFsnMultipliedValue", getHaveFsnMultipliedValue())
            .append("validNum3", getValidNum3())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .append("remark", getRemark())
        .toString();
    }
}
