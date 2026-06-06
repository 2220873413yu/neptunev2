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
 * 空投领取记录对象 t_airdrop_claim
 *
 * @author xms
 * @date 2026-01-01
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_airdrop_claim")
public class AirdropClaim extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 领取流水号/锁定号 */
    @Excel(name = "流水号",sort = 1, width = 30)
    @ApiModelProperty(value = "领取流水号/锁定号")
    private String claimNo;
    /** 轮次编号 */
    @Excel(name = "空投编号", sort = 2, width = 30)
    @ApiModelProperty(value = "轮次编号")
    private String roundNo;
    /** 用户ID */
    @Excel(name = "用户ID", sort = 3)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    /** 1:支付中,2:成功,3:超时,4:超时但支付 */
    @Excel(name = "支付状态", sort = 4 ,dictType = "t_airdrop_claim_status")
    @ApiModelProperty(value = "1支付中 2成功 3超时 4取消")
    private Integer status;

	/** 本次应发放的代币数量 */
	@Excel(name = "领取数量",sort = 5)
	@ApiModelProperty(value = "本次应发放的代币数量")
	private BigDecimal tokenAmount;

	/** 支付金额 */
	@Excel(name = "支付OKB数量",sort = 6)
	private BigDecimal payAmount;

	/**
	 * 每次领取需支付的价值多少u的OKB数量
	 */
	@Excel(name = "支付价值u数量",sort = 6)
	private BigDecimal okbPayAmount;

	/** 链上交易哈希 */
	@Excel(name = "链上交易哈希", sort = 7, width = 60)
	@ApiModelProperty(value = "链上交易哈希")
	private String txHash;


	/**
	 * 业务状态是否处理 0:否,1:是
	 */
	@Excel(name = "业务状态是否处理", sort = 7,readConverterExp = "0=否,1=是")
	private Integer bizStatus;

	/**
	 * 是否要处理升级业务 0:否,1:是
	 */
	@Excel(name = "是否要处理升级业务 0:否,1:是", sort = 7,readConverterExp = "0=否,1=是")
	private Integer bizStatus1;

    /** 锁定超时时间，默认锁定时间+5分钟 */
    @Excel(name = "锁定超时时间", sort = 8, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lockExpireAt;


	/** 领取时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "领取时间", sort = 9, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date completedAt;

	/** 创建时间 */
	@Excel(name = "创建时间", sort = 10)
	private Integer createDate;

    /** 支付币种(废弃) */
//    @Excel(name = "支付币种")
//    @ApiModelProperty(value = "支付币种")
    private String payToken;


    /** 锁定时间(废弃) */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "锁定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lockedAt;



    /** 超时/取消/失败原因(废弃) */
//    @Excel(name = "超时/取消/失败原因")
//    @ApiModelProperty(value = "超时/取消/失败原因")
    private String releaseReason;




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
            .append("claimNo", getClaimNo())
            .append("roundNo", getRoundNo())
            .append("userId", getUserId())
            .append("status", getStatus())
            .append("lockExpireAt", getLockExpireAt())
            .append("tokenAmount", getTokenAmount())
            .append("txHash", getTxHash())
            .append("payToken", getPayToken())
            .append("payAmount", getPayAmount())
            .append("lockedAt", getLockedAt())
            .append("completedAt", getCompletedAt())
            .append("releaseReason", getReleaseReason())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
