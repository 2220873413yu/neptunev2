package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.UserMoneySnapshot;

/**
 * 用户余额快照Mapper接口
 *
 * @author xms
 * @date 2026-03-16
 */
public interface UserMoneySnapshotMapper extends XmsMapper<UserMoneySnapshot>
{
    /**
     * 查询用户余额快照列表
     *
     * @param userMoneySnapshot 用户余额快照
     * @return 用户余额快照集合
     */
    public List<UserMoneySnapshot> selectUserMoneySnapshotList(UserMoneySnapshot userMoneySnapshot);

}
