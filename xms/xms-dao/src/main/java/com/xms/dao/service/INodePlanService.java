package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.NodePlan;

/**
 * 认购节点配置Service接口
 *
 * @author xms
 * @date 2026-01-16
 */
public interface INodePlanService extends XmsDataService<NodePlan>
{

    /**
     * 查询认购节点配置列表
     *
     * @param nodePlan 认购节点配置
     * @return 认购节点配置集合
     */
    public List<NodePlan> selectNodePlanList(NodePlan nodePlan);

	/**
	 * 修改认购节点配置
	 * @param nodePlan
	 * @return
	 */
	int updateNodePlanById(NodePlan nodePlan);
}
