package com.xms.web.task;

import com.xms.common.utils.StringUtils;
import com.xms.web.service.IAsyncTaskService;
import com.xms.web.service.IAsyncUserUpgradeService;
import com.xms.web.service.ScheduleTaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时调度任务处理
 *
 * @author: renegadePISTA
 * @createDate: 2023/9/9
 */
@Slf4j
@Component("xmsTask")
@AllArgsConstructor
public class XmsTask {
	private final IAsyncTaskService asyncTaskServiceImpl;
	private final IAsyncUserUpgradeService asyncUserUpgradeServiceImpl;
	private final ScheduleTaskService scheduleTaskServiceImpl;
	@Deprecated
	public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
		System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
	}

	@Deprecated
	public void ryParams(String params) {
		System.out.println("执行有参方法：" + params);
	}

	@Deprecated
	public void ryNoParams() {
		System.out.println("执行无参方法");
	}


	/**
	 * 清楚XX天前的日志
	 */
	public void dealSysLogs(Integer days) throws Exception {
		log.info("清除 {} 天前的日志", days);
		asyncTaskServiceImpl.dealSysLogs(days);
	}

	/**
	 * 处理消费者阻塞进入死信队列的消息
	 */
	public void dealRedisDeadMsg() throws Exception {
		log.info("处理消费者阻塞进入死信队列的消息");
		asyncTaskServiceImpl.dealRedisDeadMsg();
	}

	/**
	 * 处理事务消息阻塞的
	 */
	public void taskMsgCycle() throws Exception {
		log.info("处理事务消息阻塞的的消息");
		asyncTaskServiceImpl.taskMsgCycle();
	}


	/**
	 * 任务类型100 认购节点订单每日天数减1,每减少30天释放一次奖励
	 *
	 */
	public void handelNodePlanOrder() {
		log.info("任务类型100 认购节点订单每日天数减1,每减少30天释放一次奖励");
		asyncTaskServiceImpl.handelNodePlanOrder();
	}

	/**
	 * 任务类型101 处理质押订单收益
	 */
	public void handelStakeOrder() {
		log.info("任务类型101 处理质押订单收益");
		asyncTaskServiceImpl.handelStakeOrder();
	}

	/**
	 * 任务类型102 每日释放保险仓利润
	 */
	public void handelStakeOrder102() {
		log.info("任务类型102 每日释放保险仓利润");
		asyncTaskServiceImpl.handelStakeOrder102();
	}

	/**
	 * 任务类型103 每日计算日质押的日利率
	 */
	public void handelStakeOrder103() {
		log.info("任务类型103 每日计算日质押的日利率");
		asyncTaskServiceImpl.handelStakeOrder103();
	}

	/**
	 * 任务类型104 财富仓收益解锁
	 */
	public void handelStakeOrder104() {
		log.info("任务类型104 财富仓收益解锁");
		asyncTaskServiceImpl.handelStakeOrder104();
	}



	/**
	 * handelStakeOrder105 补偿任务
	 */
	public void handelStakeOrder105() {
		log.info("handelStakeOrder105 补偿任务");
		asyncTaskServiceImpl.handelStakeOrder105();
	}

	/**
	 * 任务类型106 H赠送释放每日释放
	 */
	public void handelHGiftRelease106() {
		log.info("任务类型106 H赠送释放每日释放");
		asyncTaskServiceImpl.handelHGiftRelease106();
	}

	/**
	 * 补偿任务 重新计算用户等级
	 */
	public void handelHGiftRelease() {
		log.info("补偿任务 重新计算用户等级");
		asyncTaskServiceImpl.handelHGiftRelease();
	}

//	/**
//	 * 任务类型103 每日统计平台币价格
//	 *
//	 */
//	public void dailyPlatformCoinPriceRecord103 () {
//		log.info("任务类型103 每日统计平台币价格");
//		asyncTaskServiceImpl.dailyPlatformCoinPriceRecord103();
//	}



//	/**
//	 * 寻找遗漏处理增加团队的业绩矿机订单.
//	 */
//	public void task103Handler() {
//		// 寻找遗漏处理增加团队的业绩矿机订单
//		asyncTaskServiceImpl.task103Handler();
//	}

	/**
	 * 补偿基金订单赎回本期的时候.t+1时间到了但是还没有执行发放本金任务  0/20 * * * * ?
	 */
//	public void compensateUnpaidPrincipalOrders() {
//		// 补偿基金订单赎回本期的时候.t+1时间到了但是还没有执行发放本金任务
//		log.info("补偿基金订单赎回本期的时候.t+1时间到了但是还没有执行发放本金任务");
//		asyncTaskServiceImpl.compensateUnpaidPrincipalOrders();
//	}




//	/**
//	 * 补偿任务
//	 *
//	 */
//	public void task102Handler() {
//		asyncTaskServiceImpl.task102Handler();
//	}

//
//	/**
//	 * 任务类型103 复利手续费分红
//	 * 20%平台沉淀
//	 * 20%合伙人平均分
//	 * 30%级别加权分红（同级别就平均分）
//	 * 30%给积分≥30分的用户加权分红（分数可配置）
//	 *
//	 */
//	public void task103Handler() {
//		// 任务类型103 复利手续费分红
//		log.info("任务类型103 复利手续费分红");
//		asyncTaskServiceImpl.task103Handler();
//	}
//
//	/**
//	 * 任务类型104 卖币手续费分红
//	 * 40%合伙人平均分
//	 * 60%留存（每一笔要有留存记录，并且后台需要进行汇总统计--方便2.0链上处理）
//	 *
//	 */
//	public void task104Handler() {
//		// 任务类型104 卖币手续费分红
//		log.info("任务类型104 卖币手续费分红");
//		asyncTaskServiceImpl.task104Handler();
//	}
//
//	public void task105Handler() {
//		asyncTaskServiceImpl.task105Handler();
//	}
}
