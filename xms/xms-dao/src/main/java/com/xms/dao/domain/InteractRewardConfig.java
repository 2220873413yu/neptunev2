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
 * 互动奖比例配置对象 t_interact_reward_config
 *
 * @author xms
 * @date 2025-11-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_interact_reward_config")
public class InteractRewardConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 层级：1=上一层/父级，2=下一层/直推，3=下二层/二代 */
    @Excel(name = "层级：1=上一层/父级，2=下一层/直推，3=下二层/二代")
    @ApiModelProperty(value = "层级：1=上一层/父级，2=下一层/直推，3=下二层/二代")
    private Integer level;
    /** 奖励比例（百分比，示例 10.0000 表示10%） */
    @Excel(name = "奖励比例", readConverterExp = "百=分比，示例,1=0.0000,表=示10%")
    private BigDecimal rewardRatio;

	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String createBy;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("level", getLevel())
            .append("rewardRatio", getRewardRatio())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
