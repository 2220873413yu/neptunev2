package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3UserRecommendRewardConfigMapper;
import com.xms.dao.domain.W3UserRecommendRewardConfig;
import com.xms.dao.service.IW3UserRecommendRewardConfigService;

/**
 * 用户推荐奖配置Service业务层处理
 *
 * @author xms
 * @date 2025-08-09
 */
@Service
public class W3UserRecommendRewardConfigServiceImpl extends XmsDataServiceImpl<W3UserRecommendRewardConfigMapper, W3UserRecommendRewardConfig> implements IW3UserRecommendRewardConfigService
{


    /**
     * 查询用户推荐奖配置列表
     *
     *
     * @param w3UserRecommendRewardConfig 用户推荐奖配置
     * @return 用户推荐奖配置
     */
    @Override
    public List<W3UserRecommendRewardConfig> selectW3UserRecommendRewardConfigList(W3UserRecommendRewardConfig w3UserRecommendRewardConfig)
    {
        return baseMapper.selectW3UserRecommendRewardConfigList(w3UserRecommendRewardConfig);
    }

}
