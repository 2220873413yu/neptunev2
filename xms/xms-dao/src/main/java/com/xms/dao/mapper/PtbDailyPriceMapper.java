package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.PtbDailyPrice;

/**
 * 平台币每日价格Mapper接口
 *
 * @author xms
 * @date 2025-08-09
 */
public interface PtbDailyPriceMapper extends XmsMapper<PtbDailyPrice>
{
    /**
     * 查询平台币每日价格列表
     *
     * @param ptbDailyPrice 平台币每日价格
     * @return 平台币每日价格集合
     */
    public List<PtbDailyPrice> selectPtbDailyPriceList(PtbDailyPrice ptbDailyPrice);

}
