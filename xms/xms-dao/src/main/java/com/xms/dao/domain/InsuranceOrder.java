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
 * 保险仓释放订单对象 t_insurance_order
 *
 * @author xms
 * @date 2026-03-11
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_insurance_order")
public class InsuranceOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单号 */
    @Excel(name = "订单号",sort = 1,width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /** 矿机天数 */
    @Excel(name = "矿机天数",sort = 2)
    @ApiModelProperty(value = "矿机天数")
    private Integer days;
    /** 剩余天数 */
    @Excel(name = "剩余天数",sort = 3)
    @ApiModelProperty(value = "剩余天数")
    private Integer haveDays;
    /** 日产出 */
    @Excel(name = "日产出",sort = 4)
    @ApiModelProperty(value = "日产出")
    private BigDecimal dayOutReward;
        /** 剩余保险仓产出 */
        @Excel(name = "剩余保险仓产出",sort = 5)
        @ApiModelProperty(value = "剩余保险仓产出")
        private BigDecimal hsaveInsuranceBalance;
    /** 保险仓余额 */
    @Excel(name = "保险仓余额",sort = 6)
    @ApiModelProperty(value = "保险仓余额")
    private BigDecimal insuranceBalance;

    /** 状态 0:释放中,1:已结束 */
    @Excel(name = "订单状态",sort = 7,readConverterExp = "0=释放中,1=已结束")
    @ApiModelProperty(value = "状态 0:释放中,1:已结束")
    private Integer status;
    /** 轮次表id */
    @Excel(name = "轮次表编号",sort = 8)
    @ApiModelProperty(value = "轮次表id")
    private Long stakeRoundId;

	@TableField(exist = false)
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("days", getDays())
            .append("haveDays", getHaveDays())
            .append("dayOutReward", getDayOutReward())
            .append("insuranceBalance", getInsuranceBalance())
            .append("hsaveInsuranceBalance", getHsaveInsuranceBalance())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("stakeRoundId", getStakeRoundId())
        .toString();
    }
}
