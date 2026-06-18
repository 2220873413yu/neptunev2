package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.StakeOrder;
import com.xms.dao.entity.dto.StakeDepositSourceAmountDto;

/**
 * 质押订单Mapper接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface StakeOrderMapper extends XmsMapper<StakeOrder>
{
    /**
     * 查询质押订单列表
     *
     * @param stakeOrder 质押订单
     * @return 质押订单集合
     */
    public List<StakeOrder> selectStakeOrderList(StakeOrder stakeOrder);

    /**
     * 按入金来源统计成功订单入金总额
     *
     * @return 入金来源金额统计集合
     */
    public List<StakeDepositSourceAmountDto> selectDepositSourceAmountStats();

}
