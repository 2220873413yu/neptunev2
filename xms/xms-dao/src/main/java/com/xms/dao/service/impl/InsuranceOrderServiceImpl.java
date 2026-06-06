package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.InsuranceOrderMapper;
import com.xms.dao.domain.InsuranceOrder;
import com.xms.dao.service.IInsuranceOrderService;

/**
 * 保险仓释放订单Service业务层处理
 *
 * @author xms
 * @date 2026-03-11
 */
@Service
public class InsuranceOrderServiceImpl extends XmsDataServiceImpl<InsuranceOrderMapper, InsuranceOrder> implements IInsuranceOrderService
{


    /**
     * 查询保险仓释放订单列表
     *
     *
     * @param insuranceOrder 保险仓释放订单
     * @return 保险仓释放订单
     */
    @Override
    public List<InsuranceOrder> selectInsuranceOrderList(InsuranceOrder insuranceOrder)
    {
        return baseMapper.selectInsuranceOrderList(insuranceOrder);
    }

}
