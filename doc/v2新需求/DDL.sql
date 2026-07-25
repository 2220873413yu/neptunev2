CREATE TEMPORARY TABLE tmp_delete_user_ids (
  user_id BIGINT NOT NULL PRIMARY KEY
);

-- 顶号本人
INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (10105);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (10089);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (7513);
INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (1001);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (2324);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (3823);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (6964);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (6960);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (6958);

INSERT IGNORE INTO tmp_delete_user_ids (user_id)
VALUES (1005);

-- 顶号全部下级团队成员
INSERT IGNORE INTO tmp_delete_user_ids (user_id)
SELECT DISTINCT relation.pos_user_id
FROM t_user_relation relation
         INNER JOIN t_user_info user_info
                    ON user_info.user_id = relation.pos_user_id
WHERE relation.par_user_id = 1005
  AND relation.distance > 0
  AND relation.active_flag = 1;




-- 1. 删除用户主信息
DELETE user_info
FROM t_user_info user_info
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = user_info.user_id;

-- 2. 删除用户钱包
DELETE user_money
FROM t_user_money user_money
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = user_money.id;

-- 3. 删除用户财富仓
DELETE wealth_vault
FROM t_user_wealth_vault wealth_vault
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = wealth_vault.id;





-- 4.删除关系
DELETE relation
FROM t_user_relation relation
INNER JOIN tmp_delete_user_ids delete_user
  ON relation.par_user_id = delete_user.user_id
  OR relation.pos_user_id = delete_user.user_id;


 -- 删除节点订单
DELETE t_node_plan_order
FROM t_node_plan_order node_plan_order
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = node_plan_order.user_id;

 -- 买h代币订单
DELETE t_buy_h_order
FROM t_buy_h_order buy_h_order
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = buy_h_order.user_id;


 -- 奖金记录
DELETE xms_reward_record
FROM xms_reward_record reward_record
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = reward_record.user_id;


 -- 钱包流水
DELETE t_user_money_log
FROM t_user_money_log money_log
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = money_log.user_id;


 -- 提现记录
DELETE t_withdrawal
FROM t_withdrawal withdrawal
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = withdrawal.user_id;


       -- 质押记录
DELETE t_stake_order
FROM t_stake_order stake_order
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = stake_order.user_id;

       -- 质押仓位
DELETE t_user_stake_position
FROM t_user_stake_position stake_position
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = stake_position.user_id;

       -- h赠送释放
       DELETE t_h_gift_release_bucket
FROM t_h_gift_release_bucket release_bucket
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = release_bucket.user_id;

              -- h_to acp
       DELETE t_old_h_to_acp_deposit_record
FROM t_old_h_to_acp_deposit_record acp_deposit_record
INNER JOIN tmp_delete_user_ids delete_user
  ON delete_user.user_id = acp_deposit_record.user_id;
