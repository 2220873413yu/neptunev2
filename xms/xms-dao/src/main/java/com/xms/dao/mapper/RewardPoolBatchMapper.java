package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.RewardPoolBatch;

/**
 * 分红批次记录Mapper接口
 *
 * @author xms
 * @date 2025-12-08
 */
public interface RewardPoolBatchMapper extends XmsMapper<RewardPoolBatch>
{
    /**
     * 查询分红批次记录列表
     *
     * @param rewardPoolBatch 分红批次记录
     * @return 分红批次记录集合
     */
    public List<RewardPoolBatch> selectRewardPoolBatchList(RewardPoolBatch rewardPoolBatch);

}
