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
 * 活期矿机领取日志对象 t_w3_mining_user_performance_log
 *
 * @author xms
 * @date 2025-04-26
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_mining_user_performance_log")
public class W3MiningUserPerformanceLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 业绩 */
    @Excel(name = "业绩")
    @ApiModelProperty(value = "业绩")
    private BigDecimal performance;
    /** 业务订单号 */
    @Excel(name = "业务订单号")
    @ApiModelProperty(value = "业务订单号")
    private String orderNo;
    /** 业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务 */
    @Excel(name = "业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务")
    @ApiModelProperty(value = "业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务")
    private Integer bizStatus;
    /** 订单id集合 ,号拆分 */
    @Excel(name = "订单id集合 ,号拆分")
    @ApiModelProperty(value = "订单id集合 ,号拆分")
    private String ids;
    /** 价值多少usdt的业绩 ,号拆分 */
    @Excel(name = "价值多少usdt的业绩 ,号拆分")
    @ApiModelProperty(value = "价值多少usdt的业绩 ,号拆分")
    private String usdtValuds;


	/** 创建者 */
	@TableField(exist = false)
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
	/** remark */
	@TableField(exist = false)
	private String remark;
	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("performance", getPerformance())
            .append("orderNo", getOrderNo())
            .append("bizStatus", getBizStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("ids", getIds())
            .append("usdtValuds", getUsdtValuds())
        .toString();
    }
}
