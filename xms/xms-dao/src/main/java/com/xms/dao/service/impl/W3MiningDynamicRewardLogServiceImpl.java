package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3MiningDynamicRewardLogMapper;
import com.xms.dao.domain.W3MiningDynamicRewardLog;
import com.xms.dao.service.IW3MiningDynamicRewardLogService;

/**
 * 挖矿动态奖日志Service业务层处理
 *
 * @author xms
 * @date 2025-04-15
 */
@Service
public class W3MiningDynamicRewardLogServiceImpl extends XmsDataServiceImpl<W3MiningDynamicRewardLogMapper, W3MiningDynamicRewardLog> implements IW3MiningDynamicRewardLogService
{


    /**
     * 查询挖矿动态奖日志列表
     *
     *
     * @param w3MiningDynamicRewardLog 挖矿动态奖日志
     * @return 挖矿动态奖日志
     */
    @Override
    public List<W3MiningDynamicRewardLog> selectW3MiningDynamicRewardLogList(W3MiningDynamicRewardLog w3MiningDynamicRewardLog)
    {
        return baseMapper.selectW3MiningDynamicRewardLogList(w3MiningDynamicRewardLog);
    }

}
