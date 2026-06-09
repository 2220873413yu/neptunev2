package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.dao.domain.RechargeRecord;
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
import com.xms.dao.domain.UserIncomeSummary;
import com.xms.dao.service.IUserIncomeSummaryService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户收益信息Controller
 *
 * @author xms
 * @date 2025-08-14
 */
@RestController
@RequestMapping("/xms/userIncomeSummary")
public class UserIncomeSummaryController extends BaseController
{
    @Autowired
    private IUserIncomeSummaryService userIncomeSummaryService;

	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询用户收益信息列表
 */
@PreAuthorize("@ss.hasPermi('xms:userIncomeSummary:list')")
@GetMapping("/list")
    public TableDataInfo list(UserIncomeSummary userIncomeSummary)
    {
		if(StrUtil.isNotBlank(userIncomeSummary.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userIncomeSummary.getUserAccount())
				.one();
			if(userInfo != null){
				userIncomeSummary.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}
        startPage();
        List<UserIncomeSummary> list = userIncomeSummaryService.selectUserIncomeSummaryList(userIncomeSummary);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserIncomeSummary::getUserId).collect(Collectors.toList()));
			for (UserIncomeSummary bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
		return getDataTable(list);
    }

    /**
     * 导出用户收益信息列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userIncomeSummary:export')")
    @Log(title = "用户收益信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserIncomeSummary userIncomeSummary)
    {
		if(StrUtil.isNotBlank(userIncomeSummary.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userIncomeSummary.getUserAccount())
				.one();
			if(userInfo != null){
				userIncomeSummary.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<UserIncomeSummary> util = new ExcelUtil<UserIncomeSummary>(UserIncomeSummary.class);
				util.exportExcel(response, new ArrayList<>(), "充值记录数据");
				return;
			}
		}

        List<UserIncomeSummary> list = userIncomeSummaryService.selectUserIncomeSummaryList(userIncomeSummary);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserIncomeSummary::getUserId).collect(Collectors.toList()));
			for (UserIncomeSummary bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
		ExcelUtil<UserIncomeSummary> util = new ExcelUtil<UserIncomeSummary>(UserIncomeSummary.class);
        util.exportExcel(response, list, "用户收益信息数据");
    }

    /**
     * 获取用户收益信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userIncomeSummary:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId) {
        return success(userIncomeSummaryService.getById(userId));
    }

    /**
     * 新增用户收益信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userIncomeSummary:add')")
    @Log(title = "用户收益信息", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserIncomeSummary userIncomeSummary) {
        return toAjax(userIncomeSummaryService.save(userIncomeSummary));
    }

    /**
     * 修改用户收益信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userIncomeSummary:edit')")
    @Log(title = "用户收益信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserIncomeSummary userIncomeSummary) {
        return toAjax(userIncomeSummaryService.updateById(userIncomeSummary));
    }

    /**
     * 删除用户收益信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userIncomeSummary:remove')")
    @Log(title = "用户收益信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        return toAjax(userIncomeSummaryService.removeByIds(Arrays.asList(userIds)));
    }
}
