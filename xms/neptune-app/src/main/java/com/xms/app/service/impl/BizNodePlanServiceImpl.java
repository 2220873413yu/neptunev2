package com.xms.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.system.SystemUtil;
import com.xms.app.entity.req.BuyNodePlanReq;
import com.xms.app.entity.req.CreatePositionOrderReq;
import com.xms.app.entity.req.NodePackageReq;
import com.xms.app.entity.resp.BuyNodePlanResp;
import com.xms.app.entity.vo.NodePlanVo;
import com.xms.app.service.BizNodePlanService;
import com.xms.common.config.redis.lock.RedisLock;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.ConstantSys;
import com.xms.common.constant.RedisConstant;
import com.xms.common.constant.SysConstant;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.common.exception.ServiceException;
import com.xms.common.result.ResponseCode;
import com.xms.common.utils.SecurityUtils;
import com.xms.common.utils.SignUtil;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.domain.*;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 节点业务相关实现类
 * @author xms
 * @date 2023/6/12
 */
@Slf4j
@Service
public class BizNodePlanServiceImpl implements BizNodePlanService {

	@Autowired
	private INodePlanService nodePlanService;

	@Autowired
	private INodePlanOrderService nodePlanOrderService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private IStakeRoundService stakeRoundServiceImpl;


	@Autowired
	private IBuyHOrderService buyHOrderServiceImpl;

	@Value("${lq.md5Key}")
	private String md5Key;

	@Autowired
	private ISysParaService sysParaService;

	@Autowired
	private IUserLevelConfigService userLevelConfigService;


	@Override
	public ResultPista<List<NodePlanVo>> nodePlanInfo() {
		String ratio = sysParaService.getValue(ConstantSys.biz_global_static_income_ratio);
		List<NodePlanVo> planVoList = nodePlanService.lambdaQuery()
			.eq(NodePlan::getStatus, 1)
			.orderByAsc(NodePlan::getSortOrder)
			.select(NodePlan::getNodeLevel,NodePlan::getSoldQuota,NodePlan::getPurchaseAmount,
				NodePlan::getWeightCoefficient,NodePlan::getStudioSubsidyRatio)
			.list().stream().map(record -> {
				NodePlanVo nodePlanVo = new NodePlanVo();
				nodePlanVo.setNll(record.getNodeLevel());
				nodePlanVo.setSqo(record.getSoldQuota());
				nodePlanVo.setPam(record.getPurchaseAmount());
				nodePlanVo.setWcf(record.getWeightCoefficient());
				nodePlanVo.setSsr(record.getStudioSubsidyRatio());
				nodePlanVo.setGsi(ratio);
				return nodePlanVo;
			}).collect(Collectors.toList());
		return ResultPista.data(planVoList);
	}

	/**
	 * 购买节点身份,返回订单号和支付金额
	 * @param req 请求参数
	 * @return 返回订单号和支付金额
	 * @throws Exception
	 */
	@Override
	@RedisLock(value = RedisConstant.LockConstant.XMS_BUY_NODE_PLAN, param = "#userId")
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<BuyNodePlanResp> createOrder(BuyNodePlanReq req, Long userId) {
		BuyNodePlanResp buyNodePlanResp = new BuyNodePlanResp();
		//购买的节点身份小于现在的
		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, userId)
			.one();
		if(userInfo.getNodeLevel()>=req.getNll()){
			throw new ServiceException(ResponseCode.CODE_1251);
		}


		NodePlan nodePlan = nodePlanService.lambdaQuery()
			.eq(NodePlan::getNodeLevel, req.getNll())
			.eq(NodePlan::getStatus, 1)
			.one();
		if(nodePlan==null){
			throw new ServiceException(ResponseCode.CODE_1252);
		}

		//判断余额够不够
		if(req.getAmt().compareTo(nodePlan.getPurchaseAmount())<0){
			throw new ServiceException(ResponseCode.CODE_1015);
		}

