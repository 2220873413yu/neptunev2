package com.xms.app.controller;

import com.github.pagehelper.PageInfo;
import com.xms.app.entity.bo.DestroyInfoBo;
import com.xms.app.entity.dto.CardPackageDto;
import com.xms.app.entity.dto.ReleaseConfigDto;
import com.xms.app.entity.req.*;
import com.xms.app.entity.resp.*;
import com.xms.app.entity.vo.AirdropClaimPageInfoVo;
import com.xms.app.entity.vo.AirdropClaimRecordVo;
import com.xms.app.entity.vo.CreateDestroyOrderVo;
import com.xms.app.entity.vo.NodePlanVo;
import com.xms.app.service.BizCardService;
import com.xms.app.service.BizMiningService;
import com.xms.app.service.BizStakeService;
import com.xms.common.annotation.RateLimiter;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.enums.LimitType;
import com.xms.common.exception.ServiceException;
import com.xms.common.utils.SecurityUtils;
import com.xms.dao.domain.RewardRecord;
import com.xms.dao.entity.dto.DestroyOrderDto;
import com.xms.dao.entity.dto.InterestPackDto;
import com.xms.dao.entity.dto.InterestStatDayDto;
import com.xms.dao.service.XmsCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 质押相关业务
 *
 *
 * @since 2023-06-12
 */
@Api(tags = "质押相关业务")
@RestController
@RequestMapping("/api/stake")
public class BizStakeController {

	@Autowired
	private BizCardService bizCardService;

	@Autowired
	private BizStakeService bizStakeService;

	@Autowired
	private XmsCommonService xmsCommonServiceImpl;

	/**
	 * H余额换ACP入金
	 *
	 * @param req 请求参数
	 * @return 质押订单号
	 */
	@ApiOperation(value = "H余额换ACP入金")
	@PostMapping(value = "/hBalanceAcpDeposit")
	@RepeatSubmit
	public ResultPista<String> hBalanceAcpDeposit(@Valid @RequestBody HBalanceAcpDepositReq req) {
		return bizStakeService.hBalanceToAcpDeposit(req);
	}

	/**
	 * 我的质押信息
	 *
	 * @return 节点对象
	 */
	@ApiOperation(value = "我的质押信息")
	@GetMapping(value = "/msio")
	public ResultPista<MyStakeInfoResp> myStakeInfo()  throws Exception{
		return bizCardService.myStakeInfo();
	}

	/**
	 * 我的收益页面
	 *
	 * @return 节点对象
	 */
	@ApiOperation(value = "我的收益页面")
	@GetMapping(value = "/msiio")
	public ResultPista<MyStakeIncomeResp> myStakeIncomeInfo()  throws Exception{
		return bizCardService.myStakeIncomeInfo();
	}



//	/**
//	 * 购买节点
//	 *
//	 * @return 购买节点
//	 */
//	@ApiOperation(value = "购买节点")
//	@PostMapping(value = "/buyNodePlan")
//	@RepeatSubmit
//	public ResultPista<BuyNodePlanResp> buyNodePlan(@Valid @RequestBody  BuyNodePlanReq req)  throws Exception{
//		return bizCardService.buyNodePlan(req, SecurityUtils.getLoginAppUser().getUserId());
//	}

}
