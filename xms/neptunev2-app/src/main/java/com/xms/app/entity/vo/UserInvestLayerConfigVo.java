package com.xms.app.entity.vo;

import java.math.BigDecimal;

public class UserInvestLayerConfigVo {

	/** 原属性 level，层级编码 */
	private Integer lv;

	/** 原属性 minInvest，最低投资额度 */
	private BigDecimal mi;

	/** 原属性 layerCount，可获得层数 */
	private Integer lc;

	/** 原属性 rewardRatio，层级奖励比例 */
	private BigDecimal rr;

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public BigDecimal getMi() {
		return mi;
	}

	public void setMi(BigDecimal mi) {
		this.mi = mi;
	}

	public Integer getLc() {
		return lc;
	}

	public void setLc(Integer lc) {
		this.lc = lc;
	}

	public BigDecimal getRr() {
		return rr;
	}

	public void setRr(BigDecimal rr) {
		this.rr = rr;
	}
}
