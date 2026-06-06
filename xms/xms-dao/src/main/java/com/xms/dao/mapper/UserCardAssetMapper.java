package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserCardAsset;

/**
 * 卡片持有信息Mapper接口
 *
 * @author xms
 * @date 2025-12-05
 */
public interface UserCardAssetMapper extends XmsMapper<UserCardAsset>
{
    /**
     * 查询卡片持有信息列表
     *
     * @param userCardAsset 卡片持有信息
     * @return 卡片持有信息集合
     */
    public List<UserCardAsset> selectUserCardAssetList(UserCardAsset userCardAsset);

}
