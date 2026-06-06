package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3MiningUserPerformanceLogMapper;
import com.xms.dao.domain.W3MiningUserPerformanceLog;
import com.xms.dao.service.IW3MiningUserPerformanceLogService;

/**
 * 活期矿机领取日志Service业务层处理
 *
 * @author xms
 * @date 2025-04-26
 */
@Service
public class W3MiningUserPerformanceLogServiceImpl extends XmsDataServiceImpl<W3MiningUserPerformanceLogMapper, W3MiningUserPerformanceLog> implements IW3MiningUserPerformanceLogService
{


    /**
     * 查询活期矿机领取日志列表
     *
     *
     * @param w3MiningUserPerformanceLog 活期矿机领取日志
     * @return 活期矿机领取日志
     */
    @Override
    public List<W3MiningUserPerformanceLog> selectW3MiningUserPerformanceLogList(W3MiningUserPerformanceLog w3MiningUserPerformanceLog)
    {
        return baseMapper.selectW3MiningUserPerformanceLogList(w3MiningUserPerformanceLog);
    }

}
