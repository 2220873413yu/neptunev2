package com.xms.app.service;

import com.xms.app.entity.bo.BuyPointsCallbackBo;
import com.xms.app.entity.bo.StakeOrderCallbackBo;
import com.xms.common.core.domain.api.ResultPista;

/**
 * 质押业务接口类
 */
public interface BizStakeService {
	/**
	 * 质押订单回调
	 * @param req
	 * @return
	 */
	ResultPista<String> stakeOrderCallback(StakeOrderCallbackBo req);

	/**
	 * 购买积分回调
	 * @param req
	 * @return
	 */
	ResultPista<String> buyPointsCallback(BuyPointsCallbackBo req);
}
