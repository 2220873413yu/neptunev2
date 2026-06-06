package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3MiningClaimRecord;

/**
 * 矿机静态领取记录Mapper接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface W3MiningClaimRecordMapper extends XmsMapper<W3MiningClaimRecord>
{
    /**
     * 查询矿机静态领取记录列表
     *
     * @param w3MiningClaimRecord 矿机静态领取记录
     * @return 矿机静态领取记录集合
     */
    public List<W3MiningClaimRecord> selectW3MiningClaimRecordList(W3MiningClaimRecord w3MiningClaimRecord);

}
