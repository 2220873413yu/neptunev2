package com.xms.app.service;

import com.github.pagehelper.PageInfo;
import com.xms.app.entity.TotalEarningsDto;
import com.xms.app.entity.bo.DestroyCallbackBo;
import com.xms.app.entity.bo.DestroyInfoBo;
import com.xms.app.entity.req.NodePackageReq;
import com.xms.app.entity.req.SwapOrderCallbackReq;
import com.xms.dao.entity.dto.DestroyOrderDto;
import com.xms.dao.entity.dto.InterestPackDto;
import com.xms.dao.entity.dto.InterestStatDayDto;
import com.xms.app.entity.dto.ReleaseConfigDto;
import com.xms.app.entity.dto.mining.PackageOrderDto;
import com.xms.app.entity.req.RedeemVo;
import com.xms.app.entity.req.ReleaseOrderReq;
import com.xms.app.entity.resp.CreateOrderResp;
import com.xms.app.entity.vo.*;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.dao.domain.MiningPackage;
import jakarta.validation.Valid;

import java.util.List;

public interface BizMiningService {

	/**
	 * 获取矿机列表
	 * @return
	 */
	ResultPista<List<W3MiningPackageVo>> list(Integer type);

	/**
	 * 节点回调事件
	 */
	ResultPista<String> nodePackageCallback(NodePackageReq req);

	/**
	 * swap订单回调
	 */
	ResultPista<String> swapOrderCallback(SwapOrderCallbackReq req);

//	/**
//	 * 激活订单回调事件
//	 */
//	ResultPista<String> activeOrderCallback(DestroyCallbackBo req);
}
