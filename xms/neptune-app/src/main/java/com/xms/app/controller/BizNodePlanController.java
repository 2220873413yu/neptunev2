package com.xms.app.controller;

import com.xms.app.entity.req.BuyNodePlanReq;
import com.xms.app.entity.req.CreatePositionOrderReq;
import com.xms.app.entity.resp.BuyNodePlanResp;
import com.xms.app.entity.vo.NodePlanVo;
import com.xms.app.service.BizNodePlanService;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 节点业务相关
 */
@Api(tags = "节点业务相关")
@RestController
@RequestMapping("/api/np")
public class BizNodePlanController {

	@Autowired
	private BizNodePlanService bizCardService;

	/**
	 * 获取节点信息
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "获取节点信息")
	@GetMapping(value = "/nio")
	public ResultPista<List<NodePlanVo>> nodePlanInfo()  throws Exception{
		return bizCardService.nodePlanInfo();
	}


	/**
	 * 购买节点身份,返回订单号和支付金额
	 * @param req 请求参数
	 * @return 返回订单号和支付金额
	 * @throws Exception
	 */
	@ApiOperation(value = "购买节点")
	@PostMapping(value = "/cor")
	@RepeatSubmit
	public ResultPista<BuyNodePlanResp> createOrder(@Valid @RequestBody BuyNodePlanReq req)  throws Exception{
		return bizCardService.createOrder(req, SecurityUtils.getLoginAppUser().getUserId());
	}

	/**
	 * 创建购买贡献分订单
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "创建购买贡献分订单")
	@PostMapping(value = "/cpor")
	@RepeatSubmit
	public ResultPista<BuyNodePlanResp> createPositionOrder(@Valid @RequestBody CreatePositionOrderReq req)  throws Exception{
		return bizCardService.createPositionOrder(req, SecurityUtils.getLoginAppUser().getUserId());
	}
}
