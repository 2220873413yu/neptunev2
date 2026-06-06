package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3UserLevelLog;

/**
 * 用户等级变更记录Mapper接口
 *
 * @author xms
 * @date 2025-04-14
 */
public interface W3UserLevelLogMapper extends XmsMapper<W3UserLevelLog>
{
    /**
     * 查询用户等级变更记录列表
     *
     * @param w3UserLevelLog 用户等级变更记录
     * @return 用户等级变更记录集合
     */
    public List<W3UserLevelLog> selectW3UserLevelLogList(W3UserLevelLog w3UserLevelLog);

}
