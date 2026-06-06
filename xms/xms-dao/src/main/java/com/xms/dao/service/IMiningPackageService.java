package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.MiningPackage;

/**
 * 基金套餐Service接口
 *
 * @author xms
 * @date 2025-08-07
 */
public interface IMiningPackageService extends XmsDataService<MiningPackage>
{

    /**
     * 查询基金套餐列表
     *
     * @param miningPackage 基金套餐
     * @return 基金套餐集合
     */
    public List<MiningPackage> selectMiningPackageList(MiningPackage miningPackage);

	int updateRecordById(MiningPackage miningPackage);
}
