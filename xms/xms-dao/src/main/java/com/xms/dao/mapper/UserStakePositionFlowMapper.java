package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserStakePositionFlow;

/**
 * 用户持仓变动流水Mapper接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface UserStakePositionFlowMapper extends XmsMapper<UserStakePositionFlow>
{
    /**
     * 查询用户持仓变动流水列表
     *
     * @param userStakePositionFlow 用户持仓变动流水
     * @return 用户持仓变动流水集合
     */
    public List<UserStakePositionFlow> selectUserStakePositionFlowList(UserStakePositionFlow userStakePositionFlow);

}
