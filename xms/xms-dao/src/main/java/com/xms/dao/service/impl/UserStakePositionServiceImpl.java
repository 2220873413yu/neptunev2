package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserStakePositionMapper;
import com.xms.dao.domain.UserStakePosition;
import com.xms.dao.service.IUserStakePositionService;

/**
 * 用户质押持仓汇总Service业务层处理
 *
 * @author xms
 * @date 2026-03-06
 */
@Service
public class UserStakePositionServiceImpl extends XmsDataServiceImpl<UserStakePositionMapper, UserStakePosition> implements IUserStakePositionService
{


    /**
     * 查询用户质押持仓汇总列表
     *
     *
     * @param userStakePosition 用户质押持仓汇总
     * @return 用户质押持仓汇总
     */
    @Override
    public List<UserStakePosition> selectUserStakePositionList(UserStakePosition userStakePosition)
    {
        return baseMapper.selectUserStakePositionList(userStakePosition);
    }

}
