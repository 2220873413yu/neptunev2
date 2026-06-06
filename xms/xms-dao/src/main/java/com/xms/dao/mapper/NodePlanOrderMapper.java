package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.NodePlanOrder;

/**
 * 用户节点订单Mapper接口
 *
 * @author xms
 * @date 2026-01-16
 */
public interface NodePlanOrderMapper extends XmsMapper<NodePlanOrder>
{
    /**
     * 查询用户节点订单列表
     *
     * @param nodePlanOrder 用户节点订单
     * @return 用户节点订单集合
     */
    public List<NodePlanOrder> selectNodePlanOrderList(NodePlanOrder nodePlanOrder);

}
