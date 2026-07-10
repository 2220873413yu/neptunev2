-- 查询真实入金量(acp)
-- select sum(stake_amount) from t_stake_order where deposit_source_type = 1

-- t_stake_round 增量：增加每轮爆仓检测开关。
-- 说明：
-- 1. liquidation_check_enabled=1 时，提现/购买贡献分成功后允许投递爆仓检测消息。
-- 2. liquidation_check_enabled=0 时，不投递、不检测、不爆仓。
-- 3. 不复用 biz_status；biz_status 继续表示爆仓事件 bizType=3 是否已处理。

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_stake_round'
    AND COLUMN_NAME = 'liquidation_check_enabled'
);

SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE t_stake_round ADD COLUMN liquidation_check_enabled tinyint NOT NULL DEFAULT 1 COMMENT ''爆仓检测开关：0关闭，1开启'' AFTER biz_status',
  'SELECT ''t_stake_round.liquidation_check_enabled already exists'''
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;



UPDATE `t_sys_para` SET `para_code` = 'biz_h_deposit_min_amount', `para_value` = '100', `para_desc` = 'h闪兑成acp入金入金最小金额 例如1,就是1', `visible` = '0', `create_time` = '2026-06-07 16:13:28', `update_time` = '2026-06-17 16:48:27', `active_flag` = 1, `remark` = '	\r\nh闪兑成acp入金入金最小金额 例如1,就是1' WHERE `sys_para_id` = 14;




-- t_user_info 增量：增加真实/映射个人及团队业绩字段。
-- 说明：
-- 1. real_* 统计正常 ACP 入金(type=1)和用户 H 余额换 ACP 入金(type=4)。
-- 2. mapping_* 统计旧系统 H 换 ACP 入金(type=3)。
-- 3. 四个字段只做分类记录，不参与现有总业绩、等级、小区业绩和奖励计算。

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'real_performance'
);
SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE t_user_info ADD COLUMN real_performance decimal(20,6) NOT NULL DEFAULT 0.000000 COMMENT ''真实个人业绩'' AFTER performance',
  'SELECT ''t_user_info.real_performance already exists'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'mapping_performance'
);
SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE t_user_info ADD COLUMN mapping_performance decimal(20,6) NOT NULL DEFAULT 0.000000 COMMENT ''映射个人业绩'' AFTER real_performance',
  'SELECT ''t_user_info.mapping_performance already exists'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'real_umbrella_performance'
);
SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE t_user_info ADD COLUMN real_umbrella_performance decimal(20,6) NOT NULL DEFAULT 0.000000 COMMENT ''真实团队业绩'' AFTER umbrella_performance',
  'SELECT ''t_user_info.real_umbrella_performance already exists'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'mapping_umbrella_performance'
);
SET @ddl := IF(
  @col_exists = 0,
  'ALTER TABLE t_user_info ADD COLUMN mapping_umbrella_performance decimal(20,6) NOT NULL DEFAULT 0.000000 COMMENT ''映射团队业绩'' AFTER real_umbrella_performance',
  'SELECT ''t_user_info.mapping_umbrella_performance already exists'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;






SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'old_performance'
);
SET @ddl := IF(
  @col_exists > 0,
  'ALTER TABLE t_user_info DROP COLUMN old_performance',
  'SELECT ''t_user_info.old_performance does not exist'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'old_history_performance'
);
SET @ddl := IF(
  @col_exists > 0,
  'ALTER TABLE t_user_info DROP COLUMN old_history_performance',
  'SELECT ''t_user_info.old_history_performance does not exist'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'old_sub_performance'
);
SET @ddl := IF(
  @col_exists > 0,
  'ALTER TABLE t_user_info DROP COLUMN old_sub_performance',
  'SELECT ''t_user_info.old_sub_performance does not exist'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists := (
  SELECT COUNT(1)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 't_user_info'
    AND COLUMN_NAME = 'old_umbrella_performance'
);
SET @ddl := IF(
  @col_exists > 0,
  'ALTER TABLE t_user_info DROP COLUMN old_umbrella_performance',
  'SELECT ''t_user_info.old_umbrella_performance does not exist'''
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
