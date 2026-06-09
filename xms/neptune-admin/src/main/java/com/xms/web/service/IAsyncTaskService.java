package com.xms.web.service;

import java.util.Map;

/**
 * @author: renengadePISTA
 * @createDate: 2023/9/18
 */
public interface IAsyncTaskService {
	void dealRedisDeadMsg();

	Map<String, Object> getTask(String type, String date);

	int addTask(String type, String date);

	Boolean  transactionExcute(String customerNo, String transactionId);

	void dealSysLogs(Integer days);

	void taskMsgCycle();


		/**
	 * 补偿基金订单赎回本期的时候.t+1时间到了但是还没有执行发放本金任务
	 */
	void compensateUnpaidPrincipalOrders();

	/**
	 * 任务类型103 每日统计平台币价格
	 */
	void dailyPlatformCoinPriceRecord103();


	/**
	 * 任务类型102 v9节点均分提现手续费分红任务
	 */
	//void distributePtbInterest102(Integer parDate);

	/**
	 * 查询没有处理的节点订单
	 * 描述:查询没有处理的节点订单
	 */
	void processOverdueDestroyOrders();

	/**
	 * 寻找遗漏处理增加团队的业绩矿机订单
	 */
	void task103Handler();

	/**
	 * 补偿任务
	 */
	void task102Handler();

	/**
	 * 定时拉取ido订单处理
	 */
	void getIdoOrder();

	/**
	 * 任务类型100 认购节点订单每日天数减1,每减少30天释放一次奖励
	 */
	void handelNodePlanOrder();

	/**
	 * 任务类型101 处理质押订单收益
	 */
	void handelStakeOrder();

	/**
	 * 任务类型102 每日释放保险仓利润
	 */
	void handelStakeOrder102();

	/**
	 * 任务类型103 每日计算日质押的日利率
	 */
	void handelStakeOrder103();

	/**
	 * 任务类型104 财富仓收益解锁
	 */
	void handelStakeOrder104();

	/**
	 * handelStakeOrder105 补偿任务
	 */
	void handelStakeOrder105();

	/**
	 * 任务类型106 H赠送释放每日释放
	 */
	void handelHGiftRelease106();
}
