package com.xms.app.service;

import com.github.pagehelper.PageInfo;
import com.xms.app.entity.bo.WithdrawalCallbackBo;
import com.xms.app.entity.dto.ReleaseBucketListDto;
import com.xms.app.entity.req.JuNotifyReq;
import com.xms.app.entity.resp.*;
import com.xms.app.entity.vo.UserBankInfoVo;
import com.xms.app.entity.vo.UserBankVo;
import com.xms.app.entity.vo.WithdrawalVo;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.dao.entity.bo.WithdrawalBo;
import com.xms.dao.entity.domain.Withdrawal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface BizWithdrawalService {
	void bindUserBank(UserBankVo req);

	void unBindUserBank(Long id);

	UserBankInfoVo getUserBank(Integer type);

	/**
	 * 发起提现
	 * @param withdrawalVo
	 * @param userId
	 */
    int addWithdrawal(WithdrawalVo withdrawalVo, Long userId);

	/**
	 * 提现汇总信息
	 * @param userId
	 * @return
	 */
	WithdrawalSummaryResp withdrawalSummary(Long userId);

	/**
	 * 提现回调
	 * @param req
	 * @return
	 */
	ResultPista<String> withdrawalCallback(WithdrawalCallbackBo req);

	/**
	 * 提现记录
	 * @param pageIndex 当前页 默认1
	 * @param pageSize 每页长度 默认20(最大20)
	 * @return
	 */
	PageInfo<WithdrawalBo> listWithdrawRecord(Integer pageIndex, Integer pageSize, Long userId);

	/**
	 * 提现 回调通知
	 * @param req
	 */
    void updateJuStatus(JuNotifyReq req);

	/**
	 * 提现配置
	 * @return
	 */
	List<WithdrawalConfigResp> withdrawalConfig();

	/**
	 * h代币释放信息 锁仓中/已释放
	 * @return
	 */
	GiftReleaseBucketDto giftReleaseBucket();

}
