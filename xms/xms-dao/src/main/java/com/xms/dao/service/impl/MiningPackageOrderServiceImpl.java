package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xms.common.constant.ConstantType;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.CollectionUtil;
import com.xms.common.utils.SecurityUtils;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.MiningPackage;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.req.AddMiningOrderReq;
import com.xms.dao.service.IMiningPackageService;
import com.xms.dao.service.UserInfoService;
import com.xms.dao.service.UserWalletService;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.MiningPackageOrderMapper;
import com.xms.dao.domain.MiningPackageOrder;
import com.xms.dao.service.IMiningPackageOrderService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 基金订单Service业务层处理
 *
 * @author xms
 * @date 2025-08-07
 */
@Service
public class MiningPackageOrderServiceImpl extends XmsDataServiceImpl<MiningPackageOrderMapper, MiningPackageOrder> implements IMiningPackageOrderService
{


	/**
	 * 计算运行了第n天,会获得多少钱。最大不能超过本金
	 * @param principal 本金
	 * @param dailyRate 日利率 例如1%
	 * @param runDays 运行天数
	 * @return
	 */
	public static BigDecimal getMaxReward(BigDecimal principal,BigDecimal dailyRate,Integer runDays){
		BigDecimal pendingReward = principal.multiply(dailyRate).multiply(new BigDecimal(runDays));
		if(pendingReward.compareTo(principal)>0){
			pendingReward = principal;
		}
		return pendingReward;
	}

    /**
     * 查询基金订单列表
     *
     *
     * @param miningPackageOrder 基金订单
     * @return 基金订单
     */
    @Override
    public List<MiningPackageOrder> selectMiningPackageOrderList(MiningPackageOrder miningPackageOrder)
    {
        return baseMapper.selectMiningPackageOrderList(miningPackageOrder);
    }


	/**
	 * 添加手动拨付基金订单
	 *
	 * @param req
	 * @return
	 */
	@Override
	public int saveMiningOrder(AddMiningOrderReq req) {
		return 1;
	}

	@Override
	public List<Integer> getDistinctDays() {
		return baseMapper.getDistinctDays();
	}
}
