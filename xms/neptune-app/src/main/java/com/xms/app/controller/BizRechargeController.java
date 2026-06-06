//package com.xms.app.controller;
//
//
//import com.xms.app.entity.dto.RechargeRecordDto;
//import com.xms.app.entity.req.CreateRechargeOrder;
//import com.xms.app.entity.resp.CreateOrderResp;
//import com.xms.app.entity.vo.CreateDestroyOrderVo;
//import com.xms.app.service.BizRechargeService;
//import com.xms.common.annotation.RepeatSubmit;
//import com.xms.common.core.domain.api.ResultPista;
//import com.xms.common.exception.ServiceException;
//import com.xms.common.utils.SecurityUtils;
//import com.xms.dao.domain.RechargeRecord;
//import com.xms.dao.domain.UserRechangeAddress;
//import com.xms.dao.service.XmsCommonService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * 用户充值相关 前端控制器
// *
// *
// * @since 2023-06-12
// */
//@Api(tags = "用户充值相关")
//@RestController
//@RequestMapping("/api/recharge")
//public class BizRechargeController {
//
//	@Autowired
//	private BizRechargeService bizRechargeService;
//
//	@Autowired
//	private XmsCommonService xmsCommonServiceImpl;
//
//
//
//	/**
//	 * 充值MAI(USDT->MAI)
//	 * @param req 充值请求参数
//	 * @return 订单号
//	 * @throws Exception
//	 */
////	@ApiOperation(value = "充值MAI(USDT->MAI)")
////	@PostMapping(value = "/createOrder")
////	@RepeatSubmit
////	public ResultPista<CreateOrderResp> createOrder(@Valid @RequestBody CreateRechargeOrder req)  throws Exception{
////	/*	ResultPista resultPista = xmsCommonServiceImpl.checkMineSettleTime();
////		if (!resultPista.isSuccess()) {
////			throw new ServiceException(resultPista.getMsg());
////		}*/
////		return bizRechargeService.createOrder(req);
////	}
//
//	/**
//	 * 充值记录
//	 *
//	 * @param lastId   当前记录最后一个ID
//	 * @return
//	 */
//	@ApiOperation("充值记录")
//	@GetMapping("/listRechargeRecord")
//	public ResultPista<List<RechargeRecordDto>> listRechargeRecord(Long lastId) {
//		return bizRechargeService.listRechargeRecord(lastId);
//	}
//}
