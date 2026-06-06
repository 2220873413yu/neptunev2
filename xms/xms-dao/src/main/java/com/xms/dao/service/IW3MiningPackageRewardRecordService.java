package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3MiningPackageRewardRecord;

/**
 * 矿机订单奖励分发记录Service接口
 *
 * @author xms
 * @date 2025-04-14
 */
public interface IW3MiningPackageRewardRecordService extends XmsDataService<W3MiningPackageRewardRecord>
{

    /**
     * 查询矿机订单奖励分发记录列表
     *
     * @param w3MiningPackageRewardRecord 矿机订单奖励分发记录
     * @return 矿机订单奖励分发记录集合
     */
    public List<W3MiningPackageRewardRecord> selectW3MiningPackageRewardRecordList(W3MiningPackageRewardRecord w3MiningPackageRewardRecord);

}
