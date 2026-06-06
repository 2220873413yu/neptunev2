package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3MiningClaimRecord;

/**
 * 矿机静态领取记录Service接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface IW3MiningClaimRecordService extends XmsDataService<W3MiningClaimRecord>
{

    /**
     * 查询矿机静态领取记录列表
     *
     * @param w3MiningClaimRecord 矿机静态领取记录
     * @return 矿机静态领取记录集合
     */
    public List<W3MiningClaimRecord> selectW3MiningClaimRecordList(W3MiningClaimRecord w3MiningClaimRecord);

}