		//复购开关
		//biz_node_plan_open_or_close
		String openOrClose = sysParaService.getValue(ConstantSys.biz_node_plan_open_or_close);
		if(openOrClose.equals("0")){
			//看下之前是否买成功过
			Long count = nodePlanOrderService.lambdaQuery()
				.eq(NodePlanOrder::getUserId, userId)
				.in(NodePlanOrder::getBizStatus, 1, 2)
				.count();
			//如果存在就不允许购买了
			if(count>0){
				throw new ServiceException(ResponseCode.CODE_1253);
			}
		}
		NodePlanOrder lastBuyOrder = nodePlanOrderService.lambdaQuery()
			.eq(NodePlanOrder::getUserId, userId)
			.in(NodePlanOrder::getBizStatus, 1, 2)
			.orderByDesc(NodePlanOrder::getId)
			.last("limit 1")
			.one();
		BigDecimal  lastBuyAmount = BigDecimal.ZERO;
		if(lastBuyOrder !=null){
			//补差价
			NodePlan lastNodePlan = nodePlanService.lambdaQuery()
				.eq(NodePlan::getNodeLevel, lastBuyOrder.getNodePlanLevel())
				.eq(NodePlan::getStatus, 1)
				.one();
			//之前购买花费的金额
			lastBuyAmount = lastNodePlan.getPurchaseAmount();
		}

		//remark 实际支付价格
		//3000  3000
		//10000  70000
		//30000  200000
		//查询是否购买过有对应订单

		//查询有没有购买过订单
		NodePlanOrder nodePlanOrder = nodePlanOrderService.lambdaQuery()
			.eq(NodePlanOrder::getUserId, userId)
			.eq(NodePlanOrder::getBizStatus, 0)
			.orderByDesc(NodePlanOrder::getId)
			.last("limit 1")
			.one();
		if(nodePlanOrder !=null){
			//判断创建时间和此刻有没有超过15分钟
			long minutes = DateUtil.between(nodePlanOrder.getCreateTime(), new Date(), DateUnit.MINUTE);
			if (minutes >= 15) {
				// 已超过15分钟
				//创建新的订单
				nodePlanOrder  = new NodePlanOrder();
				nodePlanOrder.setOrderNo(IDUtils.getSnowflakeStr());
				nodePlanOrder.setUserId(userId);
				nodePlanOrder.setNodePlanLevel(req.getNll());
				//如果之前购买了
				if(lastBuyAmount.compareTo(BigDecimal.ZERO)>0){
					//补差价
					nodePlanOrder.setAmount(nodePlan.getPurchaseAmount().subtract(lastBuyAmount));
				}else{
					nodePlanOrder.setAmount(nodePlan.getPurchaseAmount());
				}

				nodePlanOrder.setBizStatus(0);
				nodePlanOrder.setCreateTime(new Date());
				nodePlanOrder.setTotalAmount(nodePlanOrder.getAmount());
				nodePlanOrder.setHaveAmount(nodePlanOrder.getAmount());
				nodePlanOrder.setHaveDay(360);
				nodePlanOrder.setTotalDay(360);
				nodePlanOrder.setRemark(nodePlan.getPurchaseAmount().stripTrailingZeros().toPlainString());
				nodePlanOrderService.save(nodePlanOrder);

				buyNodePlanResp.setOno(nodePlanOrder.getOrderNo());
				buyNodePlanResp.setAmt(nodePlanOrder.getAmount());
				return ResultPista.data(buyNodePlanResp);

			} else {
				// 未超过15分钟
				//如果等级和之前一样直接返回
				if(nodePlanOrder.getNodePlanLevel().equals(req.getNll())){
					//找到之前的订单记录、如果价格一样就继续支付
					if(nodePlanOrder.getAmount().compareTo(nodePlan.getPurchaseAmount())!=0){
						nodePlanOrder.setAmount(nodePlan.getPurchaseAmount());
						//修改价格
						boolean update = nodePlanOrderService.lambdaUpdate()
							.eq(NodePlanOrder::getId, nodePlanOrder.getId())
							.eq(NodePlanOrder::getBizStatus,nodePlanOrder.getBizStatus())
							.set(lastBuyAmount.compareTo(BigDecimal.ZERO)  >0,NodePlanOrder::getAmount, nodePlan.getPurchaseAmount().subtract(lastBuyAmount))
							.set(lastBuyAmount.compareTo(BigDecimal.ZERO) <=0,NodePlanOrder::getAmount, nodePlan.getPurchaseAmount())
							.set(NodePlanOrder::getUpdateTime, new Date())
							.set(NodePlanOrder::getRemark, nodePlan.getPurchaseAmount().stripTrailingZeros().toPlainString())
							.update();
						if(!update){
							throw new ServiceException(ResponseCode.CODE_1005);
						}
					}
					buyNodePlanResp.setOno(nodePlanOrder.getOrderNo());
					buyNodePlanResp.setAmt(nodePlanOrder.getAmount());
					return ResultPista.data(buyNodePlanResp);
				}
				//抛异常
				throw new ServiceException(ResponseCode.CODE_1257);
			}
		}


