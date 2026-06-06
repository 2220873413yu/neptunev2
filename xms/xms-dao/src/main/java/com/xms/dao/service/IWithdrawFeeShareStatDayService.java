package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.WithdrawFeeShareStatDay;

/**
 * 提现手续费分红Service接口
 *
 * @author xms
 * @date 2025-11-23
 */
public interface IWithdrawFeeShareStatDayService extends XmsDataService<WithdrawFeeShareStatDay>
{

    /**
     * 查询提现手续费分红列表
     *
     * @param withdrawFeeShareStatDay 提现手续费分红
     * @return 提现手续费分红集合
     */
    public List<WithdrawFeeShareStatDay> selectWithdrawFeeShareStatDayList(WithdrawFeeShareStatDay withdrawFeeShareStatDay);

}
