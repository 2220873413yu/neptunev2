package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.WealthVaultStageConfig;

/**
 * 财富仓阶段解锁配置Service接口
 *
 * @author xms
 * @date 2026-03-11
 */
public interface IWealthVaultStageConfigService extends XmsDataService<WealthVaultStageConfig>
{

    /**
     * 查询财富仓阶段解锁配置列表
     *
     * @param wealthVaultStageConfig 财富仓阶段解锁配置
     * @return 财富仓阶段解锁配置集合
     */
    public List<WealthVaultStageConfig> selectWealthVaultStageConfigList(WealthVaultStageConfig wealthVaultStageConfig);

}
