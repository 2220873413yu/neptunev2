package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.xms.common.constant.SysConstant;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.StakeDailySnapshotMapper;
import com.xms.dao.domain.StakeDailySnapshot;
import com.xms.dao.service.IStakeDailySnapshotService;

/**
 * 每日质押数据快照Service业务层处理
 *
 * @author xms
 * @date 2026-03-30
 */
@Service
public class StakeDailySnapshotServiceImpl extends XmsDataServiceImpl<StakeDailySnapshotMapper, StakeDailySnapshot> implements IStakeDailySnapshotService
{


    /**
     * 查询每日质押数据快照列表
     *
     *
     * @param stakeDailySnapshot 每日质押数据快照
     * @return 每日质押数据快照
     */
    @Override
    public List<StakeDailySnapshot> selectStakeDailySnapshotList(StakeDailySnapshot stakeDailySnapshot)
    {
        return baseMapper.selectStakeDailySnapshotList(stakeDailySnapshot);
    }

	public static void main(String[] args) {
		System.out.println(new BigDecimal("15419.0000000")
			.divide(new BigDecimal("13078260.4546000"), 3, RoundingMode.DOWN)
			);
	}
}
