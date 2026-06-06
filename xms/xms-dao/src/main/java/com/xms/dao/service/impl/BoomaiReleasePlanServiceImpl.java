package com.xms.dao.service.impl;

import java.util.List;

import com.xms.dao.entity.dto.InterestPackDto;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.BoomaiReleasePlanMapper;
import com.xms.dao.domain.BoomaiReleasePlan;
import com.xms.dao.service.IBoomaiReleasePlanService;

/**
 * boomai收益线性释放计划Service业务层处理
 *
 * @author xms
 * @date 2025-11-19
 */
@Service
public class BoomaiReleasePlanServiceImpl extends XmsDataServiceImpl<BoomaiReleasePlanMapper, BoomaiReleasePlan> implements IBoomaiReleasePlanService
{


    /**
     * 查询boomai收益线性释放计划列表
     *
     *
     * @param boomaiReleasePlan boomai收益线性释放计划
     * @return boomai收益线性释放计划
     */
    @Override
    public List<BoomaiReleasePlan> selectBoomaiReleasePlanList(BoomaiReleasePlan boomaiReleasePlan)
    {
        return baseMapper.selectBoomaiReleasePlanList(boomaiReleasePlan);
    }

	@Override
	public List<InterestPackDto> getMyInterestPacks(Long userId) {
		return baseMapper.getMyInterestPacks(userId);
	}
}
