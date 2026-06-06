package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xms.common.exception.ServiceException;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserLevelConfigMapper;
import com.xms.dao.domain.UserLevelConfig;
import com.xms.dao.service.IUserLevelConfigService;

/**
 * 用户等级考核配置Service业务层处理
 *
 * @author xms
 * @date 2025-12-03
 */
@Service
public class UserLevelConfigServiceImpl extends XmsDataServiceImpl<UserLevelConfigMapper, UserLevelConfig> implements IUserLevelConfigService
{


    /**
     * 查询用户等级考核配置列表
     *
     *
     * @param userLevelConfig 用户等级考核配置
     * @return 用户等级考核配置
     */
    @Override
    public List<UserLevelConfig> selectUserLevelConfigList(UserLevelConfig userLevelConfig)
    {
        return baseMapper.selectUserLevelConfigList(userLevelConfig);
    }

	@Override
	public int updateRecordById(UserLevelConfig userLevelConfig) {


		if(userLevelConfig.getUmbrellaPerformance().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("小区业绩不能小于0");
		}
		if(userLevelConfig.getRewardRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("平级奖比例不能小于0");
		}

		if(userLevelConfig.getPeerRewardRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("平级奖比例不能小于0");
		}

		if(userLevelConfig.getMinBuyAmount().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("购买贡献分最低h代币限制不能小于0");
		}
		updateById(userLevelConfig);
		return 1;
	}
}
