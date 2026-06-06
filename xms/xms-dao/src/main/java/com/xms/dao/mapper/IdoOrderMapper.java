package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.IdoOrder;

/**
 * ido订单记录Mapper接口
 *
 * @author xms
 * @date 2025-12-25
 */
public interface IdoOrderMapper extends XmsMapper<IdoOrder>
{
    /**
     * 查询ido订单记录列表
     *
     * @param idoOrder ido订单记录
     * @return ido订单记录集合
     */
    public List<IdoOrder> selectIdoOrderList(IdoOrder idoOrder);

}
