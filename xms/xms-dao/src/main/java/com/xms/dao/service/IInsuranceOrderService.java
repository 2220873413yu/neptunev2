package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.InsuranceOrder;

/**
 * 保险仓释放订单Service接口
 *
 * @author xms
 * @date 2026-03-11
 */
public interface IInsuranceOrderService extends XmsDataService<InsuranceOrder>
{

    /**
     * 查询保险仓释放订单列表
     *
     * @param insuranceOrder 保险仓释放订单
     * @return 保险仓释放订单集合
     */
    public List<InsuranceOrder> selectInsuranceOrderList(InsuranceOrder insuranceOrder);

}
