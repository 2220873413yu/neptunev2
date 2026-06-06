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
 * 用户信息快照对象 t_user_info_snapshot
 *
 * @author xms
 * @date 2026-03-16
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_info_snapshot")
public class UserInfoSnapshot extends BaseEntity {
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
    /** 用户id */
    @Excel(name = "用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 钱包地址 */
    @Excel(name = "钱包地址")
    @ApiModelProperty(value = "钱包地址")
    private String account;
    /** 用户编码 */
    @Excel(name = "用户编码")
    @ApiModelProperty(value = "用户编码")
    private String userCode;
    /** 头像 */
    @Excel(name = "头像")
    @ApiModelProperty(value = "头像")
    private String avatar;
    /** 邮箱 */
    @Excel(name = "邮箱")
    @ApiModelProperty(value = "邮箱")
    private String email;
    /** 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点 */
    @Excel(name = "节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点")
    @ApiModelProperty(value = "节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点")
    private Long nodeLevel;
    /** 保底的节点等级 */
    @Excel(name = "保底的节点等级")
    @ApiModelProperty(value = "保底的节点等级")
    private Long minNodeLevel;
    /** 层级等级 考核层级奖 */
    @Excel(name = "层级等级 考核层级奖")
    @ApiModelProperty(value = "层级等级 考核层级奖")
    private Long layerLevel;
    /** 保底的层级等级 */
    @Excel(name = "保底的层级等级")
    @ApiModelProperty(value = "保底的层级等级")
    private Long minLayerLevel;
    /** 等级(0.无 1.S1 2.S2 3.S3 4.S4 5.S5 6.S6,7.S7,8.S8) */
    @Excel(name = "等级(0.无 1.S1 2.S2 3.S3 4.S4 5.S5 6.S6,7.S7,8.S8)")
    @ApiModelProperty(value = "等级(0.无 1.S1 2.S2 3.S3 4.S4 5.S5 6.S6,7.S7,8.S8)")
    private Long gameLevel;
    /** 保底等级 */
    @Excel(name = "保底等级")
    @ApiModelProperty(value = "保底等级")
    private Long minGameLevel;
    /** 邀请用户编码 */
    @Excel(name = "邀请用户编码")
    @ApiModelProperty(value = "邀请用户编码")
    private String inviteUserCode;
    /** 邀请用户id */
    @Excel(name = "邀请用户id")
    @ApiModelProperty(value = "邀请用户id")
    private Long inviteUserId;
    /** 状态(1.正常 2.冻结) */
    @Excel(name = "状态(1.正常 2.冻结)")
    @ApiModelProperty(value = "状态(1.正常 2.冻结)")
    private Long status;
    /** 是否有效用户(0.否 1.是) */
    @Excel(name = "是否有效用户(0.否 1.是)")
    @ApiModelProperty(value = "是否有效用户(0.否 1.是)")
    private Long isValid;
    /** 直推用户数 */
    @Excel(name = "直推用户数")
    @ApiModelProperty(value = "直推用户数")
    private Long subNum;
    /** 直推有效用户数 */
    @Excel(name = "直推有效用户数")
    @ApiModelProperty(value = "直推有效用户数")
    private Long validSubNum;
    /** 团队用户数 */
    @Excel(name = "团队用户数")
    @ApiModelProperty(value = "团队用户数")
    private Long umbrellaNum;
    /** 团队有效用户数 */
    @Excel(name = "团队有效用户数")
    @ApiModelProperty(value = "团队有效用户数")
    private Long validUmbrellaNum;
    /** 我的业绩(质押量) */
    @Excel(name = "我的业绩(质押量)")
    @ApiModelProperty(value = "我的业绩(质押量)")
    private BigDecimal performance;
    /** 直推业绩(矿机) */
    @Excel(name = "直推业绩(矿机)")
    @ApiModelProperty(value = "直推业绩(矿机)")
    private BigDecimal subMining;
    /** 团队业绩(矿机) */
    @Excel(name = "团队业绩(矿机)")
    @ApiModelProperty(value = "团队业绩(矿机)")
    private BigDecimal performanceMining;
    /** 小区业绩(质押量) */
    @Excel(name = "小区业绩(质押量)")
    @ApiModelProperty(value = "小区业绩(质押量)")
    private BigDecimal communityPerformance;
    /** 直推业绩(质押量) */
    @Excel(name = "直推业绩(质押量)")
    @ApiModelProperty(value = "直推业绩(质押量)")
    private BigDecimal subPerformance;
    /** 团队业绩(质押量) */
    @Excel(name = "团队业绩(质押量)")
    @ApiModelProperty(value = "团队业绩(质押量)")
    private BigDecimal umbrellaPerformance;
    /** 父级链 */
    @Excel(name = "父级链")
    @ApiModelProperty(value = "父级链")
    private String parentChain;
    /** USDT 提现开关(1.关 2.开) */
    @Excel(name = "USDT 提现开关(1.关 2.开)")
    @ApiModelProperty(value = "USDT 提现开关(1.关 2.开)")
    private Long withdrawalOpenOrClose;
    /** 最近登录的ip地址 */
    @Excel(name = "最近登录的ip地址")
    @ApiModelProperty(value = "最近登录的ip地址")
    private String lastLoginIp;
    /** 可分红数量 */
    @Excel(name = "可分红数量")
    @ApiModelProperty(value = "可分红数量")
    private BigDecimal dividendAvailableAmount;
    /** 已分红数量 */
    @Excel(name = "已分红数量")
    @ApiModelProperty(value = "已分红数量")
    private BigDecimal distributedAmount;
    /** 是否工作室补贴 0:否,1:是(废弃) */
    @Excel(name = "是否工作室补贴 0:否,1:是(废弃)")
    @ApiModelProperty(value = "是否工作室补贴 0:否,1:是(废弃)")
    private Long hasStudioSubsidyEligible;



	@TableField(exist = false)
	private Integer finaNodeLevel;
	@TableField(exist = false)
	private Integer finaLayerLevel;
	@TableField(exist = false)
	private Integer finaGameLevel;
	@TableField(exist = false)
	private String createBy;

	@TableField(exist = false)
	private String updateBy;

	@TableField(exist = false)
	private String remark;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("snapshotId", getSnapshotId())
            .append("stakeRoundId", getStakeRoundId())
            .append("snapshotTime", getSnapshotTime())
            .append("userId", getUserId())
            .append("account", getAccount())
            .append("userCode", getUserCode())
            .append("avatar", getAvatar())
            .append("email", getEmail())
            .append("nodeLevel", getNodeLevel())
            .append("minNodeLevel", getMinNodeLevel())
            .append("layerLevel", getLayerLevel())
            .append("minLayerLevel", getMinLayerLevel())
            .append("gameLevel", getGameLevel())
            .append("minGameLevel", getMinGameLevel())
            .append("inviteUserCode", getInviteUserCode())
            .append("inviteUserId", getInviteUserId())
            .append("status", getStatus())
            .append("isValid", getIsValid())
            .append("subNum", getSubNum())
            .append("validSubNum", getValidSubNum())
            .append("umbrellaNum", getUmbrellaNum())
            .append("validUmbrellaNum", getValidUmbrellaNum())
            .append("performance", getPerformance())
            .append("subMining", getSubMining())
            .append("performanceMining", getPerformanceMining())
            .append("communityPerformance", getCommunityPerformance())
            .append("subPerformance", getSubPerformance())
            .append("umbrellaPerformance", getUmbrellaPerformance())
            .append("parentChain", getParentChain())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("withdrawalOpenOrClose", getWithdrawalOpenOrClose())
            .append("lastLoginIp", getLastLoginIp())
            .append("deleted", getDeleted())
            .append("dividendAvailableAmount", getDividendAvailableAmount())
            .append("distributedAmount", getDistributedAmount())
            .append("hasStudioSubsidyEligible", getHasStudioSubsidyEligible())
        .toString();
    }
}
