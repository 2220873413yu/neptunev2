package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.StakeRoundMapper;
import com.xms.dao.domain.StakeRound;
import com.xms.dao.service.IStakeRoundService;

/**
 * 全局质押轮次Service业务层处理
 *
 * @author xms
 * @date 2026-03-06
 */
@Service
public class StakeRoundServiceImpl extends XmsDataServiceImpl<StakeRoundMapper, StakeRound> implements IStakeRoundService
{


    /**
     * 查询全局质押轮次列表
     *
     *
     * @param stakeRound 全局质押轮次
     * @return 全局质押轮次
     */
    @Override
    public List<StakeRound> selectStakeRoundList(StakeRound stakeRound)
    {
        return baseMapper.selectStakeRoundList(stakeRound);
    }

}
