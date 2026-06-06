package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3MiningClaimRecordMapper;
import com.xms.dao.domain.W3MiningClaimRecord;
import com.xms.dao.service.IW3MiningClaimRecordService;

/**
 * 矿机静态领取记录Service业务层处理
 *
 * @author xms
 * @date 2025-04-10
 */
@Service
public class W3MiningClaimRecordServiceImpl extends XmsDataServiceImpl<W3MiningClaimRecordMapper, W3MiningClaimRecord> implements IW3MiningClaimRecordService
{


    /**
     * 查询矿机静态领取记录列表
     *
     *
     * @param w3MiningClaimRecord 矿机静态领取记录
     * @return 矿机静态领取记录
     */
    @Override
    public List<W3MiningClaimRecord> selectW3MiningClaimRecordList(W3MiningClaimRecord w3MiningClaimRecord)
    {
        return baseMapper.selectW3MiningClaimRecordList(w3MiningClaimRecord);
    }

}
