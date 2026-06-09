package com.xms.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.xms.app.entity.PtbPriceDto;
import com.xms.app.entity.dto.RewardRecordDto;
import com.xms.app.entity.dto.TodayIncomeDto;
import com.xms.app.entity.resp.IncomeOverviewResp;
import com.xms.app.entity.vo.UserMoneySwapVo;
import com.xms.app.service.BizUserMoneyService;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.*;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.CollectionUtil;
import com.xms.common.utils.Func;
import com.xms.common.utils.SecurityUtils;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.FlashExchangeRecord;
import com.xms.dao.domain.PtbDailyPrice;
import com.xms.dao.domain.RewardRecord;
import com.xms.dao.domain.RewardStatDay;
import com.xms.dao.entity.bo.RewardRecordBo;
import com.xms.dao.entity.bo.UserMoneyBo;
import com.xms.dao.entity.bo.UserMoneyLogBo;
import com.xms.dao.entity.domain.UserMoney;
import com.xms.dao.entity.domain.UserMoneyLog;
import com.xms.dao.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BizUserMoneyServiceImpl implements BizUserMoneyService {


	@Autowired
	private UserMoneyLogService userMoneyLogService;

	@Autowired
	private IUserMoneyService userMoneyService;

	@Autowired
	private UserWalletService userWalletServiceImpl;

	@Autowired
	private ISysParaService sysParaServiceImpl;

	@Autowired
	private IPtbDailyPriceService ptbDailyPriceServiceImpl;

	@Autowired
	private IFlashExchangeRecordService flashExchangeRecordService;

	@Autowired
	private XmsRedis xmsRedis;

	@Autowired
	private IRewardRecordService rewardRecordService;

	@Autowired
	private IRewardStatDayService rewardStatDayService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BigDecimal transferBalance(Long userId) {
		//查询钱包
		UserMoney userMoney = userMoneyService.lambdaQuery()
			.eq(UserMoney::getId, userId)
			.one();
		if (userMoney == null) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}
		BigDecimal validNum8 = userMoney.getValidNum8() == null ? BigDecimal.ZERO : userMoney.getValidNum8();
		BigDecimal validNum7 = userMoney.getValidNum7() == null ? BigDecimal.ZERO : userMoney.getValidNum7();
		if(validNum8.compareTo(BigDecimal.ZERO)<=0){
			return BigDecimal.ZERO;
		}
		String code = IDUtils.getSnowflakeStr();

		// 先扣除资产8
		int count = userWalletServiceImpl.handerUserMoney(validNum8.negate(),
			code, userId, userId, ConstantType.user_money_log_source_type.type_17,
			ConstantType.user_money_coin_type.type_8);
		if (count != 1) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}

		// 资产7按现有余额扣减，最多扣到资产8这么多
		BigDecimal deductNum7 = validNum8.min(validNum7);
		if(deductNum7.compareTo(BigDecimal.ZERO) > 0){
			count = userWalletServiceImpl.handerUserMoney(deductNum7.negate(),
				code, userId, userId, ConstantType.user_money_log_source_type.type_17,
				ConstantType.user_money_coin_type.type_7);
			if (count != 1) {
				throw new ServiceException(ResponseCode.CODE_1015);
			}
		}

		// 资产8全部划转到资产3
		count = userWalletServiceImpl.handerUserMoney(validNum8,
			code, userId, userId, ConstantType.user_money_log_source_type.type_18,
			ConstantType.user_money_coin_type.type_3);
		if (count != 1) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}
		return validNum8;
	}

	/**
	 * 奖金记录(收益记录)
	 * @param pageIndex 当前页 默认1
	 * @param pageSize 每页长度 默认20(最大20)
	 * @param bizType 业务类型 -1:查询全部,1:静态收益,2:动态收益,3:团队奖励
	 * @param dateType 时间类型-1:查询全部,1:今日,2:本周,3:本月
	 * @return
	 */
	@Override
	public PageInfo<RewardRecordBo> rewardList(Integer pageIndex, Integer pageSize, Long userId, Integer bizType,
											   Integer dateType) {
		return rewardRecordService.rewardList(pageIndex,pageSize,userId,bizType,dateType);
	}

	/**
	 * 收益卡片/类型分布切换
	 * 一次性返回收益卡片 + 类型分布（今日/本周/本月/总）
	 * @return
	 */
	@Override
	public IncomeOverviewResp incomeOverview() {
		Long userId = SecurityUtils.getFrontUserId();
		Date now = DateUtil.date();
		Long today = Long.valueOf(DateUtil.format(now, "yyyyMMdd"));
		Long weekStart = Long.valueOf(DateUtil.format(DateUtil.beginOfWeek(now), "yyyyMMdd"));
		Long monthStart = Long.valueOf(DateUtil.format(DateUtil.beginOfMonth(now), "yyyyMMdd"));
		Long queryStart = weekStart < monthStart ? weekStart : monthStart;

		List<RewardStatDay> statList = rewardStatDayService.lambdaQuery()
			.eq(RewardStatDay::getUserId, userId)
			.ge(RewardStatDay::getStatDate, queryStart)
			.list();

		List<IncomeOverviewResp.IncomeScopeDetail> scopes = new ArrayList<>(4);
		scopes.add(buildScopeDetail(1, filterSum(statList, today, today)));
		scopes.add(buildScopeDetail(2, filterSum(statList, weekStart, today)));
		scopes.add(buildScopeDetail(3, filterSum(statList, monthStart, today)));
		scopes.add(buildScopeDetail(4, aggregateAll(userId)));

		IncomeOverviewResp resp = new IncomeOverviewResp();
		resp.setScopes(scopes);
		return resp;
	}

	private RewardSum filterSum(List<RewardStatDay> statList, Long start, Long end) {
		if (CollectionUtil.isEmpty(statList)) {
			return new RewardSum(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		}
		BigDecimal staticValue = BigDecimal.ZERO;
		BigDecimal dynamicValue = BigDecimal.ZERO;
		BigDecimal teamValue = BigDecimal.ZERO;
		for (RewardStatDay statDay : statList) {
			Long statDate = statDay.getStatDate();
			boolean ge = start == null || statDate >= start;
			boolean le = end == null || statDate <= end;
			if (ge && le) {
				staticValue = staticValue.add(defaultValue(statDay.getStaticAmount()));
				dynamicValue = dynamicValue.add(defaultValue(statDay.getDynamicAmount()));
				teamValue = teamValue.add(defaultValue(statDay.getTeamAmount()));
			}
		}
		return new RewardSum(staticValue, dynamicValue, teamValue);
	}

	private RewardSum aggregateAll(Long userId) {
		RewardStatDay aggregate = rewardStatDayService.getBaseMapper().selectOne(
			new QueryWrapper<RewardStatDay>()
				.select("COALESCE(SUM(static_amount),0) AS static_amount",
					"COALESCE(SUM(dynamic_amount),0) AS dynamic_amount",
					"COALESCE(SUM(team_amount),0) AS team_amount")
				.eq("user_id", userId)
		);
		if (aggregate == null) {
			return new RewardSum(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		}
		return new RewardSum(aggregate.getStaticAmount(), aggregate.getDynamicAmount(), aggregate.getTeamAmount());
	}

	private IncomeOverviewResp.IncomeScopeDetail buildScopeDetail(Integer scopeType, RewardSum sum) {
		BigDecimal totalAmount = sum.staticAmount
			.add(sum.dynamicAmount)
			.add(sum.teamAmount);

		IncomeOverviewResp.IncomeScopeDetail detail = new IncomeOverviewResp.IncomeScopeDetail();
		detail.setScopeType(scopeType);
		detail.setAmount(totalAmount);
		List<IncomeOverviewResp.IncomeTypePortion> portions = new ArrayList<>();
		portions.add(buildPortion(1, sum.staticAmount, totalAmount));
		portions.add(buildPortion(2, sum.dynamicAmount, totalAmount));
		portions.add(buildPortion(3, sum.teamAmount, totalAmount));
		detail.setTypePortions(portions);
		return detail;
	}

	private IncomeOverviewResp.IncomeTypePortion buildPortion(Integer type, BigDecimal amount, BigDecimal total) {
		IncomeOverviewResp.IncomeTypePortion portion = new IncomeOverviewResp.IncomeTypePortion();
		portion.setType(type);
		BigDecimal safeAmount = amount == null ? BigDecimal.ZERO : amount;
		portion.setAmount(safeAmount);
		if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
			portion.setRatio(BigDecimal.ZERO);
		} else {
			portion.setRatio(safeAmount
				.divide(total, ConstantStatic.twoScale, ConstantStatic.roundingModeNew)
				.multiply(new BigDecimal("100")));
		}
		return portion;
	}

	private BigDecimal defaultValue(BigDecimal amount) {
		return amount == null ? BigDecimal.ZERO : amount;
	}

	/**
	 * 获取奖金记录
	 * @param lastId
	 * @param sourceTypes
	 * @return
	 */
	@Override
	public List<RewardRecordDto> rewardRecord(Long lastId, List<Integer> sourceTypes) {
		if(CollectionUtil.isEmpty(sourceTypes)){
			return new ArrayList<>();
		}
		List<RewardRecordDto> result = rewardRecordService.lambdaQuery()
			.eq(RewardRecord::getUserId, SecurityUtils.getFrontUserId())
			.in(RewardRecord::getSourceType, sourceTypes)
			.lt(Func.isNotEmpty(lastId), RewardRecord::getId, lastId)
			.select(RewardRecord::getId, RewardRecord::getAmount, RewardRecord::getSourceType,
				RewardRecord::getSourceOrderCode, RewardRecord::getSourceUserId,
				RewardRecord::getCreateTime, RewardRecord::getRealTimePrice)
			.orderByDesc(RewardRecord::getId).last(SysConstant.PAGE_LIMIT)
			.list().stream().map(item -> {
				RewardRecordDto rewardRecordDto = new RewardRecordDto();
				rewardRecordDto.setId(item.getId());
				rewardRecordDto.setAmount(item.getAmount());
				rewardRecordDto.setSourceType(item.getSourceType());
				rewardRecordDto.setSourceOrderCode(item.getSourceOrderCode());
				rewardRecordDto.setSourceUserId(item.getSourceUserId());
				rewardRecordDto.setCreateTime(item.getCreateTime());
				rewardRecordDto.setRealTimePrice(item.getRealTimePrice());
				return rewardRecordDto;
			}).collect(Collectors.toList());
		return result;
	}

	/**
	 * 查询用户钱包
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public UserMoneyBo getUserMoney(Long userId) {
		//查询钱包
		UserMoney userMoney = userMoneyService.lambdaQuery().eq(UserMoney::getId, userId)
			.one();
		UserMoneyBo userMoneyBo = new UserMoneyBo();
		userMoneyBo.setValidNum1(userMoney.getValidNum1());
		userMoneyBo.setValidNum2(userMoney.getValidNum2());
		userMoneyBo.setValidNum3(userMoney.getValidNum3());
		userMoneyBo.setValidNum4(userMoney.getValidNum4());
		userMoneyBo.setValidNum5(userMoney.getValidNum5());
		userMoneyBo.setValidNum6(userMoney.getValidNum6());
		userMoneyBo.setValidNum7(userMoney.getValidNum7());
		userMoneyBo.setValidNum8(userMoney.getValidNum8());
		return userMoneyBo;
	}

	/**
	 * 划转记录
	 * @param req
	 * @param userId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void swap(UserMoneySwapVo req, Long userId) {
		//usdt兑换平台币闪兑开关
		if(req.getBizType().equals(0)){
			String usdtToSgm = sysParaServiceImpl.getValue(ConstantSys.biz_usdt_to_sgm_enable);
			if(!"1".equals(usdtToSgm)){
				throw new ServiceException(ResponseCode.CODE_1117);
			}
		}
		UserMoney userMoney = userMoneyService.lambdaQuery()
			.eq(UserMoney::getId, userId)
			.one();
		//余额不足
		Integer fromCoinType=null;
		Integer targetCoinType=null;
		BigDecimal feeRatio = BigDecimal.ZERO;
		if(req.getBizType().equals(SysConstant.ZERO)){
			if(userMoney.getValidNum1().compareTo(req.getAmount())<0){
				throw new ServiceException(ResponseCode.CODE_1015);
			}
			fromCoinType = ConstantType.user_money_coin_type.type_1;
			targetCoinType = ConstantType.user_money_coin_type.type_2;
			feeRatio =new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_swap_usdt_to_p_fee_ratio))
				.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		}else if(req.getBizType().equals(SysConstant.ONE)){
			if(userMoney.getValidNum2().compareTo(req.getAmount())<0){
				throw new ServiceException(ResponseCode.CODE_1015);
			}
			fromCoinType = ConstantType.user_money_coin_type.type_2;
			targetCoinType = ConstantType.user_money_coin_type.type_1;
			feeRatio =new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_swap_p_to_usdt_fee_ratio))
				.divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew);
		}else{
			throw new ServiceException(ResponseCode.CODE_500);
		}

		//
		String code = IDUtils.getSnowflakeStr();
		//划转扣除
		int count = userWalletServiceImpl.handerUserMoney(req.getAmount().negate(), code, userId, userId, ConstantType.user_money_log_source_type.type_9,
			fromCoinType);
		if (count != 1) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}
		BigDecimal feeAmount = req.getAmount().multiply(feeRatio).setScale(ConstantStatic.twoScale, ConstantStatic.roundingModeNew);

		//到账金额
		BigDecimal actualAmount = req.getAmount().subtract(feeAmount);
		BigDecimal pPrice = new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_p_price));
		if(req.getBizType().equals(SysConstant.ZERO)){
			// USDT换平台币：除以平台币价格
			actualAmount = actualAmount.divide(pPrice,ConstantStatic.twoScale, ConstantStatic.roundingModeNew);
		}else if(req.getBizType().equals(SysConstant.ONE)){
			// 平台币换USDT：乘以平台币价格
			actualAmount = actualAmount.multiply(pPrice).setScale(ConstantStatic.twoScale, ConstantStatic.roundingModeNew);
		}
		//划转增加
		count = userWalletServiceImpl.handerUserMoney(actualAmount, code, userId, userId, ConstantType.user_money_log_source_type.type_10,
			targetCoinType);
		if (count != 1) {
			throw new ServiceException(ResponseCode.CODE_1015);
		}
		FlashExchangeRecord flashExchangeRecord = new FlashExchangeRecord();
		flashExchangeRecord.setUserId(userId);
		flashExchangeRecord.setType(req.getBizType());
		flashExchangeRecord.setBalance(req.getAmount());
		flashExchangeRecord.setFee(feeAmount);
		flashExchangeRecord.setFeeRatio(feeRatio);
		flashExchangeRecord.setPtbPrice(pPrice);
		flashExchangeRecord.setReceivedAmount(actualAmount);
		boolean save = flashExchangeRecordService.save(flashExchangeRecord);
		if(!save){
			throw new ServiceException(ResponseCode.CODE_1002);
		}
	}

	/**
	 * 查询平台币最近7天价格
	 * @return
	 */
	@Override
	public ResultPista<List<PtbPriceDto>> queryPtbLast7Price() {
		List<PtbPriceDto> result = xmsRedis.get(RedisConstant.PTB_PRICE_KEY, () -> {
			List<PtbPriceDto> ptbPriceDtoList = ptbDailyPriceServiceImpl.lambdaQuery()
				.orderByDesc(PtbDailyPrice::getId)
				.select(PtbDailyPrice::getDate, PtbDailyPrice::getId, PtbDailyPrice::getPrice)
				.list().reversed()
				.stream().map(record -> {
					PtbPriceDto dto = new PtbPriceDto();
					dto.setDate(record.getDate());
					dto.setPrice(record.getPrice());
					return dto;
				}).collect(Collectors.toList());
			return ptbPriceDtoList;
		});
		//拿今日数据
		if (CollectionUtil.isNotEmpty(result)) {
			PtbPriceDto todayDto = new PtbPriceDto();
			todayDto.setDate(Long.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
			todayDto.setPrice(new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_p_price)));
			result.addLast(todayDto);
		} else {
			result = new ArrayList<>();
			PtbPriceDto todayDto = new PtbPriceDto();
			todayDto.setDate(Long.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
			todayDto.setPrice(new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_p_price)));
			result.add(todayDto);
		}
