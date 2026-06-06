package com.xms.common.mq.dynamic;

import lombok.Data;

/**
 * 购买基金订单、赎回订单传输对象
 * @description:
 * @author: xms
 * @date: 2022/7/26 10:04
 */
@Data
public class OrderMsgDO {
	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 业务类型 1:质押的业务处理,2:爆仓检测,3:爆仓事件处理
	 */
	private Integer bizType;

	/**
	 * 用户地址
	 */
	private String address;
}
