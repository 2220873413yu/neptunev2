package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.StakeRoundLiquidationLogMapper;
import com.xms.dao.domain.StakeRoundLiquidationLog;
import com.xms.dao.service.IStakeRoundLiquidationLogService;

/**
 * 轮次爆仓判定与执行日志Service业务层处理
 *
 * @author xms
 * @date 2026-03-06
 */
@Service
public class StakeRoundLiquidationLogServiceImpl extends XmsDataServiceImpl<StakeRoundLiquidationLogMapper, StakeRoundLiquidationLog> implements IStakeRoundLiquidationLogService
{


    /**
     * 查询轮次爆仓判定与执行日志列表
     *
     *
     * @param stakeRoundLiquidationLog 轮次爆仓判定与执行日志
     * @return 轮次爆仓判定与执行日志
     */
    @Override
    public List<StakeRoundLiquidationLog> selectStakeRoundLiquidationLogList(StakeRoundLiquidationLog stakeRoundLiquidationLog)
    {
        return baseMapper.selectStakeRoundLiquidationLogList(stakeRoundLiquidationLog);
    }

}
