package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.RewardPoolBatch;

/**
 * 分红批次记录Service接口
 *
 * @author xms
 * @date 2025-12-08
 */
public interface IRewardPoolBatchService extends XmsDataService<RewardPoolBatch>
{

    /**
     * 查询分红批次记录列表
     *
     * @param rewardPoolBatch 分红批次记录
     * @return 分红批次记录集合
     */
    public List<RewardPoolBatch> selectRewardPoolBatchList(RewardPoolBatch rewardPoolBatch);

}
