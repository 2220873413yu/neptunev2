package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.xms.common.exception.ServiceException;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.UserInvestLayerConfigMapper;
import com.xms.dao.domain.UserInvestLayerConfig;
import com.xms.dao.service.IUserInvestLayerConfigService;

/**
 * 层奖配置Service业务层处理
 *
 * @author xms
 * @date 2026-03-05
 */
@Service
public class UserInvestLayerConfigServiceImpl extends XmsDataServiceImpl<UserInvestLayerConfigMapper, UserInvestLayerConfig> implements IUserInvestLayerConfigService
{


    /**
     * 查询层奖配置列表
     *
     *
     * @param userInvestLayerConfig 层奖配置
     * @return 层奖配置
     */
    @Override
    public List<UserInvestLayerConfig> selectUserInvestLayerConfigList(UserInvestLayerConfig userInvestLayerConfig)
    {
        return baseMapper.selectUserInvestLayerConfigList(userInvestLayerConfig);
    }

	@Override
	public int updateConfigById(UserInvestLayerConfig req) {
		if(req.getMinInvest().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("最低投资额度不能小于0");
		}
		if(req.getRewardRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("层级奖比例不能小于0");
		}

		baseMapper.updateById(req);
		lambdaUpdate()
			.gt(UserInvestLayerConfig::getLevel,0)
			.set(UserInvestLayerConfig::getRewardRatio,req.getRewardRatio())
			.update();
		return 1;
	}
}
