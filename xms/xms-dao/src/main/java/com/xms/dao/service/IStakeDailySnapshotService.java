package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.StakeDailySnapshot;

/**
 * 每日质押数据快照Service接口
 *
 * @author xms
 * @date 2026-03-30
 */
public interface IStakeDailySnapshotService extends XmsDataService<StakeDailySnapshot>
{

    /**
     * 查询每日质押数据快照列表
     *
     * @param stakeDailySnapshot 每日质押数据快照
     * @return 每日质押数据快照集合
     */
    public List<StakeDailySnapshot> selectStakeDailySnapshotList(StakeDailySnapshot stakeDailySnapshot);

}
