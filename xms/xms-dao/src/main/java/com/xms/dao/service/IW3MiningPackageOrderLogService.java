package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3MiningPackageOrderLog;

/**
 * 矿机订单余额宝变更记录Service接口
 *
 * @author xms
 * @date 2025-04-14
 */
public interface IW3MiningPackageOrderLogService extends XmsDataService<W3MiningPackageOrderLog>
{

    /**
     * 查询矿机订单余额宝变更记录列表
     *
     * @param w3MiningPackageOrderLog 矿机订单余额宝变更记录
     * @return 矿机订单余额宝变更记录集合
     */
    public List<W3MiningPackageOrderLog> selectW3MiningPackageOrderLogList(W3MiningPackageOrderLog w3MiningPackageOrderLog);

}
