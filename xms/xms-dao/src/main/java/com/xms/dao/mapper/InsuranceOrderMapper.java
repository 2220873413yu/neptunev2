package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.InsuranceOrder;

/**
 * 保险仓释放订单Mapper接口
 *
 * @author xms
 * @date 2026-03-11
 */
public interface InsuranceOrderMapper extends XmsMapper<InsuranceOrder>
{
    /**
     * 查询保险仓释放订单列表
     *
     * @param insuranceOrder 保险仓释放订单
     * @return 保险仓释放订单集合
     */
    public List<InsuranceOrder> selectInsuranceOrderList(InsuranceOrder insuranceOrder);

}
