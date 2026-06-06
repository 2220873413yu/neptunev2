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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 用户余额快照对象 t_user_money_snapshot
 *
 * @author xms
 * @date 2026-03-16
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_money_snapshot")
public class UserMoneySnapshot extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 快照主键id */
    private Long snapshotId;
    /** 轮次id */
    @Excel(name = "轮次id")
    @ApiModelProperty(value = "轮次id")
    private Long stakeRoundId;
    /** 快照时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "快照时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date snapshotTime;
    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 节点收益 */
    @Excel(name = "节点收益")
    @ApiModelProperty(value = "节点收益")
    private BigDecimal validNum1;
    /** 静态 */
    @Excel(name = "静态")
    @ApiModelProperty(value = "静态")
    private BigDecimal validNum2;
    /** 动态 */
    @Excel(name = "动态")
    @ApiModelProperty(value = "动态")
    private BigDecimal validNum3;
    /** 财富 */
    @Excel(name = "财富")
    @ApiModelProperty(value = "财富")
    private BigDecimal validNum4;
    /** H代币(/魔盒/手续费) */
    @Excel(name = "H代币(/魔盒/手续费)")
    @ApiModelProperty(value = "H代币(/魔盒/手续费)")
    private BigDecimal validNum5;
    /** 工作室收益 */
    @Excel(name = "工作室收益")
    @ApiModelProperty(value = "工作室收益")
    private BigDecimal validNum6;
    /** 贡献分 */
    @Excel(name = "贡献分")
    @ApiModelProperty(value = "贡献分")
    private BigDecimal validNum7;
    /** 今日可提现动态 */
    @Excel(name = "今日可提现动态")
    @ApiModelProperty(value = "今日可提现动态")
    private BigDecimal validNum8;
    /** 可用余额数 */
    @Excel(name = "可用余额数")
    @ApiModelProperty(value = "可用余额数")
    private BigDecimal validNum9;


	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private String remark;
	@TableField(exist = false)
	private Date createTime;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("snapshotId", getSnapshotId())
            .append("stakeRoundId", getStakeRoundId())
            .append("snapshotTime", getSnapshotTime())
            .append("id", getId())
            .append("validNum1", getValidNum1())
            .append("validNum2", getValidNum2())
            .append("validNum3", getValidNum3())
            .append("validNum4", getValidNum4())
            .append("validNum5", getValidNum5())
            .append("validNum6", getValidNum6())
            .append("validNum7", getValidNum7())
            .append("validNum8", getValidNum8())
            .append("validNum9", getValidNum9())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
        .toString();
    }
}
