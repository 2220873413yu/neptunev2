package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserStakePositionFlowMapper;
import com.xms.dao.domain.UserStakePositionFlow;
import com.xms.dao.service.IUserStakePositionFlowService;

/**
 * 用户持仓变动流水Service业务层处理
 *
 * @author xms
 * @date 2026-03-06
 */
@Service
public class UserStakePositionFlowServiceImpl extends XmsDataServiceImpl<UserStakePositionFlowMapper, UserStakePositionFlow> implements IUserStakePositionFlowService
{


    /**
     * 查询用户持仓变动流水列表
     *
     *
     * @param userStakePositionFlow 用户持仓变动流水
     * @return 用户持仓变动流水
     */
    @Override
    public List<UserStakePositionFlow> selectUserStakePositionFlowList(UserStakePositionFlow userStakePositionFlow)
    {
        return baseMapper.selectUserStakePositionFlowList(userStakePositionFlow);
    }

}
