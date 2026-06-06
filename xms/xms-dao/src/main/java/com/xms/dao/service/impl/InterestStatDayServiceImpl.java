package com.xms.dao.service.impl;

import java.util.List;

import com.xms.dao.entity.dto.InterestStatDayDto;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.InterestStatDayMapper;
import com.xms.dao.domain.InterestStatDay;
import com.xms.dao.service.IInterestStatDayService;

/**
 * 每日利息汇总Service业务层处理
 *
 * @author xms
 * @date 2025-11-25
 */
@Service
public class InterestStatDayServiceImpl extends XmsDataServiceImpl<InterestStatDayMapper, InterestStatDay> implements IInterestStatDayService
{


    /**
     * 查询每日利息汇总列表
     *
     *
     * @param interestStatDay 每日利息汇总
     * @return 每日利息汇总
     */
    @Override
    public List<InterestStatDay> selectInterestStatDayList(InterestStatDay interestStatDay)
    {
        return baseMapper.selectInterestStatDayList(interestStatDay);
    }

	@Override
	public List<InterestStatDayDto> todayInterest(Long userId) {
		return baseMapper.todayInterest(userId);
	}
}
