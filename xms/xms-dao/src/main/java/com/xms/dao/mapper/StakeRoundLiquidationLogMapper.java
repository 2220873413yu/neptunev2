package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.StakeRoundLiquidationLog;

/**
 * 轮次爆仓判定与执行日志Mapper接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface StakeRoundLiquidationLogMapper extends XmsMapper<StakeRoundLiquidationLog>
{
    /**
     * 查询轮次爆仓判定与执行日志列表
     *
     * @param stakeRoundLiquidationLog 轮次爆仓判定与执行日志
     * @return 轮次爆仓判定与执行日志集合
     */
    public List<StakeRoundLiquidationLog> selectStakeRoundLiquidationLogList(StakeRoundLiquidationLog stakeRoundLiquidationLog);

}
