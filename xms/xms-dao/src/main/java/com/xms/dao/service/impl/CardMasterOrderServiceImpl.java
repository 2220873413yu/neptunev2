package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.CardMasterOrderMapper;
import com.xms.dao.domain.CardMasterOrder;
import com.xms.dao.service.ICardMasterOrderService;

/**
 * 购买记录Service业务层处理
 *
 * @author xms
 * @date 2025-12-04
 */
@Service
public class CardMasterOrderServiceImpl extends XmsDataServiceImpl<CardMasterOrderMapper, CardMasterOrder> implements ICardMasterOrderService
{


    /**
     * 查询购买记录列表
     *
     *
     * @param cardMasterOrder 购买记录
     * @return 购买记录
     */
    @Override
    public List<CardMasterOrder> selectCardMasterOrderList(CardMasterOrder cardMasterOrder)
    {
        return baseMapper.selectCardMasterOrderList(cardMasterOrder);
    }

}
