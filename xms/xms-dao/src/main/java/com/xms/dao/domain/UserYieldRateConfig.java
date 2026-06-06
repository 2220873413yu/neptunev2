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
 * 用户收益率规则配置对象 t_user_yield_rate_config
 *
 * @author xms
 * @date 2026-03-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_yield_rate_config")
public class UserYieldRateConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 初始日收益率(如1=1%) */
    @Excel(name = "初始日收益率(如1=1%)")
    @ApiModelProperty(value = "初始日收益率(如1=1%)")
    private BigDecimal initialDailyRate;
    /** 最低日收益率(如5=5%) */
    @Excel(name = "最低日收益率(如5=5%)")
    @ApiModelProperty(value = "最低日收益率(如5=5%)")
    private BigDecimal minDailyRate;
    /** 最高日收益率(如1.5=1.5%) */
    @Excel(name = "最高日收益率(如1.5=1.5%)")
    @ApiModelProperty(value = "最高日收益率(如1.5=1.5%)")
    private BigDecimal maxDailyRate;
    /** 连续未提取收益达到N天触发增长 */
    @Excel(name = "连续未提取收益达到N天触发增长")
    @ApiModelProperty(value = "连续未提取收益达到N天触发增长")
    private Integer growthConsecutiveDays;
    /** 每次增长步长(如0.1=0.1%) */
    @Excel(name = "每次增长步长(如0.1=0.1%)")
    @ApiModelProperty(value = "每次增长步长(如0.1=0.1%)")
    private BigDecimal growthRateStep;
    /** 单次提取超过当前参与量比例阈值(如10=10%) */
    @Excel(name = "单次提取超过当前参与量比例阈值(如10=10%)")
    @ApiModelProperty(value = "单次提取超过当前参与量比例阈值(如10=10%)")
    private BigDecimal decayWithdrawThresholdRatio;
    /** 每次衰减步长(如0.1=0.1%) */
    @Excel(name = "每次衰减步长(如0.1=0.1%)")
    @ApiModelProperty(value = "每次衰减步长(如0.1=0.1%)")
    private BigDecimal decayRateStep;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("initialDailyRate", getInitialDailyRate())
            .append("minDailyRate", getMinDailyRate())
            .append("maxDailyRate", getMaxDailyRate())
            .append("growthConsecutiveDays", getGrowthConsecutiveDays())
            .append("growthRateStep", getGrowthRateStep())
            .append("decayWithdrawThresholdRatio", getDecayWithdrawThresholdRatio())
            .append("decayRateStep", getDecayRateStep())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
        .toString();
    }
}
