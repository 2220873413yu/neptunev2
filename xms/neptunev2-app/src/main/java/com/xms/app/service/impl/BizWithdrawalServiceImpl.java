package com.xms.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.github.pagehelper.PageInfo;
import com.uduncloud.sdk.domain.ResultMsg;
import com.xms.app.entity.bo.WithdrawalCallbackBo;
import com.xms.app.entity.dto.ReleaseBucketListDto;
import com.xms.app.entity.req.JuNotifyReq;
import com.xms.app.entity.resp.*;
import com.xms.app.entity.vo.UserBankInfoVo;
import com.xms.app.entity.vo.UserBankVo;
import com.xms.app.entity.vo.WithdrawalVo;
import com.xms.app.handler.CustomException;
import com.xms.app.service.BizWithdrawalService;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.config.redis.delayqueue.RedissonDelayOrder;
import com.xms.common.core.domain.AjaxResult;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.common.mq.dynamic.OrderMsgDO;
import com.xms.common.utils.*;
import com.xms.common.utils.googleUtil.GoogleAuthenticator;
import com.xms.common.utils.spring.SpringUtils;
import com.xms.dao.domain.*;
import com.xms.dao.entity.bo.WithdrawalBo;
import com.xms.dao.service.WithdrawalService;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.*;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.UserMoney;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.service.*;
import com.xms.dao.service.impl.WithdrawalServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.xms.app.service.impl.BizUserServiceImpl.checkWallet;


@Service
@Slf4j
public class BizWithdrawalServiceImpl implements BizWithdrawalService {
	@Autowired
	private WithdrawalService withdrawalService;

	@Autowired
	private IUserMoneyService userMoneyServiceImpl;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private UserWalletService userWalletServiceImpl;

	@Autowired
	private WalletUtil walletUtil;

	@Autowired
	private IStakeRoundService stakeRoundService;

	@Autowired
	private IUserStakePositionService iUserStakePositionService;

	@Autowired
	private IUserYieldRateConfigService userYieldRateConfigService;

	@Autowired
	private AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementServiceImpl;

	@Autowired
	private XmsRedis xmsRedis;

	@Autowired
	private ISwapOrderService swapOrderService;

	@Autowired
	private IWithdrawalConfigService withdrawalConfigService;

	@Autowired
	private IUserWealthVaultService userWealthVaultService;

	@Autowired
	private IUserWealthVaultFlowService userWealthVaultFlowService;

	@Value("${lq.md5Key}")
	private String md5Key;

	@Autowired
	private IHGiftReleaseBucketService hGiftReleaseBucketService;

