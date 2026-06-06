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
 * 分红池配置对象 t_reward_pool_config
 *
 * @author xms
 * @date 2025-12-08
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_reward_pool_config")
public class RewardPoolConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 池子类型：1:矿池,2:消费分红池,3:手续费分红池 */
    @Excel(name = "池子类型：1:矿池,2:消费分红池,3:手续费分红池")
    @ApiModelProperty(value = "池子类型：1:矿池,2:消费分红池,3:手续费分红池")
    private Integer poolType;
    /** 池子名称 */
    @Excel(name = "池子名称")
    @ApiModelProperty(value = "池子名称")
    private String poolName;
    /** 静态分配比例，如0.5表示50% */
    @Excel(name = "静态分配比例，如0.5表示50%")
    @ApiModelProperty(value = "静态分配比例，如0.5表示50%")
    private BigDecimal staticRatio;
    /** 动态分配比例，如0.5表示50% */
    @Excel(name = "动态分配比例，如0.5表示50%")
    @ApiModelProperty(value = "动态分配比例，如0.5表示50%")
    private BigDecimal dynamicRatio;
    /** 每日产出量（矿池才用） */
    @Excel(name = "每日产出量", readConverterExp = "矿=池才用")
    private BigDecimal dailyOutput;

	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("poolType", getPoolType())
            .append("poolName", getPoolName())
            .append("staticRatio", getStaticRatio())
            .append("dynamicRatio", getDynamicRatio())
            .append("dailyOutput", getDailyOutput())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
