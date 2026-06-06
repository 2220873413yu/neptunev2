package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.RewardPoolConfig;

/**
 * 分红池配置Mapper接口
 *
 * @author xms
 * @date 2025-12-08
 */
public interface RewardPoolConfigMapper extends XmsMapper<RewardPoolConfig>
{
    /**
     * 查询分红池配置列表
     *
     * @param rewardPoolConfig 分红池配置
     * @return 分红池配置集合
     */
    public List<RewardPoolConfig> selectRewardPoolConfigList(RewardPoolConfig rewardPoolConfig);

}
