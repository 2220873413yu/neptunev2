package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.W3CommunitySubsidy;

/**
 * 社区补贴配置Mapper接口
 *
 * @author xms
 * @date 2025-04-10
 */
public interface W3CommunitySubsidyMapper extends XmsMapper<W3CommunitySubsidy>
{
    /**
     * 查询社区补贴配置列表
     *
     * @param w3CommunitySubsidy 社区补贴配置
     * @return 社区补贴配置集合
     */
    public List<W3CommunitySubsidy> selectW3CommunitySubsidyList(W3CommunitySubsidy w3CommunitySubsidy);

}
