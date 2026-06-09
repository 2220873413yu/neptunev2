package com.xms.app.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
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

	/**
	 * 旧系统历史业绩(oldHistoryPerformance)
	 */
	private BigDecimal ohpe;

	/**
	 * 旧系统个人业绩
	 */
	private BigDecimal ope;

	/**
	 * 旧系统团队业绩
	 */
	private BigDecimal oUPe;

}
