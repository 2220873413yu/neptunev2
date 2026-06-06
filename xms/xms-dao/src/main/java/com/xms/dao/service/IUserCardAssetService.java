package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserCardAsset;

/**
 * 卡片持有信息Service接口
 *
 * @author xms
 * @date 2025-12-05
 */
public interface IUserCardAssetService extends XmsDataService<UserCardAsset>
{

    /**
     * 查询卡片持有信息列表
     *
     * @param userCardAsset 卡片持有信息
     * @return 卡片持有信息集合
     */
    public List<UserCardAsset> selectUserCardAssetList(UserCardAsset userCardAsset);

}
