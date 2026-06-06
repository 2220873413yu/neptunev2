package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.xms.common.constant.SysConstant;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.spring.SpringUtils;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3MiningPackageOrderMapper;
import com.xms.dao.domain.W3MiningPackageOrder;
import com.xms.dao.service.IW3MiningPackageOrderService;

/**
 * w3矿机订单Service业务层处理
 *
 * @author xms
 * @date 2025-04-10
 */
@Service
@Slf4j
public class W3MiningPackageOrderServiceImpl extends XmsDataServiceImpl<W3MiningPackageOrderMapper, W3MiningPackageOrder> implements IW3MiningPackageOrderService
{


    /**
     * 查询w3矿机订单列表
     *
     *
     * @param w3MiningPackageOrder w3矿机订单
     * @return w3矿机订单
     */
    @Override
    public List<W3MiningPackageOrder> selectW3MiningPackageOrderList(W3MiningPackageOrder w3MiningPackageOrder)
    {

		List<W3MiningPackageOrder> w3MiningPackageOrders = baseMapper.selectW3MiningPackageOrderList(w3MiningPackageOrder);

		if(!CollectionUtil.isEmpty(w3MiningPackageOrders)){
			//设置正在运行中的矿机日利率
			W3MiningPackageOrder defaultPackage = SpringUtils.getBean(W3MiningPackageOrderServiceImpl.class).lambdaQuery()
				.eq(W3MiningPackageOrder::getType, SysConstant.ZERO)
				.last("limit 1")
				.one();

			for (W3MiningPackageOrder miningPackageOrder : w3MiningPackageOrders) {
				if(miningPackageOrder.getType().equals(SysConstant.ZERO) && miningPackageOrder.getStatus().equals(SysConstant.ZERO)){
					miningPackageOrder.setDayRatio(defaultPackage.getDayRatio());
				}
			}
		}
		return w3MiningPackageOrders;
    }

	@Override
	public int updateRecordById(W3MiningPackageOrder req) {
		W3MiningPackageOrder w3MiningPackageOrder = lambdaQuery()
			.eq(W3MiningPackageOrder::getId, req.getId())
			.eq(W3MiningPackageOrder::getType,1)
			.eq(W3MiningPackageOrder::getStatus, SysConstant.ZERO)
			.one();
		if(w3MiningPackageOrder == null){
			throw new ServiceException("矿机记录不存在,请刷新后重试");
		}
		if(req.getDayRatio().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("日利率不能小于等于0");
		}
		if(req.getDayRatio().compareTo(w3MiningPackageOrder.getDayRatio())!=0){
			lambdaUpdate()
				.eq(W3MiningPackageOrder::getStatus, SysConstant.ZERO)
				.eq(W3MiningPackageOrder::getId, req.getId())
				.set(W3MiningPackageOrder::getDayRatio, req.getDayRatio())
				.update();
			log.info("修改矿机日利率 矿机id:{},日利率:{}",req.getId(),req.getDayRatio());
		}
		return 1;
	}
}
