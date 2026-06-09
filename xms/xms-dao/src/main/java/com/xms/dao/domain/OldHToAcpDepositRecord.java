package com.xms.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xms.common.annotation.Excel;
import com.xms.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 旧系统H换ACP入金记录对象 t_old_h_to_acp_deposit_record
 *
 * @author xms
 * @date 2026-06-07
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_old_h_to_acp_deposit_record")
public class OldHToAcpDepositRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 记录编号 */
	@Excel(name = "记录编号", sort = 1, width = 30)
	@ApiModelProperty(value = "记录编号")
	private String recordNo;

	/** 旧系统订单号/请求号 */
	@Excel(name = "旧系统订单号", sort = 2, width = 30)
	@ApiModelProperty(value = "旧系统订单号/请求号")
	private String oldOrderNo;

	/** 新系统用户ID */
	@Excel(name = "用户ID", sort = 3)
	@ApiModelProperty(value = "新系统用户ID")
	private Long userId;

	/** 用户钱包地址/账号快照 */
	@Excel(name = "钱包地址", sort = 4, width = 40)
	@ApiModelProperty(value = "用户钱包地址/账号快照")
	private String account;

	/** 旧系统传入的已释放H数量 */
	@Excel(name = "旧H数量", sort = 5)
	@ApiModelProperty(value = "旧系统传入的已释放H数量")
	private BigDecimal oldHAmount;

	/** H当时U价快照 */
	@Excel(name = "H单价U", sort = 6)
	@ApiModelProperty(value = "H当时U价快照")
	private BigDecimal hPriceUsdtSnapshot;

	/** 旧H折U价值 */
	@Excel(name = "折U价值", sort = 7)
	@ApiModelProperty(value = "旧H折U价值")
	private BigDecimal oldHUsdtAmount;

	/** ACP当时U价快照 */
	@Excel(name = "ACP单价U", sort = 8)
	@ApiModelProperty(value = "ACP当时U价快照")
	private BigDecimal acpPriceUsdtSnapshot;

	/** 换算后的ACP入金数量 */
	@Excel(name = "ACP入金数量", sort = 9)
	@ApiModelProperty(value = "换算后的ACP入金数量")
	private BigDecimal acpDepositAmount;

	/** 新系统质押订单ID */
	@Excel(name = "质押订单ID", sort = 10)
	@ApiModelProperty(value = "新系统质押订单ID")
	private Long stakeOrderId;

	/** 新系统质押订单号 */
	@Excel(name = "新系统订单号", sort = 11, width = 30)
	@ApiModelProperty(value = "新系统质押订单号")
	private String stakeOrderNo;

	/** 入金来源类型：3旧系统H换ACP入金 */
	@Excel(name = "入金来源", sort = 12, readConverterExp = "3=旧系统H换ACP入金")
	@ApiModelProperty(value = "入金来源类型：3旧系统H换ACP入金")
	private Integer depositSourceType;

	/** 状态：1成功 */
	@Excel(name = "状态", sort = 13, readConverterExp = "1=成功")
	@ApiModelProperty(value = "状态：1成功")
	private Integer status;

	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private Integer deleted;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("id", getId())
			.append("recordNo", getRecordNo())
			.append("oldOrderNo", getOldOrderNo())
			.append("userId", getUserId())
			.append("account", getAccount())
			.append("oldHAmount", getOldHAmount())
			.append("hPriceUsdtSnapshot", getHPriceUsdtSnapshot())
			.append("oldHUsdtAmount", getOldHUsdtAmount())
			.append("acpPriceUsdtSnapshot", getAcpPriceUsdtSnapshot())
			.append("acpDepositAmount", getAcpDepositAmount())
			.append("stakeOrderId", getStakeOrderId())
			.append("stakeOrderNo", getStakeOrderNo())
			.append("depositSourceType", getDepositSourceType())
			.append("status", getStatus())
			.append("remark", getRemark())
			.append("createTime", getCreateTime())
			.append("updateTime", getUpdateTime())
			.toString();
	}
}
