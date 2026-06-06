package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.BuyHOrder;

/**
 * 购买H代币订单Service接口
 *
 * @author xms
 * @date 2026-03-10
 */
public interface IBuyHOrderService extends XmsDataService<BuyHOrder>
{

    /**
     * 查询购买H代币订单列表
     *
     * @param buyHOrder 购买H代币订单
     * @return 购买H代币订单集合
     */
    public List<BuyHOrder> selectBuyHOrderList(BuyHOrder buyHOrder);

}
