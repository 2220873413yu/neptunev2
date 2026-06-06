package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3MiningPackage;

/**
 * 挖矿套餐Mapper接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface W3MiningPackageMapper extends XmsMapper<W3MiningPackage>
{
    /**
     * 查询挖矿套餐列表
     *
     * @param w3MiningPackage 挖矿套餐
     * @return 挖矿套餐集合
     */
    public List<W3MiningPackage> selectW3MiningPackageList(W3MiningPackage w3MiningPackage);

}
