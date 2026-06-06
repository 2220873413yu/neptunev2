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
 * 认购节点配置对象 t_node_plan
 *
 * @author xms
 * @date 2026-01-16
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_node_plan")
public class NodePlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点 */
    @Excel(name = "节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点")
    @ApiModelProperty(value = "节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点")
    private Integer nodeLevel;
    /** 节点名称_英文 */
    @Excel(name = "节点名称_英文")
    @ApiModelProperty(value = "节点名称_英文")
    private String nodeNameEn;
    /** 节点名称_简体中文 */
    @Excel(name = "节点名称_简体中文")
    @ApiModelProperty(value = "节点名称_简体中文")
    private String nodeNameHk;

    /** 认购金额 */
    @Excel(name = "认购金额", readConverterExp = "美=元")
    private BigDecimal purchaseAmount;
    /** 确认成功的销量 */
    @Excel(name = "确认成功的销量")
    @ApiModelProperty(value = "确认成功的销量")
    private Long soldQuota;
    /** 权重系数 */
    @Excel(name = "权重系数")
    private BigDecimal weightCoefficient;
    /** 工作室补贴 例如:1 就是1% */
    @Excel(name = "工作室补贴 例如:1 就是1%")
    private BigDecimal studioSubsidyRatio;

	/**
	 * 年化收益率
	 */
	@Excel(name = "年化收益率")
    private BigDecimal annualRate;
    /** 状态 是否启用 */
    @Excel(name = "状态 1:启用,0:停用")
    @ApiModelProperty(value = "状态 1:启用,0:停用")
    private Integer status;
    /** 排序值 */
    @Excel(name = "排序值")
    @ApiModelProperty(value = "排序值")
    private Long sortOrder;


	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private Integer deleted;

	@TableField(exist = false)
	private String remark;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("nodeLevel", getNodeLevel())
            .append("nodeNameEn", getNodeNameEn())
            .append("nodeNameHk", getNodeNameHk())
            .append("purchaseAmount", getPurchaseAmount())
            .append("soldQuota", getSoldQuota())
            .append("status", getStatus())
            .append("sortOrder", getSortOrder())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
