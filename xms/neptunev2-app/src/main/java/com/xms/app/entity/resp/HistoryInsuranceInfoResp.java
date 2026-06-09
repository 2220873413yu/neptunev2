package com.xms.app.entity.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 历史保险信息响应类
 *
 * @author xms
 * @date 2022/01/01
 */
@Data
public class HistoryInsuranceInfoResp {

	/** 当前总质押金额 */
	private BigDecimal totalStakeAmount;

	/**
	 * 个人亏损额
	 */
	private BigDecimal personalLossAmount;

	/**
	 * 剩余可赔付
	 */
	private BigDecimal remainingCompensationLimit;

	/**
	 * 可赔付额度
	 */
	private BigDecimal allCompensationLimit;

	/**
	 * 保险仓赔付资格状态 0:无资格,1:有资格
	 */
	private Integer insuranceQualifyStatus;

	/**
	 * 领取赔付是否有资格 0:无资格,1:有资格
	 */
	private Integer insuranceCompensationQualifyStatus;
}
