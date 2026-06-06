package com.xms.dao.service;

import java.math.BigDecimal;
import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.FlashExchangeRecord;

/**
 * 闪兑记录Service接口
 *
 * @author xms
 * @date 2025-08-14
 */
public interface IFlashExchangeRecordService extends XmsDataService<FlashExchangeRecord>
{

    /**
     * 查询闪兑记录列表
     *
     * @param flashExchangeRecord 闪兑记录
     * @return 闪兑记录集合
     */
    public List<FlashExchangeRecord> selectFlashExchangeRecordList(FlashExchangeRecord flashExchangeRecord);

	/**
	 * 查询闪兑总金额
	 * @return
	 */
	BigDecimal queryTotalFlashExchangeAmount();
}
