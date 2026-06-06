package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.PtbDailyPriceMapper;
import com.xms.dao.domain.PtbDailyPrice;
import com.xms.dao.service.IPtbDailyPriceService;

/**
 * 平台币每日价格Service业务层处理
 *
 * @author xms
 * @date 2025-08-09
 */
@Service
public class PtbDailyPriceServiceImpl extends XmsDataServiceImpl<PtbDailyPriceMapper, PtbDailyPrice> implements IPtbDailyPriceService
{


    /**
     * 查询平台币每日价格列表
     *
     *
     * @param ptbDailyPrice 平台币每日价格
     * @return 平台币每日价格
     */
    @Override
    public List<PtbDailyPrice> selectPtbDailyPriceList(PtbDailyPrice ptbDailyPrice)
    {
        return baseMapper.selectPtbDailyPriceList(ptbDailyPrice);
    }

}
