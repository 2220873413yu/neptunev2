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
 * 提现配置对象 t_withdrawal_config
 *
 * @author xms
 * @date 2026-03-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_withdrawal_config")
public class WithdrawalConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 币种 1:节点收益,2:静态收益,3:动态收益,4:财富仓,5:H代币,6:工作室收益,7:贡献分 */
    @Excel(name = "币种 1:节点收益,2:静态收益,3:动态收益,4:财富仓,5:H代币,6:工作室收益,7:贡献分")
    @ApiModelProperty(value = "币种 1:节点收益,2:静态收益,3:动态收益,4:财富仓,5:H代币,6:工作室收益,7:贡献分")
    private Long coinType;
    /** 提现开关(1:开,2:关) */
    @Excel(name = "提现开关(1:开,2:关)")
    @ApiModelProperty(value = "提现开关(1:开,2:关)")
    private Long withdrawOpen;
    /** 最小提现金额 */
    @Excel(name = "最小提现金额")
    @ApiModelProperty(value = "最小提现金额")
    private BigDecimal minWithdrawAmount;
    /** 手续费比例(例如:10表示10%) */
    @Excel(name = "手续费比例(例如:10表示10%)")
    @ApiModelProperty(value = "手续费比例(例如:10表示10%)")
    private BigDecimal feeRatio;
    /** 财富仓比例(例如:15表示15%) */
    @Excel(name = "财富仓比例(例如:15表示15%)")
    @ApiModelProperty(value = "财富仓比例(例如:15表示15%)")
    private BigDecimal wealthVaultRatio;
    /** 保险仓比例(例如:15表示15%) */
    @Excel(name = "保险仓比例(例如:15表示15%)")
    @ApiModelProperty(value = "保险仓比例(例如:15表示15%)")
    private BigDecimal insuranceVaultRatio;

	/** 单日免审核次数 */
	@Excel(name = "单日免审核次数")
	@ApiModelProperty(value = "单日免审核次数")
	private Integer dailyFreeAuditCount;
	/** 单日提现额度 */
	@Excel(name = "单日提现额度")
	@ApiModelProperty(value = "提现额度")
	private BigDecimal withdrawLimit;

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
            .append("coinType", getCoinType())
            .append("withdrawOpen", getWithdrawOpen())
            .append("minWithdrawAmount", getMinWithdrawAmount())
            .append("feeRatio", getFeeRatio())
			.append("dailyFreeAuditCount", getDailyFreeAuditCount())
			.append("withdrawLimit", getWithdrawLimit())
            .append("wealthVaultRatio", getWealthVaultRatio())
            .append("insuranceVaultRatio", getInsuranceVaultRatio())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
        .toString();
    }
}
