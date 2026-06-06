package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserWealthVaultFlowMapper;
import com.xms.dao.domain.UserWealthVaultFlow;
import com.xms.dao.service.IUserWealthVaultFlowService;

/**
 * 用户财富仓流水Service业务层处理
 *
 * @author xms
 * @date 2026-03-11
 */
@Service
public class UserWealthVaultFlowServiceImpl extends XmsDataServiceImpl<UserWealthVaultFlowMapper, UserWealthVaultFlow> implements IUserWealthVaultFlowService
{


    /**
     * 查询用户财富仓流水列表
     *
     *
     * @param userWealthVaultFlow 用户财富仓流水
     * @return 用户财富仓流水
     */
    @Override
    public List<UserWealthVaultFlow> selectUserWealthVaultFlowList(UserWealthVaultFlow userWealthVaultFlow)
    {
        return baseMapper.selectUserWealthVaultFlowList(userWealthVaultFlow);
    }

}
