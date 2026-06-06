package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.CardUpgradeLog;

/**
 * 卡片升级日志Mapper接口
 *
 * @author xms
 * @date 2025-12-06
 */
public interface CardUpgradeLogMapper extends XmsMapper<CardUpgradeLog>
{
    /**
     * 查询卡片升级日志列表
     *
     * @param cardUpgradeLog 卡片升级日志
     * @return 卡片升级日志集合
     */
    public List<CardUpgradeLog> selectCardUpgradeLogList(CardUpgradeLog cardUpgradeLog);

}
