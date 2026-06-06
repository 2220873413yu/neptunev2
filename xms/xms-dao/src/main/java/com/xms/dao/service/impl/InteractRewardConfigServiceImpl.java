package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.InteractRewardConfigMapper;
import com.xms.dao.domain.InteractRewardConfig;
import com.xms.dao.service.IInteractRewardConfigService;

/**
 * 互动奖比例配置Service业务层处理
 *
 * @author xms
 * @date 2025-11-25
 */
@Service
public class InteractRewardConfigServiceImpl extends XmsDataServiceImpl<InteractRewardConfigMapper, InteractRewardConfig> implements IInteractRewardConfigService
{


    /**
     * 查询互动奖比例配置列表
     *
     *
     * @param interactRewardConfig 互动奖比例配置
     * @return 互动奖比例配置
     */
    @Override
    public List<InteractRewardConfig> selectInteractRewardConfigList(InteractRewardConfig interactRewardConfig)
    {
        return baseMapper.selectInteractRewardConfigList(interactRewardConfig);
    }

}
