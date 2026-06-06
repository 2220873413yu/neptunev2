package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3UserLevelLogMapper;
import com.xms.dao.domain.W3UserLevelLog;
import com.xms.dao.service.IW3UserLevelLogService;

/**
 * 用户等级变更记录Service业务层处理
 *
 * @author xms
 * @date 2025-04-14
 */
@Service
public class W3UserLevelLogServiceImpl extends XmsDataServiceImpl<W3UserLevelLogMapper, W3UserLevelLog> implements IW3UserLevelLogService
{


    /**
     * 查询用户等级变更记录列表
     *
     *
     * @param w3UserLevelLog 用户等级变更记录
     * @return 用户等级变更记录
     */
    @Override
    public List<W3UserLevelLog> selectW3UserLevelLogList(W3UserLevelLog w3UserLevelLog)
    {
        return baseMapper.selectW3UserLevelLogList(w3UserLevelLog);
    }

}
