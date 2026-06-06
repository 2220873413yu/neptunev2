package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserWealthVault;

/**
 * 用户财富表Mapper接口
 *
 * @author xms
 * @date 2026-03-05
 */
public interface UserWealthVaultMapper extends XmsMapper<UserWealthVault>
{
    /**
     * 查询用户财富表列表
     *
     * @param userWealthVault 用户财富表
     * @return 用户财富表集合
     */
    public List<UserWealthVault> selectUserWealthVaultList(UserWealthVault userWealthVault);

}
