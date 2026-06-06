package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.WealthVaultStageConfigMapper;
import com.xms.dao.domain.WealthVaultStageConfig;
import com.xms.dao.service.IWealthVaultStageConfigService;

/**
 * 财富仓阶段解锁配置Service业务层处理
 *
 * @author xms
 * @date 2026-03-11
 */
@Service
public class WealthVaultStageConfigServiceImpl extends XmsDataServiceImpl<WealthVaultStageConfigMapper, WealthVaultStageConfig> implements IWealthVaultStageConfigService
{


    /**
     * 查询财富仓阶段解锁配置列表
     *
     *
     * @param wealthVaultStageConfig 财富仓阶段解锁配置
     * @return 财富仓阶段解锁配置
     */
    @Override
    public List<WealthVaultStageConfig> selectWealthVaultStageConfigList(WealthVaultStageConfig wealthVaultStageConfig)
    {
        return baseMapper.selectWealthVaultStageConfigList(wealthVaultStageConfig);
    }

}
