package com.xms.app.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.common.configuration.ClientConfiguration;
import com.binance.connector.client.common.configuration.SignatureConfiguration;
import com.binance.connector.client.wallet.rest.api.WalletRestApi;
import com.binance.connector.client.wallet.rest.model.WithdrawRequest;
import com.binance.connector.client.wallet.rest.model.WithdrawResponse;
import com.github.pagehelper.PageInfo;
import com.xms.app.config.RobotConfig;
import com.xms.app.entity.LoginBo;
import com.xms.app.entity.TeamOverviewDto;
import com.xms.app.entity.bo.*;
import com.xms.app.entity.dto.MyDirectMemberDto;
import com.xms.app.entity.dto.MyTeamInfoDto;
import com.xms.app.entity.dto.MyTeamMemberDto;
import com.xms.app.entity.dto.MyTeamMemberPageDto;
import com.xms.app.entity.resp.DynamicRewardPageResp;
import com.xms.app.entity.req.BindEmailVo;
import com.xms.app.entity.req.BindGoogleCodeVo;
import com.xms.app.entity.req.BindInviteUserReq;
import com.xms.app.entity.req.UserBaseInfoVo;
import com.xms.app.entity.resp.HistoryInsuranceInfoResp;
import com.xms.app.entity.resp.InsuranceInfoResp;
import com.xms.app.entity.vo.*;
import com.xms.app.service.BizUserService;
import com.xms.common.annotation.Anonymous;
import com.xms.common.annotation.RateLimiter;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.core.domain.model.xms.LoginAppUser;
import com.xms.common.enums.LimitType;
import com.xms.common.utils.CollectionUtil;
import com.xms.common.utils.SecurityUtils;
import com.xms.common.utils.WalletUtil;
import com.xms.common.utils.sign.Md5Utils;
import com.xms.dao.domain.IdoOrder;
import com.xms.dao.domain.UserInvestLayerConfig;
import com.xms.dao.domain.UserLevelConfig;
import com.xms.dao.domain.UserRechangeAddress;
import com.xms.dao.entity.bo.DirectUserBo;
import com.xms.dao.entity.bo.UserInfoBo;
import com.xms.dao.entity.bo.UserMoneyBo;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.UserMoneyLog;
import com.xms.dao.service.IDestroyOrderService;
import com.xms.dao.service.IIdoOrderService;
import com.xms.dao.service.IUserRechangeAddressService;
import com.xms.dao.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * 用户信息表 前端控制器
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/userinfo")
@Slf4j
public class UserInfoController {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private BizUserService bizUserService;

	@Autowired
	private HttpServletRequest request;



//	/**
//	 * 获取算力页面展示数据
//	 *
//	 * @return 返回随机数
//	 */
//	@ApiOperation(value = "获取算力页面展示数据")
//	@GetMapping(value = "/computingPowerData")
//	public ResultPista<ComputingPowerBo> computingPowerData() {
//		return ResultPista.data(bizUserService.computingPowerData());
//	}
//
//	/**
//	 * 获取算力奖励产出列表
//	 * @param lastId
//	 * @return
//	 */
//	@ApiOperation(value = "获取算力奖励产出列表")
//	@GetMapping(value = "/powerDataList")
//	public ResultPista<List<UserMoneyLog>> powerDataList(Long lastId) {
//		return ResultPista.data(bizUserService.powerDataList(lastId));
//	}

//	/**
//	 * 获取用户资产信息
//	 *
//	 * @return 用户资产信息 DTO
//	 */
//	@ApiOperation(value = "获取用户资产信息")
//	@GetMapping(value = "/getUserAssetInfo")
//	public ResultPista<UserAssetInfoBo> getUserAssetInfo() {
//		Long userId = SecurityUtils.getLoginAppUser().getUserId();
//		// 空实现占位，后续可在 BizUserServiceImpl 中补充具体资产统计逻辑
//		return ResultPista.data(bizUserService.getUserAssetInfo(userId));
//	}



	/**
	 * 获取随机消息
	 *
	 * @param address 钱包地址
	 * @return 返回随机数
	 */
	@ApiOperation(value = "获取随机消息")
	@GetMapping(value = "/getMessage")
	public ResultPista<String> getMessage(
		@ApiParam(value = "钱包地址", required = true) @NotBlank @RequestParam String address) {
		address = address.toLowerCase();
		return ResultPista.data(bizUserService.getMessage(address));
	}


