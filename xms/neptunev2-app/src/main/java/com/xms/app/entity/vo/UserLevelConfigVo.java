package com.xms.app.entity.vo;

import java.math.BigDecimal;

public class UserLevelConfigVo {

	/** 原属性 level，等级编码 */
	private Integer lv;

	/** 原属性 performance，个人业绩 */
	private BigDecimal pf;

	/** 原属性 umbrellaPerformance，小区业绩 */
	private BigDecimal up;

	/** 原属性 rewardRatio，级差奖比例 */
	private BigDecimal rr;

	/** 原属性 hasStudioSubsidy，是否有工作室补贴 */
	private Integer hss;

	/** 原属性 peerRewardRatio，平级奖比例 */
	private BigDecimal prr;

	/** 原属性 minBuyAmount，最少买入贡献分数量 */
	private BigDecimal mba;

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public BigDecimal getPf() {
		return pf;
	}

	public void setPf(BigDecimal pf) {
		this.pf = pf;
	}

	public BigDecimal getUp() {
		return up;
	}

	public void setUp(BigDecimal up) {
		this.up = up;
	}

	public BigDecimal getRr() {
		return rr;
	}

	public void setRr(BigDecimal rr) {
		this.rr = rr;
	}

	public Integer getHss() {
		return hss;
	}

	public void setHss(Integer hss) {
		this.hss = hss;
	}

	public BigDecimal getPrr() {
		return prr;
	}

	public void setPrr(BigDecimal prr) {
		this.prr = prr;
	}

	public BigDecimal getMba() {
		return mba;
	}

	public void setMba(BigDecimal mba) {
		this.mba = mba;
	}
}
