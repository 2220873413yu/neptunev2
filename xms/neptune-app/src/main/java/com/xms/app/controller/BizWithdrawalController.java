package com.xms.app.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.binance.connector.client.common.ApiException;
import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.common.configuration.ClientConfiguration;
import com.binance.connector.client.common.configuration.SignatureConfiguration;
import com.binance.connector.client.wallet.rest.api.WalletRestApi;
import com.binance.connector.client.wallet.rest.model.*;
import com.github.pagehelper.PageInfo;
import com.xms.app.entity.resp.WithdrawalConfigResp;
import com.xms.app.entity.resp.WithdrawalInfo;
import com.xms.app.entity.resp.WithdrawalSummaryResp;
import com.xms.app.entity.vo.WithdrawalVo;
import com.xms.app.service.BizWithdrawalService;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.constant.SysConstant;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.utils.SecurityUtils;
import com.xms.dao.entity.bo.RewardRecordBo;
import com.xms.dao.entity.bo.WithdrawalBo;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.service.XmsCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户提现相关 前端控制器
 *
 * @since 2023-06-12
 */
@Api(tags = "用户提现相关")
@RestController
@RequestMapping("/api/wal")
public class BizWithdrawalController {

	@Autowired
	private BizWithdrawalService bizWithdrawalService;

	@Autowired
	private XmsCommonService xmsCommonServiceImpl;


//	/**
//	 * 查询用户可提现、待生效金额
//	 */
//	@ApiOperation("查询用户可提现、待生效金额")
//	@GetMapping("/withdrawalInfo")
//	public ResultPista<WithdrawalInfo> withdrawalInfo() {
//		return ResultPista.data(
//			bizWithdrawalService.withdrawalInfo(SecurityUtils.getLoginAppUser().getUserId()));
//	}

	/**
	 * 提现配置
	 * @return
	 */
	@GetMapping(value = "/walcg")
	public ResultPista<List<WithdrawalConfigResp>> withdrawalConfig() {
		return ResultPista.data(bizWithdrawalService.withdrawalConfig());
	}

	/**
	 * 发起提现.提bommai
	 *
	 * @param withdrawalVo
	 * @return
	 */
	@ApiOperation(value = "提现")
	@RepeatSubmit
	@PostMapping(value = "/awal")
	public ResultPista<Void> addWithdrawal(@Valid @RequestBody WithdrawalVo withdrawalVo) {
/*		ResultPista resultPista = xmsCommonServiceImpl.checkMineSettleTime(1);
		if (!resultPista.isSuccess()) {
			throw new ServiceException(resultPista.getMsg());
		}
		resultPista = xmsCommonServiceImpl.checkMineSettleTime(2);
		if (!resultPista.isSuccess()) {
			throw new ServiceException(resultPista.getMsg());
		}*/
		bizWithdrawalService.addWithdrawal(withdrawalVo, SecurityUtils.getLoginAppUser().getUserId());
		return ResultPista.success();
	}

//	/**
//	 * 提现汇总（总提现/今日提现/待处理）
//	 */
//	@ApiOperation("提现汇总")
//	@GetMapping("/summary")
//	public ResultPista<WithdrawalSummaryResp> withdrawalSummary() {
//		return ResultPista.data(
//			bizWithdrawalService.withdrawalSummary(SecurityUtils.getLoginAppUser().getUserId()));
//	}


	/**
	 * 提现记录
	 * @param pageIndex 当前页 默认1
	 * @param pageSize 每页长度 默认20(最大20)
	 * @return
	 */
	@ApiOperation("提现记录")
	@GetMapping("/lwrd")
	public ResultPista<PageInfo<WithdrawalBo>> listWithdrawRecord(
		@ApiParam(value = "当前页", required = true) @NotNull @RequestParam(defaultValue = "1") Integer pageIndex,
		@ApiParam(value = "每页长度", required = true) @NotNull @RequestParam(defaultValue = "20") Integer pageSize) {
		pageSize = pageSize >= 20 ? SysConstant.TWENTY : pageSize;
		return ResultPista.data(bizWithdrawalService.listWithdrawRecord(pageIndex, pageSize,SecurityUtils.getLoginAppUser().getUserId()));
	}

}
