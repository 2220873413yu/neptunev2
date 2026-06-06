package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3MiningPackageOrderLog;

/**
 * 矿机订单余额宝变更记录Mapper接口
 *
 * @author xms
 * @date 2025-04-14
 */
public interface W3MiningPackageOrderLogMapper extends XmsMapper<W3MiningPackageOrderLog>
{
    /**
     * 查询矿机订单余额宝变更记录列表
     *
     * @param w3MiningPackageOrderLog 矿机订单余额宝变更记录
     * @return 矿机订单余额宝变更记录集合
     */
    public List<W3MiningPackageOrderLog> selectW3MiningPackageOrderLogList(W3MiningPackageOrderLog w3MiningPackageOrderLog);

}
