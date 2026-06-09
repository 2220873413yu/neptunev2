package com.xms.app.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xms.app.entity.bo.*;
import com.xms.app.entity.req.NodePackageReq;
import com.xms.app.entity.req.SwapOrderCallbackReq;
import com.xms.app.service.*;
import com.xms.common.annotation.Anonymous;
import com.xms.common.constant.SysConstant;
import com.xms.common.core.domain.api.ResultPista;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 回调相关
 *
 *
 * @since 2023-06-12
 */
@Api(tags = "回调相关")
@RestController
@RequestMapping("/api")
public class OpenController {


	@Autowired
	private BizWithdrawalService bizWithdrawalService;

	@Autowired
	private BizNodePlanService bizNodePlanService;

	@Autowired
	private BizStakeService bizStakeService;


	/**
	 * 提现回调
	 */
	@PostMapping("/withdrawal/callback")
	@Anonymous
	public ResultPista<String> withdrawalCallback(@Validated @RequestBody WithdrawalCallbackBo req) {
		return bizWithdrawalService.withdrawalCallback(req);
	}

	/**
	 * 用户购买节点身份回调
	 * @param req
	 * @return 返回 200是成功
	 */
	@PostMapping("/notify/nodeIdentity")
	@Anonymous
	public ResultPista<String> nodeIdentityCallback(@Validated @RequestBody NodePackageReq req) {
		return bizNodePlanService.nodeIdentityCallback(req);
	}

	/**
	 * 旧系统H入金回调
	 */
	@PostMapping("/oldH/deposit/callback")
	@Anonymous
	public ResultPista<String> oldHToAcpDepositCallback(@Validated @RequestBody OldHToAcpDepositCallbackBo req) {
		return bizStakeService.oldHToAcpDepositCallback(req);
	}

	/**
	 * 质押订单回调
	 */
	@PostMapping("/stakeOrder/callback")
	@Anonymous
	public ResultPista<String> stakeOrderCallback(@Validated @RequestBody StakeOrderCallbackBo req) {
		return bizStakeService.stakeOrderCallback(req);
	}

	/**
	 * h代币购买积分回调事件
	 */
	@PostMapping("/buyPointsCallback/callback")
	@Anonymous
	public ResultPista<String> buyPointsCallback(@Validated @RequestBody BuyPointsCallbackBo req) {
		return bizStakeService.buyPointsCallback(req);
	}


//	/**
//	 * 充值回调
//	 */
//	@PostMapping("/notify/recharge")
//	@Anonymous
//	public ResultPista<String> rechargeCallback(@Validated @RequestBody DestroyCallbackBo req) {
//		return bizRechargeService.rechargeCallback(req);
//	}

//
//	/**
//	 * swap订单回调(链上进行swap的时候进行回调)
//	 */
//	@PostMapping("/notify/swapOrder")
//	@Anonymous
//	public ResultPista<String> swapOrderCallback(@Validated @RequestBody SwapOrderCallbackReq req) {
//		return bizMiningService.swapOrderCallback(req);
//	}
//
//	/**
//	 * 用户支付成功 创建激活码订单，回调接口(支付激活币)
//	 */
//	@PostMapping("/activeOrder/callback")
//	@Anonymous
//	public ResultPista<String> activeOrderCallback(@Validated @RequestBody DestroyCallbackBo req) {
//		return bizCardService.activeOrderCallback(req);
//	}
//
//	/**
//	 * 领取空投回调
//	 */
//	@PostMapping("/claimAirdrop/callback")
//	@Anonymous
//	public ResultPista<String> claimAirdropCallback(@Validated @RequestBody DestroyCallbackBo req) {
//		return bizCardService.claimAirdropCallback(req);
//	}



}
