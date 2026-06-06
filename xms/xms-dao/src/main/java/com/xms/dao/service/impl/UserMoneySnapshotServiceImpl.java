package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserMoneySnapshotMapper;
import com.xms.dao.domain.UserMoneySnapshot;
import com.xms.dao.service.IUserMoneySnapshotService;

/**
 * 用户余额快照Service业务层处理
 *
 * @author xms
 * @date 2026-03-16
 */
@Service
public class UserMoneySnapshotServiceImpl extends XmsDataServiceImpl<UserMoneySnapshotMapper, UserMoneySnapshot> implements IUserMoneySnapshotService
{


    /**
     * 查询用户余额快照列表
     *
     *
     * @param userMoneySnapshot 用户余额快照
     * @return 用户余额快照
     */
    @Override
    public List<UserMoneySnapshot> selectUserMoneySnapshotList(UserMoneySnapshot userMoneySnapshot)
    {
        return baseMapper.selectUserMoneySnapshotList(userMoneySnapshot);
    }

}
