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
 * 每日利息汇总对象 xms_interest_stat_day
 *
 * @author xms
 * @date 2025-11-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "xms_interest_stat_day")
public class InterestStatDay extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户ID */
    @Excel(name = "用户ID",sort = 1)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /** 订单号 */
    @Excel(name = "订单号",sort = 2, width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /** 结息日期 yyyymmdd */
    @Excel(name = "结息日期",sort = 3)
    @ApiModelProperty(value = "结息日期 yyyymmdd")
    private Long statDate;
    /** 当日总利息 */
    @Excel(name = "当日总利息",sort = 4)
    @ApiModelProperty(value = "当日总利息")
    private BigDecimal todayInterest;



	@TableField(exist = false)
	private String remark;
	@TableField(exist = false)
	private Date updateTime;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	@Excel(name = "钱包地址",sort = 1, width = 40)
	private String userAccount;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("orderNo", getOrderNo())
            .append("statDate", getStatDate())
            .append("todayInterest", getTodayInterest())
            .append("createTime", getCreateTime())
        .toString();
    }
}
