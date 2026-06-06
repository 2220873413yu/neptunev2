package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserStakePosition;

/**
 * 用户质押持仓汇总Service接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface IUserStakePositionService extends XmsDataService<UserStakePosition>
{

    /**
     * 查询用户质押持仓汇总列表
     *
     * @param userStakePosition 用户质押持仓汇总
     * @return 用户质押持仓汇总集合
     */
    public List<UserStakePosition> selectUserStakePositionList(UserStakePosition userStakePosition);

}
