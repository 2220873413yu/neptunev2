START TRANSACTION;

UPDATE t_user_info root_user
    INNER JOIN (
    SELECT
    COUNT(*) AS branch_count,
    SUM(COALESCE(child.umbrella_num, 0) + 1) AS new_umbrella_num,
    SUM(COALESCE(child.performance, 0)) AS new_sub_performance,
    SUM(
    COALESCE(child.performance, 0)
    + COALESCE(child.umbrella_performance, 0)
    ) AS new_umbrella_performance,
    SUM(
    COALESCE(child.real_performance, 0)
    + COALESCE(child.real_umbrella_performance, 0)
    ) AS new_real_umbrella_performance,
    SUM(
    COALESCE(child.mapping_performance, 0)
    + COALESCE(child.mapping_umbrella_performance, 0)
    ) AS new_mapping_umbrella_performance,
    SUM(
    COALESCE(child.performance, 0)
    + COALESCE(child.umbrella_performance, 0)
    )
    - MAX(
    COALESCE(child.performance, 0)
    + COALESCE(child.umbrella_performance, 0)
    ) AS new_community_performance
    FROM t_user_info child
    WHERE child.user_id IN (1002, 6950)
    ) stat ON 1 = 1
    SET
        root_user.sub_num = stat.branch_count,
        root_user.umbrella_num = stat.new_umbrella_num,
        root_user.sub_performance = stat.new_sub_performance,
        root_user.umbrella_performance = stat.new_umbrella_performance,
        root_user.real_umbrella_performance = stat.new_real_umbrella_performance,
        root_user.mapping_umbrella_performance = stat.new_mapping_umbrella_performance,
        root_user.community_performance = GREATEST(
        COALESCE(stat.new_community_performance, 0),
        0
        ),
        root_user.update_time = NOW()
WHERE root_user.user_id = 1000
  AND stat.branch_count = 2;

COMMIT;
