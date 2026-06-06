package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3MiningPackage;

/**
 * 挖矿套餐Service接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface IW3MiningPackageService extends XmsDataService<W3MiningPackage>
{

    /**
     * 查询挖矿套餐列表
     *
     * @param w3MiningPackage 挖矿套餐
     * @return 挖矿套餐集合
     */
    public List<W3MiningPackage> selectW3MiningPackageList(W3MiningPackage w3MiningPackage);

    int saveRecord(W3MiningPackage w3MiningPackage);

	int updateRecordById(W3MiningPackage w3MiningPackage);

	/**
	 * 同步正在挖矿中的日利率
	 * @param id
	 * @return
	 */
	int syncDailyInterestRate(Long id);
}
