package com.xms.app.entity.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xms.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现配置
 */
@Data
public class WithdrawalConfigResp {

	/** 主键id */
	private Long id;
	/** 币种 1:节点收益,2:静态收益,3:动态收益,4:财富仓,5:H代币,6:工作室收益,7:贡献分 */
	private Long ctp;
	/** 提现开关(1:开,2:关) */
	private Long wdo;
	/** 最小提现金额 */
	private BigDecimal mwa;
	/** 手续费比例(例如:10表示10%) */
	private BigDecimal fer;
	/** 财富仓比例(例如:15表示15%) */
	private BigDecimal wvr;
	/** 保险仓比例(例如:15表示15%) */
	private BigDecimal ivr;
}