	/**
	 * 检查钱包地址是否注册过,返回false是没注册过,true注册过
	 * @param address 钱包地址
	 * @return
	 */
	@ApiOperation(value = "检查账号是否注册过")
	@Anonymous
	@GetMapping("/cas")
	public ResultPista<Boolean> checkAddress(@NotBlank @RequestParam String address) {
		if(StrUtil.isBlank(address)){
			return ResultPista.fail("钱包地址不能为空");
		}
		UserInfo one = userInfoService.lambdaQuery().eq(UserInfo::getAccount, address).one();
		if (one == null) {
			return ResultPista.data(false);
		}
		return ResultPista.data(true);
	}

	/**
	 * 登录接口
	 *
	 * @param loginVo
	 * @return
	 */
	@ApiOperation(value = "登录")
	@PostMapping(value = "/login")
	public ResultPista<LoginAppUser> login(@Valid @RequestBody LoginVo loginVo) {
		loginVo.setAddress(loginVo.getAddress().toLowerCase());
		return bizUserService.login(loginVo);
	}


//	/**
//	 * 查询全网节点信息
//	 *
//	 * @return
//	 */
//	@ApiOperation(value = "查询全网节点信息")
//	@GetMapping(value = "/getTotalNode")
//	public ResultPista<Integer> getTotalNode() {
//		List<IdoOrder> idoOrderList = idoOrderService.lambdaQuery()
//			.eq(IdoOrder::getBizStatus, 2)
//			.select(IdoOrder::getShares)
//			.list();
//		Integer totalNode = 0;
//		if(CollectionUtil.isNotEmpty(idoOrderList)){
//			for (IdoOrder idoOrder : idoOrderList) {
//				totalNode =totalNode + idoOrder.getShares();
//			}
//		}
//		return ResultPista.data(totalNode);
//	}

	/**
	 * 查询本轮保险仓资格相关
	 *
	 * @return
	 */
	@ApiOperation(value = "查询本轮保险仓资格相关")
	@GetMapping(value = "/getIInfo")
	public ResultPista<InsuranceInfoVo> getInsuranceInfo() {
		InsuranceInfoResp resp = bizUserService.getInsuranceInfo(SecurityUtils.getLoginAppUser().getUserId());
		if (resp == null) {
			return ResultPista.data(null);
		}
		InsuranceInfoVo vo = new InsuranceInfoVo();
		vo.setIv(resp.isInvest());
		vo.setIqs(resp.getInsuranceQualifyStatus());
		vo.setTw(resp.getTotalWithdrawal());
		vo.setCwq(resp.getCumulativeWithdrawalQuota());
		vo.setMwq(resp.getMonthlyWithdrawalQuota());
		vo.setCmw(resp.getCurrentMonthWithdrawn());
		return ResultPista.data(vo);
	}

	/**
	 * 查询历史保险仓
	 *
	 * @return
	 */
	@ApiOperation(value = "查询历史保险仓")
	@GetMapping(value = "/hiio")
	public ResultPista<List<HistoryInsuranceInfoVo>> historyInsuranceInfo() {
		List<HistoryInsuranceInfoResp> respList = bizUserService.historyInsuranceInfo(SecurityUtils.getLoginAppUser().getUserId());
		List<HistoryInsuranceInfoVo> voList = new java.util.ArrayList<>();
		if (CollectionUtil.isNotEmpty(respList)) {
			for (HistoryInsuranceInfoResp resp : respList) {
				HistoryInsuranceInfoVo vo = new HistoryInsuranceInfoVo();
				vo.setTsa(resp.getTotalStakeAmount());
				vo.setPla(resp.getPersonalLossAmount());
				vo.setRcl(resp.getRemainingCompensationLimit());
				vo.setAcl(resp.getAllCompensationLimit());
				vo.setIqs(resp.getInsuranceQualifyStatus());
				vo.setIcqs(resp.getInsuranceCompensationQualifyStatus());
				voList.add(vo);
			}
		}
		return ResultPista.data(voList);
	}


