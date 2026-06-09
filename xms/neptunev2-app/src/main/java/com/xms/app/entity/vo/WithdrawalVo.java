package com.xms.app.entity.vo;

import com.xms.common.annotation.ValidDiyStatus;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 提现表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalVo {

	/**
	 * 提现金额
	 */
    @ApiModelProperty(value = "提现金额",required=true)
    @NotNull
    @Positive
    private BigDecimal cgb;

	/**
	 * 币种 1:节点收益(ACP),2:静态(ACP),3:动态(ACP),4:财富仓(ACP),5:保险仓(魔盒收益/手续费)(ACP),6:工作室收益(ACP),9:H代币
	 */
    @ApiModelProperty(value = "币种",required=true)
    @NotNull
	@ValidDiyStatus(values = {1,2,3,4,5,6,9}, message = "coinType error")
    private Integer ctp;


	@ApiModelProperty(value = "签名")
	@NotBlank
	private String sig;

	/**
	 * 随机数不能为空
	 */
	@NotBlank(message = "随机数不能为空")
	private String rdn;
}
