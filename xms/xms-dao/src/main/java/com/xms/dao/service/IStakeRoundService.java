package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.StakeRound;

/**
 * 全局质押轮次Service接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface IStakeRoundService extends XmsDataService<StakeRound>
{

    /**
     * 查询全局质押轮次列表
     *
     * @param stakeRound 全局质押轮次
     * @return 全局质押轮次集合
     */
    public List<StakeRound> selectStakeRoundList(StakeRound stakeRound);

}
