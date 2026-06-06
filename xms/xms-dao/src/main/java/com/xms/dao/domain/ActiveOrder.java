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
 * 用户激活订单对象 t_active_order
 *
 * @author xms
 * @date 2025-12-30
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_active_order")
public class ActiveOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单号 */
    @Excel(name = "订单号",sort = 1, width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /** 用户ID */
    @Excel(name = "用户ID",sort = 2)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
	/** 支付了多少金额 */
	@Excel(name = "支付了多少金额", sort = 3)
	@ApiModelProperty(value = "支付了多少金额")
	private BigDecimal amount;
	/**
	 * 赠送了多少次空投机会
	 */
	@Excel(name = "赠送次数", sort = 4)
	private Integer activationCount;

	/** 锁定超时时间，默认锁定时间+5分钟 */
	@Excel(name = "锁定超时时间", sort = 7, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date lockExpireAt;


    /** 交易hash */
    @Excel(name = "交易hash", sort = 6, width = 60)
    @ApiModelProperty(value = "交易hash")
    private String txHash;

    /** 业务状态 0:待支付,1:已支付,2:过期关闭,3:超时支付 */
    @Excel(name = "业务状态", sort = 5, dictType = "t_active_order_biz_status")
    @ApiModelProperty(value = "业务状态 0:待支付,1:已支付,2:已关闭")
    private Integer bizStatus;

	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String remark;

	@TableField(exist = false)
	@Excel(name = "钱包地址", sort = 2, width = 40)
	private String address;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("activationCount", getActivationCount())
            .append("amount", getAmount())
            .append("txHash", getTxHash())
            .append("createTime", getCreateTime())
            .append("bizStatus", getBizStatus())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
