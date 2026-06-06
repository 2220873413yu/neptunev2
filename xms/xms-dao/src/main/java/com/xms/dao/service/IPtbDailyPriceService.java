package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.PtbDailyPrice;

/**
 * 平台币每日价格Service接口
 *
 * @author xms
 * @date 2025-08-09
 */
public interface IPtbDailyPriceService extends XmsDataService<PtbDailyPrice>
{

    /**
     * 查询平台币每日价格列表
     *
     * @param ptbDailyPrice 平台币每日价格
     * @return 平台币每日价格集合
     */
    public List<PtbDailyPrice> selectPtbDailyPriceList(PtbDailyPrice ptbDailyPrice);

}
