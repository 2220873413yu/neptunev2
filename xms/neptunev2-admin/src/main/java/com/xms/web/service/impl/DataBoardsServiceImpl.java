package com.xms.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.RedisConstant;
import com.xms.common.constant.SysConstant;
import com.xms.common.thread.asyncTool.AsyncPlus;
import com.xms.common.thread.asyncTool.WorkerWrapper;
import com.xms.common.utils.Kv;
import com.xms.common.utils.SecurityUtils;
import com.xms.dao.domain.RechargeRecord;
import com.xms.dao.domain.StakeOrder;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.entity.vo.IndexDataPanelVo;
import com.xms.dao.service.IRechargeRecordService;
import com.xms.dao.service.IStakeOrderService;
import com.xms.dao.service.IndexDataService;
import com.xms.dao.service.UserInfoService;
import com.xms.dao.service.impl.WithdrawalServiceImpl;
import com.xms.web.domain.LineDataDO;
import com.xms.web.mapper.DataBoardMapper;
import com.xms.web.service.DataBoardsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: GT63S
 * @createDate: 2024/9/4
 */
@AllArgsConstructor
@Service
@Slf4j
public class DataBoardsServiceImpl implements DataBoardsService {
	private final UserInfoService userInfoServiceImpl;
	private final DataBoardMapper dataBoardMapper;
	private final IRechargeRecordService rechargeRecordService;
	private final XmsRedis xmsRedis;
	private final IndexDataService indexDataServiceImpl;
	private final IStakeOrderService stakeOrderServiceImpl;

