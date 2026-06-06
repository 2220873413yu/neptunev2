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
 * 用户财富表对象 t_user_wealth_vault
 *
 * @author xms
 * @date 2026-03-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_wealth_vault")
public class UserWealthVault extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户id */
    @TableId(type = IdType.AUTO)
	@Excel(name = "用户ID",sort = 1)
    private Long id;
    /** 1段 */

    @Excel(name = "1段财富仓",sort = 2)
    @ApiModelProperty(value = "1段")
    private BigDecimal seg1Amount;
    /** 2段 */
    @Excel(name = "2段财富仓",sort = 3)
    @ApiModelProperty(value = "2段")
    private BigDecimal seg2Amount;
    /** 3段 */
    @Excel(name = "3段财富仓",sort = 4)
    @ApiModelProperty(value = "3段")
    private BigDecimal seg3Amount;
    /** 4段 */
    @Excel(name = "4段财富仓",sort = 5)
    @ApiModelProperty(value = "4段")
    private BigDecimal seg4Amount;
    /** 5段 */
    @Excel(name = "5段财富仓",sort = 6)
    @ApiModelProperty(value = "5段")
    private BigDecimal seg5Amount;
    /** 6段 */
	//@Excel(name = "6段")
    @ApiModelProperty(value = "6段")
    private BigDecimal seg6Amount;
    /** 7段 */
    //@Excel(name = "7段")
    @ApiModelProperty(value = "7段")
    private BigDecimal seg7Amount;


	/**
	 * 钱包地址
	 */
	@TableField(exist = false)
	@Excel(name = "钱包地址", sort = 1)
	private String userAccount;

	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;

	@TableField(exist = false)
	private String remark;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("seg1Amount", getSeg1Amount())
            .append("seg2Amount", getSeg2Amount())
            .append("seg3Amount", getSeg3Amount())
            .append("seg4Amount", getSeg4Amount())
            .append("seg5Amount", getSeg5Amount())
            .append("seg6Amount", getSeg6Amount())
            .append("seg7Amount", getSeg7Amount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
