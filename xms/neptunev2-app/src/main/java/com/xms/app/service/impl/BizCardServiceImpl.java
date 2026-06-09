package com.xms.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.system.SystemUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
import com.xms.app.entity.bo.DestroyCallbackBo;
import com.xms.app.entity.dto.CardPackageDto;
import com.xms.app.entity.dto.CardUpgradeLogDto;
import com.xms.app.entity.req.*;
import com.xms.app.entity.resp.*;
import com.xms.app.entity.vo.AirdropClaimPageInfoVo;
import com.xms.app.entity.vo.AirdropClaimRecordVo;
import com.xms.app.entity.vo.NodePlanVo;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.config.redis.delayqueue.RedissonDelayHandler;
import com.xms.common.config.redis.delayqueue.RedissonDelayOrder;
import com.xms.common.constant.*;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.app.service.BizCardService;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.mq.dynamic.OrderMsgDO;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.Func;
import com.xms.common.utils.SecurityUtils;
import com.xms.common.utils.SignUtil;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.*;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.UserMoney;
import com.xms.dao.entity.domain.UserMoneyLog;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.entity.vo.ParentUserTaskVo;
import com.xms.dao.service.*;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 卡片类型业务实现类
 * @author xms
 * @date 2023/04/05
 */
@Service
@Slf4j
public class BizCardServiceImpl implements BizCardService {



	@Autowired
	private ICardPackageService cardPackageService;

	@Autowired
	private ICardUpgradeLogService cardUpgradeLogService;

	@Autowired
	private ICardOrderService cardOrderService;

	@Autowired
	private ICardMasterOrderService cardMasterOrderService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private IUserMoneyService userMoneyService;

	@Autowired
	private UserWalletService userWalletServiceImpl;

	@Autowired
	private IRewardRecordService rewardRecordService;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private IUserLevelConfigService userLevelConfigService;

	@Autowired
	private IUserCardAssetService userCardAssetService;

	@Autowired
	private AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementServiceImpl;

	@Autowired
	private ICardUpgradeLogService cardUpgradeLogServiceImpl;

	@Autowired
	private IActiveOrderService activeOrderService;

	@Autowired
	private IAirdropRoundService airdropRoundService;

	@Autowired
	private IAirdropClaimService airdropClaimService;

	@Autowired
	private RedissonDelayHandler redissonDelayHandler;

	@Autowired
	private XmsRedis xmsRedis;

	@Value("${lq.md5Key}")
	private String md5Key;

	@Value("${lq.tokenName}")
	private String tokenName;

	@Value("${lq.baseUrl}")
	private String baseUrl;

	@Autowired
	private INodePlanService nodePlanService;

	@Autowired
	private INodePlanOrderService nodePlanOrderService;

	@Autowired
	private IStakeRoundService stakeRoundService;

	@Autowired
	private IUserYieldRateConfigService userYieldRateConfigService;

	@Autowired
	private IUserStakePositionService userStakePositionService;

	@Autowired
	private WithdrawalService withdrawalService;

