package com.xms.app.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * h释放信息
 * @author xms
 * @date 2023/10/11
 */
@Data
public class ReleaseBucketListDto {
	private Long id;
	private String bucketNo;
	private BigDecimal totalAmount;
	private BigDecimal releasedAmount;
	private BigDecimal remainingAmount;
	private BigDecimal dailyReleaseAmount;
	private Integer releaseDays;
	private Integer releasedDays;
	private Integer startDate;
	private Integer lastReleaseDate;
	private Integer status;
	private Date createTime;
}