	@Override
	public Map<String, Object> getUserDataBoard() throws Exception {
		//查询充值数据
		WorkerWrapper<Long, Map<String, Double>> rechargeWrapper = getRechargeWrapper();
		//查询提现数据
		WorkerWrapper<Long, Map<String, BigDecimal>> withdrawWrapper = getWithdrawWrapper();
		//查询用户数据
		WorkerWrapper<Long, Map<String, Object>> userTotalWrapper = getUserTotalWrapper();
		AsyncPlus.beginWork(120000L, userTotalWrapper, rechargeWrapper, withdrawWrapper);
		Map<String, Object> set = Kv.create();
		if (userTotalWrapper.getWorkResult() != null) {
			set.putAll(userTotalWrapper.getWorkResult().getResult());
		}
		if (rechargeWrapper.getWorkResult() != null) {
			set.putAll(rechargeWrapper.getWorkResult().getResult());
		}
		if (withdrawWrapper.getWorkResult() != null) {
			set.putAll(withdrawWrapper.getWorkResult().getResult());
		}

		IndexDataPanelVo indexDataPanelVo = indexDataServiceImpl.getIndexDataPanelVo();
//		set.put("v3", indexDataPanelVo.getV3());
//		set.put("v4", indexDataPanelVo.getV4());
		set.put("v7", indexDataPanelVo.getV7());
		set.put("v6", indexDataPanelVo.getV6());

		set.put("v8", indexDataPanelVo.getV8());
		set.put("v9", indexDataPanelVo.getV9());
		set.put("v10", indexDataPanelVo.getV10());
		set.put("v11", indexDataPanelVo.getV11());
		set.put("v12", indexDataPanelVo.getV12());
		set.put("v13", indexDataPanelVo.getV13());
//		set.put("v14", indexDataPanelVo.getV14());
//		set.put("v15", indexDataPanelVo.getV15());
//		set.put("v16", indexDataPanelVo.getV16());
//		set.put("v17", indexDataPanelVo.getV17());
//		set.put("v24", indexDataPanelVo.getV24());
//		set.put("v25", indexDataPanelVo.getV25());
		set.put("v26", indexDataPanelVo.getV26());
		set.put("v27", indexDataPanelVo.getV27());
		set.put("v28", indexDataPanelVo.getV28());

		set.put("v29", indexDataPanelVo.getV29());
		set.put("v30", indexDataPanelVo.getV30());
		set.put("v31", indexDataPanelVo.getV31());
		set.put("v32", indexDataPanelVo.getV32().negate());
		set.put("v33", indexDataPanelVo.getV33().negate());
		set.put("v34", indexDataPanelVo.getV34().negate());

		set.put("v35", indexDataPanelVo.getV35());
		set.put("v36", indexDataPanelVo.getV36());
		set.put("v37", indexDataPanelVo.getV37());
		set.put("v38", indexDataPanelVo.getV38());
		set.put("v39", indexDataPanelVo.getV39());
		set.put("v40", indexDataPanelVo.getV40());

		set.put("v41", indexDataPanelVo.getV41());
		set.put("v42", indexDataPanelVo.getV42());
		set.put("v43", indexDataPanelVo.getV43());
		set.put("v44", indexDataPanelVo.getV44());
		set.put("v45", indexDataPanelVo.getV45());
		set.put("v46", indexDataPanelVo.getV46());
		set.put("v47", indexDataPanelVo.getV47());
		set.put("v48", indexDataPanelVo.getV48());
		set.put("v49", indexDataPanelVo.getV49());
		set.put("v50", indexDataPanelVo.getV50());
		set.put("v51", indexDataPanelVo.getV51());
		set.put("v52", indexDataPanelVo.getV52());
		set.put("v53", indexDataPanelVo.getV53());

		List<Withdrawal> withdrawalList = SpringUtil.getBean(WithdrawalServiceImpl.class).lambdaQuery()
			.in(Withdrawal::getStatus, 3)
			.apply("create_time >= CURDATE()")
			.select(Withdrawal::getChangeBalance, Withdrawal::getCoinType)
			.list();

		BigDecimal v18= withdrawalList.stream()
			.filter(record->{
				return record.getCoinType() ==1;
			})
			.map(Withdrawal::getChangeBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

//		BigDecimal v19= withdrawalList.stream()
//			.filter(record->{
//				return record.getCoinType() ==2;
//			})
//			.map(Withdrawal::getChangeBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

		set.put("v18", v18);
		//bpay今日提现
		//set.put("v19", v19);
		set.put("v20", SpringUtil.getBean(WithdrawalServiceImpl.class).selectUsdtTotalBalance());
		//set.put("v21", SpringUtil.getBean(WithdrawalServiceImpl.class).selectBPayTotalBalance());


//		set.put("v22", rechargeRecordService.selectTotalRechargeBalance());
//		set.put("v23", rechargeRecordService.selectTodayRechargeBalance());
		return set;
	}

	@Override
	public Map<String, Object> listGroupTotal() throws Exception {
		Map<String, Object> set = Kv.create();
		//查询用户数据
		WorkerWrapper<Long, Map<String, Object>> userRegisterGroupWrapper = getUserRegisterGroupWrapper();
		//查询充值数据
		WorkerWrapper<Long, Map<String, Object>> lineRechargeChartData = getLineRechargeChartData();
		//查询提现数据
		WorkerWrapper<Long, Map<String, Object>> lineWdwChartData = getLineWdwChartData();
		//查询质押数据
		WorkerWrapper<Long, Map<String, Object>> lineStakeChartData = getLineStakeChartData();
		AsyncPlus.beginWork(120000L, userRegisterGroupWrapper, lineWdwChartData, lineRechargeChartData, lineStakeChartData);
		if (userRegisterGroupWrapper.getWorkResult() != null) {
			set.put("userRegisterGroup", userRegisterGroupWrapper.getWorkResult().getResult());
		}
		if (lineWdwChartData.getWorkResult() != null) {
			set.put("lineWdwChartData", lineWdwChartData.getWorkResult().getResult());
		}
		if (lineRechargeChartData.getWorkResult() != null) {
			set.put("lineRechargeChartData", lineRechargeChartData.getWorkResult().getResult());
		}
		if (lineStakeChartData.getWorkResult() != null) {
			set.put("lineStakeChartData", lineStakeChartData.getWorkResult().getResult());
		}
		return set;
	}

	@Override
	public Map<String, Object> listOrderGroupTotal() throws Exception {
		//查询订单报单数量
		WorkerWrapper<Long, List<LineDataDO>> orderWrapper = getOrderWrapper();
		//查询拨比数量
		WorkerWrapper<Long, List<LineDataDO>> rewardWrapper = getRewardWrapper();
		AsyncPlus.beginWork(120000L, orderWrapper, rewardWrapper);
		Map<String, Object> set = Kv.create();
		TreeMap<String, LineDataDO> finalResult;
		if (orderWrapper.getWorkResult() != null) {
			List<LineDataDO> orderList = orderWrapper.getWorkResult().getResult();
			finalResult = new TreeMap<>(orderList.stream().collect(Collectors.toMap(LineDataDO::getXkey, p -> p)));
			List<LineDataDO> dos = null;
			if (rewardWrapper.getWorkResult() != null) {
				dos = rewardWrapper.getWorkResult().getResult();
				Map<String, LineDataDO> tempResult = dos.stream().collect(Collectors.toMap(LineDataDO::getXkey, p -> p));
				finalResult.putAll(tempResult);
			}
			//组装数据
			List<BigDecimal> orderAmount = new ArrayList<>();
			List<BigDecimal> rewardAmount = new ArrayList<>();
			List<String> everyDate = new ArrayList<>();
			for (Map.Entry<String, LineDataDO> entry : finalResult.entrySet()) {
				everyDate.add(entry.getKey());
				orderList.stream().filter(lineDataDO -> lineDataDO.getXkey().equals(entry.getKey())).findFirst().
					ifPresentOrElse(lineDataDO ->
							orderAmount.add(lineDataDO.getExpectedRewardData()),
						() -> orderAmount.add(BigDecimal.ZERO));
				if (dos != null) {
					dos.stream().filter(lineDataDO -> lineDataDO.getXkey().equals(entry.getKey())).findFirst().
						ifPresentOrElse(lineDataDO -> rewardAmount.add(lineDataDO.getExpectedRewardDataV2()),
							() -> rewardAmount.add(BigDecimal.ZERO));
				} else {
					rewardAmount.add(BigDecimal.ZERO);
				}
			}
			set.put("key", everyDate);
			set.put("orderAmount", orderAmount);
			set.put("rewardAmount", rewardAmount);
		}
		return set;
	}

	private WorkerWrapper<Long, List<LineDataDO>> getRewardWrapper() {
		return new WorkerWrapper.Builder<Long, List<LineDataDO>>()
			.id("8")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				String yes = DateUtil.formatDate(DateUtil.yesterday());
				List<LineDataDO> result = xmsRedis.get(RedisConstant.USER_REWARD_GROUP + yes, dataBoardMapper::listRewardGroupTotal,
					RedisConstant.SECONDS_EXPIRE_TIME / SysConstant.TWO, TimeUnit.MINUTES);
				//计算今天的
				BigDecimal reward = dataBoardMapper.listRewardGroupTotalToday();
				if (reward != null) {
					if (result == null || result.isEmpty()) {
						result = new ArrayList<>();
					}
					LineDataDO lineDataDO = new LineDataDO();
					lineDataDO.setXkey(DateUtil.formatDate(new Date()));
					lineDataDO.setExpectedRewardDataV2(reward);
					result.add(lineDataDO);
				}
				return result;
			})
			.param(SecurityUtils.getUserId()).build();
	}

	private WorkerWrapper<Long, List<LineDataDO>> getOrderWrapper() {
		return new WorkerWrapper.Builder<Long, List<LineDataDO>>()
			.id("7")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				String yes = DateUtil.formatDate(DateUtil.yesterday());
				List<LineDataDO> result = new ArrayList<>();
				if (result == null || result.isEmpty()) {
					result = new ArrayList<>();
				}
				LineDataDO lineDataDO = new LineDataDO();
				BigDecimal usdtNum = BigDecimal.ZERO;
				lineDataDO.setExpectedRewardData(usdtNum);
				result.add(lineDataDO);
				return result;
			})
			.param(SecurityUtils.getUserId()).build();
	}

	private WorkerWrapper<Long, Map<String, Object>> getLineWdwChartData(String param) {
		return new WorkerWrapper.Builder<Long, Map<String, Object>>()
			.id("6")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, Object> realData = new HashMap<>(2);
				String yes = DateUtil.formatDate(DateUtil.yesterday());
				Map<String, Object> yesData = xmsRedis.get(RedisConstant.USER_WITHDRAW_GROUP + yes, () -> {
					Map<String, Object> map = new HashMap<>(2);
					map.put("expectedData", null);
					map.put("key", null);
					List<LineDataDO> felTotal = dataBoardMapper.getLineWdwChartData();
					if (!felTotal.isEmpty()) {
						List<BigDecimal> price = new ArrayList<>();
						List<String> everyDate = new ArrayList<>();
						for (LineDataDO objectMap : felTotal) {
							price.add(objectMap.getExpectedRewardData());
							everyDate.add(objectMap.getXkey());
						}
						map.put("expectedRewardData", price);
						map.put("key", everyDate);
					}
					return map;
				}, RedisConstant.SECONDS_EXPIRE_TIME / SysConstant.TWO, TimeUnit.MINUTES);
				realData.putAll(yesData);
				//计算今天的
				BigDecimal felTotalToday = dataBoardMapper.getLineWdwChartDataToday();
				if (felTotalToday != null) {
					if (realData.get("key") != null) {
						List<String> keys = (List<String>) realData.get("key");
						List<BigDecimal> expectedDatas = (List<BigDecimal>) realData.get("expectedRewardData");
						keys.add(DateUtil.today());
						expectedDatas.add(felTotalToday);
					} else {
						List<String> keys = new ArrayList<>();
						List<BigDecimal> expectedDatas = new ArrayList<>();
						keys.add(DateUtil.today());
						expectedDatas.add(felTotalToday);
						realData.put("expectedRewardData", expectedDatas);
						realData.put("key", keys);
					}
				}
				return realData;
			})
			.param(SecurityUtils.getUserId())
			.build();
	}

	private WorkerWrapper<Long, Map<String, Object>> getLineWdwChartData() {
		return new WorkerWrapper.Builder<Long, Map<String, Object>>()
			.id("6")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, Object> realData = initWithdrawChartData();
				String yes = DateUtil.formatDate(DateUtil.yesterday());
				Map<String, Object> yesData = xmsRedis.get(RedisConstant.USER_WITHDRAW_GROUP + yes, () -> {
					List<Withdrawal> list1 = SpringUtil.getBean(WithdrawalServiceImpl.class).lambdaQuery()
						.in(Withdrawal::getStatus, 3)
						.apply(" create_time < CURDATE()  AND create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)")
						.orderByAsc(Withdrawal::getId)
						.list();

					return buildWithdrawChartData(list1);
				}, RedisConstant.SECONDS_EXPIRE_TIME / SysConstant.TWO, TimeUnit.MINUTES);
				realData.putAll(yesData);

				//计算今天的
				List<Withdrawal> list = SpringUtil.getBean(WithdrawalServiceImpl.class).lambdaQuery()
					.in(Withdrawal::getStatus, 3)
					.apply(" create_time >=CURDATE() ").orderByAsc(Withdrawal::getId).list();

				if (list != null && !list.isEmpty()) {
					appendWithdrawChartData(realData, DateUtil.today(), list);
				}
				return realData;
			})
			.param(SecurityUtils.getUserId())
			.build();
	}

	private Map<String, Object> initWithdrawChartData() {
		Map<String, Object> data = new HashMap<>(2);
		data.put("key", new ArrayList<String>());
		List<Map<String, Object>> series = new ArrayList<>();
		for (Map.Entry<Integer, String> entry : getWithdrawCoinTypeMap().entrySet()) {
			Map<String, Object> seriesItem = new HashMap<>(3);
			seriesItem.put("coinType", entry.getKey());
			seriesItem.put("name", entry.getValue());
			seriesItem.put("data", new ArrayList<BigDecimal>());
			series.add(seriesItem);
		}
		data.put("series", series);
		return data;
	}

	private Map<String, Object> buildWithdrawChartData(List<Withdrawal> withdrawalList) {
		Map<String, Object> chartData = initWithdrawChartData();
		if (CollectionUtil.isEmpty(withdrawalList)) {
			return chartData;
		}
		TreeMap<String, List<Withdrawal>> groupedMap = new TreeMap<>(withdrawalList.stream()
			.collect(Collectors.groupingBy(withdrawal -> DateUtil.formatDate(withdrawal.getCreateTime()))));
		for (Map.Entry<String, List<Withdrawal>> entry : groupedMap.entrySet()) {
			appendWithdrawChartData(chartData, entry.getKey(), entry.getValue());
		}
		return chartData;
	}

	private void appendWithdrawChartData(Map<String, Object> chartData, String date, List<Withdrawal> withdrawalList) {
		List<String> dateList = (List<String>) chartData.get("key");
		dateList.add(date);
		Map<Integer, BigDecimal> amountByCoinType = withdrawalList.stream()
			.collect(Collectors.groupingBy(Withdrawal::getCoinType,
				Collectors.reducing(BigDecimal.ZERO, Withdrawal::getChangeBalance, BigDecimal::add)));
		List<Map<String, Object>> series = (List<Map<String, Object>>) chartData.get("series");
		for (Map<String, Object> seriesItem : series) {
			Integer coinType = (Integer) seriesItem.get("coinType");
			List<BigDecimal> data = (List<BigDecimal>) seriesItem.get("data");
			data.add(amountByCoinType.getOrDefault(coinType, BigDecimal.ZERO));
		}
	}

	private LinkedHashMap<Integer, String> getWithdrawCoinTypeMap() {
		LinkedHashMap<Integer, String> coinTypeMap = new LinkedHashMap<>();
		coinTypeMap.put(1, "节点收益");
		coinTypeMap.put(2, "静态");
		coinTypeMap.put(3, "动态");
		coinTypeMap.put(4, "财富");
		coinTypeMap.put(5, "魔盒");
		coinTypeMap.put(6, "工作室收益");
		return coinTypeMap;
	}

	private WorkerWrapper<Long, Map<String, Object>> getLineStakeChartData() {
		return new WorkerWrapper.Builder<Long, Map<String, Object>>()
			.id("10")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, Object> realData = initStakeChartData();
				String yes = DateUtil.formatDate(DateUtil.yesterday());
				Map<String, Object> yesData = xmsRedis.get(RedisConstant.USER_STAKE_GROUP + yes, () -> {
					List<StakeOrder> list = stakeOrderServiceImpl.lambdaQuery()
						.eq(StakeOrder::getStatus, 1)
						.eq(StakeOrder::getBizStatus, 1)
						.apply(" create_time < CURDATE() AND create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)")
						.orderByAsc(StakeOrder::getId)
						.list();
					return buildStakeChartData(list);
				}, RedisConstant.SECONDS_EXPIRE_TIME / SysConstant.TWO, TimeUnit.MINUTES);
				realData.putAll(yesData);

				List<StakeOrder> todayList = stakeOrderServiceImpl.lambdaQuery()
					.eq(StakeOrder::getStatus, 1)
					.eq(StakeOrder::getBizStatus, 1)
					.apply(" create_time >= CURDATE() ")
					.orderByAsc(StakeOrder::getId)
					.list();
				if (CollectionUtil.isNotEmpty(todayList)) {
					appendStakeChartData(realData, DateUtil.today(), todayList);
				}
				return realData;
			})
			.param(SecurityUtils.getUserId())
			.build();
	}

	private Map<String, Object> initStakeChartData() {
		Map<String, Object> data = new HashMap<>(2);
		data.put("key", new ArrayList<String>());
		List<Map<String, Object>> series = new ArrayList<>();
		Map<String, Object> seriesItem = new HashMap<>(2);
		seriesItem.put("name", "质押量");
		seriesItem.put("data", new ArrayList<BigDecimal>());
		series.add(seriesItem);
		data.put("series", series);
		return data;
	}

	private Map<String, Object> buildStakeChartData(List<StakeOrder> stakeOrderList) {
		Map<String, Object> chartData = initStakeChartData();
		if (CollectionUtil.isEmpty(stakeOrderList)) {
			return chartData;
		}
		TreeMap<String, List<StakeOrder>> groupedMap = new TreeMap<>(stakeOrderList.stream()
			.collect(Collectors.groupingBy(stakeOrder -> DateUtil.formatDate(stakeOrder.getCreateTime()))));
		for (Map.Entry<String, List<StakeOrder>> entry : groupedMap.entrySet()) {
			appendStakeChartData(chartData, entry.getKey(), entry.getValue());
		}
		return chartData;
	}

	private void appendStakeChartData(Map<String, Object> chartData, String date, List<StakeOrder> stakeOrderList) {
		List<String> dateList = (List<String>) chartData.get("key");
		dateList.add(date);
		BigDecimal stakeAmount = stakeOrderList.stream()
			.map(StakeOrder::getStakeAmount)
			.filter(Objects::nonNull)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		List<Map<String, Object>> series = (List<Map<String, Object>>) chartData.get("series");
		if (CollectionUtil.isNotEmpty(series)) {
			List<BigDecimal> data = (List<BigDecimal>) series.get(0).get("data");
			data.add(stakeAmount);
		}
	}

	private WorkerWrapper<Long, Map<String, Object>> getLineRechargeChartData() {
		return new WorkerWrapper.Builder<Long, Map<String, Object>>()
			.id("5")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, Object> realData = new HashMap<>(4);
				String yes = DateUtil.formatDate(DateUtil.yesterday());
				Map<String, Object> yesData = xmsRedis.get(RedisConstant.USER_RECHARGE_GROUP + yes, () -> {
					Map<String, Object> map = new HashMap<>(4);
					map.put("rechargeUsdt", null);
					//map.put("rechargeEnergyPool", null);
					map.put("key", null);
					List<RechargeRecord> list1 = rechargeRecordService.lambdaQuery()
						.eq(RechargeRecord::getStatus, 1)
						.apply(" create_time < CURDATE()  AND create_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
						.orderByAsc(RechargeRecord::getId)
						.list();

					List<BigDecimal> priceUsdt = new ArrayList<>();
					//List<BigDecimal> priceEnergyPool = new ArrayList<>();
					List<String> everyDate = new ArrayList<>();
					if (!list1.isEmpty()) {
						//按照日期分组
						TreeMap<String, List<RechargeRecord>> temp = new TreeMap<>(list1.stream().collect(Collectors.groupingBy(recharge ->
							DateUtil.formatDate(recharge.getCreateTime()))));
						for (Map.Entry<String, List<RechargeRecord>> entry : temp.entrySet()) {
							everyDate.add(entry.getKey());
							BigDecimal usdtNum = BigDecimal.ZERO;
							//BigDecimal poolNum = BigDecimal.ZERO;
							for (RechargeRecord recharge : entry.getValue()) {
								usdtNum = usdtNum.add(recharge.getRechargeAmount());
//								else {
//									poolNum = poolNum.add(recharge.getRechargeAmount())
//										.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew);
//								}
							}
							priceUsdt.add(usdtNum);
							//priceEnergyPool.add(poolNum);
						}
						//map.put("rechargeEnergyPool", priceEnergyPool);
						map.put("rechargeUsdt", priceUsdt);
						map.put("key", everyDate);
					}
					return map;
				}, RedisConstant.SECONDS_EXPIRE_TIME / SysConstant.TWO, TimeUnit.MINUTES);
				realData.putAll(yesData);

				//计算今天的
		/*		List<Recharge> list = rechargeServiceImpl.lambdaQuery().eq(Recharge::getStatus, 1)
					.apply(" create_time >=CURDATE() ").orderByAsc(Recharge::getId).list();
*/
				List<RechargeRecord> list1 = rechargeRecordService.lambdaQuery()
					.eq(RechargeRecord::getStatus, 1).apply(" create_time >=CURDATE() ").orderByAsc(RechargeRecord::getId).list();
				if (list1 != null && !list1.isEmpty()) {
					List<BigDecimal> priceUsdt = new ArrayList<>();
					List<BigDecimal> priceEnergyPool = new ArrayList<>();
					List<String> everyDate = new ArrayList<>();
					if (realData.get("key") != null) {
						everyDate = (List<String>) realData.get("key");
						priceUsdt = (List<BigDecimal>) realData.get("rechargeUsdt");
						//priceUsdt = (List<BigDecimal>) realData.get("rechargeEnergyPool");
						//priceEnergyPool = (List<BigDecimal>) realData.get("rechargeUsdt");
						//priceEnergyPool = (List<BigDecimal>) realData.get("rechargeUsdt");
					}
					everyDate.add(DateUtil.formatDate(list1.getFirst().getCreateTime()));
					BigDecimal usdtNum = BigDecimal.ZERO;
					BigDecimal poolNum = BigDecimal.ZERO;
					for (RechargeRecord recharge : list1) {
						usdtNum = usdtNum.add(recharge.getRechargeAmount()
							.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
					}
					priceUsdt.add(usdtNum);
					priceEnergyPool.add(poolNum);
					//realData.put("rechargeEnergyPool", priceEnergyPool);
					realData.put("rechargeUsdt", priceUsdt);
					realData.put("key", everyDate);
				}
				realData.put("lineType", "USDT");
				//realData.put("lineTypePool", "CNY");
				return realData;
			})
			.param(SecurityUtils.getUserId()).build();
	}


	private WorkerWrapper<Long, Map<String, Object>> getUserRegisterGroupWrapper() {
		return new WorkerWrapper.Builder<Long, Map<String, Object>>()
			.id("4")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, Object> realData = new HashMap<>(2);
				realData.put("expectedData", null);
				realData.put("key", null);
				//获取当前月份
				String format = DateUtil.formatDate(DateUtil.yesterday());
				List<LineDataDO> felTotal = xmsRedis.get(RedisConstant.USER_REGISTER_GROUP + format,
					dataBoardMapper::listUserGroupTotal, RedisConstant.SECONDS_EXPIRE_TIME / SysConstant.TEN, TimeUnit.HOURS);
				if (felTotal != null && !felTotal.isEmpty()) {
					List<Integer> price = new ArrayList<>(felTotal.size());
					List<String> everyDate = new ArrayList<>(felTotal.size());
					for (LineDataDO objectMap : felTotal) {
						everyDate.add(objectMap.getXkey());
						price.add(objectMap.getExpectedData());
					}
					realData.put("expectedData", price);
					realData.put("key", everyDate);
				}
				Long count = userInfoServiceImpl.lambdaQuery().apply(" create_time >= CURDATE()").eq(UserInfo::getDeleted,0).count();
				if (count > 0) {
					List<Integer> expectedData = new ArrayList<>();
					List<String> everyDate = new ArrayList<>();
					if (realData.get("key") != null) {
						everyDate = (List<String>) realData.get("key");
						expectedData = (List<Integer>) realData.get("expectedData");
					}
					everyDate.add(DateUtil.today());
					expectedData.add(count.intValue());
					realData.put("expectedData", expectedData);
					realData.put("key", everyDate);
				}
				return realData;
			})
			.param(SecurityUtils.getUserId()).build();
	}

	private WorkerWrapper<Long, Map<String, BigDecimal>> getWithdrawWrapper() {
		return new WorkerWrapper.Builder<Long, Map<String, BigDecimal>>()
			.id("1")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, BigDecimal> result = new HashMap<>(8);
				result.put("wdwUsdt", dataBoardMapper.countWdwNum());
				BigDecimal wdwToday = dataBoardMapper.countWdwNumToday();
				result.put("wdwToday", wdwToday);
				return result;
			})
			.param(SecurityUtils.getUserId())
			.build();
	}

	private WorkerWrapper<Long, Map<String, Double>> getRechargeWrapper() {
		return new WorkerWrapper.Builder<Long, Map<String, Double>>()
			.id("2")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, Double> result = new HashMap<>(8);
				result.put("rechargeUsdt", 0D);
				//result.put("rechargeEnergyPool", 0D);
				result.put("rechargeUsdtToday", 0D);
				//result.put("rechargeEnergyPoolToday", 0D);
				List<RechargeRecord> list1 = rechargeRecordService.lambdaQuery().eq(RechargeRecord::getStatus, 1).list();
				for (RechargeRecord rechargeRecord : list1) {
					result.put("rechargeUsdt", result.get("rechargeUsdt") + rechargeRecord.getRechargeAmount()
						.setScale(ConstantStatic.eightScale, ConstantStatic.roundingModeNew).doubleValue());
//					else {
//						result.put("rechargeEnergyPool", result.get("rechargeEnergyPool") + rechargeRecord.getRechargeAmount()
//							.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
//							.doubleValue());
//					}

					//当日
					if (DateUtil.formatDate(rechargeRecord.getCreateTime()).equals(DateUtil.formatDate(new Date()))) {
						result.put("rechargeUsdtToday", result.get("rechargeUsdtToday") + rechargeRecord.getRechargeAmount()
							.setScale(ConstantStatic.eightScale, ConstantStatic.roundingModeNew).doubleValue());
//						else {
//							result.put("rechargeEnergyPoolToday", result.get("rechargeEnergyPoolToday") + rechargeRecord.getRechargeAmount()
//								.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew)
//								.doubleValue());
//						}
					}

				}
				log.debug("result={}", result);
				return result;
			})
			.param(SecurityUtils.getUserId())
			.build();
	}

	private WorkerWrapper<Long, Map<String, Object>> getUserTotalWrapper() {
		return new WorkerWrapper.Builder<Long, Map<String, Object>>()
			.id("3")
			.worker((Long userId, Map<String, WorkerWrapper> allWrappers) -> {
				Map<String, Object> map = new HashMap<>();
				Long count = userInfoServiceImpl.lambdaQuery()
					.eq(UserInfo::getDeleted,0)
					.count();
				map.put("userTotal", count);
				Long todayUserCount = userInfoServiceImpl.lambdaQuery().apply("create_time >= CURDATE()").count();
				map.put("todayUserCount", todayUserCount);
				return map;
			})
			.param(SecurityUtils.getUserId())
			.build();
	}

}
