
-- 初始化之前的财富数据(多少用户就要加多条少记录)
INSERT INTO t_user_wealth_vault (id)
SELECT ui.user_id
FROM t_user_info ui
         LEFT JOIN t_user_wealth_vault uwv ON uwv.id = ui.user_id
WHERE uwv.id IS NULL;



-- h代币价格需要改为真实获取交易所价格(完成)
-- 需要把定时任务改一下(完成)

-- 需要额外处理节点年华收益问题。之前的订单默认20%之前的节点配置默认是20%

UPDATE `t_node_plan`
SET `annual_rate` = 20;

UPDATE `t_node_plan_order`
SET `annual_rate` = 20;

-- 需要把测试环境的数据导入过来 菜单、字典、角色权限
-- 手动刷新缓存
TRUNCATE TABLE t_sys_para;
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (1, 'biz_node_plan_open_or_close', '1', '节点补差价升级开关 0:关,1其他值开', '0', '2026-03-03 11:32:55', '2026-03-19 20:07:42', 1, '节点补差价升级开关 0:关,1其他值开');
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (2, 'biz_global_static_income_ratio', '15', '节点分红拿全网静态收益 例如1,就是1%', '0', '2026-03-03 15:55:58', '2026-03-19 10:40:38', 1, NULL);
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (3, 'biz_min_stake_amount', '1000', '质押最低投资金额 例如写1000,低于1000的h的质押金额不算有效订单', '0', '2026-03-06 14:14:17', NULL, 1, NULL);
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (4, 'biz_studio_subsidy_min_umbrella_performance', '100000', '工作室补贴资格最小小区业绩（单位H），例如100000表示小区业绩达到10万H后可获得工作室补贴角色', '0', '2026-03-06 20:23:33', '2026-03-19 10:42:41', 1, NULL);
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (5, 'biz_stake_new_reward_pool_ratio', '10', '质押新增奖励池比例，例如 10 表示每日全网静态收益总额的 10% 作为新增奖励池', '0', '2026-03-10 19:20:04', NULL, 1, NULL);
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (6, 'biz_h_token_buy_points_ratio', '6', 'H代币购买积分兑换比例，例如 6 表示 1H可兑换6积分', '0', '2026-03-10 20:11:07', '2026-03-17 20:20:36', 1, NULL);
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (7, 'biz_withdrawal_fee_collect_address', '0x62f23B9ae62f8004794EE15B82371c9605bBC121', '手续费分红地址', '0', '2026-03-10 21:30:54', NULL, 1, NULL);
INSERT INTO `neptune`.`t_sys_para` (`sys_para_id`, `para_code`, `para_value`, `para_desc`, `visible`, `create_time`, `update_time`, `active_flag`, `remark`) VALUES (9, 'mine_trade_time_limit', '23:59-00:30', '系统每日结算动态+静态时间,这个时间内不允许交易', '0', '2023-09-23 11:10:15', '2025-11-29 23:56:24', 1, '1');


TRUNCATE TABLE t_stake_round;
INSERT INTO `neptune`.`t_stake_round` (`id`, `status`, `biz_status`, `start_time`, `liquidation_time`, `end_time`, `player_stake_total`, `studio_subsidy_total`, `withdraw_reward_total_full`, `buy_point_total`, `remark`, `create_time`, `update_time`, `insurance_balance`) VALUES (1, 0, 0, '2026-03-28 14:00:00', NULL, NULL, 0.000000, 0.000000, 0.000000, 0.000000, NULL, '2026-03-28 14:00:00', NULL, 0.000000);

