package com.xms.common.mq.dynamic;

import java.util.List;

/**
 * 购买卡片/升级卡片之后的业务
 */
public interface AsyncDynamicOrderSettlementService {
	/**
	 * 购买卡片/升级卡片之后的业务
	 */
	public void sendMessage(List<OrderMsgDO> orderMsgDOList);


	/**
	 * 跨链分发之后的订单处理
	 * @param performanceUpdateVO
	 */
	public void sendMessage(UserPerformanceUpdateVO performanceUpdateVO);
}
