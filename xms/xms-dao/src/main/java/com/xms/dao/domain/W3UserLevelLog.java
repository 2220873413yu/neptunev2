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
 * 用户等级变更记录对象 t_w3_user_level_log
 *
 * @author xms
 * @date 2025-04-14
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_user_level_log")
public class W3UserLevelLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 变更之前等级 */
    @Excel(name = "变更之前等级")
    @ApiModelProperty(value = "变更之前等级")
    private Integer oldLevel;
    /** 变更之后等级 */
    @Excel(name = "变更之后等级")
    @ApiModelProperty(value = "变更之后等级")
    private Integer newLevel;
    /** 团队业绩 */
    @Excel(name = "团队业绩")
    @ApiModelProperty(value = "团队业绩")
    private BigDecimal umbrellaPerformance;
    /** 小区业绩 */
    @Excel(name = "小区业绩")
    @ApiModelProperty(value = "小区业绩")
    private BigDecimal communityPerformance;
    /** 升级还是降级 0:升级,1:降级 */
    @Excel(name = "升级还是降级 0:升级,1:降级")
    @ApiModelProperty(value = "升级还是降级 0:升级,1:降级")
    private Integer type;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("oldLevel", getOldLevel())
            .append("newLevel", getNewLevel())
            .append("umbrellaPerformance", getUmbrellaPerformance())
            .append("communityPerformance", getCommunityPerformance())
            .append("createTime", getCreateTime())
            .append("type", getType())
        .toString();
    }
}
