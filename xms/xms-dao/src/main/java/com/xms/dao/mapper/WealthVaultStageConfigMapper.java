package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.WealthVaultStageConfig;

/**
 * 财富仓阶段解锁配置Mapper接口
 *
 * @author xms
 * @date 2026-03-11
 */
public interface WealthVaultStageConfigMapper extends XmsMapper<WealthVaultStageConfig>
{
    /**
     * 查询财富仓阶段解锁配置列表
     *
     * @param wealthVaultStageConfig 财富仓阶段解锁配置
     * @return 财富仓阶段解锁配置集合
     */
    public List<WealthVaultStageConfig> selectWealthVaultStageConfigList(WealthVaultStageConfig wealthVaultStageConfig);

}
