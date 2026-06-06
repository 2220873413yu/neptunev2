package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3MiningPackageOrderLogMapper;
import com.xms.dao.domain.W3MiningPackageOrderLog;
import com.xms.dao.service.IW3MiningPackageOrderLogService;

/**
 * 矿机订单余额宝变更记录Service业务层处理
 *
 * @author xms
 * @date 2025-04-14
 */
@Service
public class W3MiningPackageOrderLogServiceImpl extends XmsDataServiceImpl<W3MiningPackageOrderLogMapper, W3MiningPackageOrderLog> implements IW3MiningPackageOrderLogService
{


    /**
     * 查询矿机订单余额宝变更记录列表
     *
     *
     * @param w3MiningPackageOrderLog 矿机订单余额宝变更记录
     * @return 矿机订单余额宝变更记录
     */
    @Override
    public List<W3MiningPackageOrderLog> selectW3MiningPackageOrderLogList(W3MiningPackageOrderLog w3MiningPackageOrderLog)
    {
        return baseMapper.selectW3MiningPackageOrderLogList(w3MiningPackageOrderLog);
    }

}
