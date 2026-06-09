package com.xms.app.service;

import com.github.pagehelper.PageInfo;
import com.xms.app.entity.PtbPriceDto;
import com.xms.app.entity.dto.RewardRecordDto;
import com.xms.app.entity.dto.TodayIncomeDto;
import com.xms.app.entity.resp.IncomeInfoResp;
import com.xms.app.entity.resp.IncomeOverviewResp;
import com.xms.app.entity.vo.UserMoneySwapVo;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.dao.domain.FlashExchangeRecord;
import com.xms.dao.entity.bo.RewardRecordBo;
import com.xms.dao.entity.bo.UserMoneyBo;
import com.xms.dao.entity.bo.UserMoneyLogBo;
import com.xms.dao.entity.domain.UserMoneyLog;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface BizUserMoneyService {

	/**
	 * 查询用户钱包
	 * @param userId
	 * @return
	 */
	UserMoneyBo getUserMoney(Long userId);

	/**
	 * 闪兑
	 * @param req
	 * @param userId
	 */
    void swap(UserMoneySwapVo req, Long userId);

	/**
	 * 查询平台币最近7天价格
	 * @return
	 */
	ResultPista<List<PtbPriceDto>> queryPtbLast7Price();

	/**
	 * 闪兑记录
	 * @param lastId
	 * @return
	 */
	ResultPista<List<FlashExchangeRecord>> swapRecord(Long lastId);

	/**
	 * 今日收益
	 * @param userId
	 * @return
	 */
	TodayIncomeDto todayIncome(Long userId);

	/**
	 * 奖金记录
	 * @param lastId
	 * @param sourceTypes
	 * @return
	 */
	List<RewardRecordDto> rewardRecord(Long lastId, List<Integer> sourceTypes);

	/**
	 * 收益卡片/类型分布切换
	 * 一次性返回收益卡片 + 类型分布（今日/本周/本月/总）
	 */
	IncomeOverviewResp incomeOverview();

	/**
	 * 奖金记录(收益记录)
	 * @param pageIndex 当前页 默认1
	 * @param pageSize 每页长度 默认20(最大20)
	 * @param bizType 业务类型 -1:查询全部,1:静态收益,2:动态收益,3:团队奖励
	 * @param dateType 时间类型-1:查询全部,1:今日,2:本周,3:本月
	 * @return
	 */
	PageInfo<RewardRecordBo> rewardList(Integer pageIndex, Integer pageSize, Long userId, Integer bizType,
										Integer dateType);

	/**
	 * 资产划转
	 * @param userId
	 * @return
	 */
	BigDecimal transferBalance(Long userId);
}
