package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserMoneySnapshot;

/**
 * 用户余额快照Service接口
 *
 * @author xms
 * @date 2026-03-16
 */
public interface IUserMoneySnapshotService extends XmsDataService<UserMoneySnapshot>
{

    /**
     * 查询用户余额快照列表
     *
     * @param userMoneySnapshot 用户余额快照
     * @return 用户余额快照集合
     */
    public List<UserMoneySnapshot> selectUserMoneySnapshotList(UserMoneySnapshot userMoneySnapshot);

}
