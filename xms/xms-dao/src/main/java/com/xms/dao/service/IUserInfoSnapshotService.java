package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserInfoSnapshot;

/**
 * 用户信息快照Service接口
 *
 * @author xms
 * @date 2026-03-16
 */
public interface IUserInfoSnapshotService extends XmsDataService<UserInfoSnapshot>
{

    /**
     * 查询用户信息快照列表
     *
     * @param userInfoSnapshot 用户信息快照
     * @return 用户信息快照集合
     */
    public List<UserInfoSnapshot> selectUserInfoSnapshotList(UserInfoSnapshot userInfoSnapshot);

}
