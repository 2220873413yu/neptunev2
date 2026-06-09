package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xms.common.annotation.Log;
import com.xms.common.core.controller.BaseController;
import com.xms.common.core.domain.AjaxResult;
import com.xms.common.enums.BusinessType;
import com.xms.dao.domain.UserRechangeAddress;
import com.xms.dao.service.IUserRechangeAddressService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户充值地址Controller
 *
 * @author xms
 * @date 2025-08-04
 */
@RestController
@RequestMapping("/xms/userRechangeAddress")
public class UserRechangeAddressController extends BaseController
{
    @Autowired
    private IUserRechangeAddressService userRechangeAddressService;

	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询用户充值地址列表
 */
@PreAuthorize("@ss.hasPermi('xms:userRechangeAddress:list')")
@GetMapping("/list")
    public TableDataInfo list(UserRechangeAddress userRechangeAddress)
    {
		if(StrUtil.isNotBlank(userRechangeAddress.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userRechangeAddress.getUserAccount())
				.one();
			if(userInfo != null){
				userRechangeAddress.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}

        startPage();
        List<UserRechangeAddress> list = userRechangeAddressService.selectUserRechangeAddressList(userRechangeAddress);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserRechangeAddress::getUserId).collect(Collectors.toList()));
			for (UserRechangeAddress bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
		return getDataTable(list);
    }

    /**
     * 导出用户充值地址列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userRechangeAddress:export')")
    @Log(title = "用户充值地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserRechangeAddress userRechangeAddress)
    {
		if(StrUtil.isNotBlank(userRechangeAddress.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userRechangeAddress.getUserAccount())
				.one();
			if(userInfo != null){
				userRechangeAddress.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<UserRechangeAddress> util = new ExcelUtil<UserRechangeAddress>(UserRechangeAddress.class);
				util.exportExcel(response, new ArrayList<>(), "用户充值地址数据");
			}
		}
        List<UserRechangeAddress> list = userRechangeAddressService.selectUserRechangeAddressList(userRechangeAddress);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserRechangeAddress::getUserId).collect(Collectors.toList()));
			for (UserRechangeAddress bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
		ExcelUtil<UserRechangeAddress> util = new ExcelUtil<UserRechangeAddress>(UserRechangeAddress.class);
        util.exportExcel(response, list, "用户充值地址数据");
    }

    /**
     * 获取用户充值地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userRechangeAddress:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userRechangeAddressService.getById(id));
    }

//    /**
//     * 新增用户充值地址
//     */
//    @PreAuthorize("@ss.hasPermi('xms:userRechangeAddress:add')")
//    @Log(title = "用户充值地址", businessType = BusinessType.INSERT)
//    @PostMapping
//    @RepeatSubmit
//    public AjaxResult add(@RequestBody UserRechangeAddress userRechangeAddress) {
//        return toAjax(userRechangeAddressService.save(userRechangeAddress));
//    }
//
//    /**
//     * 修改用户充值地址
//     */
//    @PreAuthorize("@ss.hasPermi('xms:userRechangeAddress:edit')")
//    @Log(title = "用户充值地址", businessType = BusinessType.UPDATE)
//    @PutMapping
//    @RepeatSubmit
//    public AjaxResult edit(@RequestBody UserRechangeAddress userRechangeAddress) {
//        return toAjax(userRechangeAddressService.updateById(userRechangeAddress));
//    }
//
//    /**
//     * 删除用户充值地址
//     */
//    @PreAuthorize("@ss.hasPermi('xms:userRechangeAddress:remove')")
//    @Log(title = "用户充值地址", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(userRechangeAddressService.removeByIds(Arrays.asList(ids)));
//    }
}
