package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.AirdropRound;

/**
 * 空投轮次配置Mapper接口
 *
 * @author xms
 * @date 2026-01-01
 */
public interface AirdropRoundMapper extends XmsMapper<AirdropRound>
{
    /**
     * 查询空投轮次配置列表
     *
     * @param airdropRound 空投轮次配置
     * @return 空投轮次配置集合
     */
    public List<AirdropRound> selectAirdropRoundList(AirdropRound airdropRound);

}
