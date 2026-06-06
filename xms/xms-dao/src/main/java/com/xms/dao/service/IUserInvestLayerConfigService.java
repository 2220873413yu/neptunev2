package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.UserInvestLayerConfig;

/**
 * 层奖配置Service接口
 *
 * @author xms
 * @date 2026-03-05
 */
public interface IUserInvestLayerConfigService extends XmsDataService<UserInvestLayerConfig>
{

    /**
     * 查询层奖配置列表
     *
     * @param userInvestLayerConfig 层奖配置
     * @return 层奖配置集合
     */
    public List<UserInvestLayerConfig> selectUserInvestLayerConfigList(UserInvestLayerConfig userInvestLayerConfig);

	/**
	 * 更新配置
	 * @param userInvestLayerConfig
	 * @return
	 */
	int updateConfigById(UserInvestLayerConfig userInvestLayerConfig);
}
