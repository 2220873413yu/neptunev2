package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3StaticRewardLog;

/**
 * 全网静态分红日志Service接口
 *
 * @author xms
 * @date 2025-04-15
 */
public interface IW3StaticRewardLogService extends XmsDataService<W3StaticRewardLog>
{

    /**
     * 查询全网静态分红日志列表
     *
     * @param w3StaticRewardLog 全网静态分红日志
     * @return 全网静态分红日志集合
     */
    public List<W3StaticRewardLog> selectW3StaticRewardLogList(W3StaticRewardLog w3StaticRewardLog);

}
