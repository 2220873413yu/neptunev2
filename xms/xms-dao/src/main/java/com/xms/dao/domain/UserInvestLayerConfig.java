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
 * 层奖配置对象 t_user_invest_layer_config
 *
 * @author xms
 * @date 2026-03-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_invest_layer_config")
public class UserInvestLayerConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 层级编码(如M1~M9) */
    @Excel(name = "层级编码(如M1~M9)")
    @ApiModelProperty(value = "层级编码(如M1~M9)")
    private Integer level;
    /** 最低投资额度 */
    @Excel(name = "最低投资额度")
    @ApiModelProperty(value = "最低投资额度")
    private BigDecimal minInvest;
    /** 可获得层数 */
    @Excel(name = "可获得层数")
    @ApiModelProperty(value = "可获得层数")
    private Integer layerCount;

	/** 层级奖励比例 */
	@Excel(name = "奖励比例")
	private BigDecimal rewardRatio;


	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("level", getLevel())
            .append("minInvest", getMinInvest())
            .append("layerCount", getLayerCount())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
