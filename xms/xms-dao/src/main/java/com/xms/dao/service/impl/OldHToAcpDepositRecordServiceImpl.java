package com.xms.dao.service.impl;

import com.xms.dao.domain.OldHToAcpDepositRecord;
import com.xms.dao.mapper.OldHToAcpDepositRecordMapper;
import com.xms.dao.service.IOldHToAcpDepositRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 旧系统H换ACP入金记录Service业务层处理
 *
 * @author xms
 * @date 2026-06-07
 */
@Service
public class OldHToAcpDepositRecordServiceImpl extends XmsDataServiceImpl<OldHToAcpDepositRecordMapper, OldHToAcpDepositRecord>
	implements IOldHToAcpDepositRecordService {
	/**
	 * 查询旧系统H换ACP入金记录列表
	 *
	 * @param oldHToAcpDepositRecord 旧系统H换ACP入金记录
	 * @return 旧系统H换ACP入金记录
	 */
	@Override
	public List<OldHToAcpDepositRecord> selectOldHToAcpDepositRecordList(OldHToAcpDepositRecord oldHToAcpDepositRecord) {
		return baseMapper.selectOldHToAcpDepositRecordList(oldHToAcpDepositRecord);
	}
}
