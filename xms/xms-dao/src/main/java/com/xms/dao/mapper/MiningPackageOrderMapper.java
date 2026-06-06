package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.MiningPackageOrder;
import org.apache.ibatis.annotations.Select;

/**
 * 基金订单Mapper接口
 *
 * @author xms
 * @date 2025-08-07
 */
public interface MiningPackageOrderMapper extends XmsMapper<MiningPackageOrder>
{
    /**
     * 查询基金订单列表
     *
     * @param miningPackageOrder 基金订单
     * @return 基金订单集合
     */
    public List<MiningPackageOrder> selectMiningPackageOrderList(MiningPackageOrder miningPackageOrder);

	/**
	 * 获取所有 distinct days
	 * @return
	 */
	@Select("SELECT DISTINCT days FROM t_mining_package_order ORDER BY days")
    List<Integer> getDistinctDays();

}
