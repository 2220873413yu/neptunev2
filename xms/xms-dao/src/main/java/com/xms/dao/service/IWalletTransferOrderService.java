package com.xms.dao.service;

import com.xms.dao.domain.WalletTransferOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分发记录Service接口
 *
 * @author xms
 * @date 2025-06-22
 */
public interface IWalletTransferOrderService extends XmsDataService<WalletTransferOrder>
{

    /**
     * 查询分发记录列表
     *
     * @param walletTransferOrder 分发记录
     * @return 分发记录集合
     */
    public List<WalletTransferOrder> selectWalletTransferOrderList(WalletTransferOrder walletTransferOrder);

	/**
	 * 累计分发BSC金额
	 * @return
	 */
	BigDecimal queryTotalBSCAmount();


/**
	 * 累计分发TRC金额
	 * @return
	 */
	BigDecimal queryTotalTRCAmount();
}
