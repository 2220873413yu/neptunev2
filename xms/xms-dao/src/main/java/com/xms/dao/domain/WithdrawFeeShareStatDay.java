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
 * 提现手续费分红对象 t_withdraw_fee_share_stat_day
 *
 * @author xms
 * @date 2025-11-23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_withdraw_fee_share_stat_day")
public class WithdrawFeeShareStatDay extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单号 */
    @Excel(name = "订单号",sort = 1, width = 30)
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /** 统计日期 yyyymmdd */
    //@Excel(name = "统计日期 yyyymmdd")
    @ApiModelProperty(value = "统计日期 yyyymmdd")
    private Long statDate;
    /** 当日提现手续费总额 */
    @Excel(name = "当日提现手续费总额",sort = 2)
    @ApiModelProperty(value = "当日提现手续费总额")
    private BigDecimal totalFee;
    /** 实际分红总额 */
    @Excel(name = "实际分红总额",sort = 3)
    @ApiModelProperty(value = "实际分红总额")
    private BigDecimal distributedFee;
    /** 符合条件的V9用户数 */
    @Excel(name = "符合条件的V9用户数",sort = 4)
    @ApiModelProperty(value = "符合条件的V9用户数")
    private Long userCount;
    /** 人均分得金额 */
    @Excel(name = "人均分得金额",sort = 5)
    @ApiModelProperty(value = "人均分得金额")
    private BigDecimal perUserAmount;
    /** 参与分红快照 userId#account,逗号分隔 */
    //@Excel(name = "参与分红快照 userId#account,逗号分隔")
    @ApiModelProperty(value = "参与分红快照 userId#account,逗号分隔")
    private String shareUserSnapshot;
    /** 未分红原因 */
    //@Excel(name = "未分红原因")
    @ApiModelProperty(value = "未分红原因")
    private String failReason;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("statDate", getStatDate())
            .append("totalFee", getTotalFee())
            .append("distributedFee", getDistributedFee())
            .append("userCount", getUserCount())
            .append("perUserAmount", getPerUserAmount())
            .append("shareUserSnapshot", getShareUserSnapshot())
            .append("failReason", getFailReason())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
        .toString();
    }
}
