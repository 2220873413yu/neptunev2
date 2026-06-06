package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.AirdropClaim;

/**
 * 空投领取记录Mapper接口
 *
 * @author xms
 * @date 2026-01-01
 */
public interface AirdropClaimMapper extends XmsMapper<AirdropClaim>
{
    /**
     * 查询空投领取记录列表
     *
     * @param airdropClaim 空投领取记录
     * @return 空投领取记录集合
     */
    public List<AirdropClaim> selectAirdropClaimList(AirdropClaim airdropClaim);

}
