package com.xms.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.common.collect.Lists;
import com.xms.app.entity.bo.*;
import com.xms.app.entity.dto.*;
import com.xms.app.entity.req.BindInviteUserReq;
import com.xms.app.entity.resp.DynamicRewardPageResp;
import com.xms.app.entity.resp.HistoryInsuranceInfoResp;
import com.xms.app.entity.resp.InsuranceInfoResp;
import com.xms.app.handler.CustomException;
import com.xms.common.constant.ExternalApiConstant;
import com.xms.app.entity.req.BindEmailVo;
import com.xms.app.entity.vo.*;
import com.xms.app.util.AliyunSenMailUtil;
import com.xms.app.util.TLSSigAPIv2;
import com.xms.app.entity.req.UserBaseInfoVo;
import com.xms.app.service.BizUserService;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.*;
import com.xms.common.exception.ServiceException;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.common.mq.dynamic.OrderMsgDO;
import com.xms.common.utils.*;
import com.xms.common.utils.ip.IpUtils;
import com.xms.dao.domain.*;
import com.xms.dao.entity.domain.*;
import com.xms.dao.entity.dto.TeamDestroyStatDto;
import com.xms.dao.service.*;
import com.xms.dao.mapper.UserInfoMapper;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.core.domain.model.xms.LoginAppUser;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.sign.Md5Utils;
import com.xms.common.utils.spring.SpringUtils;
import com.xms.dao.service.impl.RewardRecordServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BizUserServiceImpl implements BizUserService {

	private static final String SDK_APP_ID = "1721002266";
	private static final String SECRET_KEY = "01acd6bad44c551dd72e3fad285275586c060dd32ef483d8f9958d3eae5ede46"; // 需要替换为真实密钥
	private static final String ADMIN_IDENTIFIER = "administrator"; // App管理员账号


	@Autowired
	private IDestroyOrderService destroyOrderService;

	@Autowired
	private UserInfoService userInfoServiceImpl;

	@Autowired
	private IUserStakePositionService userStakePositionService;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private IStakeRoundService stakeRoundService;

	@Autowired
	private WithdrawalService withdrawalService;

	@Autowired
	private IIdoOrderService idoOrderService;

	@Autowired
	private AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementServiceImpl;


	@Autowired
	private UserRelationService userRelationService;

	@Autowired
	private IUserMoneyService userMoneyService;

	@Autowired
	private UserMoneyLogService userMoneyLogService;

	@Autowired
	private AppTokenService appTokenService;

	@Autowired
	private IUserIncomeSummaryService userIncomeSummaryService;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private IRewardRecordService rewardRecordService;

	@Autowired
	private XmsRedis xmsRedis;

	@Autowired
	private IEmailConfigService emailConfigService;

	@Autowired
	private Environment environment;

	@Autowired
	private ICoinPriceService coinPriceService;

	@Autowired
	private IPtbDailyPriceService ptbDailyPriceService;

	@Autowired
	private INodePlanOrderService nodePlanOrderService;

	@Autowired
	private IUserWealthVaultService userWealthVaultService;

	@Autowired
	private IWealthVaultStageConfigService wealthVaultStageConfigService;

	@Autowired
	private IUserLevelConfigService userLevelConfigService;

	@Autowired
	private IUserInvestLayerConfigService investLayerConfigService;

	@Autowired
	private IUserStakePositionService userStakePositionServiceImpl;

	@Override
	public List<HistoryInsuranceInfoResp> historyInsuranceInfo(Long userId) {

		//查询
		List<HistoryInsuranceInfoResp> result = userStakePositionService.lambdaQuery()
			.eq(UserStakePosition::getUserId, userId)
			.eq(UserStakePosition::getStatus, 2)
			.select(UserStakePosition::getTotalStakeAmount, UserStakePosition::getPersonalLossAmount,
				UserStakePosition::getAllCompensationLimit,
				UserStakePosition::getRemainingCompensationLimit, UserStakePosition::getInsuranceQualifyStatus, UserStakePosition::getInsuranceCompensationQualifyStatus)
			.list()
			.stream().map(record -> {
				HistoryInsuranceInfoResp entity = new HistoryInsuranceInfoResp();
				entity.setTotalStakeAmount(record.getTotalStakeAmount());
				entity.setPersonalLossAmount(record.getPersonalLossAmount());
				entity.setRemainingCompensationLimit(record.getRemainingCompensationLimit());
				entity.setAllCompensationLimit(record.getAllCompensationLimit());
				entity.setInsuranceQualifyStatus(record.getInsuranceQualifyStatus());
				entity.setInsuranceCompensationQualifyStatus(record.getInsuranceCompensationQualifyStatus());
				return entity;
			}).collect(Collectors.toList());
		return result;
	}

	@Override
	public InsuranceInfoResp getInsuranceInfo(Long userId) {
		InsuranceInfoResp resp = new InsuranceInfoResp();
		//查询本轮有没有投资过
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		if(stakeRound != null){
			UserStakePosition userStakePosition = userStakePositionServiceImpl.lambdaQuery()
				.eq(UserStakePosition::getUserId, userId)
				.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
				.one();
			if(userStakePosition!=null){
				resp.setInvest(true);
				resp.setInsuranceQualifyStatus(userStakePosition.getInsuranceQualifyStatus());

				//月静态提现额度
				BigDecimal monthlyWithdrawalQuota= userStakePosition.getTotalStakeAmount().multiply(new BigDecimal("0.1"))
					.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
				resp.setMonthlyWithdrawalQuota(monthlyWithdrawalQuota);
				//计算月已提现金额
				BigDecimal currentMonthWithdrawn = BigDecimal.ZERO;
				if (stakeRound.getCreateTime() != null) {
					List<Withdrawal> staticWithdrawals = withdrawalService.lambdaQuery()
						.eq(Withdrawal::getUserId, userId)
						.eq(Withdrawal::getStakeRoundId, stakeRound.getId())
						.eq(Withdrawal::getStatus, 3)
						.eq(Withdrawal::getCoinType, 2)
						.select(Withdrawal::getCreditedTime, Withdrawal::getCreateTime, Withdrawal::getChangeBalance)
						.list();
					if (CollectionUtil.isNotEmpty(staticWithdrawals)) {
						long roundStartMs = stakeRound.getCreateTime().getTime();
						long thirtyDaysMs = 30L * 24 * 60 * 60 * 1000;
						long nowPeriodIndex = (System.currentTimeMillis() - roundStartMs) / thirtyDaysMs;
						if (nowPeriodIndex < 0) {
							nowPeriodIndex = 0;
						}
						for (Withdrawal withdrawal : staticWithdrawals) {
							Date withdrawTime = withdrawal.getCreditedTime() != null ? withdrawal.getCreditedTime() : withdrawal.getCreateTime();
							if (withdrawTime == null) {
								continue;
							}
							long periodIndex = (withdrawTime.getTime() - roundStartMs) / thirtyDaysMs;
							if (periodIndex < 0) {
								periodIndex = 0;
							}
							if (periodIndex == nowPeriodIndex) {
								currentMonthWithdrawn = currentMonthWithdrawn.add(
									withdrawal.getChangeBalance() == null ? BigDecimal.ZERO : withdrawal.getChangeBalance());
							}
						}
						currentMonthWithdrawn = currentMonthWithdrawn
							.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					}
				}
				resp.setCurrentMonthWithdrawn(currentMonthWithdrawn);

 				//总提现额
				BigDecimal totalWithdrawal  = withdrawalService.lambdaQuery()
					.eq(Withdrawal::getUserId, userId)
					.eq(Withdrawal::getStakeRoundId, stakeRound.getId())
					.eq(Withdrawal::getStatus, 3)
					.in(Withdrawal::getCoinType, 2, 3)
					.orderByAsc(Withdrawal::getId)
					.list()
					.stream().map(Withdrawal::getChangeBalance)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
				resp.setTotalWithdrawal(totalWithdrawal);
				//提现数量(额度)
				BigDecimal cumulativeWithdrawalQuota= userStakePosition.getTotalStakeAmount().multiply(new BigDecimal("0.3"))
					.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
				resp.setCumulativeWithdrawalQuota(cumulativeWithdrawalQuota);

			}else{
				resp.setInvest(false);
			}
		}else{
			resp.setInvest(false);
		}
		return resp;
	}

	@Override
	public UserInvestLayerConfig getLayerConfig() {
		UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.select(UserInfo::getLayerLevel, UserInfo::getMinLayerLevel)
			.one();
		Integer layerLevel = userInfo.getLayerLevel()>userInfo.getMinLayerLevel()? userInfo.getLayerLevel(): userInfo.getMinLayerLevel();
		UserInvestLayerConfig userInvestLayerConfig = investLayerConfigService.lambdaQuery()
			.gt(UserInvestLayerConfig::getLevel, layerLevel)
			.orderByAsc(UserInvestLayerConfig::getLevel)
			.last("limit 1")
			.one();
		return userInvestLayerConfig;
	}

	@Override
	public UserLevelConfig getUserLevelConfig() {
		UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.select(UserInfo::getGameLevel, UserInfo::getMinGameLevel)
			.one();
		Integer gameLevel = userInfo.getGameLevel()>userInfo.getMinGameLevel()? userInfo.getGameLevel(): userInfo.getMinGameLevel();
		UserLevelConfig userLevelConfig = userLevelConfigService.lambdaQuery()
			.gt(UserLevelConfig::getLevel, gameLevel)
			.orderByAsc(UserLevelConfig::getLevel)
			.last("limit 1")
			.one();
		return userLevelConfig;
	}

	@Override
	public DynamicRewardPageResp dynamicRewardPageInfo() {
		DynamicRewardPageResp resp = new DynamicRewardPageResp();
		UserWealthVault userWealthVault = userWealthVaultService.lambdaQuery()
			.eq(UserWealthVault::getId, SecurityUtils.getLoginAppUser().getUserId())
			.one();

		//当前用户等级
		UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, userWealthVault.getId())
			.select(UserInfo::getUserId,UserInfo::getGameLevel,UserInfo::getMinGameLevel)
			.one();
		BigDecimal pRatio = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_h_token_buy_points_ratio));
		resp.setPointsRatio(pRatio);
		Integer gameLevel = userInfo.getGameLevel()>userInfo.getMinGameLevel()? userInfo.getGameLevel(): userInfo.getMinGameLevel();
		UserLevelConfig userLevelConfig = userLevelConfigService.lambdaQuery()
			.eq(UserLevelConfig::getLevel, gameLevel)
			.one();
		resp.setMinBuyAmount(userLevelConfig.getMinBuyAmount());
		resp.setGameLevel(gameLevel);
		//
		UserWealthVaultDto dto = new UserWealthVaultDto();
		//给dto赋值
		dto.setSeg1Amount(userWealthVault.getSeg1Amount());
		dto.setSeg2Amount(userWealthVault.getSeg2Amount());
		dto.setSeg3Amount(userWealthVault.getSeg3Amount());
		dto.setSeg4Amount(userWealthVault.getSeg4Amount());
		dto.setSeg5Amount(userWealthVault.getSeg5Amount());
		//解锁价格
		List<WealthVaultStageConfig> list = wealthVaultStageConfigService.lambdaQuery()
			.orderByAsc(WealthVaultStageConfig::getStageNo)
			.list();
		if(CollectionUtil.isNotEmpty(list)){
			dto.setSeg1UnlockPrice(list.get(0).getUnlockPrice());
			dto.setSeg2UnlockPrice(list.get(1).getUnlockPrice());
			dto.setSeg3UnlockPrice(list.get(2).getUnlockPrice());
			dto.setSeg4UnlockPrice(list.get(3).getUnlockPrice());
			dto.setSeg5UnlockPrice(list.get(4).getUnlockPrice());
		}
		BigDecimal todayTotalDynamicReward = BigDecimal.ZERO;
		BigDecimal diffReward = BigDecimal.ZERO;
		BigDecimal equalReward = BigDecimal.ZERO;
		BigDecimal levelReward = BigDecimal.ZERO;
		BigDecimal todayNodeEquityDividendIncome = BigDecimal.ZERO;
		BigDecimal todayNewReward = BigDecimal.ZERO;
		List<RewardRecord> todayRewardRecordList = rewardRecordService.lambdaQuery()
			.eq(RewardRecord::getUserId, userInfo.getUserId())
			.eq(RewardRecord::getCoinType,3)
			.in(RewardRecord::getSourceType,
				ConstantType.xms_reward_record_source_type.type_3,
				ConstantType.xms_reward_record_source_type.type_4,
				ConstantType.xms_reward_record_source_type.type_5,
				ConstantType.xms_reward_record_source_type.type_7,
				ConstantType.xms_reward_record_source_type.type_8)
			.apply("create_time >= CURDATE()")
			.select(RewardRecord::getSourceType, RewardRecord::getAmount)
			.list();
		if(CollectionUtil.isNotEmpty(todayRewardRecordList)){
			for (RewardRecord rewardRecord : todayRewardRecordList) {
				if(rewardRecord.getAmount() == null || rewardRecord.getSourceType() == null){
					continue;
				}
				BigDecimal amount = rewardRecord.getAmount();
				todayTotalDynamicReward = todayTotalDynamicReward.add(amount);
				if(rewardRecord.getSourceType().equals(ConstantType.xms_reward_record_source_type.type_3)){
					diffReward = diffReward.add(amount);
				}else if(rewardRecord.getSourceType().equals(ConstantType.xms_reward_record_source_type.type_4)){
					equalReward = equalReward.add(amount);
				}else if(rewardRecord.getSourceType().equals(ConstantType.xms_reward_record_source_type.type_5)){
					levelReward = levelReward.add(amount);
				}else if(rewardRecord.getSourceType().equals(ConstantType.xms_reward_record_source_type.type_7)){
					todayNodeEquityDividendIncome = todayNodeEquityDividendIncome.add(amount);
				}else if(rewardRecord.getSourceType().equals(ConstantType.xms_reward_record_source_type.type_8)){
					todayNewReward = todayNewReward.add(amount);
				}
			}
		}
		resp.setTodayTotalDynamicReward(todayTotalDynamicReward);
		resp.setTodayDiffReward(diffReward);
		resp.setTodayEqualReward(equalReward);
		resp.setTodayLevelReward(levelReward);
		resp.setTodayNewReward(todayNewReward);
		resp.setTodayNodeEquityDividendIncome(todayNodeEquityDividendIncome);
		resp.setWealthVaultInfo(dto);
		//当前提现轮次
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.last("limit 1")
			.select(StakeRound::getId)
			.one();
		if(stakeRound !=null){
			Map<Integer, BigDecimal> withdrawTotalByCoinType = sumWithdrawByCoinType(userInfo.getUserId(), stakeRound.getId());
			// 累计提现动态收益仓（coinType=3）
			resp.setTotalWithdrawDynamic(withdrawTotalByCoinType.getOrDefault(3, BigDecimal.ZERO));
			// 累计提现财富仓（coinType=4）
			resp.setTotalWithdrawValidNum4(withdrawTotalByCoinType.getOrDefault(4, BigDecimal.ZERO));
		}
		return resp;
	}

	/**
	 * 按币种汇总当前轮次已完成提现金额。
	 * 统一查询一次，避免重复 SQL。
	 */
	private Map<Integer, BigDecimal> sumWithdrawByCoinType(Long userId, Long stakeRoundId) {
		return withdrawalService.lambdaQuery()
			.eq(Withdrawal::getUserId, userId)
			.eq(Withdrawal::getStakeRoundId, stakeRoundId)
			.eq(Withdrawal::getStatus, 3)
			.in(Withdrawal::getCoinType, 3, 4)
			.select(Withdrawal::getCoinType, Withdrawal::getChangeBalance)
			.list()
			.stream()
			.filter(item -> item.getCoinType() != null && item.getChangeBalance() != null)
			.collect(Collectors.groupingBy(
				Withdrawal::getCoinType,
				Collectors.reducing(BigDecimal.ZERO, Withdrawal::getChangeBalance, BigDecimal::add)
			));
	}

	public static void main(String[] args) {
		System.out.println("0x54E86Bef8C25FF4Cadc53450A136a410599256FE".toLowerCase());
	}
	@Override
	public UserNodeInfoBo userNodeInfo() {
		UserNodeInfoBo result = new UserNodeInfoBo();
		List<UserNodeInfoBo.DayInfo> collect = nodePlanOrderService.lambdaQuery()
			.eq(NodePlanOrder::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.in(NodePlanOrder::getBizStatus, 1, 2)
			.orderByDesc(NodePlanOrder::getId)
			.select(NodePlanOrder::getId,NodePlanOrder::getHaveDay,NodePlanOrder::getTotalDay)
			.list()
			.stream().map(record -> {
				UserNodeInfoBo.DayInfo dayInfo = new UserNodeInfoBo.DayInfo();
				dayInfo.setHaveDay(record.getHaveDay());
				dayInfo.setTotalDay(record.getTotalDay());
				return dayInfo;
			}).collect(Collectors.toList());
		result.setDayInfo(collect);
		return result;
	}

	@NotNull
	private static ResultPista<Object> registerIM(UserInfo userInfo) {
		//调用第三方接口注册到腾讯im里面去
		String faceUrl ="https://www.sigmapro.cc/profile/upload/2025/08/19/img_v3_02pa_9933ac6e-35b5-443e-878d-16e50f89998g_20250819111743A004.png";
		// 1. 生成UserSig - 必须使用管理员账号生成，因为需要管理员权限调用API
		String userSig = "eJwtzF0LgjAYBeD-sltD5nR*QVcRtoiwD4gup5vxZs41V0TRf8-Uy-Ocw-mg4*bgPqVBKSIuRrMhg5DKQgUDc9GAgs4ablszDTpRc61BoNSLiIcxIWE4NvKlwcjeKaUEYzyqheZvEY1pEHs0mF7g0v8X*9K5PSrYLZNszVo-YB17r5gCf5uVzj1ZxHl*ste6lOc5*v4AUiQ0uQ__";

		// 2. 构建请求URL - 新加坡数据中心
		String random = String.valueOf(RandomUtil.randomLong(100000000, 999999999));
		String url = String.format(
			"https://adminapisgp.im.qcloud.com/v4/im_open_login_svc/account_import?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json",
			SDK_APP_ID, ADMIN_IDENTIFIER, userSig, random  // identifier必须是管理员账号
		);

		// 3. 构建请求体
		JSONObject requestBody = new JSONObject();
		//设置昵称
		if(StrUtil.isNotBlank(userInfo.getAccount())){
			requestBody.put("Nick", userInfo.getAccount());
		}else{
			requestBody.put("Nick", userInfo.getAccount());
		}

		//设置头像
		if(StrUtil.isNotBlank(userInfo.getAvatar())){
			faceUrl = userInfo.getAvatar();
		}
		requestBody.put("UserID", userInfo.getAccount());

		requestBody.put("FaceUrl", faceUrl);

		log.info("腾讯IM导入账号请求: URL={}, Body={}", url, requestBody.toString());
		// 4. 发送HTTP请求
		String response = HttpUtil.createPost(url)
			.header("Content-Type", "application/json")
			.body(requestBody.toString())
			.timeout(30000)
			.execute()
			.body();

		log.info("腾讯IM导入账号响应: {}", response);

		// 5. 解析响应
		if (StrUtil.isNotBlank(response)) {
			JSONObject responseJson = JSONUtil.parseObj(response);

			if ("OK".equals(responseJson.getStr("ActionStatus")) &&
				responseJson.getInt("ErrorCode") == 0) {
				return ResultPista.success();
			} else if ("FAIL".equals(responseJson.getStr("ActionStatus")) &&
				responseJson.getInt("ErrorCode") == 70399) {
				throw new ServiceException(ResponseCode.CODE_1103);
			}else{
				throw new ServiceException(ResponseCode.CODE_1103);
			}
		} else {
			throw new ServiceException(ResponseCode.CODE_1116);
		}
	}

	/**
	 * 获取token
	 *
	 * @param getUser
	 * @param appTokenService
	 * @param tokenPrefix
	 * @return
	 */
	static ResultPista<LoginAppUser> getLoginAppUserResult(UserInfo getUser, AppTokenService appTokenService, String tokenPrefix) {
		LoginAppUser loginAppUser = new LoginAppUser();
		loginAppUser.setUserId(getUser.getUserId());
		loginAppUser.setClientId(tokenPrefix);
		loginAppUser.setUserCode(getUser.getUserCode());
		String token = appTokenService.createToken(loginAppUser);
		loginAppUser.setToken(token);
		loginAppUser.setRegAddress(getUser.getAccount());
		return ResultPista.data(loginAppUser);
	}

	/**
	 * @param account    账号
	 * @param code       验证码
	 * @param verifyType 验证码 业务类型 1:注册,2:绑定邮箱,3:提现,4:修改密码,5:忘记密码
	 */
	public static void verifyCode(String account, String code, Integer verifyType, String uuid, XmsRedis xmsRedis, ISysParaService sysParaServiceImpl) {
		//校验code
		String key = StringUtils.join(RedisConstant.CAPTCHA_SMS, account, RedisConstant.SEPARATOR, verifyType, uuid);
		verifyCode(code, xmsRedis, sysParaServiceImpl, key);
	}

	private static void verifyCode(String code, XmsRedis xmsRedis, ISysParaService sysParaServiceImpl, String key) {
		String realCode = xmsRedis.get(key);
		if (!code.equals(realCode)) {
			String value = sysParaServiceImpl.getValue(SysConstant.VERIFY_CODE_OFF);
			if (!value.equals("1")) {
				throw new ServiceException(ResponseCode.VALIDATE_CODE_ERROR);
			}
		}
		xmsRedis.del(key);
	}

	/**
	 * 签名
	 *
	 * @param randomNum
	 * @param signature
	 * @param address
	 */
	static void checkWallet(String randomNum, String signature, String address, XmsRedis xmsRedis) {
		address = address.toLowerCase();
		String osName = SystemUtil.getOsInfo().getName();
		if (osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			return;
		}

		if (!xmsRedis.hasKey(ConstantStatic.USER_RANDOM + address + randomNum)) {
			throw new ServiceException(ResponseCode.RANDOM_NOT_EXIT);
		}
		//boolean validate = MetaMaskUtil.validate(signature, randomNum, address);
		boolean validate = MetaMaskUtil.verify(randomNum, signature, address);
		if (!validate) {
			throw new ServiceException(ResponseCode.SIGN_VALIDATE_ERROR);
		}
		xmsRedis.del(ConstantStatic.USER_RANDOM + address + randomNum);
	}

	@Override
	public List<UserMoneyLog> powerDataList(Long lastId) {
		List<UserMoneyLog> userMoneyLogList = userMoneyLogService.lambdaQuery()
			.eq(UserMoneyLog::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.eq(UserMoneyLog::getCoinType, 2)
			.in(UserMoneyLog::getSourceType, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
			.lt(Func.isNotEmpty(lastId), UserMoneyLog::getId, lastId)
			.orderByDesc(UserMoneyLog::getId)
			.last(SysConstant.PAGE_LIMIT)
			.list();
		return userMoneyLogList;
	}

	@Override
	public ComputingPowerBo computingPowerData() {
		ComputingPowerBo result = new ComputingPowerBo();
		Long userId = SecurityUtils.getLoginAppUser().getUserId();
		UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, userId)
			.one();

		result.setGlobalTotalPower(xmsRedis.get(RedisConstant.USER_WITHDRAW_GROUP,()->{
			return userInfoServiceImpl.userTotalComputingPower();
		}, RedisConstant.SECONDS_EXPIRE_TIME / SysConstant.SIX, TimeUnit.MINUTES));
		result.setTodayReward(userMoneyService.getTodayReward(userId));
		result.setTotalReward(userMoneyService.getTotalReward(userId));
		return result;
	}

	@Override
	public TeamViewBO getTeamView(Long userId) {
		UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, userId)
			.one();
		TeamViewBO teamViewBO = new TeamViewBO();
		//直推业绩

		teamViewBO.setSubPerformance(userInfo.getSubPerformance());
		//小区业绩
		teamViewBO.setCommunityPerformance(userInfo.getCommunityPerformance());
		//团队业绩
		teamViewBO.setUmbrellaPerformance(userInfo.getUmbrellaPerformance());
		//邀请人数
		userInfo.getUmbrellaNum();
		teamViewBO.setUmbrellaNum(userInfo.getUmbrellaNum());
		teamViewBO.setSubNum(userInfo.getSubNum());
		//直推奖励
		teamViewBO.setSubReward(userMoneyService.querySubReward(userId));
		teamViewBO.setIndirectReward(userMoneyService.queryIndirectReward(userId));
		//间推奖励
		return teamViewBO;
	}

	/**
	 * 绑定邀请人
	 * @param req 邀请信息
	 * @return
	 */
	@Override
	@RedisLock(value = RedisConstant.LockConstant.USER_LOGIN, param = "#req.userId")
	@Transactional(rollbackFor = Exception.class)
	public ResultPista bindInviteUser(BindInviteUserReq req) {
		return ResultPista.success();
	}

	/**
	 * 我的直推用户
	 * @return
	 */
	@Override
	public List<MyDirectMemberDto> listSubMembers(String address) {
		//查询现在的轮次
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		if(StrUtil.isNotBlank(address)){
			return searchUserInfo(address,stakeRound);
		}
		List<UserInfo> childUserInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getInviteUserId, SecurityUtils.getLoginAppUser().getUserId())
			.last("limit 300")
			.list();
		if(CollectionUtil.isEmpty(childUserInfo)){
			return new ArrayList<>();
		}
		Map<Long, String> inviteAccountMap = childUserInfo.stream()
			.map(UserInfo::getInviteUserId)
			.filter(Objects::nonNull)
			.distinct()
			.collect(Collectors.collectingAndThen(Collectors.toList(), inviteUserIds -> {
				if (CollectionUtil.isEmpty(inviteUserIds)) {
					return new HashMap<>(0);
				}
				List<UserInfo> inviteUsers = userInfoServiceImpl.lambdaQuery()
					.in(UserInfo::getUserId, inviteUserIds)
					.select(UserInfo::getUserId, UserInfo::getAccount)
					.list();
				if (CollectionUtil.isEmpty(inviteUsers)) {
					return new HashMap<>(0);
				}
				return inviteUsers.stream()
					.filter(u -> u.getUserId() != null)
					.collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getAccount, (a, b) -> a));
			}));

		Set<Long> userIds = childUserInfo.stream().map(UserInfo::getUserId).collect(Collectors.toSet());
		Map<Long, UserStakePosition> positionTempMap = new HashMap<>();
		if(CollectionUtil.isNotEmpty(userIds)){
			positionTempMap = userStakePositionService.lambdaQuery()
				.in(UserStakePosition::getUserId, userIds)
				.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
				.list().stream().collect(Collectors.toMap(UserStakePosition::getUserId, Function.identity(), (a, b) -> a));
		}
		final Map<Long, UserStakePosition> positionMap = positionTempMap;


		List<MyDirectMemberDto> result = childUserInfo.stream().map(childInfo -> {
				MyDirectMemberDto entity = new MyDirectMemberDto();
				//用户id
				entity.setUserId(childInfo.getUserId());
				//地址
				entity.setAccount(childInfo.getAccount());
				entity.setNl(childInfo.getNodeLevel()>childInfo.getMinNodeLevel()?childInfo.getNodeLevel():childInfo.getMinNodeLevel());
				//等级
				entity.setGl(childInfo.getGameLevel()>childInfo.getMinGameLevel()?childInfo.getGameLevel():childInfo.getMinGameLevel());
				//质押量
				entity.setPf(childInfo.getPerformance().add(childInfo.getUmbrellaPerformance()));
				//直推人邀请地址
				entity.setIua(inviteAccountMap.getOrDefault(childInfo.getInviteUserId(), ""));
				//静态收益
				//创建时间
				entity.setCt(childInfo.getCreateTime());
				//直推人数
				entity.setSub(childInfo.getSubNum());
				//团队人数
				entity.setUmb(childInfo.getUmbrellaNum());
				UserStakePosition userStakePosition = positionMap.get(entity.getUserId());
				if(userStakePosition != null){
					entity.setCdr(userStakePosition.getCurrentDayRate());
					entity.setTsa(userStakePosition.getTotalStakeAmount());
				}
				//小区业绩
				entity.setCp(childInfo.getCommunityPerformance());
				return entity;
			}).collect(Collectors.toList());
		return result;
	}

	@NotNull
	private List<MyDirectMemberDto> searchUserInfo(String address,StakeRound stakeRound) {
		if(address.length()>60){
			//抵制非法
			return new ArrayList<>();
		}
		UserInfo searchUserInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getAccount, address)
			.eq(UserInfo::getInviteUserId, SecurityUtils.getLoginAppUser().getUserId())
			.one();
		if(searchUserInfo==null){
			return new ArrayList<>();
		}
		List<MyDirectMemberDto> result = new ArrayList<>(1);
		MyDirectMemberDto entity = new MyDirectMemberDto();
		//用户id
		entity.setUserId(searchUserInfo.getUserId());
		entity.setNl(searchUserInfo.getNodeLevel()>searchUserInfo.getMinNodeLevel()?searchUserInfo.getNodeLevel():searchUserInfo.getMinNodeLevel());
		//地址
		entity.setAccount(searchUserInfo.getAccount());
		//等级
		entity.setGl(searchUserInfo.getGameLevel()>searchUserInfo.getMinGameLevel()?searchUserInfo.getGameLevel():searchUserInfo.getMinGameLevel());
		//质押量
		entity.setPf(searchUserInfo.getPerformance().add(searchUserInfo.getUmbrellaPerformance()));
		//直推人邀请地址
		UserInfo inviteUser = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.select(UserInfo::getAccount)
			.one();
		entity.setIua(inviteUser.getAccount());
		//创建时间
		entity.setCt(searchUserInfo.getCreateTime());
		//直推人数
		entity.setSub(searchUserInfo.getSubNum());
		//团队人数
		entity.setUmb(searchUserInfo.getUmbrellaNum());
		//查询收益率和连续未提取天数
		if(stakeRound!=null){
			UserStakePosition userStakePosition = userStakePositionService.lambdaQuery()
				.eq(UserStakePosition::getUserId, searchUserInfo.getUserId())
				.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
				.one();
			if(userStakePosition !=null){
				entity.setCdr(userStakePosition.getCurrentDayRate());
				entity.setTsa(userStakePosition.getTotalStakeAmount());
			}
		}

		//小区业绩
		entity.setCp(searchUserInfo.getCommunityPerformance());
		result.add(entity);
		return result;
	}

	/**
	 * 我的团队数据
	 * @return
	 */
	@Override
	public List<MyDirectMemberDto> listMyDirectMembers(String address) {
		Long userId = SecurityUtils.getLoginAppUser().getUserId();
		if (StrUtil.isBlank(address)) {
			List<UserInfo> childUsers = userInfoMapper.getTeamMembersLimited(userId, null);
			if (CollectionUtil.isEmpty(childUsers)) {
				return Collections.emptyList();
			}
			Map<Long, String> inviteAccountMap = childUsers.stream()
				.map(UserInfo::getInviteUserId)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.collectingAndThen(Collectors.toList(), inviteUserIds -> {
					if (CollectionUtil.isEmpty(inviteUserIds)) {
						return new HashMap<>(0);
					}
					List<UserInfo> inviteUsers = userInfoServiceImpl.lambdaQuery()
						.in(UserInfo::getUserId, inviteUserIds)
						.select(UserInfo::getUserId, UserInfo::getAccount)
						.list();
					if (CollectionUtil.isEmpty(inviteUsers)) {
						return new HashMap<>(0);
					}
					return inviteUsers.stream()
						.filter(u -> u.getUserId() != null)
						.collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getAccount, (a, b) -> a));
				}));
			return childUsers.stream().map(childInfo -> {
				MyDirectMemberDto entity = new MyDirectMemberDto();
				//用户id
				entity.setUserId(childInfo.getUserId());
				//地址
				entity.setAccount(childInfo.getAccount());
				//等级
				entity.setGl(childInfo.getGameLevel());
				//质押量
				entity.setPf(childInfo.getPerformance());
				//直推人邀请地址
				entity.setIua(inviteAccountMap.getOrDefault(childInfo.getInviteUserId(), ""));

				//创建时间
				entity.setCt(childInfo.getCreateTime());
				//直推人数
				entity.setSub(childInfo.getSubNum());
				//团队人数
				entity.setUmb(childInfo.getUmbrellaNum());

				//小区业绩
				entity.setCp(childInfo.getCommunityPerformance());
				//个人节点
				entity.setPf(childInfo.getPerformance());
				return entity;
			}).collect(Collectors.toList());
		}

		if (address.length() > 60) {
			//地址非法
			return new ArrayList<>();
		}

		UserInfo serachUserInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getAccount, address)
			.select(UserInfo::getUserId)
			.one();
		if (serachUserInfo == null) {
			return new ArrayList<>();
		}

		Long targetUserId = serachUserInfo.getUserId();
		Long relationCount = userRelationService.lambdaQuery()
			.eq(UserRelation::getParUserId, userId)
			.eq(UserRelation::getPosUserId, targetUserId)
			.eq(UserRelation::getActiveFlag, 1)
			.gt(UserRelation::getDistance, 0)
			.count();
		if (relationCount == null || relationCount == 0) {
			return Collections.emptyList();
		}

		UserInfo childInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, targetUserId)
			.select(UserInfo::getUserId, UserInfo::getAccount, UserInfo::getGameLevel,
				UserInfo::getPerformance, UserInfo::getCreateTime, UserInfo::getSubNum,
				UserInfo::getUmbrellaNum, UserInfo::getCommunityPerformance,UserInfo::getInviteUserId)
			.one();
		if (childInfo == null) {
			return Collections.emptyList();
		}
		String inviteUserAccount = "";
		if (childInfo.getInviteUserId() != null) {
			UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
				.eq(UserInfo::getUserId, childInfo.getInviteUserId())
				.select(UserInfo::getAccount)
				.one();
			if (userInfo != null) {
				inviteUserAccount = userInfo.getAccount();
			}
		}
		MyDirectMemberDto entity = new MyDirectMemberDto();
		//用户id
		entity.setUserId(childInfo.getUserId());
		//地址
		entity.setAccount(childInfo.getAccount());
		//等级
		entity.setGl(childInfo.getGameLevel());
		//质押量
		entity.setPf(childInfo.getPerformance());

		entity.setIua(inviteUserAccount);

		//创建时间
		entity.setCt(childInfo.getCreateTime());
		//直推人数
		entity.setSub(childInfo.getSubNum());
		//团队人数
		entity.setUmb(childInfo.getUmbrellaNum());

		//小区业绩
		entity.setCp(childInfo.getCommunityPerformance());
		//个人节点
		entity.setPf(childInfo.getPerformance());
		return Collections.singletonList(entity);
	}

	/**
	 * 我的团队数据 总成员、直推人数、团队销毁usdt、等级
	 * @param lastId lastId
	 * @param distance 层级
	 * @param level level
	 * @return
	 */
	@Override
	public MyTeamMemberPageDto listMyTeamMembers(Long lastId, Integer distance,Integer level) {
		Long userId = SecurityUtils.getLoginAppUser().getUserId();
		List<Long> levelUserIds = null;
		if (level != null) {
			levelUserIds = userInfoServiceImpl.lambdaQuery()
				.eq(UserInfo::getGameLevel, level)
				.select(UserInfo::getUserId)
				.list().stream()
				.map(UserInfo::getUserId)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
			if (CollectionUtil.isEmpty(levelUserIds)) {
				MyTeamMemberPageDto empty = new MyTeamMemberPageDto();
				empty.setTotal(0L);
				empty.setRecords(Collections.emptyList());
				return empty;
			}
		}

		LambdaQueryWrapper<UserRelation> countWrapper = new LambdaQueryWrapper<>();
		countWrapper.eq(UserRelation::getParUserId, userId)
			.eq(UserRelation::getActiveFlag, 1)
			.gt(UserRelation::getDistance, 0);
		if (distance != null) {
			countWrapper.eq(UserRelation::getDistance, distance);
		}
		if (levelUserIds != null) {
			countWrapper.in(UserRelation::getPosUserId, levelUserIds);
		}
		long total = userRelationService.count(countWrapper);

		LambdaQueryWrapper<UserRelation> pageWrapper = new LambdaQueryWrapper<>();
		pageWrapper.eq(UserRelation::getParUserId, userId)
			.eq(UserRelation::getActiveFlag, 1)
			.gt(UserRelation::getDistance, 0);
		if (distance != null) {
			pageWrapper.eq(UserRelation::getDistance, distance);
		} else {
			pageWrapper.orderByAsc(UserRelation::getDistance);
		}
		if (levelUserIds != null) {
			pageWrapper.in(UserRelation::getPosUserId, levelUserIds);
		}
		if (lastId != null) {
			UserRelation lastRelation = userRelationService.lambdaQuery()
				.eq(UserRelation::getParUserId, userId)
				.eq(UserRelation::getPosUserId, lastId)
				.eq(UserRelation::getActiveFlag, 1)
				.select(UserRelation::getId)
				.orderByDesc(UserRelation::getId)
				.last("limit 1")
				.one();
			if (lastRelation != null) {
				pageWrapper.lt(UserRelation::getId, lastRelation.getId());
			}
		}
		pageWrapper.orderByDesc(UserRelation::getId).last(SysConstant.PAGE_LIMIT);

		List<UserRelation> relationList = userRelationService.list(pageWrapper);
		if (CollectionUtil.isEmpty(relationList)) {
			MyTeamMemberPageDto empty = new MyTeamMemberPageDto();
			empty.setTotal(total);
			empty.setRecords(Collections.emptyList());
			return empty;
		}

		List<Long> childUserIds = relationList.stream()
			.map(UserRelation::getPosUserId)
			.filter(Objects::nonNull)
			.distinct()
			.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(childUserIds)) {
			MyTeamMemberPageDto empty = new MyTeamMemberPageDto();
			empty.setTotal(total);
			empty.setRecords(Collections.emptyList());
			return empty;
		}

		LambdaQueryChainWrapper<UserInfo> childQuery = userInfoServiceImpl.lambdaQuery()
			.in(UserInfo::getUserId, childUserIds)
			.select(UserInfo::getUserId, UserInfo::getAccount, UserInfo::getGameLevel,
				UserInfo::getUmbrellaPerformance,UserInfo::getCreateTime);
		if (level != null) {
			childQuery.eq(UserInfo::getGameLevel, level);
		}
		List<UserInfo> childUsers = childQuery.list();
		Map<Long, UserInfo> childUserMap = childUsers.stream()
			.collect(Collectors.toMap(UserInfo::getUserId, Function.identity(), (a, b) -> a));

		List<MyTeamMemberDto> result = new ArrayList<>(relationList.size());
		for (UserRelation relation : relationList) {
			UserInfo child = childUserMap.get(relation.getPosUserId());
			if (child == null) {
				continue;
			}
			MyTeamMemberDto dto = new MyTeamMemberDto();
			dto.setUserId(child.getUserId());
			dto.setAccount(child.getAccount());
			dto.setGameLevel(child.getGameLevel());
			dto.setUmbrellaPerformance(child.getUmbrellaPerformance());
			dto.setCreateTime(child.getCreateTime());
			dto.setDistance(relation.getDistance());
			result.add(dto);
		}
		MyTeamMemberPageDto dto = new MyTeamMemberPageDto();
		dto.setTotal(total);
		dto.setRecords(result);
		return dto;
	}

	/**
	 * 我的团队数据
	 * @param userId
	 * @return
	 */
	@Override
	public MyTeamInfoDto myTeamInfo(Long userId) {
		UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, userId)
			.select(UserInfo::getUserId,UserInfo::getSubNum,UserInfo::getUmbrellaNum,
				UserInfo::getGameLevel,UserInfo::getUmbrellaPerformance)
			.one();
		MyTeamInfoDto result = new MyTeamInfoDto();
		//团队总成员
		result.setUmbrellaNum(userInfo.getUmbrellaNum());
		//直推人数
		result.setSubNum(userInfo.getSubNum());
		//我的等级
		result.setGameLevel(userInfo.getGameLevel());
		//团队销毁USDt
		result.setUmbrellaPerformance(userInfo.getUmbrellaPerformance());
		result.setTeams(buildTeamLevelDistribution(userId));
		return result;
	}

	/**
	 * 构建团队等级分布（含全网对比数据）
	 */
	private List<TeamLevelDto> buildTeamLevelDistribution(Long userId) {
		List<Long> teamUserIds = userRelationService.lambdaQuery()
			.eq(UserRelation::getParUserId, userId)
			.eq(UserRelation::getActiveFlag, 1)
			.gt(UserRelation::getDistance, 0)
			.select(UserRelation::getPosUserId)
			.list()
			.stream()
			.map(UserRelation::getPosUserId)
			.filter(Objects::nonNull)
			.distinct()
			.collect(Collectors.toList());

		Map<Integer, Integer> teamCountMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(teamUserIds)) {
			List<Map<String, Object>> teamRows = userInfoServiceImpl.getBaseMapper().selectMaps(
				new QueryWrapper<UserInfo>()
					.in("user_id", teamUserIds)
					.select("game_level", "COUNT(1) AS cnt")
					.groupBy("game_level")
			);
			fillLevelCount(teamRows, teamCountMap);
		}

		Map<Integer, Integer> globalCountMap = new HashMap<>();
		List<Map<String, Object>> globalRows = userInfoServiceImpl.getBaseMapper().selectMaps(
			new QueryWrapper<UserInfo>()
				.select("game_level", "COUNT(1) AS cnt")
				.groupBy("game_level")
		);
		fillLevelCount(globalRows, globalCountMap);

		Set<Integer> levelSet = new TreeSet<>();
		levelSet.addAll(teamCountMap.keySet());
		levelSet.addAll(globalCountMap.keySet());

		List<TeamLevelDto> result = new ArrayList<>(levelSet.size());
		for (Integer level : levelSet) {
			TeamLevelDto dto = new TeamLevelDto();
			dto.setGameLevel(level);
			dto.setTeamCount(teamCountMap.getOrDefault(level, 0));
			dto.setGlobalCount(globalCountMap.getOrDefault(level, 0));
			result.add(dto);
		}
		return result;
	}

	private void fillLevelCount(List<Map<String, Object>> rows, Map<Integer, Integer> targetMap) {
		if (CollectionUtil.isEmpty(rows)) {
			return;
		}
		for (Map<String, Object> row : rows) {
			Object levelObj = row.get("game_level");
			Object countObj = row.get("cnt");
			if (!(levelObj instanceof Number) || !(countObj instanceof Number)) {
				continue;
			}
			Integer level = ((Number) levelObj).intValue();
			Integer count = ((Number) countObj).intValue();
			targetMap.put(level, count);
		}
	}

	/**
	 * 获取币种信息(价格、涨跌幅)
	 * @return
	 */
	@Override
	public CoinInfoBo getCoinInfo() {
		CoinInfoBo result = new CoinInfoBo();
		// 当前价格列表（BOOMAI / MAI）
		List<CoinPrice> list = coinPriceService.lambdaQuery().list();

		// 昨日价格（按日统计表）
		String yesterDay = DateUtil.format(DateUtil.yesterday(), "yyyyMMdd");
		Long dayLong = Long.valueOf(yesterDay);
		BigDecimal lastBoomaiPrice = BigDecimal.ZERO;
		BigDecimal lastMaiPrice = BigDecimal.ZERO;

		List<PtbDailyPrice> historyPriceList = ptbDailyPriceService.lambdaQuery()
			.eq(PtbDailyPrice::getDate, dayLong)
			.list();
		if (CollectionUtil.isNotEmpty(historyPriceList)) {
			for (PtbDailyPrice ptbDailyPrice : historyPriceList) {
				if (ptbDailyPrice.getCoinType() != null && ptbDailyPrice.getCoinType() == 1) {
					lastBoomaiPrice = ptbDailyPrice.getPrice();
				} else if (ptbDailyPrice.getCoinType() != null && ptbDailyPrice.getCoinType() == 2) {
					lastMaiPrice = ptbDailyPrice.getPrice();
				}
			}
		}

		// 计算涨跌幅并填充结果
		for (CoinPrice coinPrice : list) {
			if (coinPrice.getCoinType() != null && coinPrice.getCoinType() == 1) {
				BigDecimal current = coinPrice.getCurrentPrice();
				result.setBoomaiPrice(current);
				result.setBoomaiChangeRate(calcChangeRate(current, lastBoomaiPrice));
			} else if (coinPrice.getCoinType() != null && coinPrice.getCoinType() == 2) {
				BigDecimal current = coinPrice.getCurrentPrice();
				result.setMaiPrice(current);
				result.setMaiChangeRate(calcChangeRate(current, lastMaiPrice));
			}
		}

		return result;
	}

	/**
	 * 计算涨跌幅：(current - last) / last * 100
	 */
	private BigDecimal calcChangeRate(BigDecimal current, BigDecimal last) {
		if (current == null || last == null || last.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		}
		return current
			.subtract(last)
			.divide(last, ConstantStatic.newScale, ConstantStatic.roundingModeNew)
			.multiply(new BigDecimal("100"));
	}

	@Override
	@RedisLock(value = RedisConstant.LockConstant.USER_LOGIN, param = "#loginVo.address")
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<LoginAppUser> login(LoginVo loginVo) {
		//验签，随机数
		checkWallet(loginVo.getRandomNum(), loginVo.getSignature(), loginVo.getAddress(), xmsRedis);
		UserInfo userInfo;
		//根据注册钱包地址查询
		userInfo = userInfoServiceImpl.lambdaQuery().eq(UserInfo::getAccount, loginVo.getAddress()).one();
		if (userInfo == null) {
			UserInfo inviteUser;
			if (StringUtils.isBlank(loginVo.getInviteCode())) {
				throw new ServiceException(ResponseCode.CODE_1010);
			} else {
				inviteUser = userInfoServiceImpl.lambdaQuery().eq(UserInfo::getAccount, loginVo.getInviteCode()).one();
				if (inviteUser == null) {
					throw new ServiceException(ResponseCode.CODE_1010);
				}
				Long count = userStakePositionService.lambdaQuery()
					.eq(UserStakePosition::getUserId, inviteUser.getUserId())
					.count();
				if(count<=0){
					throw new ServiceException(ResponseCode.CODE_1256);
				}
			}
			if (inviteUser == null) {
				throw new ServiceException(ResponseCode.CODE_1010);
			}
			//查询上级团队用户
			List<UserRelation> urList = userRelationService.getParentList(inviteUser.getUserId());
			List<Long> parentUserIds = urList.stream().map(UserRelation::getParUserId).collect(Collectors.toList());
			//父级链
			String parentChain;
			if (StringUtils.isBlank(inviteUser.getParentChain())) {
				parentChain = String.valueOf(inviteUser.getUserId());
			} else {
				parentChain = inviteUser.getParentChain() + "," + inviteUser.getUserId();
			}
			//新增用户
			userInfo = UserInfo.builder()
				//账号
				.account(loginVo.getAddress())
				//用户编码
				.userCode(RandomUtil.randomNumbers(10))
				.gameLevel(SysConstant.ZERO)
				//保底等级
				.minGameLevel(SysConstant.ZERO)
				//用户名密码 存的是md5然后盐加密之后的密码
				.inviteUserId(inviteUser.getUserId())
				.inviteUserCode(inviteUser.getUserCode())
				.isValid(SysConstant.ZERO)
				.subNum(SysConstant.ZERO)
				.validSubNum(SysConstant.ZERO)
				.umbrellaNum(SysConstant.ZERO)
				.validUmbrellaNum(SysConstant.ZERO)
				.performance(BigDecimal.ZERO)
				.umbrellaPerformance(BigDecimal.ZERO)
				.parentChain(parentChain)
				.withdrawalOpenOrClose(SysConstant.TWO)
				.status(SysConstant.ONE)
				.build();
			boolean res = userInfoServiceImpl.save(userInfo);
			if (!res) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new ServiceException(ResponseCode.CODE_1002);
			}

			//更新上级直推人数
			userInfoServiceImpl.lambdaUpdate().setSql(" sub_num = sub_num + 1 ")
				.eq(UserInfo::getUserId, inviteUser.getUserId()).update();
			if (parentUserIds.size() > 0) {
				//更新上级团队人数
				userInfoServiceImpl.lambdaUpdate().setSql(" umbrella_num = umbrella_num + 1")
					.in(UserInfo::getUserId, parentUserIds).update();
			}
			//创建新系统钱包
			UserMoney userMoney = UserMoney.builder().id(userInfo.getUserId()).build();
			userMoneyService.save(userMoney);
			UserWealthVault userWealthVault = new UserWealthVault();
			userWealthVault.setId(userInfo.getUserId());
			userWealthVault.setSeg1Amount(BigDecimal.ZERO);
			userWealthVault.setSeg2Amount(BigDecimal.ZERO);
			userWealthVault.setSeg3Amount(BigDecimal.ZERO);
			userWealthVault.setSeg4Amount(BigDecimal.ZERO);
			userWealthVault.setSeg5Amount(BigDecimal.ZERO);
			userWealthVault.setSeg6Amount(BigDecimal.ZERO);
			userWealthVault.setSeg7Amount(BigDecimal.ZERO);
			userWealthVaultService.save(userWealthVault);
			//新增关系表
			List<UserRelation> dataList = Lists.newArrayList();
			UserRelation ur = UserRelation.builder().parUserId(userInfo.getUserId())
				.posUserId(userInfo.getUserId()).distance(0).build();
			dataList.add(ur);//新增自己
			for (UserRelation temp : urList) {
				//限制最多200层
				if (temp.getDistance() + 1 > 200) {
					throw new ServiceException(ResponseCode.CODE_1064);
				}
				UserRelation urPar = UserRelation.builder().parUserId(temp.getParUserId())
					.posUserId(userInfo.getUserId()).distance(temp.getDistance() + 1).build();
				dataList.add(urPar);
			}

			// 批量插入
			boolean b = userRelationService.saveBatch(dataList);
			if (!b) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new ServiceException(ResponseCode.CODE_1003);
			}

		} else {
			if (!userInfo.getStatus().equals(SysConstant.ONE)) {
				throw new ServiceException(ResponseCode.CODE_401);
			}
		}

		//记录用户登录IP地址
		recordUserLoginIp(userInfo);
		//删除随机数验证
		xmsRedis.del(ConstantStatic.USER_RANDOM + loginVo.getAddress());
		return getLoginAppUserResult(userInfo, appTokenService, Constants.TOKEN_APP_PREFIX);
	}

	/**
	 * 记录用户登录IP地址
	 * <p>
	 * 该方法会将用户每次登录的IP地址和时间记录到用户信息中，
	 * 最多保留最近5次的登录IP记录，格式为"时间/IP地址"。
	 * </p>
	 *
	 * @param userInfo 用户信息对象，包含用户的基本信息和登录IP记录
	 */
	private void recordUserLoginIp(UserInfo userInfo) {
		//记录登录ip
		String resIp = IpUtils.getIpAddr(ServletUtils.getRequest());
		if(StrUtil.isBlank(userInfo.getLastLoginIp())){
			userInfoServiceImpl.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getUserId())
				.set(UserInfo::getLastLoginIp, DateUtil.now()+"/"+resIp)
				.update();
		}else{
			List<String> resIpList = StrUtil.split(userInfo.getLastLoginIp(), ',')
				.stream()
				.map(String::trim) // 去掉空格
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
			resIpList.add(DateUtil.now()+"/"+resIp);
			// 保持最多5个IP记录，如果超过则移除最旧的（列表开头的）
			if(resIpList.size() > 5) {
				resIpList = resIpList.subList(resIpList.size() - 5, resIpList.size());
			}
			String resIpListStr = String.join(",", resIpList);
			userInfoServiceImpl.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getUserId())
				.set(UserInfo::getLastLoginIp, resIpListStr)
				.update();
		}
	}

	@Override
	public String getMessage(String address) {
		String radom = IdUtil.randomUUID();
		xmsRedis.set(ConstantStatic.USER_RANDOM + address + radom, radom, SysConstant.FIVE_LONG, TimeUnit.MINUTES);
		log.info(" address:{},radom:{} ", address, radom);
		return radom;
	}

	/**
	 * 注册
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPista register(RegisterSmsVo req) throws Exception {
		//账号校验
		verifyAccount(req.getAccount());
		Long count = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getAccount, req.getAccount())
			.count();
		if(count>0){
			throw new ServiceException(ResponseCode.CODE_1103);
		}

		//校验邮箱格式是否正确
		if (!Validator.isEmail(req.getEmail())) {
			throw new ServiceException(ResponseCode.CODE_1215);
		}
		 count = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getEmail, req.getEmail())
			.count();
		if(count>0){
			//throw new ServiceException(ResponseCode.CODE_1214);
		}

		UserInfo inviteUserInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserCode, req.getInviteUserCode())
			.eq(UserInfo::getDeleted,0)
			.one();
		if(inviteUserInfo == null){
			throw new ServiceException(ResponseCode.CODE_1104);
		}
		//校验验证码是否正确
		verifyCode(req.getEmail(), req.getCode(), SysConstant.ONE, req.getUuid(), xmsRedis, sysParaServiceImpl);
		//注册
		return ResultPista.data(SpringUtils.getBean(BizUserServiceImpl.class).realRegister(req,inviteUserInfo));
	}

	@Override
	public void bindEmail(BindEmailVo req) {
	/*	//校验邮箱格式是否正确
		if (!Validator.isEmail(req.getEmail())) {
			throw new ServiceException(ResponseCode.CODE_1215);
		}

		long count = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getEmail, req.getEmail())
			.count();
		if(count>0){
			throw new ServiceException(ResponseCode.CODE_1214);
		}

		//校验验证码是否正确
		verifyCode(req.getEmail(), req.getCode(), SysConstant.TWO, req.getUuid(), xmsRedis, sysParaServiceImpl);
		UserInfo queryUserInfo = userInfoServiceImpl.lambdaQuery()
			.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.select(UserInfo::getEmail,UserInfo::getUserId)
			.one();
		if(StrUtil.isNotBlank(queryUserInfo.getEmail())){
			throw new ServiceException(ResponseCode.CODE_1216);
		}
		//绑定
		boolean update = userInfoServiceImpl.lambdaUpdate()
			.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.set(UserInfo::getEmail, req.getEmail())
			.update();
		if(!update){
			throw new ServiceException(ResponseCode.CODE_1002);
		}*/
	}

	/**
	 * 验证账号是否正确
	 *
	 * @param account
	 * @return
	 */
	private void verifyAccount(String account) {
		if(!isValidAccount(account)){
			//throw new ServiceException(ResponseCode.CODE_1102);
		}
	}

	/**
	 * 验证账号是否为6-16位数字和字母组合
	 *
	 * @param account
	 * @return
	 */
	private boolean isValidAccount(String account) {
		// 允许纯字母或者数字和字母组合
		return account.matches("^[a-zA-Z]{6,16}$") ||
			account.matches("^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{6,16}$");
	}

	/**
	 * 注册
	 *
	 * @param req
	 * @param inviteUserInfo  邀请用户
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResultPista realRegister(RegisterSmsVo req,UserInfo inviteUserInfo) throws Exception {
		//登录密码
		String loginSale = RandomUtil.randomString(8);
		String loginPwd = Md5Utils.hash(req.getLoginPwd() + loginSale);

		//父级链
		String parentChain;
		if (StringUtils.isBlank(inviteUserInfo.getParentChain())) {
			parentChain = String.valueOf(inviteUserInfo.getUserId());
		} else {
			parentChain = inviteUserInfo.getParentChain() + "," + inviteUserInfo.getUserId();
		}
		UserInfo userInfo = UserInfo.builder()
			//账号
			.account(req.getAccount())
			.email(req.getEmail())
			//用户编码
			.userCode(GenUUID.getCode(6))
			.gameLevel(SysConstant.ZERO)
			//保底等级
			.minGameLevel(SysConstant.ZERO)
			.inviteUserId(inviteUserInfo.getUserId())
			.inviteUserCode(inviteUserInfo.getUserCode())
			.isValid(SysConstant.ZERO)
			.subNum(SysConstant.ZERO)
			.validSubNum(SysConstant.ZERO)
			.umbrellaNum(SysConstant.ZERO)
			.validUmbrellaNum(SysConstant.ZERO)
			.performance(BigDecimal.ZERO)
			.umbrellaPerformance(BigDecimal.ZERO)
			.parentChain(parentChain)
			.withdrawalOpenOrClose(SysConstant.TWO)
			.status(SysConstant.ONE)
			.build();

		//查询上级团队用户
		List<UserRelation> urList = userRelationService.getParentList(inviteUserInfo.getUserId());
		List<Long> parentUserIds = urList.stream().map(UserRelation::getParUserId).collect(Collectors.toList());

		try {
			boolean res = userInfoServiceImpl.save(userInfo);
			if (!res) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw new ServiceException(ResponseCode.CODE_1002);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ResponseCode.CODE_1002);
		}
		//更新上级直推人数
		userInfoServiceImpl.lambdaUpdate().setSql(" sub_num = sub_num + 1 ")
			.eq(UserInfo::getUserId, inviteUserInfo.getUserId()).update();
		if (parentUserIds.size() > 0) {
			//更新上级团队人数
			userInfoServiceImpl.lambdaUpdate().setSql(" umbrella_num = umbrella_num + 1")
				.in(UserInfo::getUserId, parentUserIds).update();
		}
		//创建新系统钱包
		UserMoney userMoney = UserMoney.builder().id(userInfo.getUserId()).build();
		userMoneyService.save(userMoney);

		//新增关系表
		List<UserRelation> dataList = Lists.newArrayList();
		UserRelation ur = UserRelation.builder().parUserId(userInfo.getUserId())
			.posUserId(userInfo.getUserId()).distance(0).build();
		dataList.add(ur);//新增自己
		for (UserRelation temp : urList) {
			//限制最多100层
			if (temp.getDistance() + 1 > 200) {
				throw new ServiceException(ResponseCode.CODE_1064);
			}
			UserRelation urPar = UserRelation.builder().parUserId(temp.getParUserId())
				.posUserId(userInfo.getUserId()).distance(temp.getDistance() + 1).build();
			dataList.add(urPar);
		}
		// 批量插入
		userRelationService.saveBatch(dataList);
		//插入用户收益表数据初始化一条
		userIncomeSummaryService.save(UserIncomeSummary.builder().userId(userInfo.getUserId()).build());
		return registerIM(userInfo);
	}

	/**
	 * 退出登录
	 *
	 * @param request
	 */
	@Override
	public ResultPista logout(HttpServletRequest request) {
		LoginAppUser loginUser = appTokenService.getLoginUser(request);
		if (com.xms.common.utils.StringUtils.isNotNull(loginUser)) {
			// 删除用户缓存记录
			appTokenService.delLoginUser(loginUser.getClientId(), loginUser.getUserId().toString());
		}
		return ResultPista.success();
	}

	/**
	 * 修改用户基础信息
	 * @param req
	 */
	@Override
	public void updateBaseInfo(UserBaseInfoVo req) {
		if(StringUtils.isNotBlank(req.getNickName()) || StringUtils.isNotBlank(req.getAvatar())){
			userInfoServiceImpl.lambdaUpdate()
				.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
				.set(StringUtils.isNotBlank(req.getNickName()),UserInfo::getAccount, req.getNickName())
				.set(StringUtils.isNotBlank(req.getAvatar()),UserInfo::getAvatar, req.getAvatar())
				.update();
			UserInfo userInfo = userInfoServiceImpl.lambdaQuery()
				.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
				.one();
			//获取域名
			userInfo.setAvatar(sysParaServiceImpl.getValue(ConstantSys.biz_image_domain)+userInfo.getAvatar());
			registerIM(userInfo);
		}
	}

	/**
	 * 获取用户收益信息
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public UserIncomeSummaryVo getIncomeSummary(Long userId) {
		UserIncomeSummaryVo result = new UserIncomeSummaryVo();
		UserIncomeSummary userIncomeSummary = userIncomeSummaryService.lambdaQuery()
			.eq(UserIncomeSummary::getUserId, userId)
			.one();
		result.setSourceType21Balance(userIncomeSummary.getSourceType21Balance1().add(userIncomeSummary.getSourceType21Balance0()));
		result.setSourceType23Balance(userIncomeSummary.getSourceType23Balance());
		result.setSourceType24Balance(userIncomeSummary.getSourceType25Balance().add(userIncomeSummary.getSourceType24Balance()));
		return result;
	}

	/**
	 * 获取用户资产信息
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public UserAssetInfoBo getUserAssetInfo(Long userId) {
		UserAssetInfoBo assetInfoBo = new UserAssetInfoBo();

		// BOOMAI余额
		assetInfoBo.setValidNum1Value(userMoneyService.getById(userId).getValidNum1());
		// 销毁价值usdt量
		assetInfoBo.setTotalUsdtValue(destroyOrderService.sumTotalUsdtValue());

		// 团队日新增业绩
		Integer todayInt = Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd"));
		TeamDestroyStatDto todayTeamStat = destroyOrderService.getTodayTeamStat(userId, todayInt);
		assetInfoBo.setTodayTeamUsdtValue(todayTeamStat.getTotalUsdt());
		assetInfoBo.setTodayTeamValidNum1Value(todayTeamStat.getTotalValidNum1());

		// 团队月新增业绩（当月）
		String currentMonthStr = DateUtil.format(DateUtil.date(), "yyyyMM");
		Integer monthStart = Integer.valueOf(currentMonthStr + "01");
		// 计算当月最后一天日期 yyyymmdd
		String monthEndStr = DateUtil.format(DateUtil.endOfMonth(DateUtil.date()), "yyyyMMdd");
		Integer monthEnd = Integer.valueOf(monthEndStr);

		TeamDestroyStatDto monthTeamStat = destroyOrderService.getMonthTeamStat(userId, monthStart, monthEnd);
		assetInfoBo.setMonthTeamUsdtValue(monthTeamStat.getTotalUsdt());
		assetInfoBo.setMonthTeamValidNum1Value(monthTeamStat.getTotalValidNum1());
		return assetInfoBo;
	}

	/**
	 * 发送邮箱验证码
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPista sendMesAuthCode(MesAuthCodeVo req) throws Exception{
		String account = req.getEmail();
		Integer bizType = req.getBizType();
		String uuid = IdUtil.fastUUID();
		String code = RandomUtil.randomNumbers(SysConstant.SIX);
		//发送邮箱验证码
		//0.1 校验邮箱格式
		if (!Validator.isEmail(account)) {
			return ResponseCode.getR(ResponseCode.CODE_1215);
		}
		//0.2 只有当 bizType 不等于 1 且不等于 2 时，才校验邮箱是否已经注册
		if (!bizType.equals(1) && !bizType.equals(2)) {
			// 校验邮箱是否已经注册
			Long count = userInfoServiceImpl.lambdaQuery()
				.eq(UserInfo::getEmail, account)
				.count();
			if (count <= 0) {
				return ResponseCode.getR(ResponseCode.CODE_1007);
			}
		}
		//获取可以用的邮箱
		List<EmailConfig> emailList = xmsRedis.get(RedisConstant.GOOGLE_EMAIL_LIST, () -> emailConfigService.lambdaQuery()
			.eq(EmailConfig::getEnable, 1).list(), RedisConstant.DAY_EXPIRE_TIME, TimeUnit.DAYS);
		if(CollectionUtil.isEmpty(emailList)){
			//throw new ServiceException(ResponseCode.CODE_1213);
		}

		xmsRedis.set(StringUtils.join(RedisConstant.CAPTCHA_SMS, req.getEmail(), RedisConstant.SEPARATOR, bizType, uuid), code, 120L, TimeUnit.SECONDS);
		//随机获取一个邮箱
		EmailConfig selectedEmail = RandomUtil.randomEle(emailList);
		AliyunSenMailUtil.MailInfo mailInfo = new AliyunSenMailUtil.MailInfo();
		mailInfo.setUsername(selectedEmail.getEmail());
		mailInfo.setPassword(selectedEmail.getAppAuthPassword());
		mailInfo.setToUser(account);
		mailInfo.setSubject("sigmaPro");
		mailInfo.setContent(MessageFormat.format("尊敬的客户您好，您本次的验证码为：{0}", code));
		if (!SystemUtil.getOsInfo().getName().toUpperCase().contains(ConstantStatic.OS_NAME_WINDOWS)){
			AliyunSenMailUtil.sendMail(mailInfo);
		}
		return ResultPista.data(uuid);
	}

	/**
 * 标准化字符串：去除多余空格，转小写
 */
private String normalizeString(String str) {
    if (str == null) {
        return "";
    }
    // 将多个连续空格替换为单个空格，去除首尾空格，转小写
    return str.trim().replaceAll("\\s+", " ").toLowerCase();
}
}
