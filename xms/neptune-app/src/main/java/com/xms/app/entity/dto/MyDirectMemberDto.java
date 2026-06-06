package com.xms.app.entity.dto;

import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 我的直推成员
 */
@Data
public class MyDirectMemberDto {

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 钱包地址
	 */
	private String account;

	/**
	 * 原属性 nodeLevel，节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点
	 */
	private Integer nl;

	/**
	 * 原属性 gameLevel，等级信息
	 */
	private Integer gl;

	/**
	 * 原属性 performance，我的业绩(质押量)
	 */
	private BigDecimal pf;

	/**
	 * 原属性 inviteUserAccount，邀请人地址
	 */
	private String iua;


	/**
	 * 原属性 createTime，创建时间
	 */
	private Date ct;


	/**
	 * 原属性 subNum，直推人数
	 */
	private Integer sub;

	/**
	 * 原属性 umbrellaNum，团队用户数
	 */
	private Integer umb;

	/** 原属性 totalStakeAmount，当前总质押金额 */
	private BigDecimal tsa = BigDecimal.ZERO;

	/**
	 * 原属性 currentDayRate，当前日收益率
	 */
	private BigDecimal cdr = BigDecimal.ZERO;

	/**
	 * 原属性 communityPerformance，小区业绩
	 */
	private BigDecimal cp;
}
