package com.xms.dao.service.impl;

import com.xms.dao.domain.WalletTransferOrder;
import com.xms.dao.mapper.WalletTransferOrderMapper;
import com.xms.dao.service.IWalletTransferOrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分发记录Service业务层处理
 *
 * @author xms
 * @date 2025-06-22
 */
@Service
public class WalletTransferOrderServiceImpl extends XmsDataServiceImpl<WalletTransferOrderMapper, WalletTransferOrder> implements IWalletTransferOrderService
{


    /**
     * 查询分发记录列表
     *
     *
     * @param walletTransferOrder 分发记录
     * @return 分发记录
     */
    @Override
    public List<WalletTransferOrder> selectWalletTransferOrderList(WalletTransferOrder walletTransferOrder)
    {
        return baseMapper.selectWalletTransferOrderList(walletTransferOrder);
    }

	@Override
	public BigDecimal queryTotalBSCAmount() {
		return baseMapper.queryTotalBSCAmount();
	}

	@Override
	public BigDecimal queryTotalTRCAmount() {
		return baseMapper.queryTotalTRCAmount();
	}
}
