package com.xms.dao.entity.bo;

import com.xms.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 *
 * @since 2023-07-25
 */
@Data
public class UserInfoBo{


	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 用户钱包地址
	 */
	private String account;

	/**
	 * 用户编码
	 */
	private String userCode;

		/**
	 * 等级 0:A0,1:A1,2:A2,3:A3,4:A4,5:A5,6:A6,7:A7,8:A8,9:A9,10:A10
	 *
	 *
	 */
	private Integer gameLevel;
	/**
	 * 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点
	 */
	private Integer nodeLevel;

	/**
	 * 邀请用户编码
	 */
	private String inviteUserCode;

	/**
	 * 邀请用户id
	 */
	private Long inviteUserId;

	/**
	 * 是否有效用户(0.否 1.是)
	 */
	private Integer isValid;

	/**
	 * 层级等级
	 */
	private Integer layerLevel;

	/**
	 * 直推用户数
	 */
	private Integer subNum;

	/**
	 * 团队用户数
	 */
	private Integer umbrellaNum;

	/**
	 * 直推业绩(质押量)
	 */
	private BigDecimal subPerformance;

	/**
	 * 团队业绩(质押量)
	 */
	private BigDecimal umbrellaPerformance;

	/**
	 * 小区业绩(质押量)
	 */
	private BigDecimal communityPerformance;

	/**
	 * 我的业绩(质押量)
	 */
	private BigDecimal performance;

	/**
	 * 我的业绩(当前质押量)
	 */
	private BigDecimal historyPerformance;

	/**
	 * 质押次数 大于0才显示邀请链接
	 */
	private Long stakeAccount;


	/**
	 * 旧系统历史业绩
	 */
	private BigDecimal oldHistoryPerformance;

	/**
	 * 旧系统个人业绩
	 */
	private BigDecimal oldPerformance;

	/**
	 * 旧系统团队业绩
	 */
	private BigDecimal oldUmbrellaPerformance;

//	/**
//	 * 我的业绩(节点数量)
//	 */
//	private BigDecimal performance;

//	/**
//	 * 直推用户数(有效)
//	 */
//	private Integer validSubNum;

//	/**
//	 * 团队用户数(有效)
//	 */
//	private Integer validUmbrellaNum;




}
