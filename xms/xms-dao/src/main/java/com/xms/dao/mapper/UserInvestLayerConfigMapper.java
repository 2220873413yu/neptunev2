package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserInvestLayerConfig;

/**
 * 层奖配置Mapper接口
 *
 * @author xms
 * @date 2026-03-05
 */
public interface UserInvestLayerConfigMapper extends XmsMapper<UserInvestLayerConfig>
{
    /**
     * 查询层奖配置列表
     *
     * @param userInvestLayerConfig 层奖配置
     * @return 层奖配置集合
     */
    public List<UserInvestLayerConfig> selectUserInvestLayerConfigList(UserInvestLayerConfig userInvestLayerConfig);

}
