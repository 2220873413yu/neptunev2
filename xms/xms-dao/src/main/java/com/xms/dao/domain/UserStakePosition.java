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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 用户质押持仓汇总对象 t_user_stake_position
 *
 * @author xms
 * @date 2026-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_stake_position")
public class UserStakePosition extends BaseEntity {
    private static final long serialVersionUID = 1L;



    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户ID",sort = 1)
    @ApiModelProperty(value = "用户id")
    private Long userId;
	/** 轮次表id */
	@Excel(name = "轮次表编号",sort = 2)
	@ApiModelProperty(value = "轮次表id")
	private Long stakeRoundId;
	/** 订单号 */
	@Excel(name = "订单号",sort = 2,width = 30)
    private String orderNo;
    /** 当前总质押金额 */
    @Excel(name = "总质押金额",sort = 3)
    @ApiModelProperty(value = "当前总质押金额")
    private BigDecimal totalStakeAmount;
    /** 当前日收益率(如1%=1) */
    @Excel(name = "日收益率",sort = 4)
    @ApiModelProperty(value = "当前日收益率(如1%=1)")
    private BigDecimal currentDayRate;
    /** 连续未提取收益天数 */
    @Excel(name = "连续未提取收益天数",sort = 5)
    @ApiModelProperty(value = "连续未提取收益天数")
    private Integer continuousNoWithdrawDays;
    /** 累计收益(静态) */
    @Excel(name = "累计收益",sort = 6)
    @ApiModelProperty(value = "累计收益")
    private BigDecimal totalReward;
	/**
	 * 累计提现(静态)
	 */
	@Excel(name = "累计提现(静态)",sort = 6)
    private BigDecimal totalWithdrawalStatic;

	/**
	 * 累计提现(动态)
	 */
	@Excel(name = "累计提现(动态)",sort = 6)
	private BigDecimal totalWithdrawalDynamic;

	/**
	 * 累计提现(工作室补贴)
	 */
	@Excel(name = "累计提现(工作室补贴)",sort = 6)
	private BigDecimal totalWithdrawalStudioSubsidy;

	/**
	 * 累计提现中，已经用于触发“降收益率”的金额（已消耗提现量）
	 */
	@Excel(name = "累计提现触发提现量",sort = 6)
	private BigDecimal rateDeductedWithdrawAmount;

    /** 当日收益(可选缓存) */
    @Excel(name = "当日收益",sort = 7)
    @ApiModelProperty(value = "当日收益(可选缓存)")
    private BigDecimal todayReward;
    /** 最近结算日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    //@Excel(name = "最近结算日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastSettleDate;
    /** 状态:1正常,2:爆仓 */
    @Excel(name = "订单状态",sort = 8,dictType = "stake_position_status")
    @ApiModelProperty(value = "状态:1正常,2:爆仓")
    private Integer status;


	/** 动态收益 */
	@Excel(name = "动态收益",sort = 9)
	private BigDecimal dynamicReward;

	/** 工作室补贴 */
	@Excel(name = "工作室补贴",sort = 10)
	private BigDecimal studioSubsidy;

	/**
	 * 个人亏损额
	 */
	@Excel(name = "个人亏损额",sort = 10)
	private BigDecimal personalLossAmount;

	/**
	 * 剩余可赔付
	 */
	@Excel(name = "剩余可赔付",sort = 11)
	private BigDecimal remainingCompensationLimit;

	/**
	 * 可赔付金额
	 */
	@Excel(name = "可赔付金额",sort = 11)
	private BigDecimal allCompensationLimit;

	/**
	 * 保险仓赔付资格状态 0:无资格,1:有资格
	 */
	@Excel(name = "保险仓赔付是否有资格",sort = 12,dictType = "t_user_info_is_valid")
	private Integer insuranceQualifyStatus;

	/**
	 * 领取赔付是否有资格 0:无资格,1:有资格
	 */
	@Excel(name = "领取赔付是否有资格",sort = 12,dictType = "t_user_info_is_valid")
	private Integer insuranceCompensationQualifyStatus;

	/**
	 * 钱包地址
	 */
	@TableField(exist = false)
	@Excel(name = "钱包地址",sort = 1)
	private String userAccount;

	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private String remark;
	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("totalStakeAmount", getTotalStakeAmount())
            .append("currentDayRate", getCurrentDayRate())
            .append("continuousNoWithdrawDays", getContinuousNoWithdrawDays())
            .append("totalReward", getTotalReward())
            .append("todayReward", getTodayReward())
            .append("lastSettleDate", getLastSettleDate())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("stakeRoundId", getStakeRoundId())
        .toString();
    }
}
