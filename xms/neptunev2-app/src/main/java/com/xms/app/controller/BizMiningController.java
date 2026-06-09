//package com.xms.app.controller;
//
//import cn.hutool.core.thread.ThreadUtil;
//import com.github.pagehelper.PageInfo;
//import com.xms.app.entity.bo.DestroyCallbackBo;
//import com.xms.app.entity.bo.DestroyInfoBo;
//import com.xms.app.service.impl.BizMiningServiceImpl;
//import com.xms.common.utils.spring.SpringUtils;
//import com.xms.dao.entity.dto.DestroyOrderDto;
//import com.xms.dao.entity.dto.InterestPackDto;
//import com.xms.dao.entity.dto.InterestStatDayDto;
//import com.xms.app.entity.dto.ReleaseConfigDto;
//import com.xms.app.entity.req.ReleaseOrderReq;
//import com.xms.app.entity.resp.CreateOrderResp;
//import com.xms.app.entity.vo.CreateDestroyOrderVo;
//import com.xms.app.service.BizMiningService;
//import com.xms.common.annotation.RepeatSubmit;
//import com.xms.common.core.domain.api.ResultPista;
//import com.xms.common.exception.ServiceException;
//import com.xms.common.utils.SecurityUtils;
//import com.xms.dao.service.XmsCommonService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
///**
// * 销毁相关 前端控制器
// *
// *
// * @since 2023-06-12
// */
//@Api(tags = "销毁")
//@RestController
//@RequestMapping("/api/mining")
//public class BizMiningController {
//	@Autowired
//	private BizMiningService bizMiningService;
//
//	@Autowired
//	private XmsCommonService xmsCommonServiceImpl;
//
//
//	/**
//	 * 获取销毁信息(当前日化、我的销毁数量)
//	 *
//	 * @return 返回随机数
//	 */
//	@ApiOperation(value = "获取销毁信息(当前日化、我的销毁数量)")
//	@GetMapping(value = "/destroyInfo")
//	public ResultPista<DestroyInfoBo> destroyInfo() {
//		return ResultPista.data(bizMiningService.destroyInfo());
//	}
//
//	/**
//	 * 加速释放配置
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation(value = "加速释放配置")
//	@GetMapping(value = "/releaseConfigList")
//	public ResultPista<List<ReleaseConfigDto>> releaseConfigList()  throws Exception{
//		return bizMiningService.releaseConfigList();
//	}
//
//	/**
//	 * 加速释放
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation(value = "加速释放")
//	@GetMapping(value = "/releaseOrder")
//	public ResultPista releaseOrder(ReleaseOrderReq req)  throws Exception{
//		ResultPista resultPista = xmsCommonServiceImpl.checkMineSettleTime(2);
//		if (!resultPista.isSuccess()) {
//			throw new ServiceException(resultPista.getMsg());
//		}
//		return bizMiningService.releaseOrder(req, SecurityUtils.getLoginAppUser().getUserId());
//	}
//
///*	*//**
//	 * 销毁 以u为单位
//	 * @return
//	 *//*
//	@ApiOperation(value = "销毁")
//	@PostMapping(value = "/test1")
//	@RepeatSubmit
//	public ResultPista test1()  throws Exception{
//		for (int i = 0; i < 100; i++) {
//			ThreadUtil.sleep(200, TimeUnit.MICROSECONDS);
//			CreateDestroyOrderVo req = new CreateDestroyOrderVo();
//			//随机100-500之间不能有效数
//			req.setDestroyAmount(new BigDecimal(Math.random() * 500 + 100));
//			ResultPista<CreateOrderResp> order = bizMiningService.createOrder(req);
//			DestroyCallbackBo callbackBo = new DestroyCallbackBo();
//			//生成随机hash
//			callbackBo.setHash(UUID.randomUUID().toString().replace("-", ""));
//			callbackBo.setSign("sss");
//			callbackBo.setOrderNo(order.getData().getOrderNo());
//			callbackBo.setAmount(order.getData().getUsdtValue());
//			SpringUtils.getBean(BizMiningServiceImpl.class).destroyCallback(callbackBo);
//		}
//		return ResultPista.success();
//	}*/
//
//	/**
//	 * 销毁 以u为单位
//	 * @return
//	 */
//	@ApiOperation(value = "销毁")
//	@PostMapping(value = "/destroyOrder")
//	@RepeatSubmit
//	public ResultPista<CreateOrderResp> createDestroyOrder(@Valid @RequestBody CreateDestroyOrderVo req)  throws Exception{
//		ResultPista resultPista = xmsCommonServiceImpl.checkMineSettleTime(1);
//		if (!resultPista.isSuccess()) {
//			throw new ServiceException(resultPista.getMsg());
//		}
//		return bizMiningService.createOrder(req);
//	}
//
//	/**
//	 * 销毁记录
//	 * @param pageIndex 当前页 默认1
//	 * @param pageSize 每页长度 默认20(最大20)
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation(value = "销毁记录")
//	@GetMapping(value = "/destroyOrderList")
//	public ResultPista<PageInfo<DestroyOrderDto>> destroyOrderList(@ApiParam(value = "当前页", required = true) @NotNull @RequestParam(defaultValue = "1") Integer pageIndex,
//																   @ApiParam(value = "每页长度", required = true) @NotNull @RequestParam(defaultValue = "20") Integer pageSize)  throws Exception{
//		return ResultPista.data(bizMiningService.destroyOrderList(pageIndex,pageSize));
//	}
//
//
//	/**
//	 * 我的利息包
//	 * @param pageIndex 当前页 默认1
//	 * @param pageSize 每页长度 默认20(最大20)
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation(value = "我的利息包")
//	@GetMapping(value = "/interestPacks")
//	public ResultPista<PageInfo<InterestPackDto>> getMyInterestPacks(@ApiParam(value = "当前页", required = true) @NotNull @RequestParam(defaultValue = "1") Integer pageIndex,
//																 @ApiParam(value = "每页长度", required = true) @NotNull @RequestParam(defaultValue = "20") Integer pageSize)  throws Exception{
//		return ResultPista.data(bizMiningService.getMyInterestPacks(pageIndex,pageSize));
//	}
//
//
//
//	/**
//	 * 每日释放利息记录
//	 * @param pageIndex 当前页 默认1
//	 * @param pageSize 每页长度 默认20(最大20)
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation(value = "每日释放利息记录")
//	@GetMapping(value = "/todayInterest")
//	public ResultPista<PageInfo<InterestStatDayDto>> todayInterest(@ApiParam(value = "当前页", required = true) @NotNull @RequestParam(defaultValue = "1") Integer pageIndex,
//															   @ApiParam(value = "每页长度", required = true) @NotNull @RequestParam(defaultValue = "20") Integer pageSize)  throws Exception{
//		return ResultPista.data(bizMiningService.todayInterest(pageIndex,pageSize));
//	}
//}
