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
 * 币种价格配置对象 t_coin_price
 *
 * @author xms
 * @date 2025-11-19
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_coin_price")
public class CoinPrice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 币种类型 1:BOOMMAI,2:MAI */
    @Excel(name = "币种类型 1:BOOMMAI,2:MAI")
    @ApiModelProperty(value = "币种类型 1:BOOMMAI,2:MAI")
    private Long coinType;
    /** 币种符号, 如 BOOMMAI/MAI */
    @Excel(name = "币种符号, 如 BOOMMAI/MAI")
    @ApiModelProperty(value = "币种符号, 如 BOOMMAI/MAI")
    private String symbol;
    /** 初始价格 */
    @Excel(name = "初始价格")
    @ApiModelProperty(value = "初始价格")
    private BigDecimal initPrice;

    /** 当前价格 */
    @Excel(name = "当前价格")
    @ApiModelProperty(value = "当前价格")
    private BigDecimal currentPrice;
    /** 状态 1:启用,0:停用 */
    @Excel(name = "状态 1:启用,0:停用")
    @ApiModelProperty(value = "状态 1:启用,0:停用")
    private Long status;

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
            .append("coinType", getCoinType())
            .append("symbol", getSymbol())
            .append("initPrice", getInitPrice())
            .append("currentPrice", getCurrentPrice())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
