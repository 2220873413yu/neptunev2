package com.xms.common.constant;

/**
 *
  * @ClassName: ConstantSys
  * @Description: 系统参数
  *
  * @date 2023年5月23日 下午3:26:14
  *
 */
public class ConstantSys {


	//BSC/USDT 提现手续费率 例如:写3 就是3%
	public static final String biz_withdrawal_valid_num2_usdt_fee_ratio = "biz_withdrawal_valid_num2_usdt_fee_ratio";

	//USDT 跨链分发订单有效阈值 例如:100 只有订单金额大于100才算(废弃)
	public static final String biz_order_valid_threshold = "biz_order_valid_threshold";

	//跨链分发订单最大划转额度  例如: 500 最大是500(废弃)
	public static final String biz_t_wallet_transfer_order_max_limit = "biz_t_wallet_transfer_order_max_limit";

	//跨链分发订单每日最大次数(废弃)
	public static final String biz_today_max_transfer_count = "biz_today_max_transfer_count";

	//奖励发放延迟时间 例如:18000 (单位:秒，即5小时后用户才能获得奖励)(废弃)
	public static final String biz_reward_release_delay_seconds = "biz_reward_release_delay_seconds";


	//USDT闪兑平台币手续费率 例如:写1 手续费就是1%
	public static final String biz_swap_usdt_to_p_fee_ratio = "biz_swap_usdt_to_p_fee_ratio";

	//平台币闪兑USDT手续费率 例如:写1 手续费就是1%
	public static final String biz_swap_p_to_usdt_fee_ratio = "biz_swap_p_to_usdt_fee_ratio";

	//平台币价格 默认为1.2
	public static final String biz_p_price = "biz_p_price";


	//基金订单扣除利息自动发放，扣除收益部分的10%手续费 例如: 写10就是10%
	public static final String biz_mining_order_reward_fee_ratio = "biz_mining_order_reward_fee_ratio";

	//基金订单利息自动发放usdt比例 例如:80 就是给80%
	public static final String biz_mining_order_reward_usdt_ratio = "biz_mining_order_reward_usdt_ratio";

	//提现免审核额度 例如:写100 100以下自动审核
	public static final String biz_withdrawal_auto_approve_limit = "biz_withdrawal_auto_approve_limit";


	//提现自动审核每日次数 例如:写3 每日有3次机会
	public static final String biz_withdrawal_auto_approve_daily_count = "biz_withdrawal_auto_approve_daily_count";

	//图片域名
	public static final String biz_image_domain = "biz_image_domain";

	//USDT兑换成SMA开关 例如:1开,其他值是关
	public static final String biz_usdt_to_sgm_enable = "biz_usdt_to_sgm_enable";

	//活期基金订单赎回到账时间 86400秒后到期对应1天
	public static final String biz_order0_arrival_time = "biz_order0_arrival_time";

	//用户降级保护期间 默认3天(259200秒)
	public static final String biz_user_arrival_time = "biz_user_arrival_time";

	//USDT转账开关(1:开,其他值关) 例如: 填1就是打开转账功能
	public static final String biz_transfer_usdt_enable = "biz_transfer_usdt_enable";

	//SMA转账开关(1:开,其他值关) 例如: 填1就是打开转账功能
	public static final String biz_transfer_sgm_enable = "biz_transfer_sgm_enable";

	//转账功能 USDT最少转账限制 例如:填写15,默认最低15起转
	public static final String biz_usdt_transfer_min_limit = "biz_usdt_transfer_min_limit";

	//转账功能 SMA最少转账限制 例如:填写15,默认最低15起转
	public static final String biz_sgm_transfer_min_limit = "biz_sgm_transfer_min_limit";

	//转账功能 USDT转账手续费率 例如:填写5 就是5%
	public static final String biz_usdt_transfer_fee_ratio = "biz_usdt_transfer_fee_ratio";

	//转账功能 SMA转账手续费率 例如:填写5 就是5%
	public static final String biz_sgm_transfer_fee_ratio = "biz_sgm_transfer_fee_ratio";



	//---------------------->
	//仅支持输入整数。例如填写 100，表示至少需满足 100U 才可发起销毁
	public static final String biz_min_destroy_qty = "biz_min_destroy_qty";




	// 销毁订单收益直推奖比例 例如:写10就是10%
	public static final String biz_destroy_order_referral_reward_ratio = "biz_destroy_order_referral_reward_ratio";

	//超越奖比例 例如:填写1 就是1%
	public static final String biz_exceed_award_ratio = "biz_exceed_award_ratio";

	//例如填写 2 表示累计产出达到 2 倍原销毁量后减产出局
	public static final String biz_release_limit_multiple = "biz_release_limit_multiple";

	//提币手续费中，用于 V9 分红奖励 的比例 例如：5 表示 5%
	public static final String biz_withdrawal_fee_v9_reward_ratio = "biz_withdrawal_fee_v9_reward_ratio";

	//提币手续费中，用于 平台项目方收益 的比例 例如：5 表示 5%
	public static final String biz_withdrawal_fee_platform_income_ratio = "biz_withdrawal_fee_platform_income_ratio";

