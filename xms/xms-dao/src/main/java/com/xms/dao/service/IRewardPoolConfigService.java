package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.RewardPoolConfig;

/**
 * 分红池配置Service接口
 *
 * @author xms
 * @date 2025-12-08
 */
public interface IRewardPoolConfigService extends XmsDataService<RewardPoolConfig>
{

    /**
     * 查询分红池配置列表
     *
     * @param rewardPoolConfig 分红池配置
     * @return 分红池配置集合
     */
    public List<RewardPoolConfig> selectRewardPoolConfigList(RewardPoolConfig rewardPoolConfig);

	/**
	 * 更新记录
	 * @param rewardPoolConfig
	 * @return
	 */
	int updateRecordById(RewardPoolConfig rewardPoolConfig);
}
