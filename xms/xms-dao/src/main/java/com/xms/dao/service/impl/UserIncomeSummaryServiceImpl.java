package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserIncomeSummaryMapper;
import com.xms.dao.domain.UserIncomeSummary;
import com.xms.dao.service.IUserIncomeSummaryService;

/**
 * 用户收益信息Service业务层处理
 *
 * @author xms
 * @date 2025-08-14
 */
@Service
public class UserIncomeSummaryServiceImpl extends XmsDataServiceImpl<UserIncomeSummaryMapper, UserIncomeSummary> implements IUserIncomeSummaryService
{


    /**
     * 查询用户收益信息列表
     *
     *
     * @param userIncomeSummary 用户收益信息
     * @return 用户收益信息
     */
    @Override
    public List<UserIncomeSummary> selectUserIncomeSummaryList(UserIncomeSummary userIncomeSummary)
    {
        return baseMapper.selectUserIncomeSummaryList(userIncomeSummary);
    }

}
