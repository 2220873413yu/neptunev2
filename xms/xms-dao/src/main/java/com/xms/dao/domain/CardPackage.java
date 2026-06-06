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
 * 卡片套餐对象 t_card_package
 *
 * @author xms
 * @date 2025-12-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_card_package")
public class CardPackage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 价格/U */
    @Excel(name = "价格/U")
    @ApiModelProperty(value = "价格/U")
    private BigDecimal price;
    /** 卡片图片 */
    @Excel(name = "卡片图片")
    @ApiModelProperty(value = "卡片图片")
    private String image;
    /** 算力 */
    @Excel(name = "算力")
    @ApiModelProperty(value = "算力")
    private BigDecimal computingPower;
    /** 卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡 */
    @Excel(name = "卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡")
    @ApiModelProperty(value = "卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡")
    private Integer cardType;
    /** 销量 */
    @Excel(name = "销量")
    @ApiModelProperty(value = "销量")
    private String sales;
    /** 是否上架 0:否,1:是 */
    @Excel(name = "是否上架 0:否,1:是")
    @ApiModelProperty(value = "是否上架 0:否,1:是")
    private Integer status;
    /** 排序 */
    @Excel(name = "排序")
    @ApiModelProperty(value = "排序")
    private Integer sort;

	/**
	 * 赠送valid_num3积分比例 例如5就是5%
	 */
	private BigDecimal validNum3GiftRatio;

	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("price", getPrice())
            .append("image", getImage())
            .append("computingPower", getComputingPower())
            .append("cardType", getCardType())
            .append("sales", getSales())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("sort", getSort())
            .append("remark", getRemark())
        .toString();
    }
}
