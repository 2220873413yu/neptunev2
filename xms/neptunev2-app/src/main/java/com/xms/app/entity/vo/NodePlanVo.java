package com.xms.app.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 认购节点配置VO
 *
 * @author xms
 * @date 2026-01-16
 */
@Data
public class NodePlanVo {

	/** 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点 */
	private Integer nll;

	/** 确认成功的销量 */
	private Long sqo;

	/** 认购金额 */
	private BigDecimal pam;

	/** 权重系数 */
	private BigDecimal wcf;

	/** 工作室补贴 例如:1 就是1% */
	private BigDecimal ssr;

	/** 全网静态收益 例如:1 就是1% */
	private String gsi;
}
