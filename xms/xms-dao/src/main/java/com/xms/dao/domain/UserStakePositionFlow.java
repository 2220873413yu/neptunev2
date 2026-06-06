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
 * 用户持仓变动流水对象 t_user_stake_position_flow
 *
 * @author xms
 * @date 2026-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_stake_position_flow")
public class UserStakePositionFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 流水号(唯一) */
    @Excel(name = "流水号(唯一)")
    @ApiModelProperty(value = "流水号(唯一)")
    private String flowNo;
    /** 用户id */
    @Excel(name = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 变动类型(废弃) */
    @Excel(name = "变动类型:STAKE/UNSTAKE/ADJUST/FREEZE/UNFREEZE")
    @ApiModelProperty(value = "变动类型:STAKE/UNSTAKE/ADJUST/FREEZE/UNFREEZE")
    private String changeType;
    /** 关联业务单号(如质押订单号) */
    @Excel(name = "关联业务单号(如质押订单号)")
    @ApiModelProperty(value = "关联业务单号(如质押订单号)")
    private String bizOrderNo;
    /** 变动金额(正加负减) */
    @Excel(name = "变动金额(正加负减)")
    @ApiModelProperty(value = "变动金额(正加负减)")
    private BigDecimal changeAmount;
    /** 变动前总质押 */
    @Excel(name = "变动前总质押")
    @ApiModelProperty(value = "变动前总质押")
    private BigDecimal beforeTotalStake;
    /** 变动后总质押 */
    @Excel(name = "变动后总质押")
    @ApiModelProperty(value = "变动后总质押")
    private BigDecimal afterTotalStake;
    /** 轮次表id */
    @Excel(name = "轮次表id")
    @ApiModelProperty(value = "轮次表id")
    private Long stakeRoundId;


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
            .append("flowNo", getFlowNo())
            .append("userId", getUserId())
            .append("changeType", getChangeType())
            .append("bizOrderNo", getBizOrderNo())
            .append("changeAmount", getChangeAmount())
            .append("beforeTotalStake", getBeforeTotalStake())
            .append("afterTotalStake", getAfterTotalStake())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("stakeRoundId", getStakeRoundId())
        .toString();
    }
}
