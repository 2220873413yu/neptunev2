package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.xms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.math.BigDecimal;
import java.util.Date;

import com.xms.common.annotation.Excel;

/**
 * 闪兑记录对象 t_flash_exchange_record
 *
 * @author xms
 * @date 2025-08-14
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_flash_exchange_record")
public class FlashExchangeRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户ID",sort = 1)
    private Long userId;
    /** 类型 0:u兑换平台币,1:平台币兑换u */
    @Excel(name = "闪兑类型",sort = 2,dictType = "t_flash_exchange_record_type")
    private Integer type;
    /** 闪兑金额 */
    @Excel(name = "闪兑金额",sort = 3)
    @ApiModelProperty(value = "闪兑金额")
    private BigDecimal balance;
    /** 手续费 */
    @Excel(name = "手续费",sort = 4)
    @ApiModelProperty(value = "手续费")
    private BigDecimal fee;
    /** 手续费比例 */
    @Excel(name = "手续费比例",sort = 5)
    @ApiModelProperty(value = "手续费比例")
    private BigDecimal feeRatio;

	/** 平台币价格 */
	@Excel(name = "SMA价格",sort = 6)
	private BigDecimal ptbPrice;

	/** 到账金额 */
	@Excel(name = "到账金额",sort = 7)
	@ApiModelProperty(value = "到账金额")
	private BigDecimal receivedAmount;

	/** 创建者 */
	@TableField(exist = false)
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private Date updateTime;
	@TableField(exist = false)
	private String remark;

	/**
	 * 用户账号
	 */
	@TableField(exist = false)
	@Excel(name = "用户账号", sort = 1)
	private String account;

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("type", getType())
            .append("balance", getBalance())
            .append("fee", getFee())
            .append("feeRatio", getFeeRatio())
            .append("createTime", getCreateTime())
        .toString();
    }
}
