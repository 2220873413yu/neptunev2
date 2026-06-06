package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.StakeDailySnapshot;

/**
 * 每日质押数据快照Mapper接口
 *
 * @author xms
 * @date 2026-03-30
 */
public interface StakeDailySnapshotMapper extends XmsMapper<StakeDailySnapshot>
{
    /**
     * 查询每日质押数据快照列表
     *
     * @param stakeDailySnapshot 每日质押数据快照
     * @return 每日质押数据快照集合
     */
    public List<StakeDailySnapshot> selectStakeDailySnapshotList(StakeDailySnapshot stakeDailySnapshot);

}
