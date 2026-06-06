package com.xms.dao.service.impl;

import java.util.List;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.AirdropClaimMapper;
import com.xms.dao.domain.AirdropClaim;
import com.xms.dao.service.IAirdropClaimService;

/**
 * 空投领取记录Service业务层处理
 *
 * @author xms
 * @date 2026-01-01
 */
@Service
public class AirdropClaimServiceImpl extends XmsDataServiceImpl<AirdropClaimMapper, AirdropClaim> implements IAirdropClaimService
{


    /**
     * 查询空投领取记录列表
     *
     *
     * @param airdropClaim 空投领取记录
     * @return 空投领取记录
     */
    @Override
    public List<AirdropClaim> selectAirdropClaimList(AirdropClaim airdropClaim)
    {
        return baseMapper.selectAirdropClaimList(airdropClaim);
    }

}
