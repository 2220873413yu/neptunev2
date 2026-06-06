package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.W3CommunitySubsidy;

/**
 * 社区补贴配置Service接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface IW3CommunitySubsidyService extends XmsDataService<W3CommunitySubsidy>
{

    /**
     * 查询社区补贴配置列表
     *
     * @param w3CommunitySubsidy 社区补贴配置
     * @return 社区补贴配置集合
     */
    public List<W3CommunitySubsidy> selectW3CommunitySubsidyList(W3CommunitySubsidy w3CommunitySubsidy);

	/**
	 * 修改社区补贴
	 * @param w3CommunitySubsidy
	 * @return
	 */
	int updateRecordById(W3CommunitySubsidy w3CommunitySubsidy);
}
