package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.xms.common.exception.ServiceException;
import com.xms.common.utils.uuid.IDUtils;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.AirdropRoundMapper;
import com.xms.dao.domain.AirdropRound;
import com.xms.dao.service.IAirdropRoundService;

/**
 * 空投轮次配置Service业务层处理
 *
 * @author xms
 * @date 2026-01-01
 */
@Service
public class AirdropRoundServiceImpl extends XmsDataServiceImpl<AirdropRoundMapper, AirdropRound> implements IAirdropRoundService
{


    /**
     * 查询空投轮次配置列表
     *
     *
     * @param airdropRound 空投轮次配置
     * @return 空投轮次配置
     */
    @Override
    public List<AirdropRound> selectAirdropRoundList(AirdropRound airdropRound)
    {
        return baseMapper.selectAirdropRoundList(airdropRound);
    }


	/**
	 * 修改空投轮次配置
	 *
	 * @param req 空投轮次配置
	 * @return 结果
	 */
	@Override
	public int updateRecordById(AirdropRound req) {
		AirdropRound airdropRound = lambdaQuery()
			.eq(AirdropRound::getId, req.getId())
			.one();
		if(airdropRound.getStatus() == 2){
			throw new ServiceException("空投已经领完了.请刷新页面后重试");
		}

		if(req.getTotalQuota()<=0){
			throw new ServiceException("轮次总次数不能小于等于0");
		}

		if(req.getTokenPerClaim().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("每次领取代币数量不能小于等于0");
		}
		if(req.getOkbPayAmount().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("需要支付的价值多少u的OKB数量不能小于等于0");
		}


		if(req.getAutoOpenNext().equals(0)){
			// 不设置
			req.setNextRoundNo("");
		}else{
			//检测是否存在
			if(StrUtil.isBlank(req.getNextRoundNo())){
				throw new ServiceException("请填写下一轮编号");
			}
			AirdropRound queryAirdropRound = lambdaQuery().eq(AirdropRound::getRoundNo, req.getNextRoundNo())
				.one();
			if(queryAirdropRound==null){
				throw new ServiceException("下一轮编号不存在");
			}
			if(queryAirdropRound.getStatus().equals(2)){
				throw new ServiceException("下一轮编号已关闭");
			}
		}

		Long haveCount = airdropRound.getLockedQuota()+ airdropRound.getClaimedQuota();
		if(req.getTotalQuota()<haveCount){
			throw new ServiceException("轮次总次数不能小于已领取次数");
		}

		lambdaUpdate()
			.eq(AirdropRound::getId, req.getId())
			.eq(AirdropRound::getStatus, airdropRound.getStatus())
			.set(AirdropRound::getStatus, req.getStatus())
			.set(AirdropRound::getTotalQuota,req.getTotalQuota())
			//获得多少xls
			.set(AirdropRound::getTokenPerClaim,req.getTokenPerClaim())
			//支付多少okb能领取
			.set(AirdropRound::getOkbPayAmount,req.getOkbPayAmount())
			//是否自动开启下一轮
			.set(AirdropRound::getAutoOpenNext,req.getAutoOpenNext())
			//下一轮编号
			.set(AirdropRound::getNextRoundNo,req.getNextRoundNo())
			.set(AirdropRound::getUpdateTime, new Date())
			.update();
		return 1;
	}

	@Override
	public int saveRecord(AirdropRound req) {
		if(req.getTotalQuota()<=0){
			throw new ServiceException("轮次总次数不能小于等于0");
		}

		if(req.getTokenPerClaim().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("每次领取代币数量不能小于等于0");
		}
		if(req.getOkbPayAmount().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("需要支付的价值多少u的OKB数量不能小于等于0");
		}

		if(req.getAutoOpenNext().equals(0)){
			// 不设置
			req.setNextRoundNo("");
		}else{
			//检测是否存在
			if(StrUtil.isBlank(req.getNextRoundNo())){
				throw new ServiceException("请填写下一轮编号");
			}
			AirdropRound airdropRound = lambdaQuery().eq(AirdropRound::getRoundNo, req.getNextRoundNo())
				.one();
			if(airdropRound==null){
				throw new ServiceException("下一轮编号不存在");
			}
			if(airdropRound.getStatus().equals(2)){
				throw new ServiceException("下一轮编号已关闭");
			}
		}

		AirdropRound airdropRound = new AirdropRound();
		String roundNo = IDUtils.getSnowflakeStr();
		airdropRound.setRoundNo(roundNo);
		airdropRound.setStatus(req.getStatus());
		airdropRound.setTotalQuota(req.getTotalQuota());
		airdropRound.setTokenPerClaim(req.getTokenPerClaim());
		//是否自动开启下一轮
		airdropRound.setAutoOpenNext(req.getAutoOpenNext());
		airdropRound.setNextRoundNo(req.getNextRoundNo());
		airdropRound.setOkbPayAmount(req.getOkbPayAmount());
		boolean save = save(airdropRound);
		if(!save){
			throw new ServiceException("保存失败");
		}
		return 1;
	}
}
