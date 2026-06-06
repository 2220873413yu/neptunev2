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
 * 每日质押数据快照对象 t_stake_daily_snapshot
 *
 * @author xms
 * @date 2026-03-30
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_stake_daily_snapshot")
public class StakeDailySnapshot extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 保险仓余额 */
    @Excel(name = "保险仓余额")
    @ApiModelProperty(value = "保险仓余额")
    private BigDecimal insuranceBalance;
    /** 本轮玩家累计参与总量(不含节点) */
    @Excel(name = "本轮玩家累计参与总量(不含节点)")
    @ApiModelProperty(value = "本轮玩家累计参与总量(不含节点)")
    private BigDecimal playerStakeTotal;
    /** 本轮累计已发放工作室补贴 */
    @Excel(name = "本轮累计已发放工作室补贴")
    @ApiModelProperty(value = "本轮累计已发放工作室补贴")
    private BigDecimal studioSubsidyTotal;
    /** 本轮累计已提取收益总额(静态+动态按100%口径) */
    @Excel(name = "本轮累计已提取收益总额(静态+动态按100%口径)")
    @ApiModelProperty(value = "本轮累计已提取收益总额(静态+动态按100%口径)")
    private BigDecimal withdrawRewardTotalFull;
    /** 买积分的h余额 */
    @Excel(name = "买积分的h余额")
    @ApiModelProperty(value = "买积分的h余额")
    private BigDecimal buyPointTotal;
    /** 轮次id */
    @Excel(name = "轮次id")
    @ApiModelProperty(value = "轮次id")
    private Long stakeRoundId;
    /** 用户待解锁财富仓余额 */
    @Excel(name = "用户待解锁财富仓余额")
    @ApiModelProperty(value = "用户待解锁财富仓余额")
    private BigDecimal lockedValidNum4;
    /** 用户财富仓余额 */
    @Excel(name = "用户财富仓余额")
    @ApiModelProperty(value = "用户财富仓余额")
    private BigDecimal totalValidNum4;
    /** 提现合约余额 */
    @Excel(name = "提现合约余额")
    @ApiModelProperty(value = "提现合约余额")
    private BigDecimal withdrawContractBalance;

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
            .append("insuranceBalance", getInsuranceBalance())
            .append("playerStakeTotal", getPlayerStakeTotal())
            .append("studioSubsidyTotal", getStudioSubsidyTotal())
            .append("withdrawRewardTotalFull", getWithdrawRewardTotalFull())
            .append("buyPointTotal", getBuyPointTotal())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("stakeRoundId", getStakeRoundId())
            .append("lockedValidNum4", getLockedValidNum4())
            .append("totalValidNum4", getTotalValidNum4())
            .append("withdrawContractBalance", getWithdrawContractBalance())
        .toString();
    }
}
