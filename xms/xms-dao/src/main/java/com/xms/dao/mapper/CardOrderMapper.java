package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.CardOrder;

/**
 * 卡片订单Mapper接口
 *
 * @author xms
 * @date 2025-12-04
 */
public interface CardOrderMapper extends XmsMapper<CardOrder>
{
    /**
     * 查询卡片订单列表
     *
     * @param cardOrder 卡片订单
     * @return 卡片订单集合
     */
    public List<CardOrder> selectCardOrderList(CardOrder cardOrder);

}
