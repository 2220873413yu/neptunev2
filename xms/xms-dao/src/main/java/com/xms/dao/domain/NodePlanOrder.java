package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.xms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xms.common.annotation.Excel;

/**
 * 用户节点订单对象 t_node_plan_order
 *
 * @author xms
 * @date 2026-01-16
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_node_plan_order")
public class NodePlanOrder extends BaseEntity {
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
    /** 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点 */
    @Excel(name = "节点等级",sort = 3,dictType = "t_node_plan_node_level")
    @ApiModelProperty(value = "节点等级")
    private Integer nodePlanLevel;
    /** 支付了多少金额 */
    @Excel(name = "支付了多少金额",sort = 4)
    @ApiModelProperty(value = "支付了多少金额")
    private BigDecimal amount;
    /** 交易hash */
    @Excel(name = "交易hash",sort = 5)
    @ApiModelProperty(value = "交易hash")
    private String txHash;

	/** 节点地址 */
	@Excel(name = "购买地址",sort = 6,width = 30)
	private String address;
    /** 业务状态 0:待支付,1:已支付,2:释放完成, */
    @Excel(name = "业务状态",sort = 7,dictType = "t_active_order_biz_status")
    @ApiModelProperty(value = "业务状态 0:待支付,1:已支付,2:释放完成,")
    private Integer bizStatus;
    /** 支付时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", sort = 8, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date paymentTime;

	/**
	 * 年化收益率
	 */
	@Excel(name = "年化收益率")
	private BigDecimal annualRate;

	/**
	 * 累计年华收益
	 */
	@Excel(name = "累计年华收益")
	private BigDecimal totalAnnual;

	/**
	 * 总共释放天数 默认360天
	 */
	private Integer totalDay;

	/**
	 * 剩余释放天数
	 */
	private Integer haveDay;

	/**
	 * 总共释放金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 剩余释放金额
	 */
	private BigDecimal haveAmount;

	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;

	/**
	 * 套餐原价
	 */
	private String remark;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("nodePlanLevel", getNodePlanLevel())
            .append("amount", getAmount())
            .append("txHash", getTxHash())
            .append("createTime", getCreateTime())
            .append("bizStatus", getBizStatus())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
