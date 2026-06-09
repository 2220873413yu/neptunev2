package com.xms.app.service;

import com.xms.app.entity.LoginBo;
import com.xms.app.entity.TeamOverviewDto;
import com.xms.app.entity.bo.*;
import com.xms.app.entity.dto.MyDirectMemberDto;
import com.xms.app.entity.dto.MyTeamInfoDto;
import com.xms.app.entity.dto.MyTeamMemberDto;
import com.xms.app.entity.dto.MyTeamMemberPageDto;
import com.xms.app.entity.req.BindEmailVo;
import com.xms.app.entity.req.BindGoogleCodeVo;
import com.xms.app.entity.req.BindInviteUserReq;
import com.xms.app.entity.req.UserBaseInfoVo;
import com.xms.app.entity.resp.DynamicRewardPageResp;
import com.xms.app.entity.resp.HistoryInsuranceInfoResp;
import com.xms.app.entity.resp.InsuranceInfoResp;
import com.xms.app.entity.vo.*;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.core.domain.model.xms.LoginAppUser;
import com.xms.dao.domain.UserInvestLayerConfig;
import com.xms.dao.domain.UserLevelConfig;
import com.xms.dao.entity.bo.UserMoneyBo;
import com.xms.dao.entity.domain.UserMoneyLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author: renengadePISTA
 * @createDate: 2023/9/12
 */
public interface BizUserService {
	/**
	 * 注册接口
	 * @param req
	 * @return
	 * @throws Exception
	 */
	ResultPista register(RegisterSmsVo req) throws Exception;

	/**
	 * 退出登录
	 * @param request
	 */
	ResultPista logout(HttpServletRequest request);



	/**
	 * 修改用户基础信息
	 * @param req
	 */
	void updateBaseInfo(@Valid UserBaseInfoVo req);


	/**
	 * 获取用户收益信息
	 * @param userId 用户id
	 * @return
	 */
	UserIncomeSummaryVo getIncomeSummary(Long userId);

	/**
	 * 获取用户资产信息
	 * @param userId 用户id
	 * @return
	 */
	UserAssetInfoBo getUserAssetInfo(Long userId);

	/**
	 * 发送邮箱验证码
	 * @param req
	 * @return
	 */
    ResultPista sendMesAuthCode(MesAuthCodeVo req)  throws Exception;

	/**
	 * 绑定邮箱
	 * @param req
	 */
	void bindEmail(BindEmailVo req);

	/**
	 * 获取随机数
	 * @param address
	 * @return
	 */
	String getMessage(String address);

	/**
	 * 登录接口
	 * @param loginVo
	 * @return
	 */
	ResultPista<LoginAppUser> login(LoginVo loginVo);

	/**
	 * 获取币种信息(价格、涨跌幅)
	 * @return
	 */
	CoinInfoBo getCoinInfo();

	/**
	 * 获取我的团队数据
	 * @param userId
	 * @return
	 */
	MyTeamInfoDto myTeamInfo(Long userId);

	/**
	 * 我的团队数据 总成员、直推人数、团队销毁usdt、等级
	 * @param lastId lastId
	 * @param distance 层级
	 * @return
	 */
	MyTeamMemberPageDto listMyTeamMembers(Long lastId, Integer distance, Integer level);

	/**
	 * 我的团队数据
	 * @return
	 */
	List<MyDirectMemberDto> listMyDirectMembers(String address);

	/**
	 * 绑定邀请用户
	 * @param req
	 * @return
	 */
	ResultPista bindInviteUser(BindInviteUserReq req);

	/**
	 * 获取用户业绩数据
	 * @return
	 */
    TeamViewBO getTeamView(Long userId);

	/**
	 * 获取算力页面展示数据
	 * @return
	 */
	ComputingPowerBo computingPowerData();

	/**
	 * 获取算力奖励产出列表
	 * @return
	 */
	List<UserMoneyLog> powerDataList(Long lastId);

	/**
	 * 获取我的直推列表
	 * @return
	 */
	List<MyDirectMemberDto> listSubMembers(String address);

	/**
	 * 获取用户节点信息
	 * @return
	 */
	UserNodeInfoBo userNodeInfo();

	/**
	 * 动态奖励收益页面展示数据
	 * @return
	 */
    DynamicRewardPageResp dynamicRewardPageInfo();

	/**
	 * 获取层级奖励配置(进度条)
	 * @return
	 */
	UserInvestLayerConfig getLayerConfig();

	/**
	 * 获取用户等级配置(如果返回为空说明满级了)
	 * @return
	 */
	UserLevelConfig getUserLevelConfig();

	/**
	 * 查询本轮保险仓资格相关
	 * @param userId
	 * @return
	 */
	InsuranceInfoResp getInsuranceInfo(Long userId);

	/**
	 * 查询历史保险仓
	 * @param userId
	 * @return
	 */
	List<HistoryInsuranceInfoResp> historyInsuranceInfo(Long userId);
}