	/**
	 * 查询用户详情
	 *
	 * @return
	 */
	@ApiOperation(value = "用户详情")
	@GetMapping(value = "/guio")
	public ResultPista<UserInfoDetailVo> getUserInfo() {
		UserInfoBo userInfoBo = userInfoService.getUserInfo(SecurityUtils.getLoginAppUser().getUserId());
		if (userInfoBo == null) {
			return ResultPista.data(null);
		}
		UserInfoDetailVo vo = new UserInfoDetailVo();
		vo.setUserId(userInfoBo.getUserId());
		vo.setAccount(Base64.getEncoder().encodeToString(userInfoBo.getAccount().getBytes(StandardCharsets.UTF_8)));
		vo.setUc(userInfoBo.getUserCode());
		vo.setGl(userInfoBo.getGameLevel());
		vo.setNl(userInfoBo.getNodeLevel());
		vo.setIuc(userInfoBo.getInviteUserCode());
		vo.setIuid(userInfoBo.getInviteUserId());
		vo.setIv(userInfoBo.getIsValid());
		vo.setLl(userInfoBo.getLayerLevel());
		vo.setSub(userInfoBo.getSubNum());
		vo.setUmb(userInfoBo.getUmbrellaNum());
		vo.setSp(userInfoBo.getSubPerformance());
		vo.setUp(userInfoBo.getUmbrellaPerformance());
		vo.setCp(userInfoBo.getCommunityPerformance());
		vo.setPf(userInfoBo.getPerformance());
		vo.setHp(userInfoBo.getHistoryPerformance());
		vo.setSa(userInfoBo.getStakeAccount());
		return ResultPista.data(vo);
	}

	/**
	 * 动态奖励页面信息
	 *
	 * @return
	 */
	@ApiOperation(value = "动态奖励页面信息")
	@GetMapping(value = "/drpio")
	public ResultPista<DynamicRewardPageVo> dynamicRewardPageInfo() {
		DynamicRewardPageResp resp = bizUserService.dynamicRewardPageInfo();
		if (resp == null) {
			return ResultPista.data(null);
		}
		DynamicRewardPageVo vo = new DynamicRewardPageVo();
		vo.setGl(resp.getGameLevel());
		vo.setTdr(resp.getTodayDiffReward());
		vo.setTer(resp.getTodayEqualReward());
		vo.setTlr(resp.getTodayLevelReward());
		vo.setTnr(resp.getTodayNewReward());
		vo.setTnedi(resp.getTodayNodeEquityDividendIncome());
		vo.setTtdr(resp.getTodayTotalDynamicReward());
		vo.setTwd(resp.getTotalWithdrawDynamic());
		vo.setTwv4(resp.getTotalWithdrawValidNum4());
		vo.setMba(resp.getMinBuyAmount());
		vo.setPr(resp.getPointsRatio());
		if (resp.getWealthVaultInfo() != null) {
			UserWealthVaultInfoVo wealthVo = new UserWealthVaultInfoVo();
			wealthVo.setS1a(resp.getWealthVaultInfo().getSeg1Amount());
			wealthVo.setS2a(resp.getWealthVaultInfo().getSeg2Amount());
			wealthVo.setS3a(resp.getWealthVaultInfo().getSeg3Amount());
			wealthVo.setS4a(resp.getWealthVaultInfo().getSeg4Amount());
			wealthVo.setS5a(resp.getWealthVaultInfo().getSeg5Amount());
			wealthVo.setS1up(resp.getWealthVaultInfo().getSeg1UnlockPrice());
			wealthVo.setS2up(resp.getWealthVaultInfo().getSeg2UnlockPrice());
			wealthVo.setS3up(resp.getWealthVaultInfo().getSeg3UnlockPrice());
			wealthVo.setS4up(resp.getWealthVaultInfo().getSeg4UnlockPrice());
			wealthVo.setS5up(resp.getWealthVaultInfo().getSeg5UnlockPrice());
			vo.setWvi(wealthVo);
		}
		return ResultPista.data(vo);
	}

	/**
	 * 查询用户节点信息
	 *
	 * @return
	 */
	@ApiOperation(value = "查询用户节点信息")
	@GetMapping(value = "/unio")
	public ResultPista<UserNodeInfoBo> userNodeInfo() {
		return ResultPista.data(bizUserService.userNodeInfo());
	}


	/**
	 * 获取层级奖励配置(如果返回为空说明满级了)
	 *
	 * @return
	 */
	@ApiOperation(value = "获取层级奖励配置")
	@GetMapping(value = "/glcg")
	public ResultPista<UserInvestLayerConfigVo> getLayerConfig() {
		UserInvestLayerConfig config = bizUserService.getLayerConfig();
		if (config == null) {
			return ResultPista.data(null);
		}
		UserInvestLayerConfigVo vo = new UserInvestLayerConfigVo();
		vo.setLv(config.getLevel());
		vo.setMi(config.getMinInvest());
		vo.setLc(config.getLayerCount());
		vo.setRr(config.getRewardRatio());
		return ResultPista.data(vo);
	}

