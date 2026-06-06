package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3UserLevelLog;

/**
 * 用户等级变更记录Service接口
 *
 * @author xms
 * @date 2025-04-14
 */
public interface IW3UserLevelLogService extends XmsDataService<W3UserLevelLog>
{

    /**
     * 查询用户等级变更记录列表
     *
     * @param w3UserLevelLog 用户等级变更记录
     * @return 用户等级变更记录集合
     */
    public List<W3UserLevelLog> selectW3UserLevelLogList(W3UserLevelLog w3UserLevelLog);

}
