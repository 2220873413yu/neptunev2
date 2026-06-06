package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3MiningPackageRewardRecordMapper;
import com.xms.dao.domain.W3MiningPackageRewardRecord;
import com.xms.dao.service.IW3MiningPackageRewardRecordService;

/**
 * 矿机订单奖励分发记录Service业务层处理
 *
 * @author xms
 * @date 2025-04-14
 */
@Service
public class W3MiningPackageRewardRecordServiceImpl extends XmsDataServiceImpl<W3MiningPackageRewardRecordMapper, W3MiningPackageRewardRecord> implements IW3MiningPackageRewardRecordService
{


    /**
     * 查询矿机订单奖励分发记录列表
     *
     *
     * @param w3MiningPackageRewardRecord 矿机订单奖励分发记录
     * @return 矿机订单奖励分发记录
     */
    @Override
    public List<W3MiningPackageRewardRecord> selectW3MiningPackageRewardRecordList(W3MiningPackageRewardRecord w3MiningPackageRewardRecord)
    {
        return baseMapper.selectW3MiningPackageRewardRecordList(w3MiningPackageRewardRecord);
    }

}
