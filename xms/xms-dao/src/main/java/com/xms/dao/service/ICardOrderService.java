package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.CardOrder;

/**
 * 卡片订单Service接口
 *
 * @author xms
 * @date 2025-12-04
 */
public interface ICardOrderService extends XmsDataService<CardOrder>
{

    /**
     * 查询卡片订单列表
     *
     * @param cardOrder 卡片订单
     * @return 卡片订单集合
     */
    public List<CardOrder> selectCardOrderList(CardOrder cardOrder);

}
