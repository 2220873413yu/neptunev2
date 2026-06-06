package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3UserRecommendRewardConfig;

/**
 * 用户推荐奖配置Service接口
 *
 * @author xms
 * @date 2025-08-09
 */
public interface IW3UserRecommendRewardConfigService extends XmsDataService<W3UserRecommendRewardConfig>
{

    /**
     * 查询用户推荐奖配置列表
     *
     * @param w3UserRecommendRewardConfig 用户推荐奖配置
     * @return 用户推荐奖配置集合
     */
    public List<W3UserRecommendRewardConfig> selectW3UserRecommendRewardConfigList(W3UserRecommendRewardConfig w3UserRecommendRewardConfig);

}
