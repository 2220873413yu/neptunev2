package com.xms.app.entity.vo;

import java.math.BigDecimal;

public class InsuranceInfoVo {

	/** 原属性 isInvest，本轮是否投资过 */
	private boolean iv;

	/** 原属性 insuranceQualifyStatus，保险仓赔付资格状态 */
	private Integer iqs;

	/** 原属性 totalWithdrawal，累计提现金额 */
	private BigDecimal tw;

	/** 原属性 cumulativeWithdrawalQuota，累计提现额度 */
	private BigDecimal cwq;

	/** 原属性 monthlyWithdrawalQuota，月提现额度 */
	private BigDecimal mwq;

	/** 原属性 currentMonthWithdrawn，当月已提现金额 */
	private BigDecimal cmw;

	public boolean isIv() {
		return iv;
	}

	public void setIv(boolean iv) {
		this.iv = iv;
	}

	public Integer getIqs() {
		return iqs;
	}

	public void setIqs(Integer iqs) {
		this.iqs = iqs;
	}

	public BigDecimal getTw() {
		return tw;
	}

	public void setTw(BigDecimal tw) {
		this.tw = tw;
	}

	public BigDecimal getCwq() {
		return cwq;
	}

	public void setCwq(BigDecimal cwq) {
		this.cwq = cwq;
	}

	public BigDecimal getMwq() {
		return mwq;
	}

	public void setMwq(BigDecimal mwq) {
		this.mwq = mwq;
	}

	public BigDecimal getCmw() {
		return cmw;
	}

	public void setCmw(BigDecimal cmw) {
		this.cmw = cmw;
	}
}
