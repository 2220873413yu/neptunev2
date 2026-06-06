package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.ActiveOrderMapper;
import com.xms.dao.domain.ActiveOrder;
import com.xms.dao.service.IActiveOrderService;

/**
 * 用户激活订单Service业务层处理
 *
 * @author xms
 * @date 2025-12-30
 */
@Service
public class ActiveOrderServiceImpl extends XmsDataServiceImpl<ActiveOrderMapper, ActiveOrder> implements IActiveOrderService
{


    /**
     * 查询用户激活订单列表
     *
     *
     * @param activeOrder 用户激活订单
     * @return 用户激活订单
     */
    @Override
    public List<ActiveOrder> selectActiveOrderList(ActiveOrder activeOrder)
    {
        return baseMapper.selectActiveOrderList(activeOrder);
    }

}
