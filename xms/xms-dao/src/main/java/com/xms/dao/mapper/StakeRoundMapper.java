package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.StakeRound;

/**
 * 全局质押轮次Mapper接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface StakeRoundMapper extends XmsMapper<StakeRound>
{
    /**
     * 查询全局质押轮次列表
     *
     * @param stakeRound 全局质押轮次
     * @return 全局质押轮次集合
     */
    public List<StakeRound> selectStakeRoundList(StakeRound stakeRound);

}
