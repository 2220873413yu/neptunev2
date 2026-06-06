package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.ActiveOrder;

/**
 * 用户激活订单Mapper接口
 *
 * @author xms
 * @date 2025-12-30
 */
public interface ActiveOrderMapper extends XmsMapper<ActiveOrder>
{
    /**
     * 查询用户激活订单列表
     *
     * @param activeOrder 用户激活订单
     * @return 用户激活订单集合
     */
    public List<ActiveOrder> selectActiveOrderList(ActiveOrder activeOrder);

}
