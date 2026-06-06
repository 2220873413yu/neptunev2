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
 * 卡片持有信息对象 t_user_card_asset
 *
 * @author xms
 * @date 2025-12-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_card_asset")
public class UserCardAsset extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 普通卡数量 */
    @Excel(name = "普通卡数量")
    @ApiModelProperty(value = "普通卡数量")
    private Integer cardLevel1;
    /** 白银卡数量 */
    @Excel(name = "白银卡数量")
    @ApiModelProperty(value = "白银卡数量")
    private Integer cardLevel2;
    /** 白金卡数量 */
    @Excel(name = "白金卡数量")
    @ApiModelProperty(value = "白金卡数量")
    private Integer cardLevel3;
    /** 黑金卡数量 */
    @Excel(name = "黑金卡数量")
    @ApiModelProperty(value = "黑金卡数量")
    private Integer cardLevel4;

	@TableField(exist = false)
	private Date updateTime;
	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private String remark;
	@TableField(exist = false)
	private Integer deleted;

	@TableField(exist = false)
	private String nickName;
	@TableField(exist = false)
	private String userCode;
	@TableField(exist = false)
	private String avatar;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cardLevel1", getCardLevel1())
            .append("cardLevel2", getCardLevel2())
            .append("cardLevel3", getCardLevel3())
            .append("cardLevel4", getCardLevel4())
            .append("createTime", getCreateTime())
        .toString();
    }
}
