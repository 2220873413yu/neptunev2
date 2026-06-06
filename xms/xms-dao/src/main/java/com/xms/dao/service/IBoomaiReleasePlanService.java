package com.xms.dao.service;

import java.util.List;

import com.xms.dao.entity.dto.InterestPackDto;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.BoomaiReleasePlan;

/**
 * boomai收益线性释放计划Service接口
 *
 * @author xms
 * @date 2025-11-19
 */
public interface IBoomaiReleasePlanService extends XmsDataService<BoomaiReleasePlan>
{

    /**
     * 查询boomai收益线性释放计划列表
     *
     * @param boomaiReleasePlan boomai收益线性释放计划
     * @return boomai收益线性释放计划集合
     */
    public List<BoomaiReleasePlan> selectBoomaiReleasePlanList(BoomaiReleasePlan boomaiReleasePlan);

    List<InterestPackDto> getMyInterestPacks(Long userId);
}
