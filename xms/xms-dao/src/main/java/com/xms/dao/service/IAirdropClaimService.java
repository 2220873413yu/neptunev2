package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.AirdropClaim;

/**
 * 空投领取记录Service接口
 *
 * @author xms
 * @date 2026-01-01
 */
public interface IAirdropClaimService extends XmsDataService<AirdropClaim>
{

    /**
     * 查询空投领取记录列表
     *
     * @param airdropClaim 空投领取记录
     * @return 空投领取记录集合
     */
    public List<AirdropClaim> selectAirdropClaimList(AirdropClaim airdropClaim);

}
