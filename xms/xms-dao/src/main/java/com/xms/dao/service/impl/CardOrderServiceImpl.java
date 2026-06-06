package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.CardOrderMapper;
import com.xms.dao.domain.CardOrder;
import com.xms.dao.service.ICardOrderService;

/**
 * 卡片订单Service业务层处理
 *
 * @author xms
 * @date 2025-12-04
 */
@Service
public class CardOrderServiceImpl extends XmsDataServiceImpl<CardOrderMapper, CardOrder> implements ICardOrderService
{


    /**
     * 查询卡片订单列表
     *
     *
     * @param cardOrder 卡片订单
     * @return 卡片订单
     */
    @Override
    public List<CardOrder> selectCardOrderList(CardOrder cardOrder)
    {
        return baseMapper.selectCardOrderList(cardOrder);
    }

}
