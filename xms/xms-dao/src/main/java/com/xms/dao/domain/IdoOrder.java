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
import java.util.Map;

import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * ido订单记录对象 t_ido_order
 *
 * @author xms
 * @date 2025-12-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_ido_order")
public class IdoOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id 如果没有存在用户系统为0 */
    @Excel(name = "用户ID",sort = 1)
    @ApiModelProperty(value = "用户id 如果没有存在用户系统为0")
    private Long userId;
    /** 购买的钱包地址 */
    @Excel(name = "钱包地址",sort = 2, width = 30)
    @ApiModelProperty(value = "购买的钱包地址")
    private String address;
    /** 购买份数 */
    @Excel(name = "购买份数",sort = 3)
    @ApiModelProperty(value = "购买份数")
    private Integer shares;
    /** 支付了多少金额 */
    @Excel(name = "支付金额",sort = 4)
    @ApiModelProperty(value = "支付了多少金额")
    private BigDecimal okbPaid;
    /** 交易hash */
    @Excel(name = "交易hash",sort = 5, width = 30)
    @ApiModelProperty(value = "交易hash")
    private String txHash;
    /** 时间戳 毫秒 */
//    @Excel(name = "时间戳 毫秒")
//    @ApiModelProperty(value = "时间戳 毫秒")
    private Long timestamp;
    /** 区块号 */
//    @Excel(name = "区块号")
//    @ApiModelProperty(value = "区块号")
    private String blockNumber;
    /** 处理状态 1:待处理,2:已处理,3:未注册丢弃 */
    @Excel(name = "处理状态", sort = 5 , dictType = "t_ido_order_biz_status")
    private Integer bizStatus;


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
            .append("userId", getUserId())
            .append("address", getAddress())
            .append("shares", getShares())
            .append("okbPaid", getOkbPaid())
            .append("txHash", getTxHash())
            .append("timestamp", getTimestamp())
            .append("blockNumber", getBlockNumber())
            .append("createTime", getCreateTime())
            .append("bizStatus", getBizStatus())
        .toString();
    }
}
