package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.CardPackage;

/**
 * 卡片套餐Service接口
 *
 * @author xms
 * @date 2025-12-04
 */
public interface ICardPackageService extends XmsDataService<CardPackage>
{

    /**
     * 查询卡片套餐列表
     *
     * @param cardPackage 卡片套餐
     * @return 卡片套餐集合
     */
    public List<CardPackage> selectCardPackageList(CardPackage cardPackage);

    /**
     * 修改卡片套餐
     *
     * @param cardPackage 卡片套餐
     * @return 结果
     */
    int updateRecordById(CardPackage cardPackage);
}
