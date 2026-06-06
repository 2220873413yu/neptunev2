package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.StakeRoundLiquidationLog;

/**
 * 轮次爆仓判定与执行日志Service接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface IStakeRoundLiquidationLogService extends XmsDataService<StakeRoundLiquidationLog>
{

    /**
     * 查询轮次爆仓判定与执行日志列表
     *
     * @param stakeRoundLiquidationLog 轮次爆仓判定与执行日志
     * @return 轮次爆仓判定与执行日志集合
     */
    public List<StakeRoundLiquidationLog> selectStakeRoundLiquidationLogList(StakeRoundLiquidationLog stakeRoundLiquidationLog);

}
