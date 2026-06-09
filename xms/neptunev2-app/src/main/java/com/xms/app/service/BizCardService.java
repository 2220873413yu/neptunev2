package com.xms.app.service;

import com.xms.app.entity.bo.DestroyCallbackBo;
import com.xms.app.entity.dto.CardPackageDto;
import com.xms.app.entity.dto.CardUpgradeLogDto;
import com.xms.app.entity.req.*;
import com.xms.app.entity.resp.*;
import com.xms.app.entity.vo.AirdropClaimPageInfoVo;
import com.xms.app.entity.vo.AirdropClaimRecordVo;
import com.xms.app.entity.vo.NodePlanVo;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.dao.domain.RewardRecord;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 卡片类型接口类
 * @author xms
 * @date 2023/04/05
 */
public interface BizCardService {
	/**
	 * 卡片列表
	 * @return
	 * @throws Exception
	 */
	ResultPista<List<CardPackageDto>> cardList();


	/**
	 * 创建卡片订单
	 * @param req 卡片订单对象
	 * @return
	 * @throws Exception
	 */
	ResultPista createCardOrder(CreateCardOrderReq req);

	/**
	 * 升级卡片
	 * @param req 升级卡片对象
	 * @return
	 * @throws Exception
	 */
	ResultPista upgradeCardOrder(UpgradeCardOrderReq req);

	/**
	 * 获取卡片升级或者购买的日志
	 * @param cardType 卡片类型
	 * @return
	 * @throws Exception
	 */
	List<CardUpgradeLogDto> getCardLog(Integer cardType, Long lastId);

	/**
	 * 获取算力记录
	 * @param lastId 最后一条记录的id
	 * @return
	 * @throws Exception
	 */
	ResultPista<List<RewardRecord>> powerLog(Long lastId);

	/**
	 * 创建激活订单
	 * @return
	 * @throws Exception
	 */
	ResultPista<CreateActiveOrderResp> createActiveOrder();

	/**
	 * 激活订单回调
	 * @param req
	 * @return
	 * @throws Exception
	 */
	ResultPista<String> activeOrderCallback(DestroyCallbackBo req);


	/**
	 * 领取空投
	 * @return
	 * @throws Exception
	 */
	ResultPista<ClaimAirdropResp> claimAirdrop(ClaimAirdropReq req);

	/**
	 * 领取空投回调
	 * @param req
	 * @return
	 * @throws Exception
	 */
	ResultPista<String> claimAirdropCallback(DestroyCallbackBo req);

	/**
	 * 获取领取记录
	 * @return
	 * @throws Exception
	 */
	ResultPista<List<AirdropClaimRecordVo>> claimRecordList(Long lastId);

	/**
	 * 我的质押信息
	 * @return
	 */
    ResultPista<MyStakeInfoResp> myStakeInfo();

	/**
	 * 我的收益页面
	 * @return
	 */
	ResultPista<MyStakeIncomeResp> myStakeIncomeInfo();
}
