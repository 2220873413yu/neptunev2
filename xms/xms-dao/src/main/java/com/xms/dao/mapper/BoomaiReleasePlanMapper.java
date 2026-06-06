package com.xms.dao.mapper;

import java.util.List;

import com.xms.dao.entity.dto.InterestPackDto;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.BoomaiReleasePlan;
import org.apache.ibatis.annotations.Param;

/**
 * boomai收益线性释放计划Mapper接口
 *
 * @author xms
 * @date 2025-11-19
 */
public interface BoomaiReleasePlanMapper extends XmsMapper<BoomaiReleasePlan>
{
    /**
     * 查询boomai收益线性释放计划列表
     *
     * @param boomaiReleasePlan boomai收益线性释放计划
     * @return boomai收益线性释放计划集合
     */
    public List<BoomaiReleasePlan> selectBoomaiReleasePlanList(BoomaiReleasePlan boomaiReleasePlan);

    List<InterestPackDto> getMyInterestPacks(@Param("userId") Long userId);
}
