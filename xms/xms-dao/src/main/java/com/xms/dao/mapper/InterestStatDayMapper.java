package com.xms.dao.mapper;

import java.util.List;

import com.xms.dao.entity.dto.InterestStatDayDto;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.InterestStatDay;
import org.apache.ibatis.annotations.Param;

/**
 * 每日利息汇总Mapper接口
 *
 * @author xms
 * @date 2025-11-25
 */
public interface InterestStatDayMapper extends XmsMapper<InterestStatDay>
{
    /**
     * 查询每日利息汇总列表
     *
     * @param interestStatDay 每日利息汇总
     * @return 每日利息汇总集合
     */
    public List<InterestStatDay> selectInterestStatDayList(InterestStatDay interestStatDay);

    List<InterestStatDayDto> todayInterest(@Param("userId") Long userId);
}
