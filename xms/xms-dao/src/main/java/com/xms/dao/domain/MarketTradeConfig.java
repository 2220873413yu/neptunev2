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
 * 交易产品行情数据管理对象 xms_market_trade_config
 *
 * @author xms
 * @date 2025-08-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "xms_market_trade_config")
public class MarketTradeConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**  市场 FX 外汇 Metal 贵金属  Futures  期货 */
    @Excel(name = " 市场 FX 外汇 Metal 贵金属  Futures  期货")
    @ApiModelProperty(value = " 市场 FX 外汇 Metal 贵金属  Futures  期货")
    private String name;
    /** 交易所名称 */
    @Excel(name = "交易所名称")
    @ApiModelProperty(value = "交易所名称")
    private String type;
    /** 是否订阅行情： 0 否 1 是 */
    @Excel(name = "是否订阅行情： 0 否 1 是")
    @ApiModelProperty(value = "是否订阅行情： 0 否 1 是")
    private Long dataType;
    /** 是否订阅盘口  0 否 1 是 */
    @Excel(name = "是否订阅盘口  0 否 1 是")
    @ApiModelProperty(value = "是否订阅盘口  0 否 1 是")
    private Long dataPankou;
    /** 交易对代码组合的 */
    @Excel(name = "交易对代码组合的")
    @ApiModelProperty(value = "交易对代码组合的")
    private String dataCode;
    /** 产品品种类型 比如cl 原油 恒指 HL */
    @Excel(name = "产品品种类型 比如cl 原油 恒指 HL")
    @ApiModelProperty(value = "产品品种类型 比如cl 原油 恒指 HL")
    private String commodityNo;
    /** 合约代码 */
    @Excel(name = "合约代码")
    @ApiModelProperty(value = "合约代码")
    private String contractNo;
    /** 产品名称 */
    @Excel(name = "产品名称")
    @ApiModelProperty(value = "产品名称")
    private String dataLabel;

	/** 产品名称繁体 */
	@Excel(name = "产品名称繁体")
	private String dataLabelHk;

	/** 产品名称英文 */
	@Excel(name = "产品名称英文")
	private String dataLabelEn;

	/** 产品名称日文 */
	@Excel(name = "产品名称日文")
	private String dataLabelJa;

	/** 产品名称韩文 */
	@Excel(name = "产品名称韩文")
	private String dataLabelKr;

	/** 备用字段 */
	@Excel(name = "备用字段")
	private String dataLabel1;

	/** 备用字段 */
	@Excel(name = "备用字段")
	private String dataLabel2;

	/** 备用字段 */
	@Excel(name = "备用字段")
	private String dataLabel3;

	/** 备用字段 */
	@Excel(name = "备用字段")
	private String dataLabel4;




    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("dataType", getDataType())
            .append("dataPankou", getDataPankou())
            .append("dataCode", getDataCode())
            .append("commodityNo", getCommodityNo())
            .append("contractNo", getContractNo())
            .append("dataLabel", getDataLabel())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("deleted", getDeleted())
        .toString();
    }
}
