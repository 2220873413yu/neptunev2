package com.xms.app.entity.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * h释放信息
 * @author xms
 * @date 2023/10/11
 */
@Data
public class GiftReleaseBucketDto {
	/**
	 * H赠送总量
	 */
	private BigDecimal totalAmount = BigDecimal.ZERO;

	/**
	 * 已释放H数量
	 */
	private BigDecimal releasedAmount = BigDecimal.ZERO;
}
