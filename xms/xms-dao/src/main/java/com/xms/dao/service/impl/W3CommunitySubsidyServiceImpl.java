package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xms.common.exception.ServiceException;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.W3CommunitySubsidyMapper;
import com.xms.dao.domain.W3CommunitySubsidy;
import com.xms.dao.service.IW3CommunitySubsidyService;

/**
 * 社区补贴配置Service业务层处理
 *
 * @author xms
 * @date 2025-04-10
 */
@Service
@Slf4j
public class W3CommunitySubsidyServiceImpl extends XmsDataServiceImpl<W3CommunitySubsidyMapper, W3CommunitySubsidy> implements IW3CommunitySubsidyService
{


    /**
     * 查询社区补贴配置列表
     *
     *
     * @param w3CommunitySubsidy 社区补贴配置
     * @return 社区补贴配置
     */
    @Override
    public List<W3CommunitySubsidy> selectW3CommunitySubsidyList(W3CommunitySubsidy w3CommunitySubsidy)
    {
        return baseMapper.selectW3CommunitySubsidyList(w3CommunitySubsidy);
    }

	@Override
	public int updateRecordById(W3CommunitySubsidy req) {
		log.info("社区奖修改 req:{}",req);
		if(req.getId() ==null){
			throw new ServiceException("社区奖id不能为空");
		}
		if(req.getRewardRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("补贴奖比例不能小于0");
		}
		lambdaUpdate()
			.eq(W3CommunitySubsidy::getId,req.getId())
			.set(W3CommunitySubsidy::getRewardRatio,req.getRewardRatio())
			.update();
		return 1;
	}
}
