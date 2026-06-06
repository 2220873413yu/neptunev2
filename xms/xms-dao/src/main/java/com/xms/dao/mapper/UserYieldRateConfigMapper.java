package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserYieldRateConfig;

/**
 * 用户收益率规则配置Mapper接口
 *
 * @author xms
 * @date 2026-03-05
 */
public interface UserYieldRateConfigMapper extends XmsMapper<UserYieldRateConfig>
{
    /**
     * 查询用户收益率规则配置列表
     *
     * @param userYieldRateConfig 用户收益率规则配置
     * @return 用户收益率规则配置集合
     */
    public List<UserYieldRateConfig> selectUserYieldRateConfigList(UserYieldRateConfig userYieldRateConfig);

}
