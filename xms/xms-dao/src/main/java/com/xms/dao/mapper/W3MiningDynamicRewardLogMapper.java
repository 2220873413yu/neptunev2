package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3MiningDynamicRewardLog;

/**
 * 挖矿动态奖日志Mapper接口
 *
 * @author xms
 * @date 2025-04-15
 */
public interface W3MiningDynamicRewardLogMapper extends XmsMapper<W3MiningDynamicRewardLog>
{
    /**
     * 查询挖矿动态奖日志列表
     *
     * @param w3MiningDynamicRewardLog 挖矿动态奖日志
     * @return 挖矿动态奖日志集合
     */
    public List<W3MiningDynamicRewardLog> selectW3MiningDynamicRewardLogList(W3MiningDynamicRewardLog w3MiningDynamicRewardLog);

}
