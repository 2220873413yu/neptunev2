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
 * 质押订单对象 t_stake_order
 *
 * @author xms
 * @date 2026-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_stake_order")
public class StakeOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单号(唯一) */
    @Excel(name = "订单号",sort = 1,width = 30)
    private String orderNo;
    /** 用户id */
    @Excel(name = "用户ID",sort = 2,width = 30)
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 轮次表id */
    @Excel(name = "轮次编号",sort = 3)
    private Long stakeRoundId;
    /** ACP入金数量 */
    @Excel(name = "ACP入金数量",sort = 4)
    @ApiModelProperty(value = "ACP入金数量")
    private BigDecimal stakeAmount;
	/** 入金来源类型：1正常ACP入金，3旧系统H换ACP入金 */
	@Excel(name = "入金来源", sort = 5, readConverterExp = "1=正常ACP入金,3=旧系统H换ACP入金")
	@ApiModelProperty(value = "入金来源类型：1正常ACP入金，3旧系统H换ACP入金")
	private Integer depositSourceType;
	/** ACP当时U价快照 */
	@Excel(name = "ACP单价U", sort = 6)
	@ApiModelProperty(value = "ACP当时U价快照")
	private BigDecimal acpPriceUsdtSnapshot;
	/** H当时U价快照 */
	@Excel(name = "H单价U", sort = 7)
	@ApiModelProperty(value = "H当时U价快照")
	private BigDecimal hPriceUsdtSnapshot;
	/** 本单折U价值 */
	@Excel(name = "折U价值", sort = 8)
	@ApiModelProperty(value = "本单折U价值")
	private BigDecimal depositUsdtAmount;
	/** 赠送比例快照 */
	@Excel(name = "赠送比例", sort = 9)
	@ApiModelProperty(value = "赠送比例快照")
	private BigDecimal giftRatioSnapshot;
	/** 本单应赠送H总量 */
	@Excel(name = "赠送H总量", sort = 10)
	@ApiModelProperty(value = "本单应赠送H总量")
	private BigDecimal giftHAmount;
    /** 状态:1成功,2:未处理 */
    @Excel(name = "订单状态",sort = 11,dictType = "stake_order_status")
    @ApiModelProperty(value = "状态:1成功,2:未处理")
    private Integer status;
	/** 业务状态是否处理 0:未处理,1:已处理 */
	@Excel(name = "业务状态",sort = 12,readConverterExp = "0=未处理,1=已处理")
    private Integer bizStatus;
    /** 链上交易hash */
    @Excel(name = "链上交易hash",sort = 13,width = 40)
    @ApiModelProperty(value = "链上交易hash")
    private String txHash;

    /**
     * 业绩归属上级用户id
     */
    @Excel(name = "业绩归属上级用户ID",sort = 14)
    private Long belongUserId;


	/**
	 * 钱包地址
	 */
	@TableField(exist = false)
	@Excel(name = "钱包地址",sort = 1)
	private String userAccount;
    /**
     * 创建日期 格式为20260101
     */
    @Excel(name = "创建日期",sort = 15)
    private Integer createDay;

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
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("stakeRoundId", getStakeRoundId())
            .append("stakeAmount", getStakeAmount())
            .append("depositSourceType", getDepositSourceType())
            .append("acpPriceUsdtSnapshot", getAcpPriceUsdtSnapshot())
            .append("hPriceUsdtSnapshot", getHPriceUsdtSnapshot())
            .append("depositUsdtAmount", getDepositUsdtAmount())
            .append("giftRatioSnapshot", getGiftRatioSnapshot())
            .append("giftHAmount", getGiftHAmount())
            .append("status", getStatus())
            .append("txHash", getTxHash())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