	@Override
	public GiftReleaseBucketDto giftReleaseBucket() {
		GiftReleaseBucketDto result = new GiftReleaseBucketDto();
		List<HGiftReleaseBucket> list = hGiftReleaseBucketService.lambdaQuery()
			.eq(HGiftReleaseBucket::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.eq(HGiftReleaseBucket::getStatus, 1)
			.select(HGiftReleaseBucket::getReleasedAmount, HGiftReleaseBucket::getTotalAmount)
			.list();
		if (CollectionUtil.isNotEmpty(list)) {
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal releasedAmount = BigDecimal.ZERO;
			for (HGiftReleaseBucket bucket : list) {
				BigDecimal bucketLockedAmount = bucket.getTotalAmount().subtract(bucket.getReleasedAmount());
				if (bucketLockedAmount.compareTo(BigDecimal.ZERO) < 0) {
					bucketLockedAmount = BigDecimal.ZERO;
				}
				totalAmount = totalAmount.add(bucket.getTotalAmount());
				releasedAmount = releasedAmount.add(bucket.getReleasedAmount());
			}
			result.setTotalAmount(totalAmount);
			result.setReleasedAmount(releasedAmount);
		}
		return result;
	}

	@Override
	public List<WithdrawalConfigResp> withdrawalConfig() {
		List<WithdrawalConfigResp> result = withdrawalConfigService.lambdaQuery()
			.list()
			.stream().map(record -> {
				WithdrawalConfigResp withdrawalConfigResp = new WithdrawalConfigResp();
				withdrawalConfigResp.setId(record.getId());
				withdrawalConfigResp.setCtp(record.getCoinType());
				withdrawalConfigResp.setWdo(record.getWithdrawOpen());
				withdrawalConfigResp.setMwa(record.getMinWithdrawAmount());
				withdrawalConfigResp.setFer(record.getFeeRatio());
				withdrawalConfigResp.setWvr(record.getWealthVaultRatio());
				withdrawalConfigResp.setIvr(record.getInsuranceVaultRatio());
				return withdrawalConfigResp;
			}).collect(Collectors.toList());
		return result;
	}

	@Override
	public void updateJuStatus(JuNotifyReq req) {
		//查询提现单号是否存在
		Withdrawal withdrawal = withdrawalService.lambdaQuery().eq(Withdrawal::getCode, req.getOrderId()).one();
		if (withdrawal == null) {
			log.warn("提现单号不存在：{}", req.getOrderId());
			return;
		}
		if (withdrawal.getStatus() != 1) {
			log.warn("提现单号已处理完成：{} status {}", req.getOrderId(), req.getStatus());
			return;
		}
		if (req.getStatus() == 1) {
			boolean update = withdrawalService.lambdaUpdate()
				.set(Withdrawal::getStatus, 3)
				.set(Withdrawal::getUpdateTime, new Date())
				.set(Withdrawal::getCreditedTime, new Date())
				.eq(Withdrawal::getId, withdrawal.getId())
				.eq(Withdrawal::getStatus, 1).update();
			if (!update) {
				throw new ServiceException(ResponseCode.CODE_1003);
			}
		} else if (req.getStatus() == 2) {
			finalWithdrawFail(withdrawal);
		}

	}

	private void finalWithdrawFail(Withdrawal withdraw) {
		withdrawalFail(withdraw);
		boolean update = withdrawalService.lambdaUpdate()
			.set(Withdrawal::getStatus, 4)
			.set(Withdrawal::getUpdateTime, new Date())
			.eq(Withdrawal::getId, withdraw.getId())
			.eq(Withdrawal::getStatus, 1).update();
		if (!update) {
			throw new ServiceException(ResponseCode.CODE_1003);
		}
	}

	/**
	 * @return void
	 * @Title: withdrawalFail
	 * @param:
	 * @Description: 提现失败，返还资金
	 */
	public void withdrawalFail(Withdrawal withdrawal) {
		int i = userWalletServiceImpl.handerUserMoney(withdrawal.getChangeBalance(), withdrawal.getCode(),
			withdrawal.getUserId(), withdrawal.getUserId()
			, ConstantType.user_money_log_source_type.type_26, withdrawal.getCoinType());
		if (i != 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new ServiceException("回退失败");
		}

		//退还手续费
		i = userWalletServiceImpl.handerUserMoney(withdrawal.getFeeBalance(), withdrawal.getCode(), withdrawal.getUserId(), withdrawal.getUserId()
			, ConstantType.user_money_log_source_type.type_32, ConstantType.user_money_coin_type.type_3);
		if (i != 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new ServiceException("FEE 回退失败");
		}
	}

	/**
	 * 保存收款信息
	 *
	 * @param req
	 */
	@Override
	public void bindUserBank(UserBankVo req) {

	}

	/**
	 * 解绑收款信息
	 */
	@Override
	public void unBindUserBank(Long id) {
	}


	/**
	 * 查询收款信息
	 *
	 * @return
	 */
	@Override
	public UserBankInfoVo getUserBank(Integer type) {
		return null;
	}


	/**
	 * 提现
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisLock(value = RedisConstant.LockConstant.XMS_WITHDRAW_APPLY, param = "#userId")
	public int addWithdrawal(WithdrawalVo req, Long userId) {

		req.setCgb(req.getCgb().setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		//提现开关
//		String withdrawalOPenOrClose = sysParaServiceImpl.getValue(ConstantSys.biz_withdrawal_open_or_close);
//		if ("1".equalsIgnoreCase(withdrawalOPenOrClose)) {
//			throw new ServiceException(ResponseCode.CODE_1108);
//		}
		//查询提现信息
		WithdrawalConfig withdrawalConfig = withdrawalConfigService.lambdaQuery()
			.eq(WithdrawalConfig::getCoinType, req.getCtp())
			.one();
		if (withdrawalConfig == null) {
			throw new ServiceException(ResponseCode.CODE_1003);
		}

		if (withdrawalConfig.getWithdrawOpen().equals(SysConstant.TWO)) {
			throw new ServiceException(ResponseCode.CODE_1108);
		}

		//查询用户
		UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getUserId, userId).one();

		//用户维度提现开关
		if (userInfo.getWithdrawalOpenOrClose().equals(SysConstant.ONE)) {
			throw new ServiceException(ResponseCode.CODE_1108);
		}

		//质押轮次id
		StakeRound stakeRound = stakeRoundService.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.one();
		if (stakeRound == null) {
			throw new ServiceException(ResponseCode.CODE_1254);
		}

		//提现额度最小判断
		if (req.getCgb().compareTo(withdrawalConfig.getMinWithdrawAmount()) < 0) {
			throw new ServiceException(String.format(ResponseCode.CODE_1109.getMsg(),
				withdrawalConfig.getMinWithdrawAmount().stripTrailingZeros().toPlainString()), ResponseCode.CODE_1109.getCode());
		}

		//验签，随机数
		checkWallet(req.getRdn(), req.getSig(), userInfo.getAccount(), xmsRedis);

		UserMoney userMoney = userMoneyServiceImpl.lambdaQuery()
			.eq(UserMoney::getId, userId)
			.one();
		if (req.getCtp().equals(ConstantType.user_money_coin_type.type_1)) {
			if (userMoney.getValidNum1().compareTo(req.getCgb()) < 0) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		} else if (req.getCtp().equals(ConstantType.user_money_coin_type.type_2)) {
			if (userMoney.getValidNum2().compareTo(req.getCgb()) < 0) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		} else if (req.getCtp().equals(ConstantType.user_money_coin_type.type_3)) {
			if (userMoney.getValidNum3().compareTo(req.getCgb()) < 0) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		} else if (req.getCtp().equals(ConstantType.user_money_coin_type.type_4)) {
			if (userMoney.getValidNum4().compareTo(req.getCgb()) < 0) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		} else if (req.getCtp().equals(ConstantType.user_money_coin_type.type_5)) {
			if (userMoney.getValidNum5().compareTo(req.getCgb()) < 0) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		}  else if (req.getCtp().equals(ConstantType.user_money_coin_type.type_6)) {
			if (userMoney.getValidNum6().compareTo(req.getCgb()) < 0) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		} else if (req.getCtp().equals(ConstantType.user_money_coin_type.type_9)) {
			if (userMoney.getValidNum9().compareTo(req.getCgb()) < 0) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		}else {
			throw new ServiceException(ResponseCode.CODE_1002);
		}


		BigDecimal ratio = withdrawalConfig.getFeeRatio().add(withdrawalConfig.getWealthVaultRatio())
			.add(withdrawalConfig.getInsuranceVaultRatio());
		ratio = ratio
			.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);

		//手续费
		BigDecimal fee = req.getCgb().multiply(ratio)
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		if (fee.compareTo(BigDecimal.ZERO) < 0) {
			//手续费设置有误
			fee = BigDecimal.ZERO;
		}

		//手续费大于提现额度
		if (fee.compareTo(req.getCgb()) >= 0) {
			throw new ServiceException(ResponseCode.CODE_1070);
		}

		//订单号
		String code = IDUtils.getSnowflakeStr();
		//扣款提现额度
		int count = userWalletServiceImpl.handerUserMoney(req.getCgb().negate(), code, userId, userId,
			ConstantType.user_money_log_source_type.type_19,
			req.getCtp());
		if (count != 1) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}

		Integer status = ConstantType.withdrawal_status.type_0;

		//提现免审核配置
		BigDecimal auditAmount = withdrawalConfig.getWithdrawLimit();
		Integer auditCount = withdrawalConfig.getDailyFreeAuditCount();
		if(req.getCgb().compareTo(auditAmount)<=0) {
			status = ConstantType.withdrawal_status.type_1;
			//查询累计提现多少钱
			List<Withdrawal> withdrawalList = withdrawalService.lambdaQuery()
				.eq(Withdrawal::getUserId, userId)
				.eq(Withdrawal::getCoinType, req.getCtp())
				.apply("create_time >= CURDATE()")
				.select(Withdrawal::getChangeBalance)
				.list();
			if (CollectionUtil.isNotEmpty(withdrawalList)) {
				BigDecimal totalWithdrawal = BigDecimal.ZERO;
				for (Withdrawal withdrawal : withdrawalList) {
					totalWithdrawal = totalWithdrawal.add(withdrawal.getChangeBalance());
				}

				if (totalWithdrawal.add(req.getCgb()).compareTo(auditAmount) > 0) {
					//超过了单日提现总额
					status = ConstantType.withdrawal_status.type_0;
				}

				if (withdrawalList.size() >= auditCount) {
					//判断提现次数超过了当日
					status = ConstantType.withdrawal_status.type_0;
				}
			}

			if(auditCount<=0){
				status = ConstantType.withdrawal_status.type_0;
			}
		}

		//新增提现记录
		Withdrawal withdrawal = Withdrawal.builder().userId(userId).code(code)
			.changeBalance(req.getCgb())
			.chainId(0)
			.feeBalance(fee)
			.feeRatio(withdrawalConfig.getFeeRatio())
			.wealthVaultRatio(withdrawalConfig.getWealthVaultRatio())
			.insuranceVaultRatio(withdrawalConfig.getInsuranceVaultRatio())
			.status(status)
			.coinType(req.getCtp())
			.stakeRoundId(stakeRound.getId())
			.accountNo(userInfo.getAccount())
			.build();
		boolean save = withdrawalService.save(withdrawal);
		if (!save) {
			throw new ServiceException(ResponseCode.CODE_1002);
		}

		//如果status=1就直接发送提现请求
		if(withdrawal.getStatus().equals(ConstantType.withdrawal_status.type_1)){
			//发送到合约(内扣)
			String tokenName = "H";
			if(!req.getCtp().equals(ConstantType.user_money_coin_type.type_9)){
				tokenName = "ACP";
			}

			Map<String, Object> formParams = new HashMap<>();
			formParams.put("orderNo", withdrawal.getCode());
			formParams.put("address", withdrawal.getAccountNo());
			formParams.put("tokenName", tokenName);
			BigDecimal finalAmount = withdrawal.getChangeBalance().subtract(withdrawal.getFeeBalance());
			formParams.put("amount", finalAmount.stripTrailingZeros().toPlainString());
			String sign = SignUtil.getSign(formParams, false, false, md5Key);
			log.info("提现业务完整参数 param:{},sign:{}", formParams,sign);
			SpringUtils.getBean(WithdrawalServiceImpl.class)
				.sendWithdrawalRequest(formParams,sign);
		}
		return 1;
	}


	/**
	 * 提现记录
	 *
	 * @param pageIndex 当前页 默认1
	 * @param pageSize  每页长度 默认20(最大20)
	 * @return
	 */
	@Override
	public PageInfo<WithdrawalBo> listWithdrawRecord(Integer pageIndex, Integer pageSize, Long userId) {
		return withdrawalService.listWithdrawRecord(pageIndex, pageSize, userId);
	}

	/**
	 * 提现回调
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResultPista<String> withdrawalCallback(WithdrawalCallbackBo req) {

		log.info("提现回调 req:{}", req);
		if (req.getSuccess() == null) {
			throw new ServiceException("验签失败");
		}

		if (!StrUtil.isBlank(req.getHash())) {
			if (req.getHash().length() > 255) {
				throw new ServiceException("hash长度不能超过255");
			}
		}

		// 将 RechargeCallbackBo 对象转换为 Map
		Map<String, Object> map = BeanUtil.beanToMap(req);
		String sign = SignUtil.getSign(map, false, false, md5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}

		Withdrawal withdrawal = withdrawalService.lambdaQuery()
			.eq(Withdrawal::getCode, req.getOrderNo())
			.one();
		if (withdrawal == null || !withdrawal.getStatus().equals(ConstantType.withdrawal_status.type_1)) {
			throw new ServiceException("提现订单已经被处理了");
		}

		Integer status = req.getSuccess().equals(false) ? 4 : 3;
		boolean update = withdrawalService.lambdaUpdate()
			.eq(Withdrawal::getId, withdrawal.getId())
			.eq(Withdrawal::getStatus, ConstantType.withdrawal_status.type_1)
			.set(Withdrawal::getStatus, status)
			.set(Withdrawal::getUpdateTime, new Date())
			.set(Withdrawal::getRemark, req.getHash())
			//审核成功
			.set(req.getSuccess() == true, Withdrawal::getCreditedTime, new Date())
			.set(req.getSuccess() == true, Withdrawal::getChainId, Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")))
			.update();
		if (!update) {
			throw new ServiceException("提现订单已经被处理了");
		}

		if (req.getSuccess().equals(false)) {
			//退还提现金额
			int i = userWalletServiceImpl.handerUserMoney(withdrawal.getChangeBalance(), withdrawal.getCode(), withdrawal.getUserId(), withdrawal.getUserId()
				, ConstantType.user_money_log_source_type.type_20, withdrawal.getCoinType());
			if (i != 1) {
				throw new ServiceException(ResponseCode.CODE_1002);
			}

		} else {
			//提现天数变为0
			UserStakePosition userStakePosition = iUserStakePositionService.lambdaQuery()
				.eq(UserStakePosition::getUserId, withdrawal.getUserId())
				.eq(UserStakePosition::getStakeRoundId, withdrawal.getStakeRoundId())
				.one();
			//只算提现静态
			if (userStakePosition != null && withdrawal.getCoinType() ==2) {
				iUserStakePositionService.lambdaUpdate()
					.eq(UserStakePosition::getId, userStakePosition.getId())
					.set(UserStakePosition::getContinuousNoWithdrawDays, 0)
					.update();
				//扣减收益率
				UserYieldRateConfig rateConfig = userYieldRateConfigService.lambdaQuery()
					.last("limit 1")
					.one();
				BigDecimal totalWithdrawAmount = withdrawalService.lambdaQuery()
					.eq(Withdrawal::getUserId,withdrawal.getUserId())
					.eq(Withdrawal::getStakeRoundId,userStakePosition.getStakeRoundId())
					.eq(Withdrawal::getCoinType,2)
					.eq(Withdrawal::getStatus,3)
					.select(Withdrawal::getChangeBalance)
					.list().stream()
					.map(item -> item.getChangeBalance() == null ? BigDecimal.ZERO : item.getChangeBalance())
					.reduce(BigDecimal.ZERO, BigDecimal::add);

				// 按当前累计提现金额和当前总质押量计算命中的10%档位数：
				// 超过/达到10%减0.1%，超过/达到20%减0.2%
				BigDecimal totalStakeAmount = userStakePosition.getTotalStakeAmount();
				if (totalStakeAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal stepAmount = totalStakeAmount.multiply(new BigDecimal("0.1"))
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					BigDecimal deductedWithdrawAmount = userStakePosition.getRateDeductedWithdrawAmount() == null
						? BigDecimal.ZERO : userStakePosition.getRateDeductedWithdrawAmount();
					BigDecimal remainingWithdrawAmount = totalWithdrawAmount.subtract(deductedWithdrawAmount);
					int deltaStepCount = getWithdrawalStepCount(remainingWithdrawAmount, stepAmount);
					if (deltaStepCount > 0) {
						BigDecimal newDayRate = userStakePosition.getCurrentDayRate()
							.subtract(rateConfig.getDecayRateStep().multiply(BigDecimal.valueOf(deltaStepCount))
								.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
						if (newDayRate.compareTo(rateConfig.getMinDailyRate()) < 0) {
							newDayRate = rateConfig.getMinDailyRate();
						}
						BigDecimal newDeductedWithdrawAmount =stepAmount.multiply(BigDecimal.valueOf(deltaStepCount))
								.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						iUserStakePositionService.lambdaUpdate()
							.eq(UserStakePosition::getId, userStakePosition.getId())
							.set(UserStakePosition::getCurrentDayRate, newDayRate)
							.setSql("rate_deducted_withdraw_amount = rate_deducted_withdraw_amount + " +newDeductedWithdrawAmount)
							.set(UserStakePosition::getUpdateTime, new Date())
							.update();
					}
				}
			}

			//判断提现的额度是否超过30%
			List<Withdrawal> withdrawalList = withdrawalService.lambdaQuery()
				.eq(Withdrawal::getUserId, withdrawal.getUserId())
				.eq(Withdrawal::getStakeRoundId, withdrawal.getStakeRoundId())
				.eq(Withdrawal::getStatus, 3)
				.in(Withdrawal::getCoinType, 2, 3)
				.orderByAsc(Withdrawal::getId)
				.list();
			if (CollectionUtil.isNotEmpty(withdrawalList)) {
				if (userStakePosition != null && userStakePosition.getInsuranceQualifyStatus() == 1) {
					//总共提现金额(静态+动态)
					BigDecimal totalWithdrawal = withdrawalList.stream().map(Withdrawal::getChangeBalance)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
					// 条件B：累计总提现是否超过累计总参与量的30%
					BigDecimal limit30Percent = userStakePosition.getTotalStakeAmount().multiply(new BigDecimal("0.3"))
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (totalWithdrawal.compareTo(limit30Percent) >= 0) {
						iUserStakePositionService.lambdaUpdate()
							.eq(UserStakePosition::getId, userStakePosition.getId())
							.set(UserStakePosition::getInsuranceQualifyStatus, 0)
							.update();
					} else {
						// 条件A：爆仓前每一个30天，静态收益提现额均未超过当月参与量的10%
						StakeRound stakeRound = stakeRoundService.getById(withdrawal.getStakeRoundId());
						if (stakeRound != null && stakeRound.getCreateTime() != null) {
							List<Withdrawal> staticWithdrawals = withdrawalList.stream()
								.filter(w -> w.getCoinType() != null && w.getCoinType() == 2)
								.collect(Collectors.toList());
							if (CollectionUtil.isNotEmpty(staticWithdrawals)) {
								long roundStartMs = stakeRound.getCreateTime().getTime();
								long thirtyDaysMs = 30L * 24 * 60 * 60 * 1000;
								Map<Long, BigDecimal> periodSumMap = new HashMap<>();
								BigDecimal monthLimit10Percent = userStakePosition.getTotalStakeAmount().multiply(new BigDecimal("0.1"))
									.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
								for (Withdrawal w : staticWithdrawals) {
									Date withdrawTime = w.getCreditedTime() != null ? w.getCreditedTime() : w.getCreateTime();
									if (withdrawTime == null) continue;
									long periodIndex = (withdrawTime.getTime() - roundStartMs) / thirtyDaysMs;
									if (periodIndex < 0) periodIndex = 0;
									periodSumMap.merge(periodIndex, w.getChangeBalance() != null ? w.getChangeBalance() : BigDecimal.ZERO, BigDecimal::add);
								}
								for (BigDecimal periodSum : periodSumMap.values()) {
									if (periodSum.compareTo(monthLimit10Percent) > 0) {
										iUserStakePositionService.lambdaUpdate()
											.eq(UserStakePosition::getId, userStakePosition.getId())
											.set(UserStakePosition::getInsuranceQualifyStatus, 0)
											.update();
										break;
									}
								}
							}
						}
					}
				}
			}

			if(userStakePosition!=null){
				if(withdrawal.getCoinType().equals(2)
					|| withdrawal.getCoinType().equals(3)
					|| withdrawal.getCoinType().equals(6)){
					iUserStakePositionService.lambdaUpdate()
						.eq(UserStakePosition::getId, userStakePosition.getId())
						.setSql(withdrawal.getCoinType() == 2,"total_withdrawal_static = total_withdrawal_static + "+ withdrawal.getChangeBalance())
						.setSql(withdrawal.getCoinType() == 3,"total_withdrawal_dynamic = total_withdrawal_dynamic + "+ withdrawal.getChangeBalance())
						.setSql(withdrawal.getCoinType() == 6,"total_withdrawal_studio_subsidy = total_withdrawal_studio_subsidy + "+ withdrawal.getChangeBalance())
						.update();

					//更新已经提现的工作室收益、或者动静态
					stakeRoundService.lambdaUpdate()
						.eq(StakeRound::getId, withdrawal.getStakeRoundId())
						.setSql(withdrawal.getCoinType().equals(2) || withdrawal.getCoinType().equals(3),"withdraw_reward_total_full = withdraw_reward_total_full + " + withdrawal.getChangeBalance())
						.setSql(withdrawal.getCoinType().equals(6),"studio_subsidy_total = studio_subsidy_total + " + withdrawal.getChangeBalance())
						.update();
				}
			}


			//提现成功,根据币种不同不同的业务
			if (withdrawal.getFeeRatio().compareTo(BigDecimal.ZERO) > 0) {
				//静态 手续费分红
				BigDecimal shareReward = withdrawal.getFeeRatio().multiply(withdrawal.getChangeBalance())
					.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
					.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
				if (shareReward.compareTo(BigDecimal.ZERO) > 0) {
					String shareAddress = sysParaServiceImpl.getValue(ConstantSys.biz_withdrawal_fee_collect_address);
					UserInfo ShareUserInfo = userInfoService.lambdaQuery()
						.eq(UserInfo::getAccount, shareAddress)
						.one();
					//手续费分红地址1 分配85%
					BigDecimal shareAddressReward = shareReward.multiply(new BigDecimal("0.85"))
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (ShareUserInfo != null) {
						int i = userWalletServiceImpl.handerUserMoney(shareAddressReward, withdrawal.getCode(), ShareUserInfo.getUserId(),
							ShareUserInfo.getUserId(), ConstantType.user_money_log_source_type.type_11, ConstantType.user_money_coin_type.type_5);
						if (i != 1) {
							throw new ServiceException(ResponseCode.CODE_1002);
						}
					}


					//手续费分红地址2
					String shareAddress1 = sysParaServiceImpl.getValue(ConstantSys.biz_withdrawal_fee_collect_address1);
					UserInfo ShareUserInfo1 = userInfoService.lambdaQuery()
						.eq(UserInfo::getAccount, shareAddress1)
						.one();
					if (ShareUserInfo1 != null) {
						BigDecimal shareAddress1Reward = shareReward.subtract(shareAddressReward)
							.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
						int i = userWalletServiceImpl.handerUserMoney(shareAddress1Reward, withdrawal.getCode(), ShareUserInfo1.getUserId(),
							ShareUserInfo1.getUserId(), ConstantType.user_money_log_source_type.type_11, ConstantType.user_money_coin_type.type_5);
						if (i != 1) {
							throw new ServiceException(ResponseCode.CODE_1002);
						}
					}
				}
			}

			if (withdrawal.getCoinType().equals(ConstantType.user_money_coin_type.type_2)
				|| withdrawal.getCoinType().equals(ConstantType.user_money_coin_type.type_3)) {
				//保险仓注入金额
				if (withdrawal.getInsuranceVaultRatio().compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal insuranceVaultReward = withdrawal.getInsuranceVaultRatio()
						.multiply(withdrawal.getChangeBalance()).setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
						.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (insuranceVaultReward.compareTo(BigDecimal.ZERO) > 0) {
						boolean update1 = stakeRoundService.lambdaUpdate()
							.eq(StakeRound::getId, withdrawal.getStakeRoundId())
							.setSql("insurance_balance = insurance_balance + " + insuranceVaultReward)
							.update();
						if (!update1) {
							throw new ServiceException(ResponseCode.CODE_1002);
						}

					}
				}


				if (withdrawal.getWealthVaultRatio().compareTo(BigDecimal.ZERO) > 0) {
					//财富仓注入
					BigDecimal wealthVaultReward = withdrawal.getWealthVaultRatio().multiply(withdrawal.getChangeBalance())
						.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
						.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
					if (wealthVaultReward.compareTo(BigDecimal.ZERO) > 0) {
						//保存财富仓奖励
						sendUserWealthVault(wealthVaultReward, withdrawal);
					}
				}
			}


			//1.爆仓检测(只有提现静态+动态收益会检测爆仓+累计提现工作室收益)
			if(withdrawal.getCoinType().equals(2)
			|| withdrawal.getCoinType().equals(3)|| withdrawal.getCoinType().equals(6)){
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
						OrderMsgDO orderMsgDO = new OrderMsgDO();
						orderMsgDO.setId(withdrawal.getStakeRoundId());
						orderMsgDO.setBizType(2);
						orderMsgDOList.add(orderMsgDO);
						asyncDynamicOrderSettlementServiceImpl.sendMessage(orderMsgDOList);

					}
				});
			}


		}

		return ResultPista.data("success");
	}

	private void sendUserWealthVault(BigDecimal wealthVaultReward, Withdrawal withdrawal) {
		ResultPista<BybitMarketServiceImpl.PriceResponse> priceResponseResultPista = SpringUtils.getBean(BybitMarketServiceImpl.class).bybitSpotPrice();
		BigDecimal hPrice = new BigDecimal(priceResponseResultPista.getData().getLp());
		//除以5份
		BigDecimal wealth = wealthVaultReward.divide(new BigDecimal(5), ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		boolean update1 = userWealthVaultService.lambdaUpdate()
			.eq(UserWealthVault::getId, withdrawal.getUserId())
			.setSql("seg1_amount = seg1_amount + " + wealth)
			.setSql("seg2_amount = seg2_amount + " + wealth)
			.setSql("seg3_amount = seg3_amount + " + wealth)
			.setSql("seg4_amount = seg4_amount + " + wealth)
			.setSql("seg5_amount = seg5_amount + " + wealth)
			.update();
		if (!update1) {
			throw new ServiceException(ResponseCode.CODE_1002);
		}
		UserWealthVault one = userWealthVaultService.lambdaQuery()
			.eq(UserWealthVault::getId, withdrawal.getUserId())
			.one();
		//添加5条记录
		List<UserWealthVaultFlow> userWealthVaultFlowList = new ArrayList<>(5);
		UserWealthVaultFlow flowEntity = new UserWealthVaultFlow();
		flowEntity.setUserId(one.getId());
		flowEntity.setSegNo(1);
		flowEntity.setSourceType(ConstantType.user_wealth_vault_flow_source_type.type_1);
		flowEntity.setSourceOrderNo(withdrawal.getCode());
		flowEntity.setChangeAmount(wealth);
		flowEntity.setBeforeAmount(one.getSeg1Amount());
		flowEntity.setAfterAmount(one.getSeg1Amount().add(wealth));
		flowEntity.setTriggerPrice(hPrice);
		userWealthVaultFlowList.add(flowEntity);

		flowEntity = new UserWealthVaultFlow();
		flowEntity.setUserId(one.getId());
		flowEntity.setSegNo(2);
		flowEntity.setSourceType(ConstantType.user_wealth_vault_flow_source_type.type_1);
		flowEntity.setSourceOrderNo(withdrawal.getCode());
		flowEntity.setChangeAmount(wealth);
		flowEntity.setBeforeAmount(one.getSeg2Amount());
		flowEntity.setAfterAmount(one.getSeg2Amount().add(wealth));
		flowEntity.setTriggerPrice(hPrice);
		userWealthVaultFlowList.add(flowEntity);

		flowEntity = new UserWealthVaultFlow();
		flowEntity.setUserId(one.getId());
		flowEntity.setSegNo(3);
		flowEntity.setSourceType(ConstantType.user_wealth_vault_flow_source_type.type_1);
		flowEntity.setSourceOrderNo(withdrawal.getCode());
		flowEntity.setChangeAmount(wealth);
		flowEntity.setBeforeAmount(one.getSeg3Amount());
		flowEntity.setAfterAmount(one.getSeg3Amount().add(wealth));
		flowEntity.setTriggerPrice(hPrice);
		userWealthVaultFlowList.add(flowEntity);

		flowEntity = new UserWealthVaultFlow();
		flowEntity.setUserId(one.getId());
		flowEntity.setSegNo(4);
		flowEntity.setSourceType(ConstantType.user_wealth_vault_flow_source_type.type_1);
		flowEntity.setSourceOrderNo(withdrawal.getCode());
		flowEntity.setChangeAmount(wealth);
		flowEntity.setBeforeAmount(one.getSeg4Amount());
		flowEntity.setAfterAmount(one.getSeg4Amount().add(wealth));
		flowEntity.setTriggerPrice(hPrice);
		userWealthVaultFlowList.add(flowEntity);

		flowEntity = new UserWealthVaultFlow();
		flowEntity.setUserId(one.getId());
		flowEntity.setSegNo(5);
		flowEntity.setSourceType(ConstantType.user_wealth_vault_flow_source_type.type_1);
		flowEntity.setSourceOrderNo(withdrawal.getCode());
		flowEntity.setChangeAmount(wealth);
		flowEntity.setBeforeAmount(one.getSeg5Amount());
		flowEntity.setAfterAmount(one.getSeg5Amount().add(wealth));
		flowEntity.setTriggerPrice(hPrice);
		userWealthVaultFlowList.add(flowEntity);
		userWealthVaultFlowService.saveBatch(userWealthVaultFlowList);
	}

	private int getWithdrawalStepCount(BigDecimal totalWithdrawAmount, BigDecimal stepAmount) {
		if (stepAmount.compareTo(BigDecimal.ZERO) <= 0 || totalWithdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
			return 0;
		}
		return Math.max(totalWithdrawAmount.divideToIntegralValue(stepAmount).intValue(), 0);
	}

	/**
	 * 提现汇总（总提现/今日提现/待处理）
	 */
	@Override
	public WithdrawalSummaryResp withdrawalSummary(Long userId) {
		WithdrawalSummaryResp resp = new WithdrawalSummaryResp();
		BigDecimal totalWithdrawal = withdrawalService.lambdaQuery()
			.eq(Withdrawal::getUserId, userId)
			.eq(Withdrawal::getStatus, ConstantType.withdrawal_status.type_3)
			.select(Withdrawal::getChangeBalance)
			.list().stream()
			.map(Withdrawal::getChangeBalance)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		resp.setTotalWithdrawal(totalWithdrawal);

		BigDecimal todayWithdrawal = withdrawalService.lambdaQuery()
			.eq(Withdrawal::getUserId, userId)
			.eq(Withdrawal::getChainId, Integer.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")))
			.eq(Withdrawal::getStatus, ConstantType.withdrawal_status.type_3)
			.select(Withdrawal::getChangeBalance)
			.list().stream()
			.map(Withdrawal::getChangeBalance)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		resp.setTodayWithdrawal(todayWithdrawal);

		BigDecimal pendingAmount = withdrawalService.lambdaQuery()
			.eq(Withdrawal::getUserId, userId)
			.in(Withdrawal::getStatus, ConstantType.withdrawal_status.type_0, ConstantType.withdrawal_status.type_1)
			.select(Withdrawal::getChangeBalance)
			.list().stream()
			.map(Withdrawal::getChangeBalance)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		resp.setPendingAmount(pendingAmount);
		return resp;
	}
}
