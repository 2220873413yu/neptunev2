package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.WithdrawFeeShareStatDayMapper;
import com.xms.dao.domain.WithdrawFeeShareStatDay;
import com.xms.dao.service.IWithdrawFeeShareStatDayService;

/**
 * 提现手续费分红Service业务层处理
 *
 * @author xms
 * @date 2025-11-23
 */
@Service
public class WithdrawFeeShareStatDayServiceImpl extends XmsDataServiceImpl<WithdrawFeeShareStatDayMapper, WithdrawFeeShareStatDay> implements IWithdrawFeeShareStatDayService
{


    /**
     * 查询提现手续费分红列表
     *
     *
     * @param withdrawFeeShareStatDay 提现手续费分红
     * @return 提现手续费分红
     */
    @Override
    public List<WithdrawFeeShareStatDay> selectWithdrawFeeShareStatDayList(WithdrawFeeShareStatDay withdrawFeeShareStatDay)
    {
        return baseMapper.selectWithdrawFeeShareStatDayList(withdrawFeeShareStatDay);
    }

}
