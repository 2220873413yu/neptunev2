package com.xms.dao.entity.vo;

import lombok.Data;

/**
 * 在用户结算的时候用到的vo对象
 * @Description:
 * @Author: luog
 * @Date: 2020/5/12 10:07
 */
@Data
public class ParentUserTaskVo {
	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 第几代
	 */
	private int distance;

	/**
	 * 层级奖等级
	 */
	private Integer layerLevel;

	/**
	 * 虚拟层级奖
	 */
	private Integer minLayerLevel;

	/**
	 * 节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点
	 */
	private Integer nodeLevel;

	/**
	 * 虚拟节点等级奖励
	 */
	private Integer minNodeLevel;

	/**
	 * 等级
	 */
	private Integer gameLevel;


	/**
	 * 虚拟等级
	 */
	private Integer minGameLevel;

	/**
	 * 是否有效 0 无效 1 有效
	 */
	private Integer isValid;

	/**
	 * 有效的直推数量
	 */
	private Integer validSubNum;
}
