package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.InteractRewardConfig;

/**
 * 互动奖比例配置Service接口
 *
 * @author xms
 * @date 2025-11-25
 */
public interface IInteractRewardConfigService extends XmsDataService<InteractRewardConfig>
{

    /**
     * 查询互动奖比例配置列表
     *
     * @param interactRewardConfig 互动奖比例配置
     * @return 互动奖比例配置集合
     */
    public List<InteractRewardConfig> selectInteractRewardConfigList(InteractRewardConfig interactRewardConfig);

}
