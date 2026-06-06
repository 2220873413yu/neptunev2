package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.NodePlanOrder;

/**
 * 用户节点订单Service接口
 *
 * @author xms
 * @date 2026-01-16
 */
public interface INodePlanOrderService extends XmsDataService<NodePlanOrder>
{

    /**
     * 查询用户节点订单列表
     *
     * @param nodePlanOrder 用户节点订单
     * @return 用户节点订单集合
     */
    public List<NodePlanOrder> selectNodePlanOrderList(NodePlanOrder nodePlanOrder);

	/**
     * 新增用户节点订单
     *
     * @param nodePlanOrder 用户节点订单
     * @return 结果
     */
    int saveNodePlanOrder(NodePlanOrder nodePlanOrder);
}
