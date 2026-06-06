package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3StaticRewardLogMapper;
import com.xms.dao.domain.W3StaticRewardLog;
import com.xms.dao.service.IW3StaticRewardLogService;

/**
 * 全网静态分红日志Service业务层处理
 *
 * @author xms
 * @date 2025-04-15
 */
@Service
public class W3StaticRewardLogServiceImpl extends XmsDataServiceImpl<W3StaticRewardLogMapper, W3StaticRewardLog> implements IW3StaticRewardLogService
{


    /**
     * 查询全网静态分红日志列表
     *
     *
     * @param w3StaticRewardLog 全网静态分红日志
     * @return 全网静态分红日志
     */
    @Override
    public List<W3StaticRewardLog> selectW3StaticRewardLogList(W3StaticRewardLog w3StaticRewardLog)
    {
        return baseMapper.selectW3StaticRewardLogList(w3StaticRewardLog);
    }

}
