package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3UserLevelConfig;

/**
 * 用户等级考核配置Mapper接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface W3UserLevelConfigMapper extends XmsMapper<W3UserLevelConfig>
{
    /**
     * 查询用户等级考核配置列表
     *
     * @param w3UserLevelConfig 用户等级考核配置
     * @return 用户等级考核配置集合
     */
    public List<W3UserLevelConfig> selectW3UserLevelConfigList(W3UserLevelConfig w3UserLevelConfig);

}
