package com.xms.app.entity.vo;

import java.math.BigDecimal;

public class UserInfoDetailVo {

	/** 原属性 userId，用户id */
	private Long userId;

	/** 原属性 account，用户钱包地址 */
	private String account;

	/** 原属性 userCode，用户编码 */
	private String uc;

	/** 原属性 gameLevel，等级 */
	private Integer gl;

	/** 原属性 nodeLevel，节点等级 */
	private Integer nl;

	/** 原属性 inviteUserCode，邀请用户编码 */
	private String iuc;

	/** 原属性 inviteUserId，邀请用户id */
	private Long iuid;

	/** 原属性 isValid，是否有效用户 */
	private Integer iv;

	/** 原属性 layerLevel，层级等级 */
	private Integer ll;

	/** 原属性 subNum，直推用户数 */
	private Integer sub;

	/** 原属性 umbrellaNum，团队用户数 */
	private Integer umb;

	/** 原属性 subPerformance，直推业绩 */
	private BigDecimal sp;

	/** 原属性 umbrellaPerformance，团队业绩 */
	private BigDecimal up;

	/** 原属性 communityPerformance，小区业绩 */
	private BigDecimal cp;

	/** 原属性 performance，我的业绩 */
	private BigDecimal pf;

	/** 原属性 historyPerformance，我的业绩(当前质押量) */
	private BigDecimal hp;

	/** 原属性 stakeAccount，质押次数 */
	private Long sa;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUc() {
		return uc;
	}

	public void setUc(String uc) {
		this.uc = uc;
	}

	public Integer getGl() {
		return gl;
	}

	public void setGl(Integer gl) {
		this.gl = gl;
	}

	public Integer getNl() {
		return nl;
	}

	public void setNl(Integer nl) {
		this.nl = nl;
	}

	public String getIuc() {
		return iuc;
	}

	public void setIuc(String iuc) {
		this.iuc = iuc;
	}

	public Long getIuid() {
		return iuid;
	}

	public void setIuid(Long iuid) {
		this.iuid = iuid;
	}

	public Integer getIv() {
		return iv;
	}

	public void setIv(Integer iv) {
		this.iv = iv;
	}

	public Integer getLl() {
		return ll;
	}

	public void setLl(Integer ll) {
		this.ll = ll;
	}

	public Integer getSub() {
		return sub;
	}

	public void setSub(Integer sub) {
		this.sub = sub;
	}

	public Integer getUmb() {
		return umb;
	}

	public void setUmb(Integer umb) {
		this.umb = umb;
	}

	public BigDecimal getSp() {
		return sp;
	}

	public void setSp(BigDecimal sp) {
		this.sp = sp;
	}

	public BigDecimal getUp() {
		return up;
	}

	public void setUp(BigDecimal up) {
		this.up = up;
	}

	public BigDecimal getCp() {
		return cp;
	}

	public void setCp(BigDecimal cp) {
		this.cp = cp;
	}

	public BigDecimal getPf() {
		return pf;
	}

	public void setPf(BigDecimal pf) {
		this.pf = pf;
	}

	public BigDecimal getHp() {
		return hp;
	}

	public void setHp(BigDecimal hp) {
		this.hp = hp;
	}

	public Long getSa() {
		return sa;
	}

	public void setSa(Long sa) {
		this.sa = sa;
	}
}
