package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.xms.common.constant.SysConstant;
import com.xms.common.exception.ServiceException;
import com.xms.dao.domain.W3MiningPackageOrder;
import com.xms.dao.service.IW3MiningPackageOrderService;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3MiningPackageMapper;
import com.xms.dao.domain.W3MiningPackage;
import com.xms.dao.service.IW3MiningPackageService;

/**
 * 挖矿套餐Service业务层处理
 *
 * @author xms
 * @date 2025-04-10
 */
@Service
@Slf4j
public class W3MiningPackageServiceImpl extends XmsDataServiceImpl<W3MiningPackageMapper, W3MiningPackage> implements IW3MiningPackageService
{

	@Autowired
	private IW3MiningPackageOrderService w3MiningPackageOrderService;

    /**
     * 查询挖矿套餐列表
     *
     *
     * @param w3MiningPackage 挖矿套餐
     * @return 挖矿套餐
     */
    @Override
    public List<W3MiningPackage> selectW3MiningPackageList(W3MiningPackage w3MiningPackage)
    {
        return baseMapper.selectW3MiningPackageList(w3MiningPackage);
    }

	@Override
	public int saveRecord(W3MiningPackage w3MiningPackage) {
		w3MiningPackage.setMultipliedValue(w3MiningPackage.getMultipliedValue().setScale(SysConstant.TWO, RoundingMode.DOWN));
		if(w3MiningPackage.getMultipliedValue().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("收益倍数不能小于0");
		}
		if(w3MiningPackage.getWfRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("FTN支付比例不能小于0");
		}
		if(w3MiningPackage.getUsdtRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("USDT支付比例不能小于0");
		}
		BigDecimal totalRatio = w3MiningPackage.getWfRatio().add(w3MiningPackage.getUsdtRatio());
		if(totalRatio.compareTo(SysConstant.BAIFENBI)!=0){
			throw new ServiceException("wf比例和USDT比例总和必须为100");
		}
		if(w3MiningPackage.getDayRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("日利率不能小于0");
		}
		if(w3MiningPackage.getMinBuyPrice().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("最小购买价格不能小于0");
		}
		boolean save = save(w3MiningPackage);
		if(!save){
			throw new ServiceException("新增失败");
		}
		return 1;
	}

	@Override
	public int updateRecordById(W3MiningPackage w3MiningPackage) {
		w3MiningPackage.setMultipliedValue(w3MiningPackage.getMultipliedValue().setScale(SysConstant.TWO, RoundingMode.DOWN));
		if(w3MiningPackage.getMultipliedValue().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("收益倍数不能小于0");
		}
		if(w3MiningPackage.getWfRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("FTN支付比例不能小于0");
		}
		if(w3MiningPackage.getUsdtRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("USDT支付比例不能小于0");
		}
		if(w3MiningPackage.getType().equals(SysConstant.ONE)){
			BigDecimal totalRatio = w3MiningPackage.getWfRatio().add(w3MiningPackage.getUsdtRatio());
			if(totalRatio.compareTo(SysConstant.BAIFENBI)!=0){
				throw new ServiceException("FTN比例和USDT比例总和必须为100");
			}
		}

		if(w3MiningPackage.getDayRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("日利率不能小于0");
		}
		if(w3MiningPackage.getMinBuyPrice().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("最小购买价格不能小于0");
		}

		if(w3MiningPackage.getBuyMaxLimit()<0){
			throw new ServiceException("最大购买数量限制不能小于0");
		}
		boolean i = updateById(w3MiningPackage);
		if(!i){
			throw new ServiceException("更新失败");
		}
		return 1;
	}

	/**
	 * 同步正在挖矿中的日利率
	 * @param id
	 * @return
	 */
	@Override
	public int syncDailyInterestRate(Long id) {
		W3MiningPackage w3MiningPackage = lambdaQuery()
			.eq(W3MiningPackage::getType, SysConstant.ONE)
			.eq(W3MiningPackage::getId, id)
			.one();
		if(w3MiningPackage == null){
			throw new ServiceException("当前矿机套餐不存在");
		}
		w3MiningPackageOrderService.lambdaUpdate()
			.eq(W3MiningPackageOrder::getMiningPackageId,id)
			.in(W3MiningPackageOrder::getStatus,SysConstant.ONE,SysConstant.ZERO)
			.set(W3MiningPackageOrder::getDayRatio,w3MiningPackage.getDayRatio())
			.update();
		log.info("同步正在挖矿中的日利率 套餐id:{}",id);
		return 1;
	}
}
