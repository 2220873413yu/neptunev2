package com.xms.dao.service.impl;

import com.xms.dao.domain.UserLevelChangeLog;
import com.xms.dao.mapper.UserLevelChangeLogMapper;
import com.xms.dao.service.IUserLevelChangeLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户等级变动日志Service业务层处理
 *
 * @author xms
 * @date 2025-06-23
 */
@Service
public class UserLevelChangeLogServiceImpl extends XmsDataServiceImpl<UserLevelChangeLogMapper, UserLevelChangeLog> implements IUserLevelChangeLogService
{


    /**
     * 查询用户等级变动日志列表
     *
     *
     * @param userLevelChangeLog 用户等级变动日志
     * @return 用户等级变动日志
     */
    @Override
    public List<UserLevelChangeLog> selectUserLevelChangeLogList(UserLevelChangeLog userLevelChangeLog)
    {
        return baseMapper.selectUserLevelChangeLogList(userLevelChangeLog);
    }

}