	//单笔订单金额达到这个数，才能算有效用户。 例如:100U 就是销毁的时候大于等于100U才算有效用户
	public static final String biz_valid_user_order_min_amount = "biz_valid_user_order_min_amount";

	//手续费分红 平台收益（分发到项目方钱包）会分发到该地址
	public static final String biz_sk_address = "biz_sk_address";


	// 新增


	//直推奖励 算力奖励比例 例如:10表示10%
	public static final String biz_direct_computing_power_ratio = "biz_direct_computing_power_ratio";



	//间推奖励 算力奖励比例 例如:5表示5%
	public static final String biz_indirect_computing_power_ratio = "biz_indirect_computing_power_ratio";

	//系统运行天数
	public static final String biz_sys_run_days = "biz_sys_run_days";

	//系统运行天数开关 1:开,其他值关
	public static final String biz_sys_enabled = "biz_sys_enabled";

	//时间加成系数 默认为:1.01
	public static final String biz_sys_variable = "biz_sys_variable";



	//---------------------->使用
	//XLS提现开关(1.关 2.开)
	public static final String biz_withdrawal_open_or_close = "biz_withdrawal_open_or_close";

	//XLS 提现最小数量
	public static final String biz_withdrawal_usdt_min = "biz_withdrawal_usdt_min";

	//XLS 提现手续费率 例如:写3 就是3%
	public static final String biz_withdrawal_valid_num1_usdt_fee_ratio = "biz_withdrawal_valid_num1_usdt_fee_ratio";

	//新加的

	/**
	 * 激活一次赠送的领取空投次数 例如:15 激活一次赠送15次
	 */
	public static final String biz_send_activation_count = "biz_send_activation_count";

	/**
	 * 激活一次需要支付的金额（与空投赠送次数的规则配套）
	 */
	public static final String biz_activation_cost = "biz_activation_cost";

	//直推奖励 u奖励比例 例如:10表示10%
	public static final String biz_direct_xls_ratio = "biz_direct_xls_ratio";

	//间推奖励 u奖励比例 例如:10表示10%
	public static final String biz_indirect_xls_ratio = "biz_indirect_xls_ratio";

	//swap订单额度生效时间 默认86400秒 约等于1天
	public static final String biz_swap_order_expire_time = "biz_swap_order_expire_time";

	//单个用户每日自动免审额度上限 例如：100 表示单个用户每天最多可自动免审 100 XLS。
	public static final String biz_daily_auto_audit_free_amount = "biz_daily_auto_audit_free_amount";

	//单个用户每日自动免审次数上限 例如：5 表示单个用户每天最多可自动免审 5 次。
	public static final String biz_daily_auto_audit_free_count = "biz_daily_auto_audit_free_count";



	//swap订单生效额度比例 例如：100 填写100 表示swap订单生效额度为swap订单额度的100%
	public static final String biz_swap_order_effective_ratio = "biz_swap_order_effective_ratio";

	//新增的

	//节点订单超时时间 默认5分钟 单位为分钟
	public static final String biz_lock_expire_at = "biz_lock_expire_at";

	//平台手续费分红 例如20就是20%
	public static final String biz_fee_dividend_rate = "biz_fee_dividend_rate";

	//商城利润分红 例如10就是10%
	public static final String biz_mall_profit_dividend_rate = "biz_mall_profit_dividend_rate";


	//新添加的------------------------------------------------------>
	//节点补差价升级开关 0:关,1其他值开
	public static final String biz_node_plan_open_or_close = "biz_node_plan_open_or_close";

	//全网静态收益 例如1,就是1%
	public static final String biz_global_static_income_ratio = "biz_global_static_income_ratio";

	//质押最低投资金额 例如写1000,低于1000的h的质押金额不算有效订单
	public static final String biz_min_stake_amount = "biz_min_stake_amount";

	//ACP单价U，临时从系统参数读取，后续可替换为真实价格来源
	public static final String biz_acp_price_usdt = "biz_acp_price_usdt";

	//H单价U，临时从系统参数读取，后续可替换为真实价格来源
	public static final String biz_h_price_usdt = "biz_h_price_usdt";

	//ACP入金赠送H比例，例如30表示赠送入金U价值的30%
	public static final String biz_acp_h_gift_ratio = "biz_acp_h_gift_ratio";

	//工作室补贴资格最小小区业绩（单位H），例如100000表示小区业绩达到10万H后可获得工作室补贴角色
	public static final String biz_studio_subsidy_min_umbrella_performance = "biz_studio_subsidy_min_umbrella_performance";

	//质押新增奖励池比例，例如 10 表示每日全网静态收益总额的 10% 作为新增奖励池
	public static final String biz_stake_new_reward_pool_ratio = "biz_stake_new_reward_pool_ratio";

	//H代币购买积分兑换比例，例如 6 表示 1H可兑换6积分
	public static final String biz_h_token_buy_points_ratio = "biz_h_token_buy_points_ratio";

	//手续费分红地址 分配手续费总共的85%
	public static final String biz_withdrawal_fee_collect_address = "biz_withdrawal_fee_collect_address";

	//手续费分红地址1 分配手续费总共的15%
	public static final String biz_withdrawal_fee_collect_address1 = "biz_withdrawal_fee_collect_address1";

}