		//创建新的订单
		nodePlanOrder  = new NodePlanOrder();
		nodePlanOrder.setOrderNo(IDUtils.getSnowflakeStr());
		nodePlanOrder.setUserId(userId);
		nodePlanOrder.setNodePlanLevel(req.getNll());
		//如果之前购买了
		if(lastBuyAmount.compareTo(BigDecimal.ZERO)>0){
			//补差价
			nodePlanOrder.setAmount(nodePlan.getPurchaseAmount().subtract(lastBuyAmount));
		}else{
			nodePlanOrder.setAmount(nodePlan.getPurchaseAmount());
		}

		nodePlanOrder.setBizStatus(0);
		nodePlanOrder.setCreateTime(new Date());
		nodePlanOrder.setTotalAmount(nodePlanOrder.getAmount());
		nodePlanOrder.setHaveAmount(nodePlanOrder.getAmount());
		nodePlanOrder.setHaveDay(360);
		nodePlanOrder.setTotalDay(360);
		nodePlanOrder.setRemark(nodePlan.getPurchaseAmount().stripTrailingZeros().toPlainString());
		nodePlanOrderService.save(nodePlanOrder);

		buyNodePlanResp.setOno(nodePlanOrder.getOrderNo());
		buyNodePlanResp.setAmt(nodePlanOrder.getAmount());
		return ResultPista.data(buyNodePlanResp);
	}

	@Override
	@RedisLock(value = RedisConstant.LockConstant.XMS_STAKE_ORDER_PLAN)
	public ResultPista<BuyNodePlanResp> createPositionOrder(CreatePositionOrderReq req, Long userId) {

		//进行中的轮次
		StakeRound stakeRound = stakeRoundServiceImpl.lambdaQuery()
			.eq(StakeRound::getStatus, 0)
			.last("for update")
			.one();
		if(stakeRound == null){
			throw new ServiceException(ResponseCode.CODE_1002);
		}

		req.setAmt(req.getAmt().setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));

		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, SecurityUtils.getLoginAppUser().getUserId())
			.one();
		Integer gameLevel = userInfo.getGameLevel()>userInfo.getMinGameLevel()? userInfo.getGameLevel(): userInfo.getMinGameLevel();
		UserLevelConfig userLevelConfig = userLevelConfigService.lambdaQuery()
			.eq(UserLevelConfig::getLevel, gameLevel)
			.one();
		if(req.getAmt().compareTo(userLevelConfig.getMinBuyAmount())<0){
			throw new ServiceException(ResponseCode.CODE_1255);
		}
		BuyNodePlanResp resp = new BuyNodePlanResp();

		BigDecimal pRatio = new BigDecimal(sysParaService.getValue(ConstantSys.biz_h_token_buy_points_ratio));

		BuyHOrder buyHOrder = new BuyHOrder();
		buyHOrder.setUserId(userId);
		buyHOrder.setOrderNo(IDUtils.getSnowflakeStr());
		buyHOrder.setWalletAddress(userInfo.getAccount());
		buyHOrder.setPayHAmount(req.getAmt());
		buyHOrder.setStatus(0);
		buyHOrder.setStakeRoundId(stakeRound.getId());
		buyHOrder.setPointsAmount(pRatio.multiply(req.getAmt())
			.setScale(ConstantStatic.newScale, ConstantStatic.roundingModeNew));
		buyHOrder.setCreateTime(new Date());
		buyHOrderServiceImpl.save(buyHOrder);
		resp.setOno(buyHOrder.getOrderNo());
		resp.setAmt(req.getAmt());
		return ResultPista.data(resp);
	}

	/**
	 * 节点身份购买回调
	 * @param req
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultPista<String> nodeIdentityCallback(NodePackageReq req) {
		log.info("节点身份购买回调 req:{}",req);
		Map<String, Object> map = BeanUtil.beanToMap(req);

		String sign = SignUtil.getSign(map, false, false, md5Key);
		String osName = SystemUtil.getOsInfo().getName();
		if (!osName.toUpperCase().contains(SysConstant.OS_NAME_WINDOWS)) {
			if (!sign.equals(req.getSign())) {
				log.error("验签失败");
				return ResultPista.fail("验签失败");
			}
		}

		NodePlanOrder nodePlanOrder = nodePlanOrderService.lambdaQuery()
			.eq(NodePlanOrder::getOrderNo, req.getOrderNo())
			.one();
		if(nodePlanOrder == null){
			throw new ServiceException("订单不存在");
		}

		if(req.getAmount().compareTo(nodePlanOrder.getAmount())<0){
			throw new ServiceException("支付的金额不对");
		}
		if(!nodePlanOrder.getBizStatus().equals(0)){
			//已经不是待支付状态、可能已经完成了
			log.info("订单已经不是待支付状态、可能已经完成了 req:{}",req);
			return ResultPista.data("success");
		}

		UserInfo userInfo = userInfoService.lambdaQuery()
			.eq(UserInfo::getUserId, nodePlanOrder.getUserId())
			.one();
		if(nodePlanOrder.getNodePlanLevel()<=userInfo.getNodeLevel()){
			throw new ServiceException(ResponseCode.CODE_1251.getMsg());
		}

		//更新用户等级
		boolean update = userInfoService.lambdaUpdate()
			.eq(UserInfo::getUserId, nodePlanOrder.getUserId())
			.eq(UserInfo::getNodeLevel, userInfo.getNodeLevel())
			.set(UserInfo::getNodeLevel, nodePlanOrder.getNodePlanLevel())
			.update();
		if(!update){
			throw new ServiceException("更新用户节点身份失败");
		}

		NodePlan nodePlan = nodePlanService.lambdaQuery()
			.eq(NodePlan::getNodeLevel, nodePlanOrder.getNodePlanLevel())
			.one();
		//更新订单状态
		update = nodePlanOrderService.lambdaUpdate()
			.eq(NodePlanOrder::getId, nodePlanOrder.getId())
			.eq(NodePlanOrder::getBizStatus, 0)
			//更新年化收益率
			.set(NodePlanOrder::getAnnualRate, nodePlan.getAnnualRate())
			.set(NodePlanOrder::getTotalAnnual,BigDecimal.ZERO)
			.set(NodePlanOrder::getBizStatus, 1)
			.set(NodePlanOrder::getAddress,req.getAddress())
			.set(NodePlanOrder::getTxHash,req.getHash())
			.set(NodePlanOrder::getPaymentTime, new Date())
			.set(NodePlanOrder::getUpdateTime, new Date())
			.update();
		if(!update){
			throw new ServiceException("更新节订单失败");
		}
		return ResultPista.data("success");
	}
}
