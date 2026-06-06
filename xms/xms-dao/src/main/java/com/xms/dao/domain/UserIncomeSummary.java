package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.xms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.math.BigDecimal;
import java.util.Date;

import com.xms.common.annotation.Excel;

/**
 * 用户收益信息对象 t_user_income_summary
 *
 * @author xms
 * @date 2025-08-14
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_income_summary")
public class UserIncomeSummary extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户id */
    private Long userId;
    /** 获得活期利息 */
    @Excel(name = "获得活期利息")
    @ApiModelProperty(value = "获得活期利息")
    private BigDecimal sourceType21Balance0;
    /** 获得固定利息 */
    @Excel(name = "获得固定利息")
    @ApiModelProperty(value = "获得固定利息")
    private BigDecimal sourceType21Balance1;
    /** 推荐奖 */
    @Excel(name = "推荐奖")
    @ApiModelProperty(value = "推荐奖")
    private BigDecimal sourceType23Balance;
    /** 团队奖 */
    @Excel(name = "团队奖")
    @ApiModelProperty(value = "团队奖")
    private BigDecimal sourceType24Balance;
    /** 平级奖 */
    @Excel(name = "平级奖")
    @ApiModelProperty(value = "平级奖")
    private BigDecimal sourceType25Balance;
	/**
	 * 删除标志0:删除,1:正常
	 */
	private Integer deleted;

	@TableField(exist = false)
	private String createBy;
	/** 创建时间 */
	@TableField(exist = false)
	private Date createTime;

	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Date updateTime;
	@TableField(exist = false)
	private String remark;
	/**
	 * 用户账号
	 */
	@TableField(exist = false)
	@Excel(name = "用户账号",sort = 2)
	private String userAccount;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("sourceType21Balance0", getSourceType21Balance0())
            .append("sourceType21Balance1", getSourceType21Balance1())
            .append("sourceType23Balance", getSourceType23Balance())
            .append("sourceType24Balance", getSourceType24Balance())
            .append("sourceType25Balance", getSourceType25Balance())
        .toString();
    }
}
