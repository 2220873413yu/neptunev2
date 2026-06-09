package com.xms.dao.entity.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xms.common.annotation.Excel;
import com.xms.common.core.domain.BaseXmsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @since 2023-07-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_info")
@ApiModel(value = "UserInfo对象", description = "用户信息表")
public class UserInfo extends BaseXmsEntity {

	private static final long serialVersionUID = 1L;



	/**
	 * 用户id
	 */
	@TableId(value = "user_id", type = IdType.AUTO)
	@Excel(name = "用户ID", sort = 1)
	private Long userId;


	/**
	 * 用户编码
	 */
	@Excel(name = "用户编码", sort = 2)
	private String userCode;


	/**
	 * 钱包地址
	 */
	@Excel(name = "钱包地址", sort = 3, width = 40)
	private String account;


	/**
	 * 邀请用户编码
	 */
	@Excel(name = "邀请用户编码", sort = 3)
	private String inviteUserCode;




	/**
	 * 邀请用户id
	 */
	@Excel(name = "邀请用户ID", sort = 4)
	private Long inviteUserId;

	/**
	 * 是否有效用户(0.否 1.是)
	 */
	@Excel(name = "是否有效用户", sort = 5,readConverterExp="0=否,1=是")
	private Integer isValid;

	/**
	 * 是否工作室补贴 0:否,1:是
	 */
	@Excel(name = "是否工作室补贴", sort = 5,readConverterExp="0=否,1=是")
	private Integer hasStudioSubsidyEligible;

	/**
	 * 头像 废弃
	 */
	//@Excel(name = "头像", sort = 2)
	private String avatar;

	/**
	 * 邮箱 废弃
	 */
	//@Excel(name = "邮箱", sort = 3)
	private String email;

	/**
	 * 用户等级
	 */
	@Excel(name = "等级", sort = 5, dictType = "t_user_info_game_level")
	private Integer gameLevel;

	/**
	 * 层级等级
	 */
	@Excel(name = "层级等级", sort = 5, dictType = "t_user_invest_layer_config_level")
	private Integer layerLevel;

	/**
	 * 保底层级等级
	 */
	@Excel(name = "保底层级等级", sort = 5, dictType = "t_user_invest_layer_config_level")
	private Integer minLayerLevel;

	/**
	 * 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点
	 */
	@Excel(name = "节点等级", sort = 5, dictType = "t_node_plan_node_level")
	private Integer nodeLevel;

	/**
	 * 保底节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点
	 */
	@Excel(name = "保底节点等级", sort = 5, dictType = "t_node_plan_node_level")
	private Integer minNodeLevel;


	/**
	 * 我的业绩(质押量)
	 */
	@Excel(name = "我的业绩(质押量)", sort = 6)
	private BigDecimal performance;

	/**
	 * 旧系统个人业绩
	 */
	@Excel(name = "旧系统个人业绩", sort = 6)
	private BigDecimal oldPerformance;

	/**
	 * 当前轮业绩
	 */
	@Excel(name = "当前轮业绩", sort = 6)
	private BigDecimal historyPerformance;

	/**
	 * 旧系统历史业绩
	 */
	@Excel(name = "旧系统历史业绩", sort = 6)
	private BigDecimal oldHistoryPerformance;


	/**
	 * 直推业绩(质押量)
	 */
	@Excel(name = "直推业绩(质押量)", sort = 6)
	private BigDecimal subPerformance;

	/**
	 * 旧系统直推业绩
	 */
	@Excel(name = "旧系统直推业绩", sort = 6)
	private BigDecimal oldSubPerformance;

	/**
	 * 小区业绩(质押量)
	 */
	@Excel(name = "小区业绩(质押量)", sort = 6)
	private BigDecimal communityPerformance;

	/**
	 * 团队业绩(质押量)
	 */
	@Excel(name = "团队业绩(质押量)", sort = 6)
	private BigDecimal umbrellaPerformance;

	/**
	 * 旧系统团队业绩
	 */
	@Excel(name = "旧系统团队业绩", sort = 6)
	private BigDecimal oldUmbrellaPerformance;

	/**
	 * 直推用户数
	 */
	@Excel(name = "直推用户数", sort = 8)
	@ApiModelProperty(value = "直推用户数")
	private Integer subNum;

	/**
	 * 直推有效用户数(暂时废弃)
	 */
	//@Excel(name = "直推有效用户数", sort = 8)
	private Integer validSubNum;

	/**
	 * 团队用户数
	 */
	@Excel(name = "团队用户数", sort = 8)
	private Integer umbrellaNum;

	/**
	 * 团队用户数(有效)(暂时废弃)
	 */
	//@Excel(name = "团队用户数(有效)", sort = 8)
	private Integer validUmbrellaNum;



	/**
	 *状态 1 正常 2 冻结
	 */
	@Excel(name = "账户状态", sort = 9,dictType = "t_user_info_status")
	private Integer status;



	/** USDT 提现开关(1.关 2.开) */
	@Excel(name = "USDT提现开关",dictType = "biz_open_or_close",sort = 11)
	private Integer withdrawalOpenOrClose;

	/**
	 * 虚拟等级
	 * 查询用户详情、查询直推用户列表、领取矿机订单奖励、返回能领几周、分复利手续费奖励
	 *
	 */
	//@Excel(name = "虚拟等级", sort = 6, dictType = "t_user_info_game_level")
	private Integer minGameLevel;


	/** 父级链 */
	//@Excel(name = "父级链")
	private String parentChain;
	@TableField(exist = false)
	private Integer finaNodeLevel;
	@TableField(exist = false)
	private Integer finaLayerLevel;
	@TableField(exist = false)
	private Integer finaGameLevel;

	/**
	 * 后台管理页面-树结构使用场景
	 */
	@TableField(exist = false)
	private Long parentId;

	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;

	/**
	 * 删除标志 0正常 1删除
	 */
	private Integer deleted;



	/**
	 * 获取父级用户ID列表
	 *
	 * 根据parentChain字段解析出所有父级用户的ID列表
	 * parentChain是以逗号分隔的父级用户ID字符串
	 *
	 * @return 父级用户ID列表，如果parentChain为空则返回空列表
	 */
	public List<Long> getParentIds() {
		if (StrUtil.isBlank(this.getParentChain())) {
			return new ArrayList<>();
		}
		// 解析成list<Long> 按照,号分割
		return Arrays.stream(this.getParentChain().split(","))
			.map(String::trim)
			.filter(s -> !s.isEmpty())
			.map(Long::valueOf)
			.collect(Collectors.toList());
	}

	public BigDecimal getEffectivePerformance() {
		return zeroIfNull(this.performance).add(zeroIfNull(this.oldPerformance));
	}

	public BigDecimal getEffectiveHistoryPerformance() {
		return zeroIfNull(this.historyPerformance).add(zeroIfNull(this.oldHistoryPerformance));
	}

	public BigDecimal getEffectiveSubPerformance() {
		return zeroIfNull(this.subPerformance).add(zeroIfNull(this.oldSubPerformance));
	}

	public BigDecimal getEffectiveUmbrellaPerformance() {
		return zeroIfNull(this.umbrellaPerformance).add(zeroIfNull(this.oldUmbrellaPerformance));
	}

	private BigDecimal zeroIfNull(BigDecimal amount) {
		return amount == null ? BigDecimal.ZERO : amount;
	}
}
