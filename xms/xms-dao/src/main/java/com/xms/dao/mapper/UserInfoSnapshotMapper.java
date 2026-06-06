package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserInfoSnapshot;

/**
 * 用户信息快照Mapper接口
 *
 * @author xms
 * @date 2026-03-16
 */
public interface UserInfoSnapshotMapper extends XmsMapper<UserInfoSnapshot>
{
    /**
     * 查询用户信息快照列表
     *
     * @param userInfoSnapshot 用户信息快照
     * @return 用户信息快照集合
     */
    public List<UserInfoSnapshot> selectUserInfoSnapshotList(UserInfoSnapshot userInfoSnapshot);

}
