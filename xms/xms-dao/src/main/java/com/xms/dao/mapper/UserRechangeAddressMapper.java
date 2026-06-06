package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserRechangeAddress;

/**
 * 用户充值地址Mapper接口
 *
 * @author xms
 * @date 2025-08-04
 */
public interface UserRechangeAddressMapper extends XmsMapper<UserRechangeAddress>
{
    /**
     * 查询用户充值地址列表
     *
     * @param userRechangeAddress 用户充值地址
     * @return 用户充值地址集合
     */
    public List<UserRechangeAddress> selectUserRechangeAddressList(UserRechangeAddress userRechangeAddress);

}
