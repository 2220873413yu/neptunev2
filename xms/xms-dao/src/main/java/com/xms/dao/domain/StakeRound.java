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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 全局质押轮次对象 t_stake_round
 *
 * @author xms
 * @date 2026-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_stake_round")
public class StakeRound extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
	@Excel(name = "轮次编号",sort = 1)
    private Long id;
    /** 状态:0进行中,1已爆仓,2已结算,3已关闭 */

    @Excel(name = "质押轮次状态",sort = 2)
    @ApiModelProperty(value = "状态:0进行中,1已爆仓,2已结算,3已关闭")
    private Integer status;

	/**
	 * 保险仓余额
	 */
	@Excel(name = "保险仓余额",sort = 3)
	private BigDecimal insuranceBalance;

	/** 轮次开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "轮次开始时间",sort = 4, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/** 是否处理爆仓业务 0:否,1:是 */
	@Excel(name = "是否处理爆仓业务",sort = 5,readConverterExp = "0=否,1=是")
	private Integer bizStatus;

	/** 爆仓检测开关：0关闭，1开启 */
	@Excel(name = "爆仓检测开关", sort = 6, readConverterExp = "0=关闭,1=开启")
	@ApiModelProperty(value = "爆仓检测开关：0关闭，1开启")
	private Integer liquidationCheckEnabled;

	/** 买积分的h余额 */
	@Excel(name = "买贡献分花费的h代币数量",sort = 6)
	private BigDecimal buyPointTotal;
	/** 本轮累计已提现工作室补贴 */
	@Excel(name = "本轮累计已提现工作室补贴",sort = 7)
	@ApiModelProperty(value = "本轮累计已提现工作室补贴")
	private BigDecimal studioSubsidyTotal;
	/** 本轮累计已提取收益总额(静态+动态按100%口径) */
	@Excel(name = "本轮累计已提取收益总额",sort = 8)
	@ApiModelProperty(value = "本轮累计已提取收益总额(静态+动态按100%口径)")
	private BigDecimal withdrawRewardTotalFull;

    /** 爆仓触发时间 */
    @Excel(name = "爆仓触发时间",sort = 9, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date liquidationTime;

    /** 轮次结束时间 */
    @Excel(name = "轮次结束时间",sort = 10, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /** 本轮玩家累计参与总量(不含节点) */
    @Excel(name = "本轮玩家累计参与总量(不含节点)")
    private BigDecimal playerStakeTotal;







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
            .append("status", getStatus())
            .append("liquidationCheckEnabled", getLiquidationCheckEnabled())
            .append("startTime", getStartTime())
            .append("liquidationTime", getLiquidationTime())
            .append("endTime", getEndTime())
            .append("playerStakeTotal", getPlayerStakeTotal())
            .append("studioSubsidyTotal", getStudioSubsidyTotal())
            .append("withdrawRewardTotalFull", getWithdrawRewardTotalFull())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
