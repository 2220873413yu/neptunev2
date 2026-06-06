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
 * 矿机订单奖励分发记录对象 t_w3_mining_package_reward_record
 *
 * @author xms
 * @date 2025-04-14
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_mining_package_reward_record")
public class W3MiningPackageRewardRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户ID */
    @Excel(name = "用户UID",sort = 1)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
	/** 订单号 */
	@Excel(name = "订单号",sort = 2)
	@ApiModelProperty(value = "订单号")
	private String orderNo;

    /** 本金 */
    @Excel(name = "本金",sort = 3)
    @ApiModelProperty(value = "本金")
    private BigDecimal fsnValue;
    /** 余额宝 */
    @Excel(name = "余额宝",sort = 4)
    @ApiModelProperty(value = "余额宝")
    private BigDecimal validNum3;
    /** 利率 */
    @Excel(name = "利率%",sort = 5)
    @ApiModelProperty(value = "利率")
    private BigDecimal ratio;

	/**
	 * 静态奖励
	 */
	@Excel(name = "预计发放利息",sort = 5)
	private BigDecimal staticReward;
	/**
	 * 最终发放静态奖励
	 */
	@Excel(name = "最终发放利息",sort = 6)
	private BigDecimal finalStaticReward;

	/**
	 * 余额宝奖励
	 */
	@Excel(name = "预计余额宝利息",sort = 7)
	private BigDecimal validNum3Reward;
	/**
	 * 最终发放余额宝奖励
	 */
	@Excel(name = "最终发放余额宝利息",sort = 8)
	private BigDecimal finalValidNum3Reward;

	/**
	 * 类型 0:活期,1:固定
	 */
	@Excel(name = "类型",sort = 9,readConverterExp="0=活期,1=定期")
	private Integer type;

    /** 日期 例如20250313 */
    //@Excel(name = "日期 例如20250313")
    @ApiModelProperty(value = "日期 例如20250313")
    private String date;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("fsnValue", getFsnValue())
            .append("validNum3", getValidNum3())
            .append("ratio", getRatio())
            .append("orderNo", getOrderNo())
            .append("createTime", getCreateTime())
            .append("date", getDate())
        .toString();
    }
}
