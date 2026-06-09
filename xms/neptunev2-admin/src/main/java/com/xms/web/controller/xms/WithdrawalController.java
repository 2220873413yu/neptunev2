package com.xms.web.controller.xms;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.Log;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.core.controller.BaseController;
import com.xms.common.core.domain.AjaxResult;
import com.xms.common.core.page.TableDataInfo;
import com.xms.common.enums.BusinessType;
import com.xms.common.exception.ServiceException;
import com.xms.common.utils.WalletUtil;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.dao.domain.RechargeRecord;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.service.UserInfoService;
import com.xms.dao.service.WithdrawalService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提现Controller
 *
 *
 * @date 2023-07-31
 */
@RestController
@RequestMapping("/xms/withdrawal")
public class WithdrawalController extends BaseController
{
	@Autowired
	private WithdrawalService withdrawalService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private WalletUtil walletUtil;


	/**
	 * 查询提现列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:withdrawal:list')")
	@GetMapping("/list")
	public TableDataInfo list(Withdrawal withdrawal)
	{
		if(StrUtil.isNotBlank(withdrawal.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, withdrawal.getUserAccount())
				.one();
			if(userInfo != null){
				withdrawal.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}

		//团队用户账号下的提现数据
//		if(StrUtil.isNotBlank(withdrawal.getTeamUserAccount())){
//			UserInfo teamUserInfo = userInfoService.lambdaQuery()
//				.eq(UserInfo::getAccount, withdrawal.getTeamUserAccount())
//				.select(UserInfo::getUserId)
//				.one();
//			if(teamUserInfo != null){
//				List<Long> childUserIdList = userInfoService.getChildUserIdList(teamUserInfo.getUserId());
//				if(CollectionUtil.isEmpty(childUserIdList)){
//					return getDataTable(new ArrayList<>());
//				}else{
//					withdrawal.setChildUserIds(childUserIdList);
//				}
//			}else{
//				return getDataTable(new ArrayList<>());
//			}
//		}
		startPage();
		List<Withdrawal> list = withdrawalService.selectWithdrawalList(withdrawal);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, UserInfo> userAccountMap = userInfoService.getUserAById(list.stream().map(Withdrawal::getUserId).collect(Collectors.toList()));
			for (Withdrawal bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()).getAccount());
				bankVo.setUserCode(userAccountMap.get(bankVo.getUserId()).getUserCode());
			}
		}
		return getDataTable(list);
	}

	/**
	 * 导出提现列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:withdrawal:export')")
	@Log(title = "提现", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, Withdrawal withdrawal)
	{
		if(StrUtil.isNotBlank(withdrawal.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, withdrawal.getUserAccount())
				.one();
			if(userInfo != null){
				withdrawal.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<Withdrawal> util = new ExcelUtil<Withdrawal>(Withdrawal.class);
				util.exportExcel(response, new ArrayList<>(), "提现数据");
				return;
			}
		}

//		//团队用户账号下的提现数据
//		if(StrUtil.isNotBlank(withdrawal.getTeamUserAccount())){
//			UserInfo teamUserInfo = userInfoService.lambdaQuery()
//				.eq(UserInfo::getAccount, withdrawal.getTeamUserAccount())
//				.select(UserInfo::getUserId)
//				.one();
//			if(teamUserInfo != null){
//				List<Long> childUserIdList = userInfoService.getChildUserIdList(teamUserInfo.getUserId());
//				if(CollectionUtil.isEmpty(childUserIdList)){
//					ExcelUtil<Withdrawal> util = new ExcelUtil<Withdrawal>(Withdrawal.class);
//					util.exportExcel(response, new ArrayList<>(), "提现数据");
//					return;
//				}else{
//					withdrawal.setChildUserIds(childUserIdList);
//				}
//			}else{
//				ExcelUtil<Withdrawal> util = new ExcelUtil<Withdrawal>(Withdrawal.class);
//				util.exportExcel(response, new ArrayList<>(), "提现数据");
//				return;
//			}
//		}

		List<Withdrawal> list = withdrawalService.selectWithdrawalList(withdrawal);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, UserInfo> userAccountMap = userInfoService.getUserAById(list.stream().map(Withdrawal::getUserId).collect(Collectors.toList()));
			for (Withdrawal bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()).getAccount());
				bankVo.setUserCode(userAccountMap.get(bankVo.getUserId()).getUserCode());
				bankVo.setStrFeeBalance(bankVo.getFeeBalance()+"BDAI");
			}
		}
		ExcelUtil<Withdrawal> util = new ExcelUtil<Withdrawal>(Withdrawal.class);
		util.exportExcel(response, list, "提现数据");
	}

	/**
	 * 获取提现详细信息
	 */
	@PreAuthorize("@ss.hasPermi('xms:withdrawal:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult getInfo(@PathVariable("id") Long id) {
		return success(withdrawalService.getById(id));
	}


	/**
	 * 修改提现
	 */
	@PreAuthorize("@ss.hasPermi('xms:withdrawal:edit')")
	@Log(title = "修改提现", businessType = BusinessType.UPDATE)
	@PutMapping
	@RepeatSubmit
	public AjaxResult edit(@RequestBody Withdrawal withdrawal) {
		return withdrawalService.updateCheckStatusById(withdrawal);
	}

}
