package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserWealthVaultSnapshot;

/**
 * 用户财富仓快照Mapper接口
 *
 * @author xms
 * @date 2026-03-16
 */
public interface UserWealthVaultSnapshotMapper extends XmsMapper<UserWealthVaultSnapshot>
{
    /**
     * 查询用户财富仓快照列表
     *
     * @param userWealthVaultSnapshot 用户财富仓快照
     * @return 用户财富仓快照集合
     */
    public List<UserWealthVaultSnapshot> selectUserWealthVaultSnapshotList(UserWealthVaultSnapshot userWealthVaultSnapshot);

}
