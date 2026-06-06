package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.xms.common.exception.ServiceException;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.NodePlanMapper;
import com.xms.dao.domain.NodePlan;
import com.xms.dao.service.INodePlanService;

/**
 * 认购节点配置Service业务层处理
 *
 * @author xms
 * @date 2026-01-16
 */
@Service
public class NodePlanServiceImpl extends XmsDataServiceImpl<NodePlanMapper, NodePlan> implements INodePlanService
{


    /**
     * 查询认购节点配置列表
     *
     *
     * @param nodePlan 认购节点配置
     * @return 认购节点配置
     */
    @Override
    public List<NodePlan> selectNodePlanList(NodePlan nodePlan)
    {
        return baseMapper.selectNodePlanList(nodePlan);
    }

	@Override
	public int updateNodePlanById(NodePlan req) {
		NodePlan queryNodePlan = lambdaQuery()
			.eq(NodePlan::getId, req.getId())
			.one();


		if(req.getPurchaseAmount() == null || req.getPurchaseAmount().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("认购金额不能小于等于0");
		}
		if(req.getWeightCoefficient() == null || req.getWeightCoefficient().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("权重系数不能为空");
		}
		if(req.getStudioSubsidyRatio() == null || req.getStudioSubsidyRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("分红上限不能为空");
		}

		if(req.getAnnualRate() == null || req.getAnnualRate().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("年化收益率不能为空");
		}
		lambdaUpdate()
			.eq(NodePlan::getId,req.getId())
			.eq(NodePlan::getStatus, queryNodePlan.getStatus())
			.set(NodePlan::getSortOrder,req.getSortOrder())
			//分红比例
			.set(NodePlan::getWeightCoefficient,req.getWeightCoefficient())
			//分红上限
			.set(NodePlan::getStudioSubsidyRatio,req.getStudioSubsidyRatio())
			.set(NodePlan::getUpdateTime,new Date() )
			.update();
		return 1;
	}
}
