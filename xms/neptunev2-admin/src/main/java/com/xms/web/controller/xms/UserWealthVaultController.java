package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.utils.CollectionUtil;
import com.xms.dao.domain.ActiveOrder;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xms.common.annotation.Log;
import com.xms.common.core.controller.BaseController;
import com.xms.common.core.domain.AjaxResult;
import com.xms.common.enums.BusinessType;
import com.xms.dao.domain.UserWealthVault;
import com.xms.dao.service.IUserWealthVaultService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户财富表Controller
 *
 * @author xms
 * @date 2026-03-05
 */
@RestController
@RequestMapping("/xms/userWealthVault")
public class UserWealthVaultController extends BaseController
{
    @Autowired
    private IUserWealthVaultService userWealthVaultService;

	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询用户财富表列表
 */
@PreAuthorize("@ss.hasPermi('xms:userWealthVault:list')")
@GetMapping("/list")
    public TableDataInfo list(UserWealthVault userWealthVault)
    {
		if(StrUtil.isNotBlank(userWealthVault.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userWealthVault.getUserAccount())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo == null){
				return getDataTable(new ArrayList<>());
			}else{
				userWealthVault.setId(userInfo.getUserId());
			}
		}
        startPage();
        List<UserWealthVault> list = userWealthVaultService.selectUserWealthVaultList(userWealthVault);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserWealthVault::getId).collect(Collectors.toList()));
			for (UserWealthVault order : list) {
				order.setUserAccount(userAccountMap.get(order.getId()));
			}
		}
		return getDataTable(list);
    }

    /**
     * 导出用户财富表列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVault:export')")
    @Log(title = "用户财富表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserWealthVault userWealthVault)
    {
		if(StrUtil.isNotBlank(userWealthVault.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userWealthVault.getUserAccount())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo == null){
				ExcelUtil<ActiveOrder> util = new ExcelUtil<ActiveOrder>(ActiveOrder.class);
				util.exportExcel(response, new ArrayList<>(), "用户财富表数据");
			}else{
				userWealthVault.setId(userInfo.getUserId());
			}
		}
        List<UserWealthVault> list = userWealthVaultService.selectUserWealthVaultList(userWealthVault);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserWealthVault::getId).collect(Collectors.toList()));
			for (UserWealthVault order : list) {
				order.setUserAccount(userAccountMap.get(order.getId()));
			}
		}
        ExcelUtil<UserWealthVault> util = new ExcelUtil<UserWealthVault>(UserWealthVault.class);
        util.exportExcel(response, list, "用户财富表数据");
    }

    /**
     * 获取用户财富表详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVault:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userWealthVaultService.getById(id));
    }

    /**
     * 新增用户财富表
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVault:add')")
    @Log(title = "用户财富表", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserWealthVault userWealthVault) {
        return toAjax(userWealthVaultService.save(userWealthVault));
    }

    /**
     * 修改用户财富表
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVault:edit')")
    @Log(title = "用户财富表", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserWealthVault userWealthVault) {
        return toAjax(userWealthVaultService.updateById(userWealthVault));
    }

    /**
     * 删除用户财富表
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVault:remove')")
    @Log(title = "用户财富表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userWealthVaultService.removeByIds(Arrays.asList(ids)));
    }
}
