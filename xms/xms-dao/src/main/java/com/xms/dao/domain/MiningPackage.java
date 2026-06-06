package com.xms.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 基金套餐对象 t_mining_package
 *
 * @author xms
 * @date 2025-08-07
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_mining_package")
public class MiningPackage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 基金名称_cn */
    @Excel(name = "基金名称_cn")
    @ApiModelProperty(value = "基金名称_cn")
    private String nameCn;
    /** 基金类型 0:活期,1:定期,2:体验式基金 */
    @Excel(name = "基金类型 0:活期,1:定期,2:体验式基金")
    @ApiModelProperty(value = "基金类型 0:活期,1:定期")
    private Integer type;
    /** 基金有效期天数 */
    @Excel(name = "基金有效期天数")
    @ApiModelProperty(value = "基金有效期天数")
    private Integer day;
    /** 日利率 */
    @Excel(name = "日利率")
    @ApiModelProperty(value = "日利率")
    private BigDecimal dayRatio;
    /** 销量 */
    @Excel(name = "销量")
    @ApiModelProperty(value = "销量")
    private Integer buyNum;
    /** 最少购买金额限制 */
    @Excel(name = "最少购买金额限制")
    @ApiModelProperty(value = "最少购买金额限制")
    private BigDecimal minBuyPrice;
	/** 最大购买金额限制 */
    @Excel(name = "最大购买金额限制")
    @ApiModelProperty(value = "最大购买金额限制")
    private BigDecimal maxBuyPrice;
    /** 每日违约金递减率(如0.5%) */
    @Excel(name = "每日违约金递减率(如0.5%)")
    @ApiModelProperty(value = "每日违约金递减率(如0.5%)")
    private BigDecimal dailyPenaltyReduction;
    /** 违约金比例(如20%) */
    @Excel(name = "违约金比例(如20%)")
    @ApiModelProperty(value = "违约金比例(如20%)")
    private BigDecimal penaltyRate;
	/** 最低违约金比例 */
    @Excel(name = "最低违约金比例")
    @ApiModelProperty(value = "最低违约金比例")
    private BigDecimal minPenaltyRate;
    /** 基金名称_hk 繁体 */
    @Excel(name = "基金名称_hk 繁体")
    @ApiModelProperty(value = "基金名称_hk 繁体")
    private String nameHk;
    /** 基金名称_en 英文 */
    @Excel(name = "基金名称_en 英文")
    @ApiModelProperty(value = "基金名称_en 英文")
    private String nameEn;
    /** 基金名称_ja 日文 */
    @Excel(name = "基金名称_ja 日文")
    @ApiModelProperty(value = "基金名称_ja 日文")
    private String nameJa;
    /** 基金名称_kr 韩文 */
    @Excel(name = "基金名称_kr 韩文")
    @ApiModelProperty(value = "基金名称_kr 韩文")
    private String nameKr;
    /** 是否上架 0:否,1:是 */
    @Excel(name = "是否上架 0:否,1:是")
    @ApiModelProperty(value = "是否上架 0:否,1:是")
    private Long status;
    /** 排序 */
    @Excel(name = "排序")
    @ApiModelProperty(value = "排序")
    private Long sort;
    /** 描述_中文 */
    @Excel(name = "描述_中文")
    @ApiModelProperty(value = "描述_中文")
    private String descriptionCn;
    /** 描述_hk 繁体 */
    @Excel(name = "描述_hk 繁体")
    @ApiModelProperty(value = "描述_hk 繁体")
    private String descriptionHk;
    /** 描述_en 英文 */
    @Excel(name = "描述_en 英文")
    @ApiModelProperty(value = "描述_en 英文")
    private String descriptionEn;
    /** 描述_ja 日文 */
    @Excel(name = "描述_ja 日文")
    @ApiModelProperty(value = "描述_ja 日文")
    private String descriptionJa;
    /** 描述_kr 韩文 */
    @Excel(name = "描述_kr 韩文")
    @ApiModelProperty(value = "描述_kr 韩文")
    private String descriptionKr;

	/** 创建者 */
	@TableField(exist = false)
	@JsonIgnore
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	@JsonIgnore
	private String updateBy;
	@JsonIgnore
	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("nameCn", getNameCn())
            .append("type", getType())
            .append("day", getDay())
            .append("dayRatio", getDayRatio())
            .append("buyNum", getBuyNum())
            .append("minBuyPrice", getMinBuyPrice())
            .append("dailyPenaltyReduction", getDailyPenaltyReduction())
            .append("penaltyRate", getPenaltyRate())
            .append("nameHk", getNameHk())
            .append("nameEn", getNameEn())
            .append("nameJa", getNameJa())
            .append("nameKr", getNameKr())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("sort", getSort())
            .append("descriptionCn", getDescriptionCn())
            .append("descriptionHk", getDescriptionHk())
            .append("descriptionEn", getDescriptionEn())
            .append("descriptionJa", getDescriptionJa())
            .append("descriptionKr", getDescriptionKr())
        .toString();
    }
}