	@Override
	public ResultPista<MyStakeIncomeResp> myStakeIncomeInfo() {
		MyStakeIncomeResp resp = new MyStakeIncomeResp();
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		UserYieldRateConfig rateConfig = userYieldRateConfigService.lambdaQuery()
			.last("limit 1")
			.one();

		resp.setGcd(rateConfig.getGrowthConsecutiveDays());
		//目前未提现天数
		//找到昨日静态收益
		BigDecimal todayStaticReward = rewardRecordService.lambdaQuery()
			.eq(RewardRecord::getSourceType, ConstantType.xms_reward_record_source_type.type_2)
			.eq(RewardRecord::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.apply("create_time >= CURDATE()")
			.select(RewardRecord::getAmount)
			.list().stream().map(RewardRecord::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		resp.setTsr(todayStaticReward);
		if(stakeRound == null){
			//参与是0.
			//没有参与日利率也是0
		}else{
			//查询是否有质押过
			UserStakePosition userStakePosition = userStakePositionService.lambdaQuery()
				.eq(UserStakePosition::getUserId, SecurityUtils.getLoginAppUser().getUserId())
				.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
				.one();
			if(userStakePosition == null){
				//没有质押过
			}else{
				//质押了
				resp.setCnw(userStakePosition.getContinuousNoWithdrawDays());
				resp.setTsa(userStakePosition.getTotalStakeAmount());
				resp.setDrt(userStakePosition.getCurrentDayRate());
			}
		}

		//当前提现轮次
		if(stakeRound !=null){
			BigDecimal totalWithdrawValidNum2 = withdrawalService.lambdaQuery()
				.eq(Withdrawal::getUserId,SecurityUtils.getLoginAppUser().getUserId())
				.eq(Withdrawal::getStakeRoundId,stakeRound.getId())
				.eq(Withdrawal::getCoinType,2)
				.eq(Withdrawal::getStatus,3)
				.select(Withdrawal::getChangeBalance)
				.list().stream().map(Withdrawal::getChangeBalance)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			resp.setTw2(totalWithdrawValidNum2);
		}
		return ResultPista.data(resp);
	}

	@Override
	public ResultPista<MyStakeInfoResp> myStakeInfo() {
		MyStakeInfoResp myStakeInfoResp = new MyStakeInfoResp();
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		if(stakeRound == null){
			return ResultPista.data(myStakeInfoResp);
		}
		//查询是否质押过
		UserStakePosition userStakePosition = userStakePositionService.lambdaQuery()
			.eq(UserStakePosition::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.eq(UserStakePosition::getStakeRoundId, stakeRound.getId())
			.one();
		if(userStakePosition == null){
			//没质押过
			UserYieldRateConfig rateConfig = userYieldRateConfigService.lambdaQuery()
				.last("limit 1")
				.one();
			myStakeInfoResp.setDrt(rateConfig.getInitialDailyRate());
		}else{
			//质押过
			myStakeInfoResp.setDrt(userStakePosition.getCurrentDayRate());
			myStakeInfoResp.setTsa(userStakePosition.getTotalStakeAmount());
		}
		return ResultPista.data(myStakeInfoResp);
	}

	/**
	 * 领取记录
	 * @param lastId
	 * @return
	 */
	@Override
	public ResultPista<List<AirdropClaimRecordVo>> claimRecordList(Long lastId) {
		List<AirdropClaimRecordVo> result = airdropClaimService.lambdaQuery()
			.eq(AirdropClaim::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.lt(Func.isNotEmpty(lastId), AirdropClaim::getId, lastId)
			.orderByDesc(AirdropClaim::getId)
			.last(SysConstant.PAGE_LIMIT)
			.select(
				AirdropClaim::getId,
				AirdropClaim::getClaimNo,
				AirdropClaim::getStatus,
				AirdropClaim::getTokenAmount,
				AirdropClaim::getTxHash,
				AirdropClaim::getPayAmount,
				AirdropClaim::getCreateTime,
				AirdropClaim::getCompletedAt
			)
			.list()
			.stream()
			.map(item -> {
				AirdropClaimRecordVo vo = new AirdropClaimRecordVo();
				vo.setId(item.getId());
				vo.setClaimNo(item.getClaimNo());
				vo.setStatus(item.getStatus());
				vo.setTokenAmount(item.getTokenAmount());
				vo.setTxHash(item.getTxHash());
				vo.setPayAmount(item.getPayAmount());
				vo.setCreateTime(item.getCreateTime());
				vo.setCompletedAt(item.getCompletedAt());
				return vo;
			}).collect(Collectors.toList());
		return ResultPista.data(result);
	}

	/**
	 * 领取空投回调
	 * @param req
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<String> claimAirdropCallback(DestroyCallbackBo req) {
		//判断是否过期了.过期了加额外备注
		return ResultPista.data("success");
	}

	/**
	 * 领取空投
	 * @param req
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResultPista<ClaimAirdropResp> claimAirdrop(ClaimAirdropReq req) {
		return ResultPista.data(new ClaimAirdropResp());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<String> activeOrderCallback(DestroyCallbackBo req) {
		return ResultPista.data("success");
	}

	/**
	 * 创建激活订单(只有未激活的用户才能创建)
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<CreateActiveOrderResp> createActiveOrder() {
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.one();
		Integer sendCount = Integer.valueOf(sysParaServiceImpl.getValue(ConstantSys.biz_send_activation_count));
		if(sendCount<=0){
			throw new ServiceException(ResponseCode.CODE_1002);
		}

		BigDecimal cost = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_activation_cost));
		if(cost.compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException(ResponseCode.CODE_1002);
		}
		//如果有未支付的返回
		Long count = activeOrderService.lambdaQuery()
			.eq(ActiveOrder::getUserId, userInfo.getUserId())
			.eq(ActiveOrder::getBizStatus, 0)
			.count();
		if(count>0){
			throw new ServiceException(ResponseCode.CODE_1244);
		}
		String masterOrderNo = IDUtils.getSnowflakeStr();
		ActiveOrder activeOrder = new ActiveOrder();
		activeOrder.setOrderNo(masterOrderNo);
		activeOrder.setUserId(userInfo.getUserId());
		activeOrder.setAmount(cost);
		activeOrder.setActivationCount(sendCount);
		Integer offset =Integer.valueOf(sysParaServiceImpl.getValue(ConstantSys.biz_lock_expire_at));
		activeOrder.setLockExpireAt(DateUtil.offsetMinute(new Date(), offset));
		activeOrder.setBizStatus(0);
		activeOrder.setCreateTime(new Date());
		boolean save = activeOrderService.save(activeOrder);
		if(!save){
			throw new ServiceException(ResponseCode.CODE_1002);
		}
		CreateActiveOrderResp resp = new CreateActiveOrderResp();
		resp.setOrderNo(masterOrderNo);
		resp.setPayAmount(cost);

		//发送到mq
		//发送到延迟队列里面去过一会消费(判断是否过期了)
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				Long targetTime = BizMiningServiceImpl.getEndTime(activeOrder.getCreateTime(), activeOrder.getLockExpireAt());
				//发送消息到延迟队列
				redissonDelayHandler.add(new RedissonDelayOrder(activeOrder.getOrderNo(), targetTime, SysConstant.FOUR,
					null, RedisConstant.StreamMsgConstant.DELAY_ORDER_TIMEOUT_QUEUE));
			}
		});
		return ResultPista.data(resp);
	}

	@Override
	public ResultPista<List<RewardRecord>> powerLog(Long lastId) {
		List<RewardRecord> list = rewardRecordService.lambdaQuery()
			.eq(RewardRecord::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.eq(RewardRecord::getCoinType, 1)
			.lt(Func.isNotEmpty(lastId), RewardRecord::getId, lastId)
			.orderByDesc(RewardRecord::getId).last(SysConstant.PAGE_LIMIT)
			.list();
		return ResultPista.data(list);
	}

	/**
	 * 获取卡片升级日志
	 * @param cardType 1:普通卡,2:白银卡,3白金卡,4:黑金卡
	 */
	public List<CardUpgradeLogDto>  getCardLog(Integer cardType, Long lastId){
		List<CardUpgradeLogDto> upgradeLogDtoList = cardUpgradeLogServiceImpl.lambdaQuery()
			.eq(CardUpgradeLog::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.and(wrapper -> wrapper.eq(CardUpgradeLog::getFromCardType, cardType)
				.or()
				.eq(CardUpgradeLog::getToCardType, cardType))
			.lt(Func.isNotEmpty(lastId), CardUpgradeLog::getId, lastId)
			.orderByDesc(CardUpgradeLog::getId).last(SysConstant.PAGE_LIMIT_10)
			.list().stream().map(record -> {
				CardUpgradeLogDto dto = new CardUpgradeLogDto();
				dto.setId(record.getId());
				dto.setCardType(cardType);
				if(record.getFlowType().equals(1)){
					dto.setFlowType(1);
					dto.setAmountDelta(record.getAmountDelta());
					dto.setComputingPower(record.getToPower());
					BigDecimal subtract = record.getPowerDelta().subtract(record.getToPower());
					subtract = subtract.compareTo(BigDecimal.ZERO)>0? subtract:BigDecimal.ZERO;
					dto.setExtraComputingPower(subtract);
				}else{
					if(cardType.equals(record.getFromCardType())){
						//升级扣卡
						dto.setBizType(1);
						dto.setAmountDelta(record.getFromPower());
					}else{
						//升级加卡
						dto.setBizType(2);
						dto.setAmountDelta(record.getAmountDelta());
						//计算新增算力
						//找到之前的
						CardOrder one = cardOrderService.lambdaQuery()
							.eq(CardOrder::getCardInstanceId, record.getCardSerialNo())
							.eq(CardOrder::getCardType, record.getToCardType())
							.one();
						dto.setToPower(one.getComputingPower());
						dto.setToExtraComputingPower(one.getExtraComputingPower());
					}
					dto.setFlowType(2);
				}
//				dto.setComputingPower(record.getComputingPower());
//				dto.setExtraComputingPower(record.getExtraComputingPower());
//				dto.setFlowType(record.getSourceType());
				dto.setCreateTime(record.getCreateTime());
				return dto;
			}).collect(Collectors.toList());

		return upgradeLogDtoList;
	}
	/**
	 * 卡片升级入口：负责同步环节（扣款、资产、订单、日志），耗时的团队业绩/奖励交由 MQ 异步处理
	 * @param req 升级请求
	 * @return 升级结果
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista upgradeCardOrder(UpgradeCardOrderReq req) {
		return ResultPista.success();
	}




	/**
	 * 创建卡片订单（同步落地扣款/订单/资产，团队奖励走异步 MQ）
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista createCardOrder(CreateCardOrderReq req) {

		return ResultPista.success();
	}

	/**
	 * 卡片列表
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPista<List<CardPackageDto>> cardList() {
		List<CardPackageDto> result = cardPackageService.lambdaQuery()
			.eq(CardPackage::getStatus,1)
			.select(CardPackage::getId, CardPackage::getPrice, CardPackage::getImage,
				CardPackage::getValidNum3GiftRatio,
				CardPackage::getComputingPower, CardPackage::getCardType,CardPackage::getSort)
			.orderByAsc(CardPackage::getSort)
			.list()
			.stream().map(record -> {
				CardPackageDto cardPackageDto = new CardPackageDto();
				cardPackageDto.setId(record.getId());
				cardPackageDto.setValidNum3GiftRatio(record.getValidNum3GiftRatio());
				cardPackageDto.setPrice(record.getPrice());
				cardPackageDto.setImage(record.getImage());
				cardPackageDto.setComputingPower(record.getComputingPower());
				cardPackageDto.setCardType(record.getCardType());
				return cardPackageDto;
			}).collect(Collectors.toList());
		return ResultPista.data(result);
	}


}
