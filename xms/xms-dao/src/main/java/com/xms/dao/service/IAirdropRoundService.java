package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.AirdropRound;

/**
 * 空投轮次配置Service接口
 *
 * @author xms
 * @date 2026-01-01
 */
public interface IAirdropRoundService extends XmsDataService<AirdropRound>
{

    /**
     * 查询空投轮次配置列表
     *
     * @param airdropRound 空投轮次配置
     * @return 空投轮次配置集合
     */
    public List<AirdropRound> selectAirdropRoundList(AirdropRound airdropRound);

	/**
     * 保存空投轮次配置
     *
     * @param airdropRound 空投轮次配置
     * @return 结果
     */
    int saveRecord(AirdropRound airdropRound);

	/**
	 * 修改空投轮次配置
	 *
	 * @param airdropRound 空投轮次配置
	 * @return 结果
	 */
	int updateRecordById(AirdropRound airdropRound);
}
