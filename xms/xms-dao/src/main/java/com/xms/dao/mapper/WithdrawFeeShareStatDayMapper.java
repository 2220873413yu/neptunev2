package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.WithdrawFeeShareStatDay;

/**
 * 提现手续费分红Mapper接口
 *
 * @author xms
 * @date 2025-11-23
 */
public interface WithdrawFeeShareStatDayMapper extends XmsMapper<WithdrawFeeShareStatDay>
{
    /**
     * 查询提现手续费分红列表
     *
     * @param withdrawFeeShareStatDay 提现手续费分红
     * @return 提现手续费分红集合
     */
    public List<WithdrawFeeShareStatDay> selectWithdrawFeeShareStatDayList(WithdrawFeeShareStatDay withdrawFeeShareStatDay);

}
