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
 * 用户等级考核配置对象 t_user_level_config
 *
 * @author xms
 * @date 2025-12-03
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_level_config")
public class UserLevelConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 等级编码(0:V0,1:V1,2:V2,3:V3...) */
    @Excel(name = "等级编码(V0,V1,V2,V3...)")
    @ApiModelProperty(value = "等级编码(V0,V1,V2,V3...)")
    private Integer level;
    /** 个人业绩(暂时废弃) */
    @Excel(name = "个人业绩")
    private BigDecimal performance;
    /** 小区业绩(质押量) */
    @Excel(name = "小区业绩(质押量)")
    private BigDecimal umbrellaPerformance;

    /** 级差奖比例 */
    @Excel(name = "级差奖比例")
    private BigDecimal rewardRatio;

	/** 是否有工作室补贴 0:否 1:是 */
    @Excel(name = "是否有工作室补贴", readConverterExp = "0=否,1=是")
    private Integer hasStudioSubsidy;

	/**
	 * 平级奖比例
	 */
	@Excel(name = "平级奖比例")
    private BigDecimal peerRewardRatio;

	/** 小区业绩(int类型 等级考核的时候使用) */
	@TableField(exist = false)
	private Integer intUmbrellaPerformance;

	/**
	 * 最少买入贡献分数量
	 */
	private BigDecimal minBuyAmount;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("level", getLevel())
            .append("performance", getPerformance())
            .append("umbrellaPerformance", getUmbrellaPerformance())
            .append("rewardRatio", getRewardRatio())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
        .toString();
    }
}
