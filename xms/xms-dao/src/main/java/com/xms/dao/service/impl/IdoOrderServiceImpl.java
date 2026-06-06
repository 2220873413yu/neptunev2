package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.IdoOrderMapper;
import com.xms.dao.domain.IdoOrder;
import com.xms.dao.service.IIdoOrderService;

/**
 * ido订单记录Service业务层处理
 *
 * @author xms
 * @date 2025-12-25
 */
@Service
public class IdoOrderServiceImpl extends XmsDataServiceImpl<IdoOrderMapper, IdoOrder> implements IIdoOrderService
{


    /**
     * 查询ido订单记录列表
     *
     *
     * @param idoOrder ido订单记录
     * @return ido订单记录
     */
    @Override
    public List<IdoOrder> selectIdoOrderList(IdoOrder idoOrder)
    {
        return baseMapper.selectIdoOrderList(idoOrder);
    }

}
