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
 * BOOMAI日利率调节规则对象 ptb_day_ratio_rule
 *
 * @author xms
 * @date 2025-11-26
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "ptb_day_ratio_rule")
public class PtbDayRatioRule extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 币种类型，如 1=BOOMAI */
    @Excel(name = "币种类型，如 1=BOOMAI")
    @ApiModelProperty(value = "币种类型，如 1=BOOMAI")
    private Long coinType;
    /** 基础日利率(如1%写0.010000) */
    @Excel(name = "基础日利率(如1%写0.010000)")
    @ApiModelProperty(value = "基础日利率(如1%写0.010000)")
    private BigDecimal baseRatio;
    /** 触发调节的涨跌幅阈值(百分比) */
    @Excel(name = "触发调节的涨跌幅阈值(百分比)")
    @ApiModelProperty(value = "触发调节的涨跌幅阈值(百分比)")
    private BigDecimal triggerThreshold;
    /** 每超出1%调整的日利率增量 */
    @Excel(name = "每超出1%调整的日利率增量")
    @ApiModelProperty(value = "每超出1%调整的日利率增量")
    private BigDecimal stepPerc;
	/**
	 * 日利率下限(1代表1%)
	 */
    @Excel(name = "日利率下限")
    @ApiModelProperty(value = "日利率下限")
    private BigDecimal minRatio;
	/**
	 * 日利率上限(1代表1%)
	 */
    @Excel(name = "日利率上限")
    @ApiModelProperty(value = "日利率上限")
    private BigDecimal maxRatio;
    /** 是否启用 1是0否 */
    @Excel(name = "是否启用 1是0否")
    @ApiModelProperty(value = "是否启用 1是0否")
    private Long enabled;

	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private Integer deleted;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("coinType", getCoinType())
            .append("baseRatio", getBaseRatio())
            .append("triggerThreshold", getTriggerThreshold())
            .append("stepPerc", getStepPerc())
            .append("minRatio", getMinRatio())
            .append("maxRatio", getMaxRatio())
            .append("enabled", getEnabled())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
