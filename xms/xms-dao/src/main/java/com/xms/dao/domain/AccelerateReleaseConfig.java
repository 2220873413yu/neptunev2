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
 * 收益加速释放配置对象 t_accelerate_release_config
 *
 * @author xms
 * @date 2025-11-21
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_accelerate_release_config")
public class AccelerateReleaseConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 加速后总释放天数（例如：120/90/60/30/10天） */
    @Excel(name = "加速后总释放天数", readConverterExp = "例=如：120/90/60/30/10天")
    private Integer targetDays;
    /** 燃料币种（如 MAI） */
    @Excel(name = "燃料币种", readConverterExp = "如=,M=AI")
    private String fuelToken;
    /** 所需燃料占本金比例，例如 10 = 10% */
    @Excel(name = "所需燃料占本金比例，例如 10 = 10%")
    @ApiModelProperty(value = "所需燃料占本金比例，例如 10 = 10%")
    private BigDecimal fuelRatio;
    /** 是否启用 0:否,1:是 */
    @Excel(name = "是否启用 0:否,1:是")
    @ApiModelProperty(value = "是否启用 0:否,1:是")
    private Integer status;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("targetDays", getTargetDays())
            .append("fuelToken", getFuelToken())
            .append("fuelRatio", getFuelRatio())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
        .toString();
    }
}
