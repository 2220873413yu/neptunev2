package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.BuyHOrderMapper;
import com.xms.dao.domain.BuyHOrder;
import com.xms.dao.service.IBuyHOrderService;

/**
 * 购买H代币订单Service业务层处理
 *
 * @author xms
 * @date 2026-03-10
 */
@Service
public class BuyHOrderServiceImpl extends XmsDataServiceImpl<BuyHOrderMapper, BuyHOrder> implements IBuyHOrderService
{


    /**
     * 查询购买H代币订单列表
     *
     *
     * @param buyHOrder 购买H代币订单
     * @return 购买H代币订单
     */
    @Override
    public List<BuyHOrder> selectBuyHOrderList(BuyHOrder buyHOrder)
    {
        return baseMapper.selectBuyHOrderList(buyHOrder);
    }

}
