package com.xms.dao.service;

import java.util.List;

import com.xms.dao.entity.dto.InterestStatDayDto;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.InterestStatDay;

/**
 * 每日利息汇总Service接口
 *
 * @author xms
 * @date 2025-11-25
 */
public interface IInterestStatDayService extends XmsDataService<InterestStatDay>
{

    /**
     * 查询每日利息汇总列表
     *
     * @param interestStatDay 每日利息汇总
     * @return 每日利息汇总集合
     */
    public List<InterestStatDay> selectInterestStatDayList(InterestStatDay interestStatDay);

    List<InterestStatDayDto> todayInterest(Long userId);
}
