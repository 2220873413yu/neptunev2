package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xms.common.exception.ServiceException;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.RewardPoolConfigMapper;
import com.xms.dao.domain.RewardPoolConfig;
import com.xms.dao.service.IRewardPoolConfigService;

/**
 * 分红池配置Service业务层处理
 *
 * @author xms
 * @date 2025-12-08
 */
@Service
public class RewardPoolConfigServiceImpl extends XmsDataServiceImpl<RewardPoolConfigMapper, RewardPoolConfig> implements IRewardPoolConfigService
{


    /**
     * 查询分红池配置列表
     *
     *
     * @param rewardPoolConfig 分红池配置
     * @return 分红池配置
     */
    @Override
    public List<RewardPoolConfig> selectRewardPoolConfigList(RewardPoolConfig rewardPoolConfig)
    {
        return baseMapper.selectRewardPoolConfigList(rewardPoolConfig);
    }

	@Override
	public int updateRecordById(RewardPoolConfig rewardPoolConfig) {
		if(rewardPoolConfig.getDailyOutput().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("每日产出量不能小于0");
		}
		if(rewardPoolConfig.getStaticRatio().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("静态分配比例不能小于等于0");
		}
		if(rewardPoolConfig.getDynamicRatio().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("动态分配比例不能小于等于0");
		}
		boolean b = updateById(rewardPoolConfig);
		if(!b){
			throw new ServiceException("配置修改失败,请稍后再试");
		}
		return 1;
	}
}