/*		List<PtbPriceDto> ptbPriceDtoList = ptbDailyPriceServiceImpl.lambdaQuery()
			.orderByDesc(PtbDailyPrice::getId)
			.last("limit 7")
			.select(PtbDailyPrice::getDate, PtbDailyPrice::getId, PtbDailyPrice::getPrice)
			.list().reversed()
			.stream().map(record -> {
				PtbPriceDto dto = new PtbPriceDto();
				dto.setDate(record.getDate());
				dto.setPrice(record.getPrice());
				return dto;
			}).collect(Collectors.toList());
		//初始化一下数据
		if(CollectionUtil.isNotEmpty(ptbPriceDtoList)){
			PtbPriceDto todayDto = new PtbPriceDto();
			todayDto.setDate(Long.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
			todayDto.setPrice(new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_p_price)));
			ptbPriceDtoList.addLast(todayDto);
		}else{
			ptbPriceDtoList = new ArrayList<>();
			PtbPriceDto todayDto = new PtbPriceDto();
			todayDto.setDate(Long.valueOf(DateUtil.format(DateUtil.date(), "yyyyMMdd")));
			todayDto.setPrice(new BigDecimal(sysParaServiceImpl.getValue(ConstantSys.biz_p_price)));
			ptbPriceDtoList.add(todayDto);
		}*/

		return ResultPista.data(result);
	}

	@Override
	public ResultPista<List<FlashExchangeRecord>> swapRecord(Long lastId) {
		List<FlashExchangeRecord> list = flashExchangeRecordService.lambdaQuery()
			.eq(FlashExchangeRecord::getUserId, SecurityUtils.getFrontUserId())
			.lt(Func.isNotEmpty(lastId), FlashExchangeRecord::getId, lastId)
			.orderByDesc(FlashExchangeRecord::getId).last(SysConstant.PAGE_LIMIT)
			.list();
		return ResultPista.data(list);
	}

	/**
	 * 今日收益
	 * @param userId
	 * @return
	 */
	@Override
	public TodayIncomeDto todayIncome(Long userId) {
		TodayIncomeDto income = new TodayIncomeDto();
		BigDecimal usdtAmount = BigDecimal.ZERO;
		BigDecimal sgmAmount = BigDecimal.ZERO;

		BigDecimal  sgmPrice = new BigDecimal(xmsRedis.get(ConstantStatic.settlement_sgm_price, ()->{
			return sysParaServiceImpl.getValue(ConstantSys.biz_p_price);
		}));
		List<UserMoneyLog> userMoneyLogList = userMoneyLogService.lambdaQuery()
			.eq(UserMoneyLog::getUserId, userId)
			.in(UserMoneyLog::getSourceType, ConstantType.user_money_log_source_type.type_21,
				ConstantType.user_money_log_source_type.type_23, ConstantType.user_money_log_source_type.type_24,
				ConstantType.user_money_log_source_type.type_25, ConstantType.user_money_log_source_type.type_29,
				ConstantType.user_money_log_source_type.type_30)
			.apply("create_time >= CURDATE()")
			.select(UserMoneyLog::getChangeBalance, UserMoneyLog::getCoinType)
			.list();
		if(CollectionUtil.isNotEmpty(userMoneyLogList)){
			for (UserMoneyLog userMoneyLog : userMoneyLogList) {
				if(userMoneyLog.getCoinType().equals(1)){
					usdtAmount = usdtAmount.add(userMoneyLog.getChangeBalance());
				}else{
					sgmAmount = sgmAmount.add(userMoneyLog.getChangeBalance());
				}
			}
		}

		usdtAmount = usdtAmount.add(sgmAmount.multiply(sgmPrice).setScale(ConstantStatic.twoScale, ConstantStatic.roundingModeNew));

		income.setUsdtAmount(usdtAmount.setScale(ConstantStatic.twoScale, RoundingMode.DOWN));
		income.setSgmAmount(sgmAmount.setScale(ConstantStatic.twoScale, RoundingMode.DOWN));
		return income;
	}

	private static class RewardSum {
		private final BigDecimal staticAmount;
		private final BigDecimal dynamicAmount;
		private final BigDecimal teamAmount;

		private RewardSum(BigDecimal staticAmount, BigDecimal dynamicAmount, BigDecimal teamAmount) {
			this.staticAmount = staticAmount == null ? BigDecimal.ZERO : staticAmount;
			this.dynamicAmount = dynamicAmount == null ? BigDecimal.ZERO : dynamicAmount;
			this.teamAmount = teamAmount == null ? BigDecimal.ZERO : teamAmount;
		}
	}
}
