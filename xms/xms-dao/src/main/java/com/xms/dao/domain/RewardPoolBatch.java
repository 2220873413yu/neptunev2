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
 * 分红批次记录对象 t_reward_pool_batch
 *
 * @author xms
 * @date 2025-12-08
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_reward_pool_batch")
public class RewardPoolBatch extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 批次ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 池子类型：1:矿池,2:消费分红池,3:手续费分红池 */
    @Excel(name = "池子类型：1:矿池,2:消费分红池,3:手续费分红池")
    @ApiModelProperty(value = "池子类型：1:矿池,2:消费分红池,3:手续费分红池")
    private Integer poolType;
    /** 分红批次号，如20251205-01 */
    @Excel(name = "分红批次号，如20251205-01")
    @ApiModelProperty(value = "分红批次号，如20251205-01")
    private String batchNo;
    /** 分红日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分红日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date batchDate;
    /** 本批次总金额 */
    @Excel(name = "本批次总金额")
    @ApiModelProperty(value = "本批次总金额")
    private BigDecimal totalAmount;
    /** 静态部分金额 */
    @Excel(name = "静态部分金额")
    @ApiModelProperty(value = "静态部分金额")
    private BigDecimal staticAmount;
    /** 动态部分金额 */
    @Excel(name = "动态部分金额")
    @ApiModelProperty(value = "动态部分金额")
    private BigDecimal dynamicAmount;
    /** 状态：0-待分配 1-分配中 2-已完成 3-失败 */
    @Excel(name = "状态：0-待分配 1-分配中 2-已完成 3-失败")
    @ApiModelProperty(value = "状态：0-待分配 1-分配中 2-已完成 3-失败")
    private Long status;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("poolType", getPoolType())
            .append("batchNo", getBatchNo())
            .append("batchDate", getBatchDate())
            .append("totalAmount", getTotalAmount())
            .append("staticAmount", getStaticAmount())
            .append("dynamicAmount", getDynamicAmount())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
