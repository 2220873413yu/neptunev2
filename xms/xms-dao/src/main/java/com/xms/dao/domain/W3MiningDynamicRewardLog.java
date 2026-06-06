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
 * 挖矿动态奖日志对象 t_w3_mining_dynamic_reward_log
 *
 * @author xms
 * @date 2025-04-15
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_w3_mining_dynamic_reward_log")
public class W3MiningDynamicRewardLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 变动额度 */
    @Excel(name = "变动额度")
    @ApiModelProperty(value = "变动额度")
    private BigDecimal changeBalance;
    /** 来源订单 */
    @Excel(name = "来源订单")
    @ApiModelProperty(value = "来源订单")
    private String sourceCode;
    /** 来源类型(1.充值 2.提现 3.推荐奖 4.级差奖 5.平级奖 6.购买套餐 7.平台扣拨) */
    @Excel(name = "来源类型(1.充值 2.提现 3.推荐奖 4.级差奖 5.平级奖 6.购买套餐 7.平台扣拨)")
    @ApiModelProperty(value = "来源类型(1.充值 2.提现 3.推荐奖 4.级差奖 5.平级奖 6.购买套餐 7.平台扣拨)")
    private Long sourceType;
    /** 来源用户ID */
    @Excel(name = "来源用户ID")
    @ApiModelProperty(value = "来源用户ID")
    private Long sourceId;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("changeBalance", getChangeBalance())
            .append("sourceCode", getSourceCode())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
        .toString();
    }
}
