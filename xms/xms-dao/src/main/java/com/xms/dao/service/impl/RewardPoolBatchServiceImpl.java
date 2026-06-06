package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.RewardPoolBatchMapper;
import com.xms.dao.domain.RewardPoolBatch;
import com.xms.dao.service.IRewardPoolBatchService;

/**
 * 分红批次记录Service业务层处理
 *
 * @author xms
 * @date 2025-12-08
 */
@Service
public class RewardPoolBatchServiceImpl extends XmsDataServiceImpl<RewardPoolBatchMapper, RewardPoolBatch> implements IRewardPoolBatchService
{


    /**
     * 查询分红批次记录列表
     *
     *
     * @param rewardPoolBatch 分红批次记录
     * @return 分红批次记录
     */
    @Override
    public List<RewardPoolBatch> selectRewardPoolBatchList(RewardPoolBatch rewardPoolBatch)
    {
        return baseMapper.selectRewardPoolBatchList(rewardPoolBatch);
    }

}
