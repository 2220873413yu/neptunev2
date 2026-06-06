package com.xms.dao.service;

import com.xms.dao.domain.W3UserLevelConfig;
import com.xms.dao.entity.domain.UserInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户等级考核配置Service接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface IW3UserLevelConfigService extends XmsDataService<W3UserLevelConfig>
{

    /**
     * 查询用户等级考核配置列表
     *
     * @param w3UserLevelConfig 用户等级考核配置
     * @return 用户等级考核配置集合
     */
    public List<W3UserLevelConfig> selectW3UserLevelConfigList(W3UserLevelConfig w3UserLevelConfig);

	/**
	 * 修改考核等级配置
	 * @param w3UserLevelConfig
	 * @return
	 */
    int updateRecordById(W3UserLevelConfig w3UserLevelConfig);


	public UserInfo tryUpdateUserLevel(Long userId, String orderNo, BigDecimal userAmount,
									   BigDecimal userUpgradeAmount);
}
