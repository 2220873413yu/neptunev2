package com.xms.dao.mapper;

import com.xms.dao.domain.WalletTransferOrder;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分发记录Mapper接口
 *
 * @author xms
 * @date 2025-06-22
 */
public interface WalletTransferOrderMapper extends XmsMapper<WalletTransferOrder>
{
    /**
     * 查询分发记录列表
     *
     * @param walletTransferOrder 分发记录
     * @return 分发记录集合
     */
    public List<WalletTransferOrder> selectWalletTransferOrderList(WalletTransferOrder walletTransferOrder);

	@Select("SELECT COALESCE(SUM(amount), 0) AS total_static_income FROM t_wallet_transfer_order where biz_type = 2 and status =2")
    BigDecimal queryTotalBSCAmount();

	@Select("SELECT COALESCE(SUM(amount), 0) AS total_static_income FROM t_wallet_transfer_order where biz_type = 1 and status =2")
    BigDecimal queryTotalTRCAmount();
}
