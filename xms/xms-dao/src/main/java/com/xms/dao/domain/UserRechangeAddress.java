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
import com.xms.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

/**
 * 用户充值地址对象 t_user_rechange_address
 *
 * @author xms
 * @date 2025-08-04
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_rechange_address")
public class UserRechangeAddress extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 用户id */
    @Excel(name = "用户ID",sort = 1)
    @ApiModelProperty(value = "用户id")
    private Long userId;
    /** 链 0:暂无,2510:BEP-20,60:ERC-20,195:TRC-20 */
    @Excel(name = "链",sort = 2,dictType = "chain_type_enum")
    private Long chainId;
    /** 充值地址 */
    @Excel(name = "充值地址",sort = 3,width = 30)
    @ApiModelProperty(value = "充值地址")
    private String address;

	/** 创建者 */
	@TableField(exist = false)
	private String createBy;
	/** 更新者 */
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private String remark;
	@TableField(exist = false)
	private Integer deleted;
	@TableField(exist = false)
	private Date updateTime;
	/**
	 * 用户账号
	 */
	@TableField(exist = false)
	@Excel(name = "用户账号",sort = 2)
	private String userAccount;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("chainId", getChainId())
            .append("address", getAddress())
            .append("createTime", getCreateTime())
        .toString();
    }
}
