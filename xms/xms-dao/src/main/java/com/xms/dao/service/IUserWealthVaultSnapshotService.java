package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserWealthVaultSnapshot;

/**
 * 用户财富仓快照Service接口
 *
 * @author xms
 * @date 2026-03-16
 */
public interface IUserWealthVaultSnapshotService extends XmsDataService<UserWealthVaultSnapshot>
{

    /**
     * 查询用户财富仓快照列表
     *
     * @param userWealthVaultSnapshot 用户财富仓快照
     * @return 用户财富仓快照集合
     */
    public List<UserWealthVaultSnapshot> selectUserWealthVaultSnapshotList(UserWealthVaultSnapshot userWealthVaultSnapshot);

}
