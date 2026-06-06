package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.xms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.math.BigDecimal;
import java.util.Date;

import com.xms.common.annotation.Excel;

/**
 * 基金订单对象 t_mining_package_order
 *
 * @author xms
 * @date 2025-08-07
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_mining_package_order")
public class MiningPackageOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 矿机套餐id */
    //@Excel(name = "矿机套餐id")
    @ApiModelProperty(value = "矿机套餐id")
    private Long miningPackageId;
    /** 订单号 */
    @Excel(name = "订单号",sort = 1,width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /** 用户id */
    @Excel(name = "用户ID",sort = 2)
    private Long userId;
    /** 购买金额(本金) */
    @Excel(name = "本金",sort = 3)
    private BigDecimal buyPrice;
    /** 矿机天数 */
    @Excel(name = "矿机天数",sort = 4)
    private Integer days;
    /** 剩余天数 */
    //@Excel(name = "剩余天数")
    @ApiModelProperty(value = "剩余天数")
    private Integer haveDays;
    /** 运行天数 */
    @Excel(name = "运行天数",sort = 5)
    @ApiModelProperty(value = "运行天数")
    private Integer runDays;

	/** 当前是第几天 */
    @Excel(name = "当前是第几天",sort = 5)
    @ApiModelProperty(value = "当前是第几天")
    private Integer currentRunDays;

    /** 矿机类型 0:活期,1:固定,2:体验式基金 */
    @Excel(name = "矿机类型",sort = 6,dictType = "t_w3_mining_package_type")
    @ApiModelProperty(value = "矿机类型 0:活期,1:固定,2:体验式基金")
    private Integer type;
    /** 日利率 */
    @Excel(name = "日利率",sort = 7)
    private BigDecimal dayRatio;

	/**
	 * 当天收益
	 */
	@Excel(name = "当天收益",sort = 7)
	private BigDecimal dayReward;

	/**
	 * 累计收益
	 */
    @Excel(name = "累计收益",sort = 8)
    private BigDecimal totalReward;

	/**
	 * 待释放收益(活期矿机才会有值)
	 */
	@Excel(name = "待释放收益",sort = 8)
	@TableField(exist = false)
	private BigDecimal pendingReward;

    /** 状态 0:释放中,1:已经达到最大倍数,2:已结束 */
    @Excel(name = "矿机状态",sort = 9,dictType = "t_mining_package_order_status")
	//@ApiModelProperty(value = "状态 0:释放中,1:已经达到最大倍数,2:已结束")
    private Integer status;
    /** 业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务 */
    //@Excel(name = "业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务")
	//@ApiModelProperty(value = "业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务")
    private Integer bizStatus;
    /** 每日违约金递减率(如0.5%) */
    @Excel(name = "每日违约金递减率(如0.5%)",sort = 10)
    //@ApiModelProperty(value = "每日违约金递减率(如0.5%)")
    private BigDecimal dailyPenaltyReduction;
    /** 违约金比例(如20%) */
    @Excel(name = "违约金比例(如20%)",sort = 11)
    //@ApiModelProperty(value = "违约金比例(如20%)")
    private BigDecimal penaltyRate;
	/** 最低违约金比例 */
	@Excel(name = "最低违约金比例",sort = 12)
	//@ApiModelProperty(value = "最低违约金比例")
	private BigDecimal minPenaltyRate;
    /** 是否退本 0:否,1:是 */
    //@Excel(name = "是否退本 0:否,1:是")
    @ApiModelProperty(value = "是否退本 0:否,1:是")
    private Integer principalReturned;
	/** 退本业务状态处理 0:未退本,1:退本了处理中,2:等待退本(活期是n+1时间的),3:退本成功 */
	@Excel(name = "退本状态",sort = 13,dictType = "t_mining_package_order_returned_biz_status")
    private Integer returnedBizStatus;

	/** 预计退本时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "预计退本时间", width = 30,sort = 98, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date returnedTime;

	/** 最大运行日期 格式为:年月日*/
	private Long maxRunDate;

	/**
	 * 订单来源 0:购买,1:后台拨付
	 */
	private Integer sourceType;


	/**
	 * 待领取金额
	 */
	private BigDecimal awaitingAmount;

	/**
	 * 领取1U消耗多少积分
	 */
	private BigDecimal pointsPerUsdt;

	/**
	 * 用户账号
	 */
	@TableField(exist = false)
	@Excel(name = "用户账号",sort = 1)
	private String userAccount;


	/** 创建者 */
	@TableField(exist = false)
	@JsonIgnore
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	@JsonIgnore
	private String updateBy;
	@JsonIgnore
	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("miningPackageId", getMiningPackageId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("buyPrice", getBuyPrice())
            .append("days", getDays())
            .append("haveDays", getHaveDays())
            .append("runDays", getRunDays())
            .append("type", getType())
            .append("dayRatio", getDayRatio())
            .append("totalReward", getTotalReward())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("bizStatus", getBizStatus())
            .append("dailyPenaltyReduction", getDailyPenaltyReduction())
            .append("penaltyRate", getPenaltyRate())
            .append("principalReturned", getPrincipalReturned())
        .toString();
    }
}
