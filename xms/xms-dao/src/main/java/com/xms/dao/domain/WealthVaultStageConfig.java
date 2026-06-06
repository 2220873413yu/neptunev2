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
 * 财富仓阶段解锁配置对象 t_wealth_vault_stage_config
 *
 * @author xms
 * @date 2026-03-11
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_wealth_vault_stage_config")
public class WealthVaultStageConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 阶段号 1-5 */
    @Excel(name = "阶段号 1-5")
    @ApiModelProperty(value = "阶段号 1-5")
    private Integer stageNo;
    /** 达到该价格后可解锁 */
    @Excel(name = "达到该价格后可解锁")
    @ApiModelProperty(value = "达到该价格后可解锁")
    private BigDecimal unlockPrice;
    /** 阶段名称 */
    @Excel(name = "阶段名称")
    @ApiModelProperty(value = "阶段名称")
    private String stageName;

	@TableField(exist = false)
	private String updateBy;
	@TableField(exist = false)
	private String createBy;
	@TableField(exist = false)
	private Integer deleted;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("stageNo", getStageNo())
            .append("unlockPrice", getUnlockPrice())
            .append("stageName", getStageName())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
        .toString();
    }
}
