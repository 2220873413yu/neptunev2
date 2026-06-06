package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.FlashExchangeRecordMapper;
import com.xms.dao.domain.FlashExchangeRecord;
import com.xms.dao.service.IFlashExchangeRecordService;

/**
 * 闪兑记录Service业务层处理
 *
 * @author xms
 * @date 2025-08-14
 */
@Service
public class FlashExchangeRecordServiceImpl extends XmsDataServiceImpl<FlashExchangeRecordMapper, FlashExchangeRecord> implements IFlashExchangeRecordService
{


    /**
     * 查询闪兑记录列表
     *
     *
     * @param flashExchangeRecord 闪兑记录
     * @return 闪兑记录
     */
    @Override
    public List<FlashExchangeRecord> selectFlashExchangeRecordList(FlashExchangeRecord flashExchangeRecord)
    {
        return baseMapper.selectFlashExchangeRecordList(flashExchangeRecord);
    }

	@Override
	public BigDecimal queryTotalFlashExchangeAmount() {
		return baseMapper.queryTotalFlashExchangeAmount();
	}
}
