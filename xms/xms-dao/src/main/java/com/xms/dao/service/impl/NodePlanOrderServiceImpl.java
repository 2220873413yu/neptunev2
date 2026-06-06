package com.xms.dao.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.xms.common.exception.ServiceException;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.NodePlan;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.service.INodePlanService;
import com.xms.dao.service.UserInfoService;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.NodePlanOrderMapper;
import com.xms.dao.domain.NodePlanOrder;
import com.xms.dao.service.INodePlanOrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户节点订单Service业务层处理
 *
 * @author xms
 * @date 2026-01-16
 */
@Service
public class NodePlanOrderServiceImpl extends XmsDataServiceImpl<NodePlanOrderMapper, NodePlanOrder> implements INodePlanOrderService
{
	@Autowired
	private INodePlanService nodePlanService;

	@Autowired
	private UserInfoService userInfoService;

    /**
     * 查询用户节点订单列表
     *
     *
     * @param nodePlanOrder 用户节点订单
     * @return 用户节点订单
     */
    @Override
    public List<NodePlanOrder> selectNodePlanOrderList(NodePlanOrder nodePlanOrder)
    {
        return baseMapper.selectNodePlanOrderList(nodePlanOrder);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveNodePlanOrder(NodePlanOrder req) {
		return 1;
	}

	private void updateTeamNodeNum(UserInfo userInfo) {
		boolean update;
		if(userInfo.getInviteUserId()!=null){
			//直推用户+节点
			update = userInfoService.lambdaUpdate()
				.eq(UserInfo::getUserId, userInfo.getInviteUserId())
				.setSql("sub_performance = sub_performance + 1 ")
				.update();
			if(!update){
				throw new ServiceException("更新用户节点失败");
			}
			//团队用户
			update = userInfoService.lambdaUpdate()
				.in(UserInfo::getUserId, userInfo.getParentIds())
				.setSql("umbrella_performance = umbrella_performance + 1 ")
				.update();
			if(!update){
				throw new ServiceException("更新团队用户节点失败");
			}
		}
	}
}
