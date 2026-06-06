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
 * 轮次爆仓判定与执行日志对象 t_stake_round_liquidation_log
 *
 * @author xms
 * @date 2026-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_stake_round_liquidation_log")
public class StakeRoundLiquidationLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单号 */
    @Excel(name = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /** 轮次表id */
    @Excel(name = "轮次表id")
    @ApiModelProperty(value = "轮次表id")
    private Long stakeRoundId;
    /** 动作类型:0判定检查,1触发爆仓,2重复触发拦截 */
    @Excel(name = "动作类型:0判定检查,1触发爆仓,2重复触发拦截")
    @ApiModelProperty(value = "动作类型:0判定检查,1触发爆仓,2重复触发拦截")
    private Integer actionType;
    /** 判定左值(玩家累计参与总量) */
    @Excel(name = "判定左值(玩家累计参与总量)")
    @ApiModelProperty(value = "判定左值(玩家累计参与总量)")
    private BigDecimal checkLeftValue;
    /** 判定右值(补贴+提取收益) */
    @Excel(name = "判定右值(补贴+提取收益)")
    @ApiModelProperty(value = "判定右值(补贴+提取收益)")
    private BigDecimal checkRightValue;
    /** 是否触发:0否,1是 */
    @Excel(name = "是否触发:0否,1是")
    @ApiModelProperty(value = "是否触发:0否,1是")
    private Integer isTriggered;
    /** 判定表达式快照 */
    @Excel(name = "判定表达式快照")
    @ApiModelProperty(value = "判定表达式快照")
    private String ruleExpr;
    /** 操作人/任务标识 */
    @Excel(name = "操作人/任务标识")
    @ApiModelProperty(value = "操作人/任务标识")
    private String operator;


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
            .append("orderNo", getOrderNo())
            .append("stakeRoundId", getStakeRoundId())
            .append("actionType", getActionType())
            .append("checkLeftValue", getCheckLeftValue())
            .append("checkRightValue", getCheckRightValue())
            .append("isTriggered", getIsTriggered())
            .append("ruleExpr", getRuleExpr())
            .append("operator", getOperator())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
        .toString();
    }
}
