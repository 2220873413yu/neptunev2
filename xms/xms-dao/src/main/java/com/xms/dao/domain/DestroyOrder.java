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
 * 销毁记录对象 t_destroy_order
 *
 * @author xms
 * @date 2025-11-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_destroy_order")
public class DestroyOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;

	/** 用户id */
	@Excel(name = "用户ID",sort = 1)
	@ApiModelProperty(value = "用户id")
	private Long userId;

    /** 订单号 */
    @Excel(name = "订单号",sort = 2,width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /** 矿机天数 */
    @Excel(name = "质押周期",sort = 4)
    @ApiModelProperty(value = "矿机天数")
    private Long days;

	/** 订单hash */
	@Excel(name = "交易hash",sort = 3,width = 30)
	private String hash;
    /** 价值多少u */
    @Excel(name = "销毁支出(U)",sort = 4)
    @ApiModelProperty(value = "价值多少u")
    private BigDecimal usdtValue;
    /** 销毁了多少个boomai */
    @Excel(name = "销毁 BOOMAI 数量",sort = 5)
    @ApiModelProperty(value = "销毁了多少个boomai")
    private BigDecimal validNum1Value;
    /** valid_num1价格 */
    @Excel(name = "销毁时 BOOMAI 单价(U)",sort = 6)
    @ApiModelProperty(value = "valid_num1价格")
    private BigDecimal lastFtnPrice;
    /** 已经获取了多少个boomai */
    @Excel(name = "已释放 BOOMAI" ,sort = 7)
    @ApiModelProperty(value = "已经获取了多少个boomai")
    private BigDecimal haveValidNum1;

    /** 订单状态 0:待支付,1:运行中,2:暂停产出 */
    @Excel(name = "订单状态", sort = 8, dictType = "t_destroy_order_status")
    private Integer status;

	/**
	 * 支付状态 0:待支付,1:支付成功,2:关闭订单(超时)
	 */
	@Excel(name = "支付状态", sort = 9, dictType = "t_destroy_order_pay_status")
    private Integer payStatus;

    /** 支付时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

	/** 创建日期 yyyymmdd */
	private Integer createDate;
    /** 业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务 */
    @Excel(name = "业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务")
    @ApiModelProperty(value = "业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务")
    private Integer bizStatus;


	/** 是否减产 0:否,1:是 */
	@Excel(name = "是否减产", sort = 10, dictType = "t_user_info_is_valid")
	@ApiModelProperty(value = "是否减产 0:否,1:是")
	private Integer isReduced;

	/** 减产目标金额 */
	@Excel(name = "减产目标(BOOMAI)", sort = 11)
	private BigDecimal reduceTargetAmount;
	/**
	 * 自下单起累计已产出的BOOMAI数量
	 */
	@Excel(name = "累计产出(BOOMAI)", sort = 12)
	private BigDecimal releaseAccumulate;


	@TableField(exist = false)
	@Excel(name = "钱包地址",sort = 1,  width = 40)
	private String userAccount;

	@TableField(exist = false)
	private Integer deleted;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private String createBy;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("days", getDays())
            .append("usdtValue", getUsdtValue())
            .append("validNum1Value", getValidNum1Value())
            .append("lastFtnPrice", getLastFtnPrice())
            .append("haveValidNum1", getHaveValidNum1())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("payTime", getPayTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("bizStatus", getBizStatus())
            .append("isReduced", getIsReduced())
        .toString();
    }
}
