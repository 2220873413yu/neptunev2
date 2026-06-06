package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.CardUpgradeLogMapper;
import com.xms.dao.domain.CardUpgradeLog;
import com.xms.dao.service.ICardUpgradeLogService;

/**
 * 卡片升级日志Service业务层处理
 *
 * @author xms
 * @date 2025-12-06
 */
@Service
public class CardUpgradeLogServiceImpl extends XmsDataServiceImpl<CardUpgradeLogMapper, CardUpgradeLog> implements ICardUpgradeLogService
{


    /**
     * 查询卡片升级日志列表
     *
     *
     * @param cardUpgradeLog 卡片升级日志
     * @return 卡片升级日志
     */
    @Override
    public List<CardUpgradeLog> selectCardUpgradeLogList(CardUpgradeLog cardUpgradeLog)
    {
        return baseMapper.selectCardUpgradeLogList(cardUpgradeLog);
    }

}
