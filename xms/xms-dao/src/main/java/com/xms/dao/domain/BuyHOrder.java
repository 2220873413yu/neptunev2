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
 * 购买H代币订单对象 t_buy_h_order
 *
 * @author xms
 * @date 2026-03-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_buy_h_order")
public class BuyHOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户ID",sort=1)
    @ApiModelProperty(value = "用户id")
    private Long userId;
	/**
	 * 轮次编号
	 */
	@Excel(name = "轮次编号",sort=2)
	private Long stakeRoundId;
    /** 购买H代币订单号 */
    @Excel(name = "订单号",sort = 2,width = 30)
    @ApiModelProperty(value = "购买H代币订单号")
    private String orderNo;
    /** 钱包地址 */
    @Excel(name = "钱包地址",sort = 3,width = 30)
    @ApiModelProperty(value = "钱包地址")
    private String walletAddress;
    /** 支付H代币数量 */
    @Excel(name = "支付H代币数量",sort = 4)
    @ApiModelProperty(value = "支付H代币数量")
    private BigDecimal payHAmount;
    /** 获取积分数量 */
    @Excel(name = "获取积分数量",sort = 5)
    @ApiModelProperty(value = "获取积分数量")
    private BigDecimal pointsAmount;
    /** 支付hash */
    @Excel(name = "支付hash",sort = 6)
    @ApiModelProperty(value = "支付hash")
    private String payHash;


	/**
	 * 是否支付 0:否,1:是
	 */
	@Excel(name="是否支付",sort = 7,dictType = "t_user_info_is_valid")
	private Integer status;



	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private String createBy;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("orderNo", getOrderNo())
            .append("walletAddress", getWalletAddress())
            .append("payHAmount", getPayHAmount())
            .append("pointsAmount", getPointsAmount())
            .append("payHash", getPayHash())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
