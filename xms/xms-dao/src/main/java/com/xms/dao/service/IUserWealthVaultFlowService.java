package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserWealthVaultFlow;

/**
 * 用户财富仓流水Service接口
 *
 * @author xms
 * @date 2026-03-11
 */
public interface IUserWealthVaultFlowService extends XmsDataService<UserWealthVaultFlow>
{

    /**
     * 查询用户财富仓流水列表
     *
     * @param userWealthVaultFlow 用户财富仓流水
     * @return 用户财富仓流水集合
     */
    public List<UserWealthVaultFlow> selectUserWealthVaultFlowList(UserWealthVaultFlow userWealthVaultFlow);

}