	/**
	 * 获取用户等级配置(如果返回为空说明满级了)
	 * @return
	 */
	@ApiOperation(value = "获取用户等级配置(如果返回为空说明满级了)")
	@GetMapping(value = "/gulccg")
	public ResultPista<UserLevelConfigVo> getUserLevelConfig() {
		UserLevelConfig config = bizUserService.getUserLevelConfig();
		if (config == null) {
			return ResultPista.data(null);
		}
		UserLevelConfigVo vo = new UserLevelConfigVo();
		vo.setLv(config.getLevel());
		vo.setPf(config.getPerformance());
		vo.setUp(config.getUmbrellaPerformance());
		vo.setRr(config.getRewardRatio());
		vo.setHss(config.getHasStudioSubsidy());
		vo.setPrr(config.getPeerRewardRatio());
		vo.setMba(config.getMinBuyAmount());
		return ResultPista.data(vo);
	}

	/**
	 * 退出登录
	 *
	 * @return
	 */
	@ApiOperation(value = "退出登录")
	@GetMapping(value = "/logout")
	public ResultPista<String> logout() {
		return bizUserService.logout(request);
	}



	/**
	 * 我的直推用户信息
	 * @return
	 */
	@ApiOperation(value = "我的直推用户信息")
	@GetMapping("/lsms")
	public ResultPista<List<MyDirectMemberDto>> listSubMembers(String address) {
		return ResultPista.data(bizUserService.listSubMembers(address));
	}

/*	*//**
	 * 我的团队用户信息
	 * @return
	 *//*
	@ApiOperation(value = "我的团队用户信息")
	@GetMapping("/listMyDirectMembers")
	public ResultPista<List<MyDirectMemberDto>> listMyDirectMembers(String address) {
		return ResultPista.data(bizUserService.listMyDirectMembers(address));
	}*/


//
//	/**
//	 * 我的团队数据 总成员、直推人数、团队销毁usdt、等级
//	 *
//	 * @return
//	 */
//	@ApiOperation(value = "我的团队数据")
//	@GetMapping(value = "/myTeamInfo")
//	public ResultPista<MyTeamInfoDto> myTeamInfo() {
//		return ResultPista.data(bizUserService.myTeamInfo(SecurityUtils.getLoginAppUser().getUserId()));
//	}
//
//	/**
//	 * 我的团队数据 总成员、直推人数、团队销毁usdt、等级
//	 * @param lastId lastId
//	 * @param distance 层级
//	 * @param level 等级
//	 * @return
//	 */
//	@ApiOperation(value = "我的团队数据")
//	@GetMapping("/teamMembers")
//	public ResultPista<MyTeamMemberPageDto> listMyTeamMembers(Long lastId,Integer distance,Integer level) {
//		return ResultPista.data(bizUserService.listMyTeamMembers(lastId,distance,level));
//	}
//



//	/**
//	 * 我的团队页面
//	 *
//	 * @return
//	 */
//	@ApiOperation(value = "我的团队页面")
//	@GetMapping(value = "/getTeamInfo")
//	public ResultPista<List<TeamOverviewDto>> getMyTeamOverview() {
//		return ResultPista.data(bizUserService.getMyTeamOverview(SecurityUtils.getLoginAppUser().getUserId()));
//	}

//	/**
//	 * 查询用户收益信息
//	 *
//	 * @return
//	 */
//	@ApiOperation(value = "查询用户收益信息")
//	@GetMapping(value = "/getIncomeSummary")
//	public ResultPista<UserIncomeSummaryVo> getIncomeSummary() {
//		return ResultPista.data(bizUserService.getIncomeSummary(SecurityUtils.getLoginAppUser().getUserId()));
//	}







//	/**
//	 * 修改用户基础信息
//	 * @param req
//	 * @return
//	 */
//	@ApiOperation(value = "修改用户基础信息")
//	@PostMapping(value = "/updateBaseInfo")
//	public ResultPista updateBaseInfo(@Valid @RequestBody UserBaseInfoVo req) {
//		bizUserService.updateBaseInfo(req);
//		return ResultPista.success();
//	}


	//	/**
//	 * 查询用户业绩信息
//	 *
//	 * @return
//	 */
//	@ApiOperation(value = "查询用户业绩信息")
//	@GetMapping(value = "/getTeamView")
//	public ResultPista<TeamViewBO> getTeamView() {
//		TeamViewBO result = bizUserService.getTeamView(SecurityUtils.getLoginAppUser().getUserId());
//		return ResultPista.data(result);
//	}


}

