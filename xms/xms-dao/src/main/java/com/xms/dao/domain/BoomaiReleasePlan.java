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
 * boomai收益线性释放计划对象 t_boomai_release_plan
 *
 * @author xms
 * @date 2025-11-19
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_boomai_release_plan")
public class BoomaiReleasePlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户ID",sort = 2)
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 来源类型 0:每日结算,1:加速合并生成 */
    @Excel(name = "来源类型" , sort = 3 ,dictType = "t_boomai_release_plan_source_type")
    @ApiModelProperty(value = "来源类型 0:每日结算,1:加速合并生成")
    private Integer sourceType;
	/**
	 * 订单号
	 */
	@Excel(name = "订单号", sort = 1, width = 30)
	private String orderNo;
    /** 关联订单号/业务单号，可选 */
    /** 本条计划总共要释放的boomai数量，例如12 */
    @Excel(name = "本次计划总产出(BOOMAI)", sort = 4)
    @ApiModelProperty(value = "本条计划总共要释放的boomai数量，例如12")
    private BigDecimal totalAmount;
    /** 已经释放的数量 */
    @Excel(name = "当前已产出(BOOMAI)", sort = 5)
    @ApiModelProperty(value = "已经释放的数量")
    private BigDecimal releasedAmount;
    /** 总释放天数：120/90/60/30/10 */
    @Excel(name = "计划产出周期(天)", sort = 6)
    @ApiModelProperty(value = "总释放天数：120/90/60/30/10")
    private Integer totalDays;
    /** 剩余释放天数 */
    @Excel(name = "剩余产出天数", sort = 7)
    @ApiModelProperty(value = "剩余释放天数")
    private Integer unreleasedDays;
    /** 状态 0:进行中,1:已释放完,2:已被合并作废 */
    @Excel(name = "状态", sort = 8, dictType = "t_boomai_release_plan_status")
    @ApiModelProperty(value = "状态 0:进行中,1:已释放完,2:已被合并作废")
    private Integer status;

	/**
	 * 这条计划的初始释放天数，默认 120
	 */
	private Integer originDays;
	/**
	 * 含义：当前实际释放周期，比如 120 / 90 / 60 / 30 / 10
	 */
	private Integer currentDays;
	/**
	 * 含义：累计已付燃料比例，相对于 120 天的总比例：
	 * 刚创建：0
	 * 加速到 90 天：0.10
	 * 再加速到 30 天：0.20
	 * 用途：
	 * 算本次加速需要补的比例：delta = newRatio - paid_fuel_ratio；
	 * 避免重复收费（只补差额）。
	 */
	private BigDecimal paidFuelRatio;

	/**
	 * 创建日期 yyyymmdd
	 */
	@Excel(name = "创建时间", sort = 9)
	private Integer createDate;

	/**
	 * 用户账号
	 */
	@TableField(exist = false)
	@Excel(name = "钱包地址", sort = 2, width = 40)
	private String userAccount;

	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String createBy;
	private String remark;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("sourceType", getSourceType())
            .append("totalAmount", getTotalAmount())
            .append("releasedAmount", getReleasedAmount())
            .append("totalDays", getTotalDays())
            .append("unreleasedDays", getUnreleasedDays())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
