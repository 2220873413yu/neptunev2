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
import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * AI分析行情对象 t_ai_market_insight
 *
 * @author xms
 * @date 2025-09-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_ai_market_insight")
public class AiMarketInsight extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 标题 */
    @Excel(name = "标题")
    @ApiModelProperty(value = "标题")
    private String title;
    /** 副标题 */
    @Excel(name = "副标题")
    @ApiModelProperty(value = "副标题")
    private String subTitle;
    /** 内容 */
    @Excel(name = "内容")
    @ApiModelProperty(value = "内容")
    private String content;
    /** 状态（0正常 1关闭） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=关闭")
    private String status;
    /** 语言类型 1:简体中文,2:繁体,3:英文,4:日本,5:韩文 */
    @Excel(name = "语言类型 1:简体中文,2:繁体,3:英文,4:日本,5:韩文")
    @ApiModelProperty(value = "语言类型 1:简体中文,2:繁体,3:英文,4:日本,5:韩文")
    private Integer type;
    /** 封面图 */
    @Excel(name = "封面图")
    @ApiModelProperty(value = "封面图")
    private String image;
    /** 内容图 */
    @Excel(name = "内容图")
    @ApiModelProperty(value = "内容图")
    private String contentImage;

	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("subTitle", getSubTitle())
            .append("content", getContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("type", getType())
            .append("image", getImage())
            .append("contentImage", getContentImage())
        .toString();
    }
}
