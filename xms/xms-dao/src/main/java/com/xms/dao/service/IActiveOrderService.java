package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.ActiveOrder;

/**
 * 用户激活订单Service接口
 *
 * @author xms
 * @date 2025-12-30
 */
public interface IActiveOrderService extends XmsDataService<ActiveOrder>
{

    /**
     * 查询用户激活订单列表
     *
     * @param activeOrder 用户激活订单
     * @return 用户激活订单集合
     */
    public List<ActiveOrder> selectActiveOrderList(ActiveOrder activeOrder);

}
