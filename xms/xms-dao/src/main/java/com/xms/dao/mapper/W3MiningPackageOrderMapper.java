package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3MiningPackageOrder;

/**
 * w3矿机订单Mapper接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface W3MiningPackageOrderMapper extends XmsMapper<W3MiningPackageOrder>
{
    /**
     * 查询w3矿机订单列表
     *
     * @param w3MiningPackageOrder w3矿机订单
     * @return w3矿机订单集合
     */
    public List<W3MiningPackageOrder> selectW3MiningPackageOrderList(W3MiningPackageOrder w3MiningPackageOrder);

}
