package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserRechangeAddressMapper;
import com.xms.dao.domain.UserRechangeAddress;
import com.xms.dao.service.IUserRechangeAddressService;

/**
 * 用户充值地址Service业务层处理
 *
 * @author xms
 * @date 2025-08-04
 */
@Service
public class UserRechangeAddressServiceImpl extends XmsDataServiceImpl<UserRechangeAddressMapper, UserRechangeAddress> implements IUserRechangeAddressService
{


    /**
     * 查询用户充值地址列表
     *
     *
     * @param userRechangeAddress 用户充值地址
     * @return 用户充值地址
     */
    @Override
    public List<UserRechangeAddress> selectUserRechangeAddressList(UserRechangeAddress userRechangeAddress)
    {
        return baseMapper.selectUserRechangeAddressList(userRechangeAddress);
    }

}
