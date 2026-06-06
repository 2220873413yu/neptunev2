package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserStakePositionFlow;

/**
 * 用户持仓变动流水Service接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface IUserStakePositionFlowService extends XmsDataService<UserStakePositionFlow>
{

    /**
     * 查询用户持仓变动流水列表
     *
     * @param userStakePositionFlow 用户持仓变动流水
     * @return 用户持仓变动流水集合
     */
    public List<UserStakePositionFlow> selectUserStakePositionFlowList(UserStakePositionFlow userStakePositionFlow);

}
