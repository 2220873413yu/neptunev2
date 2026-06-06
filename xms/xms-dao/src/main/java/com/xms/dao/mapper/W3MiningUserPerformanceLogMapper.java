package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3MiningUserPerformanceLog;

/**
 * 活期矿机领取日志Mapper接口
 *
 * @author xms
 * @date 2025-04-26
 */
public interface W3MiningUserPerformanceLogMapper extends XmsMapper<W3MiningUserPerformanceLog>
{
    /**
     * 查询活期矿机领取日志列表
     *
     * @param w3MiningUserPerformanceLog 活期矿机领取日志
     * @return 活期矿机领取日志集合
     */
    public List<W3MiningUserPerformanceLog> selectW3MiningUserPerformanceLogList(W3MiningUserPerformanceLog w3MiningUserPerformanceLog);

}
