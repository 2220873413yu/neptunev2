package com.xms.web.service;

import java.util.List;

public interface IRedisStreamBizJobService {

	/**
	 * 理财产品订单后的动态奖励结算处理失败
	 * @param list
	 * @return
	 */
	Integer handlerDynamicOrderSettlement(List list);
}
