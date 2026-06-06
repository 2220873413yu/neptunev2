package com.xms.dao.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.FlashExchangeRecord;

/**
 * 闪兑记录Mapper接口
 *
 * @author xms
 * @date 2025-08-14
 */
public interface FlashExchangeRecordMapper extends XmsMapper<FlashExchangeRecord>
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
