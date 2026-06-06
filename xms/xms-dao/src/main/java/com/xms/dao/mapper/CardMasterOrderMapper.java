package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.CardMasterOrder;

/**
 * 购买记录Mapper接口
 *
 * @author xms
 * @date 2025-12-04
 */
public interface CardMasterOrderMapper extends XmsMapper<CardMasterOrder>
{
    /**
     * 查询购买记录列表
     *
     * @param cardMasterOrder 购买记录
     * @return 购买记录集合
     */
    public List<CardMasterOrder> selectCardMasterOrderList(CardMasterOrder cardMasterOrder);

}
