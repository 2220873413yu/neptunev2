package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.CardMasterOrder;

/**
 * 购买记录Service接口
 *
 * @author xms
 * @date 2025-12-04
 */
public interface ICardMasterOrderService extends XmsDataService<CardMasterOrder>
{

    /**
     * 查询购买记录列表
     *
     * @param cardMasterOrder 购买记录
     * @return 购买记录集合
     */
    public List<CardMasterOrder> selectCardMasterOrderList(CardMasterOrder cardMasterOrder);

}
