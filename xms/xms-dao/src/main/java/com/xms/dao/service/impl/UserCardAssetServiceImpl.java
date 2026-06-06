package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserCardAssetMapper;
import com.xms.dao.domain.UserCardAsset;
import com.xms.dao.service.IUserCardAssetService;

/**
 * 卡片持有信息Service业务层处理
 *
 * @author xms
 * @date 2025-12-05
 */
@Service
public class UserCardAssetServiceImpl extends XmsDataServiceImpl<UserCardAssetMapper, UserCardAsset> implements IUserCardAssetService
{


    /**
     * 查询卡片持有信息列表
     *
     *
     * @param userCardAsset 卡片持有信息
     * @return 卡片持有信息
     */
    @Override
    public List<UserCardAsset> selectUserCardAssetList(UserCardAsset userCardAsset)
    {
        return baseMapper.selectUserCardAssetList(userCardAsset);
    }

}
