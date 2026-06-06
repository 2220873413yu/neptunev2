package com.xms.dao.service;

import java.util.List;

import com.xms.dao.entity.req.AddMiningOrderReq;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.MiningPackageOrder;

/**
 * 基金订单Service接口
 *
 * @author xms
 * @date 2025-08-07
 */
public interface IMiningPackageOrderService extends XmsDataService<MiningPackageOrder>
{

    /**
     * 查询基金订单列表
     *
     * @param miningPackageOrder 基金订单
     * @return 基金订单集合
     */
    public List<MiningPackageOrder> selectMiningPackageOrderList(MiningPackageOrder miningPackageOrder);



	/**
	 * 添加手动拨付基金订单
	 * @param req
	 * @return
	 */
	int saveMiningOrder(AddMiningOrderReq req);

	/**
	 * 查询订单天数列表
	 * @return
	 */
	List<Integer> getDistinctDays();
}
