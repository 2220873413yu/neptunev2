package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserWealthVaultMapper;
import com.xms.dao.domain.UserWealthVault;
import com.xms.dao.service.IUserWealthVaultService;

/**
 * 用户财富表Service业务层处理
 *
 * @author xms
 * @date 2026-03-05
 */
@Service
public class UserWealthVaultServiceImpl extends XmsDataServiceImpl<UserWealthVaultMapper, UserWealthVault> implements IUserWealthVaultService
{


    /**
     * 查询用户财富表列表
     *
     *
     * @param userWealthVault 用户财富表
     * @return 用户财富表
     */
    @Override
    public List<UserWealthVault> selectUserWealthVaultList(UserWealthVault userWealthVault)
    {
        return baseMapper.selectUserWealthVaultList(userWealthVault);
    }

}
