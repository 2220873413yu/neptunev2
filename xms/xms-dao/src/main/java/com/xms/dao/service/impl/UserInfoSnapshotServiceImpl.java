package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserInfoSnapshotMapper;
import com.xms.dao.domain.UserInfoSnapshot;
import com.xms.dao.service.IUserInfoSnapshotService;

/**
 * 用户信息快照Service业务层处理
 *
 * @author xms
 * @date 2026-03-16
 */
@Service
public class UserInfoSnapshotServiceImpl extends XmsDataServiceImpl<UserInfoSnapshotMapper, UserInfoSnapshot> implements IUserInfoSnapshotService
{


    /**
     * 查询用户信息快照列表
     *
     *
     * @param userInfoSnapshot 用户信息快照
     * @return 用户信息快照
     */
    @Override
    public List<UserInfoSnapshot> selectUserInfoSnapshotList(UserInfoSnapshot userInfoSnapshot)
    {
        return baseMapper.selectUserInfoSnapshotList(userInfoSnapshot);
    }

}
