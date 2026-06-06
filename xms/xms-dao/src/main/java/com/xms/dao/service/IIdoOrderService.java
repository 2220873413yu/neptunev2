package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.IdoOrder;

/**
 * ido订单记录Service接口
 *
 * @author xms
 * @date 2025-12-25
 */
public interface IIdoOrderService extends XmsDataService<IdoOrder>
{

    /**
     * 查询ido订单记录列表
     *
     * @param idoOrder ido订单记录
     * @return ido订单记录集合
     */
    public List<IdoOrder> selectIdoOrderList(IdoOrder idoOrder);

}
