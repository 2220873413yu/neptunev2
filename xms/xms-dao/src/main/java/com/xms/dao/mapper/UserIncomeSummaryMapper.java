package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserIncomeSummary;

/**
 * 用户收益信息Mapper接口
 *
 * @author xms
 * @date 2025-08-14
 */
public interface UserIncomeSummaryMapper extends XmsMapper<UserIncomeSummary>
{
    /**
     * 查询用户收益信息列表
     *
     * @param userIncomeSummary 用户收益信息
     * @return 用户收益信息集合
     */
    public List<UserIncomeSummary> selectUserIncomeSummaryList(UserIncomeSummary userIncomeSummary);

}
