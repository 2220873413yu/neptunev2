package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.MiningPackageMapper;
import com.xms.dao.domain.MiningPackage;
import com.xms.dao.service.IMiningPackageService;

/**
 * 基金套餐Service业务层处理
 *
 * @author xms
 * @date 2025-08-07
 */
@Service
public class MiningPackageServiceImpl extends XmsDataServiceImpl<MiningPackageMapper, MiningPackage> implements IMiningPackageService {


	/**
	 * 查询基金套餐列表
	 *
	 * @param miningPackage 基金套餐
	 * @return 基金套餐
	 */
	@Override
	public List<MiningPackage> selectMiningPackageList(MiningPackage miningPackage) {
		return baseMapper.selectMiningPackageList(miningPackage);
	}

	@Override
	public int updateRecordById(MiningPackage miningPackage) {
		if (miningPackage.getDayRatio().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("日利率不能小于等于0");
		}

		if (miningPackage.getMinBuyPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("最少购买金额限制不能小于等于0");
		}

		if (miningPackage.getMinPenaltyRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("最低违约金比例不能小于0");
		}

		if (miningPackage.getType().equals(0)) {
			//活期
			miningPackage.setDay(0);
			miningPackage.setPenaltyRate(BigDecimal.ZERO);
			miningPackage.setDailyPenaltyReduction(BigDecimal.ZERO);
		} else  if(miningPackage.getType().equals(1)){
			//定期
			if (miningPackage.getDay() <= 0){
				throw new RuntimeException("基金天数不能小于等于0");
			}

			if (miningPackage.getDailyPenaltyReduction().compareTo(BigDecimal.ZERO) <= 0) {
				throw new RuntimeException("每日违约金递减率不能小于等于0");
			}
			if (miningPackage.getPenaltyRate().compareTo(BigDecimal.ZERO) <= 0) {
				throw new RuntimeException("违约金比例不能小于等于0");
			}
		}else{
			//体验式基金
			if (miningPackage.getDay() <= 0){
				throw new RuntimeException("基金天数不能小于等于0");
			}

			miningPackage.setPenaltyRate(BigDecimal.ZERO);
			miningPackage.setDailyPenaltyReduction(BigDecimal.ZERO);
			if(miningPackage.getMinBuyPrice().compareTo(BigDecimal.ZERO)<=0){
				throw new RuntimeException("最少购买金额限制不能小于等于0");
			}
			if(miningPackage.getMaxBuyPrice().compareTo(BigDecimal.ZERO)<=0){
				throw new RuntimeException("最大购买金额限制不能小于等于0");
			}
			//判断最大购买金额不能小于最小购买金额
			if(miningPackage.getMaxBuyPrice().compareTo(miningPackage.getMinBuyPrice())<0){
				throw new RuntimeException("最大购买金额不能小于最小购买金额");
			}
		}


		updateById(miningPackage);
		return 1;
	}

}
