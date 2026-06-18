-- 把dict_data、dict_type、sys_job、参数表复制过来

-- 之前设置的虚拟层等级、团队等级、节点等级为空
update t_user_info
set min_node_level = 0,
    min_layer_level = 0,
    min_game_level = 0;


-- update t_user_info
-- set old_performance = 0,
--     old_history_performance = 0,
--     old_sub_performance = 0,
--     old_umbrella_performance = 0;

-- 财富仓更新为0
UPDATE t_user_wealth_vault
SET
    seg1_amount = 0,
    seg2_amount = 0,
    seg3_amount = 0,
    seg4_amount = 0,
    seg5_amount = 0,
    seg6_amount = 0,
    seg7_amount = 0,
    update_time = NOW();

-- 钱包余额更新为0
UPDATE t_user_money
SET
    valid_num1 = 0,
    valid_num2 = 0,
    valid_num3 = 0,
    valid_num4 = 0,
    valid_num5 = 0,
    valid_num6 = 0,
    valid_num7 = 0,
    valid_num8 = 0,
    valid_num9 = 0,
    update_time = NOW()
WHERE deleted = 0;






-- 初始化数据
TRUNCATE TABLE sys_logininfor;
TRUNCATE TABLE sys_job_log;

TRUNCATE TABLE xms_reward_record;
TRUNCATE TABLE t_withdrawal;
TRUNCATE TABLE t_user_wealth_vault_flow;


-- 持仓流水
-- TRUNCATE TABLE t_user_stake_position;
update t_user_stake_position
set total_stake_amount = 0,
    total_reward = 0,
    dynamic_reward = 0,
    total_withdrawal_static = 0,
    total_withdrawal_dynamic = 0,
    total_withdrawal_studio_subsidy = 0,
    studio_subsidy = 0,
    today_reward = 0;

TRUNCATE TABLE t_user_money_snapshot;
TRUNCATE TABLE t_user_money_log;
TRUNCATE TABLE t_user_stake_position_flow;


-- 需要初始化一下质押轮次的数据
TRUNCATE TABLE t_stake_order;


-- TRUNCATE TABLE t_stake_round;
-- 全局质押轮次数据设置为0
update t_stake_round
set player_stake_total = 0,
    studio_subsidy_total = 0,
    withdraw_reward_total_full = 0,
    buy_point_total = 0,
    insurance_balance = 0;


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



