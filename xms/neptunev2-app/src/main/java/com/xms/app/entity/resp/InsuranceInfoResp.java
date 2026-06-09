package com.xms.app.entity.resp;

import com.xms.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 保险仓资格相关
 */
@Data
public class InsuranceInfoResp {
	//本轮是否投资过字段
	/**
	 * true 表示已投资过
	 * false 表示未投资过
	 */
	private boolean isInvest;

	/**
	 * 保险仓赔付资格状态 0:无资格,1:有资格
	 */
	private Integer insuranceQualifyStatus = 0;

	/**
	 * 累计提现金额（历史已提现总额）
	 */
	private BigDecimal totalWithdrawal = BigDecimal.ZERO;

	/**
	 * 累计提现额度（历史可提现总额度上限）
	 */
	private BigDecimal cumulativeWithdrawalQuota = BigDecimal.ZERO;



	/**
	 * 月提现额度（每月可提现上限）
	 */
	@Excel(name = "月提现额度")
	private BigDecimal monthlyWithdrawalQuota = BigDecimal.ZERO;

	/**
	 * 当月已提现金额
	 */
	@Excel(name = "当月已提现")
	private BigDecimal currentMonthWithdrawn = BigDecimal.ZERO;

}
