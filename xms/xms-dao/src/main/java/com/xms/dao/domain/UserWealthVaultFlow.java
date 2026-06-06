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

import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 用户财富仓流水对象 t_user_wealth_vault_flow
 *
 * @author xms
 * @date 2026-03-11
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_wealth_vault_flow")
public class UserWealthVaultFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 阶段 1-5 */
    @Excel(name = "阶段 1-5")
    @ApiModelProperty(value = "阶段 1-5")
    private Integer segNo;
    /** 来源类型 1:静态提现 2:动态提现 3:其他 */
    @Excel(name = "来源类型 1:静态提现 2:动态提现 3:其他")
    @ApiModelProperty(value = "来源类型 1:静态提现 2:动态提现 3:其他")
    private Integer sourceType;
    /** 来源订单号 */
    @Excel(name = "来源订单号")
    @ApiModelProperty(value = "来源订单号")
    private String sourceOrderNo;
    /** 变动金额 */
    @Excel(name = "变动金额")
    @ApiModelProperty(value = "变动金额")
    private BigDecimal changeAmount;
    /** 变动前该段余额 */
    @Excel(name = "变动前该段余额")
    @ApiModelProperty(value = "变动前该段余额")
    private BigDecimal beforeAmount;
    /** 变动后该段余额 */
    @Excel(name = "变动后该段余额")
    @ApiModelProperty(value = "变动后该段余额")
    private BigDecimal afterAmount;
    /** 触发时价格 */
    @Excel(name = "触发时价格")
    @ApiModelProperty(value = "触发时价格")
    private BigDecimal triggerPrice;

	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;

	@TableField(exist = false)
	private Date updateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("segNo", getSegNo())
            .append("sourceType", getSourceType())
            .append("sourceOrderNo", getSourceOrderNo())
            .append("changeAmount", getChangeAmount())
            .append("beforeAmount", getBeforeAmount())
            .append("afterAmount", getAfterAmount())
            .append("triggerPrice", getTriggerPrice())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
        .toString();
    }
}
