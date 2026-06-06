package com.xms.app.service;

import com.xms.app.entity.OrderReq;
import com.xms.app.entity.bo.DestroyCallbackBo;
import com.xms.app.entity.bo.RechargeCallbackBo;
import com.xms.app.entity.dto.RechargeRecordDto;
import com.xms.app.entity.req.CreateRechargeOrder;
import com.xms.app.entity.req.JuNotifyReq;
import com.xms.app.entity.resp.CreateOrderResp;
import com.xms.app.entity.vo.RechargeVo;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.dao.domain.RechargeRecord;
import com.xms.dao.domain.UserRechangeAddress;

import java.util.List;

public interface BizRechargeService {

	/**
	 * 充值记录
	 * @param lastId
	 * @return
	 */
	ResultPista<List<RechargeRecordDto>> listRechargeRecord(Long lastId);

	/**
	 * 创建充值订单
	 * @param req
	 * @return
	 */
    ResultPista<CreateOrderResp> createOrder(CreateRechargeOrder req);

	/**
	 * 充值回调
	 * @param req
	 * @return
	 */
	ResultPista<String> rechargeCallback(DestroyCallbackBo req);

    void addRechargeLog(JuNotifyReq req);
}
