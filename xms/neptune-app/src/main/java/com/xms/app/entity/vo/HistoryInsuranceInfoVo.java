package com.xms.app.entity.vo;

import java.math.BigDecimal;

public class HistoryInsuranceInfoVo {

	/** 原属性 totalStakeAmount，当前总质押金额 */
	private BigDecimal tsa;

	/** 原属性 personalLossAmount，个人亏损额 */
	private BigDecimal pla;

	/** 原属性 remainingCompensationLimit，剩余可赔付 */
	private BigDecimal rcl;

	/** 原属性 allCompensationLimit，可赔付额度 */
	private BigDecimal acl;

	/** 原属性 insuranceQualifyStatus，保险仓赔付资格状态 */
	private Integer iqs;

	/** 原属性 insuranceCompensationQualifyStatus，领取赔付是否有资格 */
	private Integer icqs;

	public BigDecimal getTsa() {
		return tsa;
	}

	public void setTsa(BigDecimal tsa) {
		this.tsa = tsa;
	}

	public BigDecimal getPla() {
		return pla;
	}

	public void setPla(BigDecimal pla) {
		this.pla = pla;
	}

	public BigDecimal getRcl() {
		return rcl;
	}

	public void setRcl(BigDecimal rcl) {
		this.rcl = rcl;
	}

	public BigDecimal getAcl() {
		return acl;
	}

	public void setAcl(BigDecimal acl) {
		this.acl = acl;
	}

	public Integer getIqs() {
		return iqs;
	}

	public void setIqs(Integer iqs) {
		this.iqs = iqs;
	}

	public Integer getIcqs() {
		return icqs;
	}

	public void setIcqs(Integer icqs) {
		this.icqs = icqs;
	}
}
