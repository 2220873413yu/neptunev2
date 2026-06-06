package com.xms.dao.entity.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现记录Bo对象
 * @author: jiangqf
 * @date: 2020/9/7 15:05
 * @description:
 */
@Data
public class WithdrawalBo {

	/**
	 * 已完成(1,3),处理中(0,1),失败(2,4)
	 * 状态(0.待审核,1.审核成功,2.审核驳回,3:提现成功,4:打款失败)
	 */
	private Integer sts;

	/**
	 * 提现单号
	 */
	private String cod;

	/**
	 * 提现额度
	 */
	private BigDecimal cgb;

	/**
	 * 手续费额度
	 */
	private BigDecimal feb;

	/**
	 * 到账日期
	 */
	private Date crt;

	/**
	 * 创建时间
	 */
	private Date ctt;

	/**
	 * hash
	 */
	private String hsh;
}
