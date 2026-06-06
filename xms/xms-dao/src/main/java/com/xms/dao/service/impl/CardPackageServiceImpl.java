package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xms.common.constant.ConstantStatic;
import com.xms.common.exception.ServiceException;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.CardPackageMapper;
import com.xms.dao.domain.CardPackage;
import com.xms.dao.service.ICardPackageService;

/**
 * 卡片套餐Service业务层处理
 *
 * @author xms
 * @date 2025-12-04
 */
@Service
public class CardPackageServiceImpl extends XmsDataServiceImpl<CardPackageMapper, CardPackage> implements ICardPackageService
{


    /**
     * 查询卡片套餐列表
     *
     *
     * @param cardPackage 卡片套餐
     * @return 卡片套餐
     */
    @Override
    public List<CardPackage> selectCardPackageList(CardPackage cardPackage)
    {
        return baseMapper.selectCardPackageList(cardPackage);
    }

	@Override
	public int updateRecordById(CardPackage req) {
		if(req.getPrice().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("价格不能小于等于0");
		}

		if(req.getComputingPower().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("算力不能小于等于0");
		}

		if(req.getSort() <0){
			throw new ServiceException("排序不能小于0");
		}

		req.setValidNum3GiftRatio(req.getValidNum3GiftRatio().setScale(ConstantStatic.twoScale, ConstantStatic.roundingModeNew));
		if(req.getValidNum3GiftRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("赠送BDAI比例不能小于0");
		}
		if(req.getValidNum3GiftRatio().compareTo(new BigDecimal("150"))<0){
			throw new ServiceException("赠送BDAI比例不能大于150%");
		}

		updateById(req);
		return 1;
	}
}
