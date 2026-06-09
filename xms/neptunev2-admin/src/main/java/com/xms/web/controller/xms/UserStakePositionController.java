package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.dao.domain.StakeOrder;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.Withdrawal;
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
import com.xms.dao.domain.UserStakePosition;
import com.xms.dao.service.IUserStakePositionService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户质押持仓汇总Controller
 *
 * @author xms
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/xms/userStakePosition")
public class UserStakePositionController extends BaseController
{
    @Autowired
    private IUserStakePositionService userStakePositionService;

	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询用户质押持仓汇总列表
 */
@PreAuthorize("@ss.hasPermi('xms:userStakePosition:list')")
@GetMapping("/list")
    public TableDataInfo list(UserStakePosition userStakePosition)
    {
		if(StrUtil.isNotBlank(userStakePosition.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userStakePosition.getUserAccount())
				.one();
			if(userInfo != null){
				userStakePosition.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}
        startPage();
        List<UserStakePosition> list = userStakePositionService.selectUserStakePositionList(userStakePosition);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserStakePosition::getUserId).collect(Collectors.toList()));
			for (UserStakePosition bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
		return getDataTable(list);
    }

    /**
     * 导出用户质押持仓汇总列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePosition:export')")
    @Log(title = "用户质押持仓汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserStakePosition userStakePosition)
    {
		if(StrUtil.isNotBlank(userStakePosition.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, userStakePosition.getUserAccount())
				.one();
			if(userInfo != null){
				userStakePosition.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<Withdrawal> util = new ExcelUtil<Withdrawal>(Withdrawal.class);
				util.exportExcel(response, new ArrayList<>(), "用户质押持仓汇总数据");
				return;
			}
		}
        List<UserStakePosition> list = userStakePositionService.selectUserStakePositionList(userStakePosition);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(UserStakePosition::getUserId).collect(Collectors.toList()));
			for (UserStakePosition bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
        ExcelUtil<UserStakePosition> util = new ExcelUtil<UserStakePosition>(UserStakePosition.class);
        util.exportExcel(response, list, "用户质押持仓汇总数据");
    }

    /**
     * 获取用户质押持仓汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePosition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userStakePositionService.getById(id));
    }

    /**
     * 新增用户质押持仓汇总
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePosition:add')")
    @Log(title = "用户质押持仓汇总", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserStakePosition userStakePosition) {
        return toAjax(userStakePositionService.save(userStakePosition));
    }

    /**
     * 修改用户质押持仓汇总
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePosition:edit')")
    @Log(title = "用户质押持仓汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserStakePosition userStakePosition) {
        return toAjax(userStakePositionService.updateById(userStakePosition));
    }

    /**
     * 删除用户质押持仓汇总
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePosition:remove')")
    @Log(title = "用户质押持仓汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userStakePositionService.removeByIds(Arrays.asList(ids)));
    }
}
