package com.xms.dao.mapper;

import com.xms.dao.domain.UserLevelChangeLog;

import java.util.List;

/**
 * 用户等级变动日志Mapper接口
 *
 * @author xms
 * @date 2025-06-23
 */
public interface UserLevelChangeLogMapper extends XmsMapper<UserLevelChangeLog>
{
    /**
     * 查询用户等级变动日志列表
     *
     * @param userLevelChangeLog 用户等级变动日志
     * @return 用户等级变动日志集合
     */
    public List<UserLevelChangeLog> selectUserLevelChangeLogList(UserLevelChangeLog userLevelChangeLog);

}
