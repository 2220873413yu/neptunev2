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
    /** 本次质押金额(单位H) */
    @Excel(name = "质押金额",sort = 4)
    @ApiModelProperty(value = "本次质押金额(单位H)")
    private BigDecimal stakeAmount;
    /** 状态:1成功,2:未处理 */
    @Excel(name = "订单状态",sort = 5,dictType = "stake_order_status")
    @ApiModelProperty(value = "状态:1成功,2:未处理")
    private Integer status;
	/** 业务状态是否处理 0:未处理,1:已处理 */
	@Excel(name = "业务状态",sort = 6,readConverterExp = "0=未处理,1=已处理")
    private Integer bizStatus;
    /** 链上交易hash */
    @Excel(name = "链上交易hash",sort = 7,width = 40)
    @ApiModelProperty(value = "链上交易hash")
    private String txHash;

    /**
     * 业绩归属上级用户id
     */
    @Excel(name = "业绩归属上级用户ID",sort = 8)
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
    @Excel(name = "创建日期",sort = 9)
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
            .append("status", getStatus())
            .append("txHash", getTxHash())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
