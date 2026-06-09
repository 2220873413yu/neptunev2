package com.xms.dao.mapper;

import com.xms.dao.domain.OldHToAcpDepositRecord;

import java.util.List;

/**
 * 旧系统H换ACP入金记录Mapper接口
 *
 * @author xms
 * @date 2026-06-07
 */
public interface OldHToAcpDepositRecordMapper extends XmsMapper<OldHToAcpDepositRecord> {
	/**
	 * 查询旧系统H换ACP入金记录列表
	 *
	 * @param oldHToAcpDepositRecord 旧系统H换ACP入金记录
	 * @return 旧系统H换ACP入金记录集合
	 */
	public List<OldHToAcpDepositRecord> selectOldHToAcpDepositRecordList(OldHToAcpDepositRecord oldHToAcpDepositRecord);
}
