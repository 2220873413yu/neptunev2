package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3MiningPackageOrder;

/**
 * w3矿机订单Service接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface IW3MiningPackageOrderService extends XmsDataService<W3MiningPackageOrder>
{

    /**
     * 查询w3矿机订单列表
     *
     * @param w3MiningPackageOrder w3矿机订单
     * @return w3矿机订单集合
     */
    public List<W3MiningPackageOrder> selectW3MiningPackageOrderList(W3MiningPackageOrder w3MiningPackageOrder);

    int updateRecordById(W3MiningPackageOrder w3MiningPackageOrder);
}
