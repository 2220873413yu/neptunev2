package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.NodePlan;

/**
 * 认购节点配置Mapper接口
 *
 * @author xms
 * @date 2026-01-16
 */
public interface NodePlanMapper extends XmsMapper<NodePlan>
{
    /**
     * 查询认购节点配置列表
     *
     * @param nodePlan 认购节点配置
     * @return 认购节点配置集合
     */
    public List<NodePlan> selectNodePlanList(NodePlan nodePlan);

}
