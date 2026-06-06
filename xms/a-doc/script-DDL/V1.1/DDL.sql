ALTER TABLE `neptune`.`t_withdrawal_config`
    ADD COLUMN `daily_free_audit_count` int NOT NULL DEFAULT 0 COMMENT '单日免审核次数' AFTER `remark`,
ADD COLUMN `withdraw_limit` decimal(20, 8) UNSIGNED NOT NULL DEFAULT 0.00000000 COMMENT '提现额度' AFTER `daily_free_audit_count`;


CREATE TABLE `t_stake_daily_snapshot` (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                          `insurance_balance` decimal(30,6) DEFAULT '0.000000' COMMENT '保险仓余额',
                                          `player_stake_total` decimal(30,6) NOT NULL DEFAULT '0.000000' COMMENT '本轮玩家累计参与总量(不含节点)',
                                          `studio_subsidy_total` decimal(30,6) NOT NULL DEFAULT '0.000000' COMMENT '本轮累计已发放工作室补贴',
                                          `withdraw_reward_total_full` decimal(30,6) NOT NULL DEFAULT '0.000000' COMMENT '本轮累计已提取收益总额(静态+动态按100%口径)',
                                          `buy_point_total` decimal(30,6) DEFAULT '0.000000' COMMENT '买积分的h余额',
                                          `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci DEFAULT NULL COMMENT '备注',
                                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                          `stake_round_id` bigint DEFAULT NULL COMMENT '轮次id',
                                          `locked_valid_num4` decimal(30,6) DEFAULT '0.000000' COMMENT '用户待解锁财富仓余额',
                                          `total_valid_num4` decimal(30,6) DEFAULT '0.000000' COMMENT '用户财富仓余额',
                                          `withdraw_contract_balance` decimal(30,6) DEFAULT '0.000000' COMMENT '提现合约余额',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci ROW_FORMAT=DYNAMIC COMMENT='每日质押数据快照表';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('每日质押数据快照', '3006', '1', 'stakeDailySnapshot', 'xms/stakeDailySnapshot/index', 1, 0, 'C', '0', '0', 'xms:stakeDailySnapshot:list', 'log', 'admin', sysdate(), '', null, '每日质押数据快照菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('每日质押数据快照查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'xms:stakeDailySnapshot:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('每日质押数据快照新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'xms:stakeDailySnapshot:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('每日质押数据快照修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'xms:stakeDailySnapshot:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('每日质押数据快照删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'xms:stakeDailySnapshot:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('每日质押数据快照导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'xms:stakeDailySnapshot:export',       '#', 'admin', sysdate(), '', null, '');
