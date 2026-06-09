package com.xms.app.entity.vo;

import java.math.BigDecimal;

public class DynamicRewardPageVo {

	/** 原属性 gameLevel，用户等级 */
	private Integer gl;
	/** 原属性 todayDiffReward，今日极差收益 */
	private BigDecimal tdr;
	/** 原属性 todayEqualReward，今日平级奖励 */
	private BigDecimal ter;
	/** 原属性 todayLevelReward，今日层级奖收益 */
	private BigDecimal tlr;
	/** 原属性 todayNewReward，今日新增奖收益 */
	private BigDecimal tnr;
	/** 原属性 todayNodeEquityDividendIncome，今日节点权益分红收益 */
	private BigDecimal tnedi;
	/** 原属性 wealthVaultInfo，财富仓信息 */
	private UserWealthVaultInfoVo wvi;
	/** 原属性 todayTotalDynamicReward，今日累计动态 */
	private BigDecimal ttdr;
	/** 原属性 totalWithdrawDynamic，本轮累计提现动态 */
	private BigDecimal twd;
	/** 原属性 totalWithdrawValidNum4，累计提现财富仓 */
	private BigDecimal twv4;
	/** 原属性 minBuyAmount，最低起购h代币数量 */
	private BigDecimal mba;
	/** 原属性 pointsRatio，h代币兑换积分比例 */
	private BigDecimal pr;

	public Integer getGl() { return gl; }
	public void setGl(Integer gl) { this.gl = gl; }
	public BigDecimal getTdr() { return tdr; }
	public void setTdr(BigDecimal tdr) { this.tdr = tdr; }
	public BigDecimal getTer() { return ter; }
	public void setTer(BigDecimal ter) { this.ter = ter; }
	public BigDecimal getTlr() { return tlr; }
	public void setTlr(BigDecimal tlr) { this.tlr = tlr; }
	public BigDecimal getTnr() { return tnr; }
	public void setTnr(BigDecimal tnr) { this.tnr = tnr; }
	public BigDecimal getTnedi() { return tnedi; }
	public void setTnedi(BigDecimal tnedi) { this.tnedi = tnedi; }
	public UserWealthVaultInfoVo getWvi() { return wvi; }
	public void setWvi(UserWealthVaultInfoVo wvi) { this.wvi = wvi; }
	public BigDecimal getTtdr() { return ttdr; }
	public void setTtdr(BigDecimal ttdr) { this.ttdr = ttdr; }
	public BigDecimal getTwd() { return twd; }
	public void setTwd(BigDecimal twd) { this.twd = twd; }
	public BigDecimal getTwv4() { return twv4; }
	public void setTwv4(BigDecimal twv4) { this.twv4 = twv4; }
	public BigDecimal getMba() { return mba; }
	public void setMba(BigDecimal mba) { this.mba = mba; }
	public BigDecimal getPr() { return pr; }
	public void setPr(BigDecimal pr) { this.pr = pr; }
}
