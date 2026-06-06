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
 * 挖矿套餐对象 t_w3_mining_package
 *
 * @author xms
 * @date 2025-04-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_mining_package")
public class W3MiningPackage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 套餐名称 */
    @Excel(name = "套餐名称")
    @ApiModelProperty(value = "套餐名称")
    private String name;
    /** 套餐类型 0:活期,1:定期 */
    @Excel(name = "套餐类型 0:活期,1:定期")
    @ApiModelProperty(value = "套餐类型 0:活期,1:定期")
    private Integer type;
    /** 套餐有效期天数 */
    @Excel(name = "套餐有效期天数")
    @ApiModelProperty(value = "套餐有效期天数")
    private Integer day;
    /** 日利率 */
    @Excel(name = "日利率")
    @ApiModelProperty(value = "日利率")
    private BigDecimal dayRatio;
    /** 收益倍数 */
    @Excel(name = "收益倍数")
    @ApiModelProperty(value = "收益倍数")
    private BigDecimal multipliedValue;
    /** 销量 */
    @Excel(name = "销量")
    @ApiModelProperty(value = "销量")
    private String buyNum;
    /** 最少购买金额限制 */
    @Excel(name = "最少购买金额限制")
    @ApiModelProperty(value = "最少购买金额限制")
    private BigDecimal minBuyPrice;

	/** 最大购买数量限制 */
	@Excel(name = "最大购买数量限制")
	@ApiModelProperty(value = "最大购买数量限制")
	private Integer buyMaxLimit;
    /** USDT支付占比 */
    @Excel(name = "USDT支付占比")
    @ApiModelProperty(value = "USDT支付占比")
    private BigDecimal usdtRatio;
    /** FTN支付占比 */
    @Excel(name = "FTN支付占比")
    @ApiModelProperty(value = "FTN支付占比")
    private BigDecimal wfRatio;
    /** 是否上架 0:否,1:是 */
    @Excel(name = "是否上架")
    @ApiModelProperty(value = "是否上架 0:否,1:是")
    private Integer status;

	@Excel(name = "排序")
	private Integer sort;

	/** 创建者 */
	@TableField(exist = false)
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
	/** remark */
	@TableField(exist = false)
	private String remark;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("day", getDay())
            .append("dayRatio", getDayRatio())
            .append("multipliedValue", getMultipliedValue())
            .append("buyNum", getBuyNum())
            .append("minBuyPrice", getMinBuyPrice())
            .append("usdtRatio", getUsdtRatio())
            .append("wfRatio", getWfRatio())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
        .toString();
    }
}
