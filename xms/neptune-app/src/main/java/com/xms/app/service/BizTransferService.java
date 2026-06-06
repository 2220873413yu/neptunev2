package com.xms.app.service;

import com.xms.app.entity.vo.TransferOrderVo;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.dao.domain.UserTransfer;

import java.util.List;

public interface BizTransferService {
	/**
	 * 发起转账
	 * @param req
	 * @param userId
	 * @return
	 */
	ResultPista createOrder(TransferOrderVo req, Long userId);

	/**
	 * 转账记录
	 * @param lastId
	 * @param type
	 * @param coinType
	 * @return
	 */
	ResultPista<List<UserTransfer>> listTransferRecord(Long lastId, Integer type, Integer coinType);
}
