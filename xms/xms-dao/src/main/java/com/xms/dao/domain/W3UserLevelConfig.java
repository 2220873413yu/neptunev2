package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 用户等级考核配置对象 t_w3_user_level_config
 *
 * @author xms
 * @date 2025-04-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_user_level_config")
public class W3UserLevelConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 等级编码(V0,V1,V2,V3...) */
    @Excel(name = "等级",dictType = "t_user_info_game_level",sort = 1)
    private Integer level;

	/** 个人业绩 */
	@Excel(name = "个人业绩",sort = 1)
	private BigDecimal performance;

    /** 团队业绩 */
    @Excel(name = "团队业绩",sort = 1)
    private BigDecimal umbrellaPerformance;


	/**
	 * 是否考核伞下级别 0:否,1:是
	 */
	@Excel(name = "是否考核伞下级别",sort = 2,dictType = "t_user_info_is_valid")
	private Integer isUmbrellaLevel;

	/**
	 * 伞下人数
	 */
	@Excel(name = "伞下人数",sort = 3)
	private Integer umbrellaCount;

	/**
	 * 伞下等级
	 */
	@Excel(name = "伞下等级",dictType = "t_user_info_game_level",sort = 4)
	private Integer umbrellaLevel;

	/**
	 * 收益百分比
	 */
	@Excel(name = "收益百分比%",sort = 5)
	private BigDecimal rewardRatio;

	/**
	 * 平级奖比例
	 */
	@Excel(name = "平级奖比例%",sort = 5)
	private BigDecimal peerRewardRatio;

	//谷歌验证码
	@TableField(exist = false)
	private String autoCode;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("level", getLevel())
            .append("umbrellaPerformance", getUmbrellaPerformance())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
        .toString();
    }
}
