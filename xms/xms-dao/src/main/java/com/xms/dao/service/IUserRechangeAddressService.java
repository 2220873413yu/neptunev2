package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserRechangeAddress;

/**
 * 用户充值地址Service接口
 *
 * @author xms
 * @date 2025-08-04
 */
public interface IUserRechangeAddressService extends XmsDataService<UserRechangeAddress>
{

    /**
     * 查询用户充值地址列表
     *
     * @param userRechangeAddress 用户充值地址
     * @return 用户充值地址集合
     */
    public List<UserRechangeAddress> selectUserRechangeAddressList(UserRechangeAddress userRechangeAddress);

}
