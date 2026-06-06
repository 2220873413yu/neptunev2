package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserWealthVault;

/**
 * 用户财富表Service接口
 *
 * @author xms
 * @date 2026-03-05
 */
public interface IUserWealthVaultService extends XmsDataService<UserWealthVault>
{

    /**
     * 查询用户财富表列表
     *
     * @param userWealthVault 用户财富表
     * @return 用户财富表集合
     */
    public List<UserWealthVault> selectUserWealthVaultList(UserWealthVault userWealthVault);

}
