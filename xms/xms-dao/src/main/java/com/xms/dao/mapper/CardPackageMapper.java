package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.CardPackage;

/**
 * 卡片套餐Mapper接口
 *
 * @author xms
 * @date 2025-12-04
 */
public interface CardPackageMapper extends XmsMapper<CardPackage>
{
    /**
     * 查询卡片套餐列表
     *
     * @param cardPackage 卡片套餐
     * @return 卡片套餐集合
     */
    public List<CardPackage> selectCardPackageList(CardPackage cardPackage);

}
