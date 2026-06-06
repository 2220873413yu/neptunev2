package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserStakePosition;

/**
 * 用户质押持仓汇总Mapper接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface UserStakePositionMapper extends XmsMapper<UserStakePosition>
{
    /**
     * 查询用户质押持仓汇总列表
     *
     * @param userStakePosition 用户质押持仓汇总
     * @return 用户质押持仓汇总集合
     */
    public List<UserStakePosition> selectUserStakePositionList(UserStakePosition userStakePosition);

}
