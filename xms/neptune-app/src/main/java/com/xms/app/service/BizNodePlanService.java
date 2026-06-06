package com.xms.app.service;

import com.xms.app.entity.req.BuyNodePlanReq;
import com.xms.app.entity.req.CreatePositionOrderReq;
import com.xms.app.entity.req.NodePackageReq;
import com.xms.app.entity.resp.BuyNodePlanResp;
import com.xms.app.entity.vo.NodePlanVo;
import com.xms.common.core.domain.api.ResultPista;
import jakarta.validation.Valid;

import java.util.List;

public interface BizNodePlanService {
	/**
	 * 获取节点信息
	 * @return
	 */
	ResultPista<List<NodePlanVo>> nodePlanInfo();

	/**
	 * 购买节点身份,返回订单号和支付金额
	 * @param req 请求参数
	 * @return 返回订单号和支付金额
	 * @throws Exception
	 */
	ResultPista<BuyNodePlanResp> createOrder(BuyNodePlanReq req, Long userId);

	ResultPista<String> nodeIdentityCallback(NodePackageReq req);

	/**
	 * 创建购买贡献分订单
	 * @param req
	 * @param userId
	 * @return
	 */
	ResultPista<BuyNodePlanResp> createPositionOrder(CreatePositionOrderReq req, Long userId);
}
