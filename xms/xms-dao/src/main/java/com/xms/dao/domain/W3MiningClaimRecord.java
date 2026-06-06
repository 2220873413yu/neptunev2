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
 * 矿机静态领取记录对象 t_w3_mining_claim_record
 *
 * @author xms
 * @date 2025-04-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_mining_claim_record")
public class W3MiningClaimRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户ID */
    @Excel(name = "用户UID",sort = 1)
    private Long userId;
    /** 订单号 */
    @Excel(name = "订单号",sort = 2)
    private String orderNo;

	/**
	 * 来源类型 来源 0:利息,1:本金,2:余额宝
	 */
	@Excel(name = "来源类型",sort = 3, dictType = "t_w3_mining_claim_record_source_type")
	private Integer sourceType;

    /** 奖励数量(fsn) */
    @Excel(name = "奖励数量",sort = 4)
    private BigDecimal rewardAmount;
    /** 领取数量 */
    @Excel(name = "领取数量",sort = 5)
    private BigDecimal claimAmount;
	/** 手续费 */
    @Excel(name = "手续费",sort = 6)
    private BigDecimal feeAmount;

	/** 到账数量 */
    @Excel(name = "到账数量",sort = 7)
    private BigDecimal actualAmount;

	/** 领取币种 1:ftn,3:fsn */
	@Excel(name = "领取币种",sort = 8 , dictType = "t_user_money_log_coin_type")
	private Integer coinType;

	/** wf价格 */
	@Excel(name = "FTN价格",sort = 9)
	private BigDecimal wfCurrentPrice;

	/**
	 * fsn价格
	 */
	@Excel(name = "FSN价格",sort = 10)
	@ApiModelProperty(value = "FSN价格")
	private BigDecimal fsnCurrentPrice;

	/**
	 * 价值多少usdt
	 */
	@Excel(name = "U价值",sort = 11)
	private BigDecimal usdtValue;

	/**
	 * 类型 0:活期,1:固定
	 */
	private Integer type;









	/** 创建者 */
	@TableField(exist = false)
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("orderNo", getOrderNo())
            .append("rewardAmount", getRewardAmount())
            .append("claimAmount", getClaimAmount())
            .append("coinType", getCoinType())
            .append("wfCurrentPrice", getWfCurrentPrice())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .append("remark", getRemark())
        .toString();
    }
}
