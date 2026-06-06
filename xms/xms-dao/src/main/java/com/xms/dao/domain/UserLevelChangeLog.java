package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

/**
 * 用户等级变动日志对象 t_user_level_change_log
 *
 * @author xms
 * @date 2025-06-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_level_change_log")
public class UserLevelChangeLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 原等级 */
    @Excel(name = "原等级")
    @ApiModelProperty(value = "原等级")
    private Long oldLevel;
    /** 新等级 */
    @Excel(name = "新等级")
    @ApiModelProperty(value = "新等级")
    private Long newLevel;
    /** 变动类型(1:升级,2:降级) */
    @Excel(name = "变动类型(1:升级,2:降级)")
    @ApiModelProperty(value = "变动类型(1:升级,2:降级)")
    private Long changeType;
    /** 相关订单号 */
    @Excel(name = "相关订单号")
    @ApiModelProperty(value = "相关订单号")
    private String orderNo;
    /** 奖励详情(JSON格式) */
    @Excel(name = "奖励详情(JSON格式)")
    @ApiModelProperty(value = "奖励详情(JSON格式)")
    private String rewardDetail;
    /** 总奖励金额 */
    @Excel(name = "总奖励金额")
    @ApiModelProperty(value = "总奖励金额")
    private BigDecimal totalReward;
    /** 变更前历史最高等级 */
    @Excel(name = "变更前历史最高等级")
    @ApiModelProperty(value = "变更前历史最高等级")
    private Long historyMaxLevel;
    /** 是否发放奖励(0:否,1:是) */
    @Excel(name = "是否发放奖励(0:否,1:是)")
    @ApiModelProperty(value = "是否发放奖励(0:否,1:是)")
    private Long hasReward;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("oldLevel", getOldLevel())
            .append("newLevel", getNewLevel())
            .append("changeType", getChangeType())
            .append("orderNo", getOrderNo())
            .append("rewardDetail", getRewardDetail())
            .append("totalReward", getTotalReward())
            .append("historyMaxLevel", getHistoryMaxLevel())
            .append("hasReward", getHasReward())
            .append("createTime", getCreateTime())
        .toString();
    }
}
