package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.StakeOrder;
import com.xms.dao.entity.dto.StakeDepositSourceAmountDto;
import com.xms.dao.entity.dto.UserStakePerformanceSourceStatDto;

/**
 * 质押订单Service接口
 *
 * @author xms
 * @date 2026-03-06
 */
public interface IStakeOrderService extends XmsDataService<StakeOrder>
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

    /**
     * 按用户和入金来源汇总成功订单的ACP入金数量，用于真实/映射业绩历史回填。
     *
     * @return 用户入金来源业绩汇总集合
     */
    public List<UserStakePerformanceSourceStatDto> selectUserPerformanceSourceStats();

}
