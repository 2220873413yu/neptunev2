
-- 之前设置的虚拟层等级、团队等级、节点等级为空
update t_user_info
set min_node_level = 0,
    min_layer_level = 0,
    min_game_level = 0


-- 用户购买节点身份 只有身份不释放(关闭定时任务就好了 xmsTask.handelNodePlanOrder)



ALTER TABLE t_user_info
  ADD COLUMN old_performance decimal(20,6) NOT NULL DEFAULT '0.000000' COMMENT '旧系统个人业绩' AFTER performance,
  ADD COLUMN old_history_performance decimal(20,6) NOT NULL DEFAULT '0.000000' COMMENT '旧系统历史业绩' AFTER history_performance,
  ADD COLUMN old_sub_performance decimal(20,6) NOT NULL DEFAULT '0.000000' COMMENT '旧系统直推业绩' AFTER sub_performance,
  ADD COLUMN old_umbrella_performance decimal(20,6) NOT NULL DEFAULT '0.000000' COMMENT '旧系统团队业绩' AFTER umbrella_performance;

UPDATE t_user_info
SET
    old_performance = IFNULL(performance, 0),
    old_history_performance = IFNULL(history_performance, 0),
    old_sub_performance = IFNULL(sub_performance, 0),
    old_umbrella_performance = IFNULL(umbrella_performance, 0),
    performance = 0,
    history_performance = 0,
    sub_performance = 0,
    umbrella_performance = 0,
    update_time = NOW();




-- 初始化数据
TRUNCATE TABLE sys_logininfor;
TRUNCATE TABLE sys_job_log;

TRUNCATE TABLE xms_reward_record;
TRUNCATE TABLE t_withdrawal;
TRUNCATE TABLE t_user_wealth_vault_flow;


-- 持仓流水
TRUNCATE TABLE t_user_stake_position;
TRUNCATE TABLE t_user_money_snapshot;
TRUNCATE TABLE t_user_money_log;
TRUNCATE TABLE t_user_stake_position_flow;


-- 需要初始化一下质押轮次的数据
TRUNCATE TABLE t_stake_order;
TRUNCATE TABLE t_stake_round;
TRUNCATE TABLE t_buy_h_order;


TRUNCATE TABLE t_stake_daily_snapshot;
TRUNCATE TABLE t_swap_order;




update t_user_wealth_vault
set seg1_amount = 0,
    seg2_amount = 0,
    seg3_amount = 0,
    seg4_amount = 0,
    seg5_amount = 0,
    seg6_amount = 0,
    seg7_amount = 0
