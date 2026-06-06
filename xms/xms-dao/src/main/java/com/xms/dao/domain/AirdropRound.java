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
 * 空投轮次配置对象 t_airdrop_round
 *
 * @author xms
 * @date 2026-01-01
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_airdrop_round")
public class AirdropRound extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 轮次编号 */
    @Excel(name = "轮次编号")
    @ApiModelProperty(value = "轮次编号")
    private String roundNo;
    /** 0待启用 1启用 2关闭 */
    @Excel(name = "0待启用 1启用 2关闭")
    @ApiModelProperty(value = "0待启用 1启用 2关闭")
    private Integer status;
    /** 本轮总可领取次数 */
    @Excel(name = "本轮总可领取次数")
    @ApiModelProperty(value = "本轮总可领取次数")
    private Long totalQuota;

	/**
	 * 每次领取需支付的价值多少u的OKB数量
	 */
	@Excel(name = "每次领取需支付的价值多少u的OKB数量")
	private BigDecimal okbPayAmount;

    /** 已成功领取次数 */
    @Excel(name = "已成功领取次数")
    @ApiModelProperty(value = "已成功领取次数")
    private Long claimedQuota;
    /** 已锁定未完成次数 */
    @Excel(name = "已锁定未完成次数")
    @ApiModelProperty(value = "已锁定未完成次数")
    private Long lockedQuota;
    /** 每次领取代币数量 */
    @Excel(name = "每次领取代币数量")
    @ApiModelProperty(value = "每次领取代币数量")
    private BigDecimal tokenPerClaim;
    /** 本轮发完是否自动开下一轮 0否 1是 */
    @Excel(name = "本轮发完是否自动开下一轮 0否 1是")
    @ApiModelProperty(value = "本轮发完是否自动开下一轮 0否 1是")
    private Integer autoOpenNext;
    /** 预设下一轮编号，可为空 */
    @Excel(name = "预设下一轮编号，可为空")
    @ApiModelProperty(value = "预设下一轮编号，可为空")
    private String nextRoundNo;
    /** 排序 */
    @Excel(name = "排序")
    @ApiModelProperty(value = "排序")
    private Long sort;

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
            .append("roundNo", getRoundNo())
            .append("status", getStatus())
            .append("totalQuota", getTotalQuota())
            .append("claimedQuota", getClaimedQuota())
            .append("lockedQuota", getLockedQuota())
            .append("tokenPerClaim", getTokenPerClaim())
            .append("autoOpenNext", getAutoOpenNext())
            .append("nextRoundNo", getNextRoundNo())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("sort", getSort())
        .toString();
    }
}