-- 执行该数据库脚本
    SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `neptunev2`.`t_h_gift_release_bucket`  (
                                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                        `bucket_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '释放桶编号',
                                                        `user_id` bigint NOT NULL COMMENT '新系统用户ID',
                                                        `account` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号/钱包地址快照',
                                                        `source_type` tinyint NOT NULL COMMENT '来源类型：1正常ACP入金，2后台手动拨，3旧系统入金',
                                                        `source_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '来源订单号；阶段1后台拨付时为后台拨付单号',
                                                        `source_user_id` bigint NULL DEFAULT NULL COMMENT '来源用户ID，阶段1通常等于user_id',
                                                        `total_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '赠送H总量',
                                                        `released_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '已释放H数量',
                                                        `remaining_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '剩余待释放H数量',
                                                        `daily_release_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '每日释放H数量',
                                                        `release_days` int NOT NULL DEFAULT 100 COMMENT '总释放天数',
                                                        `released_days` int NOT NULL DEFAULT 0 COMMENT '已释放天数',
                                                        `start_date` int NOT NULL COMMENT '释放开始日期，yyyyMMdd',
                                                        `last_release_date` int NULL DEFAULT NULL COMMENT '最后释放日期，yyyyMMdd',
                                                        `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1释放中，2已完成，3冻结',
                                                        `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                        `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                                        PRIMARY KEY (`id`) USING BTREE,
                                                        UNIQUE INDEX `uk_hgr_bucket_no`(`bucket_no` ASC) USING BTREE,
                                                        UNIQUE INDEX `uk_hgr_source`(`source_type` ASC, `source_order_no` ASC) USING BTREE,
                                                        INDEX `idx_hgr_user_id`(`user_id` ASC) USING BTREE,
                                                        INDEX `idx_hgr_account`(`account` ASC) USING BTREE,
                                                        INDEX `idx_hgr_status_start_date`(`status` ASC, `start_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'H赠送释放桶' ROW_FORMAT = Dynamic;

CREATE TABLE `neptunev2`.`t_old_h_to_acp_deposit_record`  (
                                                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                              `record_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
                                                              `old_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '旧系统订单号/请求号',
                                                              `user_id` bigint NOT NULL COMMENT '新系统用户ID',
                                                              `account` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户钱包地址/账号快照',
                                                              `old_h_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '旧系统传入的已释放H数量',
                                                              `h_price_usdt_snapshot` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT 'H当时U价快照',
                                                              `old_h_usdt_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '旧H折U价值',
                                                              `acp_price_usdt_snapshot` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT 'ACP当时U价快照',
                                                              `acp_deposit_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '换算后的ACP入金数量',
                                                              `stake_order_id` bigint NOT NULL COMMENT '新系统t_stake_order.id',
                                                              `stake_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新系统t_stake_order.order_no',
                                                              `deposit_source_type` tinyint NOT NULL DEFAULT 3 COMMENT '入金来源类型：3旧系统H换ACP入金',
                                                              `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1成功',
                                                              `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
                                                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                                              PRIMARY KEY (`id`) USING BTREE,
                                                              UNIQUE INDEX `uk_old_h_acp_record_no`(`record_no` ASC) USING BTREE,
                                                              UNIQUE INDEX `uk_old_h_acp_old_order_no`(`old_order_no` ASC) USING BTREE,
                                                              UNIQUE INDEX `uk_old_h_acp_stake_order_no`(`stake_order_no` ASC) USING BTREE,
                                                              INDEX `idx_old_h_acp_user_id`(`user_id` ASC) USING BTREE,
                                                              INDEX `idx_old_h_acp_account`(`account` ASC) USING BTREE,
                                                              INDEX `idx_old_h_acp_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '旧系统H换ACP入金记录' ROW_FORMAT = Dynamic;

ALTER TABLE `neptunev2`.`t_stake_order` ADD COLUMN `deposit_source_type` tinyint NOT NULL DEFAULT 1 COMMENT '入金来源类型：1正常ACP入金，3旧系统H换ACP入金' AFTER `stake_amount`;

ALTER TABLE `neptunev2`.`t_stake_order` ADD COLUMN `acp_price_usdt_snapshot` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT 'ACP当时U价快照' AFTER `deposit_source_type`;

ALTER TABLE `neptunev2`.`t_stake_order` ADD COLUMN `h_price_usdt_snapshot` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT 'H当时U价快照' AFTER `acp_price_usdt_snapshot`;

ALTER TABLE `neptunev2`.`t_stake_order` ADD COLUMN `deposit_usdt_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '本单折U价值' AFTER `h_price_usdt_snapshot`;

ALTER TABLE `neptunev2`.`t_stake_order` ADD COLUMN `gift_ratio_snapshot` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '赠送比例快照，例如30表示30%' AFTER `deposit_usdt_amount`;

ALTER TABLE `neptunev2`.`t_stake_order` ADD COLUMN `gift_h_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '本单应赠送H总量' AFTER `gift_ratio_snapshot`;

ALTER TABLE `neptunev2`.`t_user_info` ADD COLUMN `old_performance` decimal(20, 6) NOT NULL DEFAULT 0.000000 COMMENT '旧系统个人业绩' AFTER `performance`;

ALTER TABLE `neptunev2`.`t_user_info` ADD COLUMN `old_history_performance` decimal(20, 6) NOT NULL DEFAULT 0.000000 COMMENT '旧系统历史业绩' AFTER `history_performance`;

ALTER TABLE `neptunev2`.`t_user_info` ADD COLUMN `old_sub_performance` decimal(20, 6) NOT NULL DEFAULT 0.000000 COMMENT '旧系统直推业绩' AFTER `sub_performance`;

ALTER TABLE `neptunev2`.`t_user_info` ADD COLUMN `old_umbrella_performance` decimal(20, 6) NOT NULL DEFAULT 0.000000 COMMENT '旧系统团队业绩' AFTER `umbrella_performance`;

ALTER TABLE `neptunev2`.`t_user_money` MODIFY COLUMN `valid_num1` decimal(40, 8) NULL DEFAULT 0.00000000 COMMENT '节点收益(ACP)' AFTER `id`;

ALTER TABLE `neptunev2`.`t_user_money` MODIFY COLUMN `valid_num2` decimal(40, 8) NULL DEFAULT 0.00000000 COMMENT '静态(ACP)' AFTER `valid_num1`;

ALTER TABLE `neptunev2`.`t_user_money` MODIFY COLUMN `valid_num3` decimal(40, 8) NULL DEFAULT 0.00000000 COMMENT '动态(ACP)' AFTER `valid_num2`;

ALTER TABLE `neptunev2`.`t_user_money` MODIFY COLUMN `valid_num4` decimal(40, 8) NULL DEFAULT 0.00000000 COMMENT '财富(ACP)' AFTER `valid_num3`;

ALTER TABLE `neptunev2`.`t_user_money` MODIFY COLUMN `valid_num5` decimal(30, 8) NULL DEFAULT 0.00000000 COMMENT 'H代币(/魔盒/手续费)(ACP)' AFTER `valid_num4`;

ALTER TABLE `neptunev2`.`t_user_money` MODIFY COLUMN `valid_num6` decimal(30, 8) NULL DEFAULT 0.00000000 COMMENT '工作室收益(ACP)' AFTER `valid_num5`;

ALTER TABLE `neptunev2`.`t_user_money` MODIFY COLUMN `valid_num9` decimal(30, 8) NULL DEFAULT 0.00000000 COMMENT 'H代币' AFTER `valid_num8`;

SET FOREIGN_KEY_CHECKS=1;


    -- 用户购买节点身份 只有身份不释放(关闭定时任务就好了 xmsTask.handelNodePlanOrder)
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

ALTER TABLE `neptunev2`.`t_stake_order` ADD COLUMN `old_h_amount` decimal(32, 6) NOT NULL DEFAULT 0.000000 COMMENT '旧系统入金H数量' AFTER `stake_amount`;
