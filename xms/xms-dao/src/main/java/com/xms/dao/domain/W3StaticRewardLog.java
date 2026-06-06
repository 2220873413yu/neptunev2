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
 * 全网静态分红日志对象 t_w3_static_reward_log
 *
 * @author xms
 * @date 2025-04-15
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_static_reward_log")
public class W3StaticRewardLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 等级 */
    @Excel(name = "等级")
    @ApiModelProperty(value = "等级")
    private Integer level;
    /** 等级分红比例 */
    @Excel(name = "等级分红比例")
    @ApiModelProperty(value = "等级分红比例")
    private BigDecimal levelRatio;
    /** 静态奖励 */
    @Excel(name = "静态奖励")
    @ApiModelProperty(value = "静态奖励")
    private BigDecimal staticReward;
    /** 分红数量 */
    @Excel(name = "分红数量")
    @ApiModelProperty(value = "分红数量")
    private BigDecimal rewardAmount;
    /** 分红人数 */
    @Excel(name = "分红人数")
    @ApiModelProperty(value = "分红人数")
    private Integer userCount;
    /** 批次号 */
    @Excel(name = "批次号")
    @ApiModelProperty(value = "批次号")
    private String orderNo;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("level", getLevel())
            .append("levelRatio", getLevelRatio())
            .append("staticReward", getStaticReward())
            .append("rewardAmount", getRewardAmount())
            .append("userCount", getUserCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("orderNo", getOrderNo())
        .toString();
    }
}
