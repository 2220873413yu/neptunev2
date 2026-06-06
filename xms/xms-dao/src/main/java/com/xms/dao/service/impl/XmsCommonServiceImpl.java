package com.xms.dao.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.SysConstant;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.DateUtils;
import com.xms.dao.mapper.AsyncTaskMapper;
import com.xms.dao.service.ISysParaService;
import com.xms.dao.service.XmsCommonService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 2023/09/22
 *
 * @author MIER
 */
@Service
@Slf4j
@AllArgsConstructor
public class XmsCommonServiceImpl implements XmsCommonService {
	private final XmsRedis xmsRedis;
	private final ISysParaService sysParaServiceImpl;
	private final AsyncTaskMapper asyncTaskMapper;




	public static boolean isInTimeRange(DateTime dateTime, String startTime, String endTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime start = LocalTime.parse(startTime, formatter);
		LocalTime end = LocalTime.parse(endTime, formatter);

		// 将 dateTime 转换为 LocalTime
		LocalTime timeToCheck = LocalTime.of(dateTime.hour(true), dateTime.minute(), dateTime.second());

		if (end.isBefore(start)) {
			// 如果结束时间小于开始时间，说明跨越了午夜
			return !timeToCheck.isBefore(start) || timeToCheck.isBefore(end);
		} else {
			// 不跨午夜的情况
			return !timeToCheck.isBefore(start) && timeToCheck.isBefore(end);
		}
	}

	public static void main(String[] args) {
		// args[0]: 当前时间，格式 yyyy-MM-dd HH:mm:ss（可选，不传默认当前时间）
		// args[1]: 结算时间段，格式 HH:mm-HH:mm（可选，不传默认 23:55-01:00）
		String nowStr = "2026-03-25 23:55:00";
		String miningLimit = args != null && args.length > 1 ? args[1] : "23:55-01:00";

		DateTime currentDate;
		try {
			// 测试时间按字符串传入，统一格式化到 yyyy-MM-dd HH:mm:ss
			currentDate = DateUtil.parse(nowStr,
				"yyyy-MM-dd HH:mm:ss",
				"yyyy-MM-dd HH:mm",
				"yyyy/MM/dd HH:mm:ss",
				"yyyy/MM/dd HH:mm");
			nowStr = DateUtil.format(currentDate, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			System.out.println("测试时间格式错误，请使用 yyyy-MM-dd HH:mm:ss，例如 2026-03-11 23:58:00");
			return;
		}

		String[] range = miningLimit.split("-");
		if (range.length != 2) {
			System.out.println("时间段格式错误，请使用 HH:mm-HH:mm，例如 23:55-01:00");
			return;
		}
		try {
			// 规范化时间段字符串，确保可格式化输出
			LocalTime start = LocalTime.parse(range[0], DateTimeFormatter.ofPattern("HH:mm"));
			LocalTime end = LocalTime.parse(range[1], DateTimeFormatter.ofPattern("HH:mm"));
			miningLimit = start.format(DateTimeFormatter.ofPattern("HH:mm")) + "-"
				+ end.format(DateTimeFormatter.ofPattern("HH:mm"));
		} catch (Exception e) {
			System.out.println("时间段格式错误，请使用 HH:mm-HH:mm，例如 23:55-01:00");
			return;
		}

		boolean inSettleRange = isInTimeRange(currentDate, range[0], range[1]);

		// main 只做时间段判断测试，不依赖数据库任务表
		ResultPista<?> result = inSettleRange
			? ResultPista.fail(ResponseCode.CODE_1024.getCode(),
			MessageFormat.format(ResponseCode.CODE_1024.getMsg(), miningLimit))
			: ResultPista.success();

		System.out.println("测试时间: " + nowStr);
		System.out.println("结算时间段: " + miningLimit);
		System.out.println("是否在结算段内: " + inSettleRange);
		System.out.println("返回结果: " + result);
	}
	/**
	 * 系统结算时间，不允许交易
	 * @return
	 */
	@Override
	public ResultPista checkMineSettleTime() {
		DateTime currentDate = DateUtil.date();
		String miningLimit = sysParaServiceImpl.getValue(SysConstant.MINE_TRADE_TIME_LIMIT);
		String[] strs = miningLimit.split("-");
		Date star1Date = DateUtil.parseTimeToday(strs[0]);
		Date end1Date = DateUtil.parseTimeToday(strs[1]);
		log.info("starDate:{} ,endDate:{}", star1Date, end1Date);
		boolean res = isInTimeRange(currentDate, strs[0], strs[1]);
		if (res) {
			//再时间段内，如果结算处理完，就成功，否则就是无法交易
			Map<String, Object> param = new HashMap<>(4);
			param.put("type", SysConstant.TSK_TYPE_101);
			param.put("date", DateUtil.format(DateUtil.date(), "yyyyMMdd"));
			Map<String, Object> task = asyncTaskMapper.getTask(param);
			if (task != null) {
				//可以继续交易，否则的话
				return ResultPista.success();
			}
			return ResultPista.fail(ResponseCode.CODE_1024.getCode(), MessageFormat.format(ResponseCode.CODE_1024.getMsg(), miningLimit));
		}
		return ResultPista.success();
	}


}
