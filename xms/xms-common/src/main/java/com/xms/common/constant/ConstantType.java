package com.xms.common.constant;

/**
 *
  * @ClassName: ConstantType
  * @Description: 常量类
  *
  * @date 2023年5月23日 下午3:26:14
  *
 */
public class ConstantType {

	public static String DYNAMIC_USER_RECHARGE_TYPE = "t_recharge_coin_type";

	//开关 1-否 2-是
	public class open_or_close{
		public static final int type_1 = 1;
		public static final int type_2 = 2;
	}

	//状态(1.正常 2.冻结)
	public class user_info_status{
		public static final int type_1 = 1;
		public static final int type_2 = 2;
	}

	//等级(0.无 1.V1 2.V2 3.V3 4.V4 5.V5 6.V6)
	public class user_info_game_level{
		public static final int type_0 = 0;
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
		public static final int type_4 = 4;
		public static final int type_5 = 5;
		public static final int type_6 = 6;
	}

	//奖励等级(1.V1 2.V2 3.V3 4.V4 5.V5 6.V6)
	public class user_level_reward_level{
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
		public static final int type_4 = 4;
		public static final int type_5 = 5;
		public static final int type_6 = 6;
	}

	//币种1:节点收益,2:静态,3:动态,4:财富,5:H代币(保险/手续费),6:工作室收益,7:贡献分,8:今日动态余额
	public class user_money_coin_type {
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
		public static final int type_4 = 4;
		public static final int type_5 = 5;
		public static final int type_6 = 6;
		public static final int type_7 = 7;
		public static final int type_8 = 8;
		public static final int type_9 = 9;
	}

	//质押/入金订单来源 1:正常ACP入金,3:旧系统H换ACP入金
	public class stake_order_deposit_source_type {
		public static final int type_1 = 1;
		public static final int type_3 = 3;
	}

	/**
	 * 1:动态提现,2
	 */
	public class user_wealth_vault_flow_source_type{
		public static final int type_1 = 1;
	}
	/**
	 *
	 * 1:节点质押释放,2:质押静态收益,3:极差奖,4:平级奖,5:层级奖,6:工作室收益
	 * 7:节点权益分红,8:奖励池新增奖励,9:保险仓赔付,10:财富仓释放,11:H赠送释放
	 * --------------------------------------------
	 */
	public class xms_reward_record_source_type{
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
		public static final int type_4 = 4;
		public static final int type_5 = 5;
		public static final int type_6 = 6;
		public static final int type_7 = 7;
		public static final int type_8 = 8;
		public static final int type_9 = 9;
		public static final int type_10 = 10;
		public static final int type_11 = 11;
		public static final int type_12 = 12;
		public static final int type_13 = 13;
		public static final int type_14 = 14;
		public static final int type_15 = 15;
		public static final int type_16 = 16;
		public static final int type_17 = 17;
		public static final int type_18 = 18;
		public static final int type_19 = 19;
		public static final int type_20 = 20;
		public static final int type_21 = 21;
		public static final int type_22 = 22;
		public static final int type_23 = 23;
		public static final int type_24 = 24;
		public static final int type_25 = 25;
		public static final int type_26 = 26;
		/** 保险仓赔付 */
		public static final int type_27 = 27;
	}

	/**
	 * 奖金业务类型 1.购买卡包,2:直推奖励(算力),3:间推奖励(算力),4:升级补算力,5:购买卡包赠送,6:升级卡包赠送
	 *
	 * 1.静态收益,2.动态收益,3.团队奖励,4:利息记录(利息包释放的记录),5:提现手续费分红,6:直推收益奖励
	 */
	public class xms_reward_record_business_type{
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
		public static final int type_4 = 4;
		public static final int type_5 = 5;
		public static final int type_6 = 6;
		public static final int type_7 = 7;
	}

	/**
	 * 奖金币种类型 1:算力,2:usdt,3:BDAI
	 */
	public class reward_record_coin_type{
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
	}

	//
	/**
	 * 1.充值,2:节点质押释放,3:质押静态收益,4:极差奖,5:平级奖,6:层级奖,7:工作室收益
	 * 8:节点权益分红,9:奖励池新增奖励,10:购买贡献分,11:手续费分红,12:爆仓扣除
	 * 13:保险仓赔付,14:财富仓释放,15:爆仓释放财富仓,16:每日烧伤,17:划转扣除,18:划转增加,19:提现,
	 * 20:提现退回
	 * 28:平台拨扣,
	 */
	public class user_money_log_source_type{
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
		public static final int type_4 = 4;
		public static final int type_5 = 5;
		public static final int type_6 = 6;
		public static final int type_7 = 7;
		public static final int type_8 = 8;
		public static final int type_9 = 9;
		public static final int type_10 = 10;
		public static final int type_11 = 11;
		public static final int type_12 = 12;
		public static final int type_13 = 13;
		public static final int type_14 = 14;
		public static final int type_15 = 15;
		public static final int type_16 = 16;
		public static final int type_17 = 17;
		public static final int type_18 = 18;
		public static final int type_19 = 19;

		public static final int type_20 = 20;
		public static final int type_21 = 21;
		public static final int type_22 = 22;
		public static final int type_23 = 23;
		public static final int type_24 = 24;
		public static final int type_25 = 25;
		public static final int type_26 = 26;
		public static final int type_27 = 27;
		public static final int type_28 = 28;
		public static final int type_29 = 29;
		public static final int type_30 = 30;
		public static final int type_32 = 32;
		/** H赠送释放 */
		public static final int type_33 = 33;
		public static final int type_44 = 44;
	}

	//状态(0.待审核,1.审核成功,2.审核驳回,3.提现成功,4.打款失败)
	public class withdrawal_status{
		public static final int type_0 = 0;
		public static final int type_1 = 1;
		public static final int type_2 = 2;
		public static final int type_3 = 3;
		public static final int type_4 = 4;
	}
}
