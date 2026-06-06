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

import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 平台币每日价格对象 t_ptb_daily_price
 *
 * @author xms
 * @date 2025-08-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_ptb_daily_price")
public class PtbDailyPrice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 价格日期 格式为 年月日 20250809 */
    //@Excel(name = "统计日期 ")
    @ApiModelProperty(value = "价格日期")
    private Long date;

	@Excel(name = "统计日期",sort = 1)
	@TableField(exist = false)
	private String dateStr;

	/** 当日价格 */
    @Excel(name = "当日价格",sort = 2)
    @ApiModelProperty(value = "当日价格")
    private BigDecimal price;

	/**
	 * 币种类型 1:BOOMMAI,2:MAI
	 */
	@Excel(name = "币种",sort = 3,dictType ="t_coin_type" )
	private Integer coinType;
	/**
	 * 涨跌幅 比例值(比如5 表示涨5%)
	 */
	@Excel(name = "涨跌幅%",sort = 4)
	private BigDecimal changeRate;
	@TableField(exist = false)
	private Date updateTime;
	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("date", getDate())
            .append("price", getPrice())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
        .toString();
    }
}
