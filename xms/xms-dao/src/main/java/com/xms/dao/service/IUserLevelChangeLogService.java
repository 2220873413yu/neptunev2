package com.xms.dao.service;

import com.xms.dao.domain.UserLevelChangeLog;

import java.util.List;

/**
 * 用户等级变动日志Service接口
 *
 * @author xms
 * @date 2025-06-23
 */
public interface IUserLevelChangeLogService extends XmsDataService<UserLevelChangeLog>
{

    /**
     * 查询用户等级变动日志列表
     *
     * @param userLevelChangeLog 用户等级变动日志
     * @return 用户等级变动日志集合
     */
    public List<UserLevelChangeLog> selectUserLevelChangeLogList(UserLevelChangeLog userLevelChangeLog);

}