TRUNCATE TABLE t_user_level_config;
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (14, 0, 0.00, 0.00, 0, 0.000000, 0.000000, '2025-12-04 10:04:05', NULL, NULL, 0, NULL, NULL, 10.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (15, 1, 0.00, 100000.00, 1, 8.000000, 0.000000, '2025-12-04 10:04:09', '2026-03-19 10:22:28', NULL, 0, NULL, NULL, 500.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (16, 2, 0.00, 500000.00, 1, 16.000000, 0.000000, '2025-12-04 10:04:11', '2026-03-19 10:22:04', NULL, 0, NULL, NULL, 2500.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (17, 3, 0.00, 1500000.00, 1, 24.000000, 0.000000, '2025-12-04 10:04:14', '2026-03-19 10:21:30', NULL, 0, NULL, NULL, 7500.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (18, 4, 0.00, 3000000.00, 1, 32.000000, 10.000000, '2025-12-04 10:04:18', '2026-03-19 10:20:55', NULL, 0, NULL, NULL, 15000.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (19, 5, 0.00, 6000000.00, 1, 40.000000, 10.000000, '2025-12-04 10:04:20', '2026-03-19 10:20:31', NULL, 0, NULL, NULL, 30000.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (20, 6, 0.00, 10000000.00, 1, 45.000000, 10.000000, '2025-12-04 10:04:24', '2026-03-19 10:19:21', NULL, 0, NULL, NULL, 50000.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (21, 7, 0.00, 20000000.00, 1, 50.000000, 10.000000, '2025-12-04 10:04:27', '2026-03-19 10:18:38', NULL, 0, NULL, NULL, 100000.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (25, 8, 0.00, 40000000.00, 1, 55.000000, 10.000000, '2026-03-05 11:45:22', '2026-03-19 10:18:50', NULL, 0, NULL, NULL, 200000.000000);
INSERT INTO `neptune`.`t_user_level_config` (`id`, `level`, `performance`, `umbrella_performance`, `has_studio_subsidy`, `reward_ratio`, `peer_reward_ratio`, `create_time`, `update_time`, `remark`, `deleted`, `create_by`, `update_by`, `min_buy_amount`) VALUES (26, 9, 0.00, 80000000.00, 1, 60.000000, 10.000000, '2026-03-05 11:45:31', '2026-03-19 11:19:24', NULL, 0, NULL, NULL, 400000.000000);

TRUNCATE TABLE t_user_invest_layer_config;
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (1, 0, 0.00000000, 0.000000, 0, '0层', '2026-03-05 11:42:08', NULL);
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (2, 1, 3000.00000000, 7.000000, 1, '1层', '2026-03-05 11:42:17', '2026-03-19 10:23:26');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (3, 2, 6000.00000000, 7.000000, 2, '2层', '2026-03-05 11:42:22', '2026-03-13 22:27:47');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (4, 3, 15000.00000000, 7.000000, 3, '3层', '2026-03-05 11:42:29', '2026-03-13 22:27:44');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (5, 4, 30000.00000000, 7.000000, 4, '4层', '2026-03-05 11:42:33', '2026-03-13 22:27:39');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (6, 5, 60000.00000000, 7.000000, 5, '5层', '2026-03-05 11:42:40', '2026-03-17 19:52:19');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (7, 6, 150000.00000000, 7.000000, 6, '6层', '2026-03-05 11:42:49', '2026-03-17 19:52:14');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (8, 7, 300000.00000000, 7.000000, 7, '7层', '2026-03-05 11:42:56', '2026-03-13 22:27:33');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (9, 8, 600000.00000000, 7.000000, 8, '8层', '2026-03-05 11:43:01', '2026-03-13 22:27:29');
INSERT INTO `neptune`.`t_user_invest_layer_config` (`id`, `level`, `min_invest`, `reward_ratio`, `layer_count`, `remark`, `create_time`, `update_time`) VALUES (10, 9, 1500000.00000000, 7.000000, 9, '9层', '2026-03-05 11:43:06', '2026-03-13 22:27:22');

TRUNCATE TABLE t_user_yield_rate_config;
INSERT INTO `neptune`.`t_user_yield_rate_config` (`id`, `initial_daily_rate`, `min_daily_rate`, `max_daily_rate`, `growth_consecutive_days`, `growth_rate_step`, `decay_withdraw_threshold_ratio`, `decay_rate_step`, `remark`, `create_time`, `update_time`, `deleted`, `create_by`, `update_by`) VALUES (1, 1.000000, 0.500000, 1.500000, 20, 0.100000, 10.000000, 0.100000, NULL, '2026-03-06 17:59:31', '2026-03-19 10:25:00', 0, NULL, NULL);

TRUNCATE TABLE t_wealth_vault_stage_config;
INSERT INTO `neptune`.`t_wealth_vault_stage_config` (`id`, `stage_no`, `unlock_price`, `stage_name`, `remark`, `create_time`, `update_time`) VALUES (1, 1, 1.000000, '阶段1财富仓', NULL, '2026-03-11 23:29:59', '2026-03-19 11:01:21');
INSERT INTO `neptune`.`t_wealth_vault_stage_config` (`id`, `stage_no`, `unlock_price`, `stage_name`, `remark`, `create_time`, `update_time`) VALUES (2, 2, 1.500000, '阶段2财富仓', NULL, '2026-03-11 23:30:04', '2026-03-13 23:36:01');
INSERT INTO `neptune`.`t_wealth_vault_stage_config` (`id`, `stage_no`, `unlock_price`, `stage_name`, `remark`, `create_time`, `update_time`) VALUES (3, 3, 2.000000, '阶段3财富仓', NULL, '2026-03-11 23:30:09', '2026-03-13 23:35:56');
INSERT INTO `neptune`.`t_wealth_vault_stage_config` (`id`, `stage_no`, `unlock_price`, `stage_name`, `remark`, `create_time`, `update_time`) VALUES (4, 4, 2.500000, '阶段4财富仓', NULL, '2026-03-11 23:30:13', '2026-03-13 23:35:53');
INSERT INTO `neptune`.`t_wealth_vault_stage_config` (`id`, `stage_no`, `unlock_price`, `stage_name`, `remark`, `create_time`, `update_time`) VALUES (5, 5, 3.000000, '阶段5财富仓', NULL, '2026-03-11 23:30:17', '2026-03-13 23:35:47');

TRUNCATE TABLE t_withdrawal;


TRUNCATE TABLE t_withdrawal_config;
INSERT INTO `neptune`.`t_withdrawal_config` (`id`, `coin_type`, `withdraw_open`, `min_withdraw_amount`, `fee_ratio`, `wealth_vault_ratio`, `insurance_vault_ratio`, `create_time`, `update_time`, `remark`) VALUES (5, 1, 1, 100.00000000, 0.0000, 0.0000, 0.0000, '2026-03-10 20:47:00', '2026-03-19 10:10:33', NULL);
INSERT INTO `neptune`.`t_withdrawal_config` (`id`, `coin_type`, `withdraw_open`, `min_withdraw_amount`, `fee_ratio`, `wealth_vault_ratio`, `insurance_vault_ratio`, `create_time`, `update_time`, `remark`) VALUES (6, 2, 1, 100.00000000, 10.0000, 0.0000, 15.0000, '2026-03-10 20:47:02', '2026-03-19 10:10:19', NULL);
INSERT INTO `neptune`.`t_withdrawal_config` (`id`, `coin_type`, `withdraw_open`, `min_withdraw_amount`, `fee_ratio`, `wealth_vault_ratio`, `insurance_vault_ratio`, `create_time`, `update_time`, `remark`) VALUES (7, 3, 1, 100.00000000, 10.0000, 15.0000, 15.0000, '2026-03-10 20:47:05', '2026-03-19 10:11:03', NULL);
INSERT INTO `neptune`.`t_withdrawal_config` (`id`, `coin_type`, `withdraw_open`, `min_withdraw_amount`, `fee_ratio`, `wealth_vault_ratio`, `insurance_vault_ratio`, `create_time`, `update_time`, `remark`) VALUES (8, 4, 1, 100.00000000, 10.0000, 0.0000, 0.0000, '2026-03-10 20:47:07', '2026-03-18 20:57:20', NULL);
INSERT INTO `neptune`.`t_withdrawal_config` (`id`, `coin_type`, `withdraw_open`, `min_withdraw_amount`, `fee_ratio`, `wealth_vault_ratio`, `insurance_vault_ratio`, `create_time`, `update_time`, `remark`) VALUES (9, 5, 1, 100.00000000, 0.0000, 0.0000, 0.0000, '2026-03-10 20:47:10', '2026-03-19 10:08:50', NULL);
INSERT INTO `neptune`.`t_withdrawal_config` (`id`, `coin_type`, `withdraw_open`, `min_withdraw_amount`, `fee_ratio`, `wealth_vault_ratio`, `insurance_vault_ratio`, `create_time`, `update_time`, `remark`) VALUES (10, 6, 1, 100.00000000, 0.0000, 0.0000, 0.0000, '2026-03-10 20:47:12', '2026-03-19 10:08:45', NULL);


TRUNCATE TABLE sys_job;
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '调度检测MQ事务阻塞消息', 'DEFAULT', 'xmsTask.taskMsgCycle', '0 0/2 * * * ?', '3', '1', '1', 'admin', '2024-05-21 15:48:44', 'admin', '2024-09-04 15:48:11', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '调度检测DLQ队列消息', 'DEFAULT', 'xmsTask.dealRedisDeadMsg', '0 0/30 * * * ?', '3', '1', '0', 'admin', '2023-09-18 15:27:28', 'admin', '2024-09-19 16:40:01', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '清除XX天前的日志', 'DEFAULT', 'xmsTask.dealSysLogs(60)', '0 0 23 1 1/1 ?', '3', '1', '0', 'admin', '2023-09-23 20:07:11', 'admin', '2024-07-18 14:18:25', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (24, '(00:15:00)任务类型100 认购订单每日天数减1,每减少30天释放一次奖励', 'DEFAULT', 'xmsTask.handelNodePlanOrder', '0 15 0 * * ?', '3', '1', '0', 'admin', '2025-11-24 21:50:45', 'admin', '2026-03-20 11:19:27', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (30, '任务类型101 处理质押订单收益', 'DEFAULT', 'xmsTask.handelStakeOrder', '0 0 0 * * ?', '1', '1', '0', 'admin', '2026-03-12 21:31:02', 'admin', '2026-03-19 00:12:14', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (31, '(00:10:00)任务类型102 每日释放保险仓利润', 'DEFAULT', 'xmsTask.handelStakeOrder102', '0 10 0 * * ?', '3', '1', '0', 'admin', '2026-03-12 21:31:51', 'admin', '2026-03-20 11:22:11', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (32, '(00:05:00)任务类型103 每日计算日质押的日利率', 'DEFAULT', 'xmsTask.handelStakeOrder103', '0 5 0 * * ?', '3', '1', '0', 'admin', '2026-03-12 21:32:30', 'admin', '2026-03-20 11:20:52', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (33, '(00:05:00)任务类型104 财富仓收益解锁', 'DEFAULT', 'xmsTask.handelStakeOrder104', '0 5 0 * * ?', '3', '1', '0', 'admin', '2026-03-12 21:33:03', 'admin', '2026-03-20 11:21:16', '');
INSERT INTO `neptune`.`sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (34, 'handelStakeOrder105 补偿任务', 'DEFAULT', 'xmsTask.handelStakeOrder105', '0 0 0 * * ?', '3', '1', '1', 'admin', NULL, '', '2026-03-17 10:19:00', '');
