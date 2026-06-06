package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserYieldRateConfigMapper;
import com.xms.dao.domain.UserYieldRateConfig;
import com.xms.dao.service.IUserYieldRateConfigService;

/**
 * 用户收益率规则配置Service业务层处理
 *
 * @author xms
 * @date 2026-03-05
 */
@Service
public class UserYieldRateConfigServiceImpl extends XmsDataServiceImpl<UserYieldRateConfigMapper, UserYieldRateConfig> implements IUserYieldRateConfigService
{


    /**
     * 查询用户收益率规则配置列表
     *
     *
     * @param userYieldRateConfig 用户收益率规则配置
     * @return 用户收益率规则配置
     */
    @Override
    public List<UserYieldRateConfig> selectUserYieldRateConfigList(UserYieldRateConfig userYieldRateConfig)
    {
        return baseMapper.selectUserYieldRateConfigList(userYieldRateConfig);
    }

}
