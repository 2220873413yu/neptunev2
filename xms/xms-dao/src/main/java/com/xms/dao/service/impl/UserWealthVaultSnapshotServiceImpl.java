package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserWealthVaultSnapshotMapper;
import com.xms.dao.domain.UserWealthVaultSnapshot;
import com.xms.dao.service.IUserWealthVaultSnapshotService;

/**
 * 用户财富仓快照Service业务层处理
 *
 * @author xms
 * @date 2026-03-16
 */
@Service
public class UserWealthVaultSnapshotServiceImpl extends XmsDataServiceImpl<UserWealthVaultSnapshotMapper, UserWealthVaultSnapshot> implements IUserWealthVaultSnapshotService
{


    /**
     * 查询用户财富仓快照列表
     *
     *
     * @param userWealthVaultSnapshot 用户财富仓快照
     * @return 用户财富仓快照
     */
    @Override
    public List<UserWealthVaultSnapshot> selectUserWealthVaultSnapshotList(UserWealthVaultSnapshot userWealthVaultSnapshot)
    {
        return baseMapper.selectUserWealthVaultSnapshotList(userWealthVaultSnapshot);
    }

}
