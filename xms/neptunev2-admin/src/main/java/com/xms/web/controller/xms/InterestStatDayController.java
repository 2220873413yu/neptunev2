package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.dao.domain.RewardStatDay;
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
import com.xms.dao.domain.InterestStatDay;
import com.xms.dao.service.IInterestStatDayService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 每日利息汇总Controller
 *
 * @author xms
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/xms/interestStatDay")
public class InterestStatDayController extends BaseController
{
    @Autowired
    private IInterestStatDayService interestStatDayService;


	@Autowired
	private UserInfoService userInfoService;
/**
 * 查询每日利息汇总列表
 */
@PreAuthorize("@ss.hasPermi('xms:interestStatDay:list')")
@GetMapping("/list")
    public TableDataInfo list(InterestStatDay interestStatDay)
    {
		if(StrUtil.isNotBlank(interestStatDay.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, interestStatDay.getUserAccount())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo != null){
				interestStatDay.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}
        startPage();
        List<InterestStatDay> list = interestStatDayService.selectInterestStatDayList(interestStatDay);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userIdMap = userInfoService.getUserAccountById(list.stream().map(InterestStatDay::getUserId).collect(Collectors.toList()));
			for (InterestStatDay order : list) {
				order.setUserAccount(userIdMap.get(order.getUserId()));
			}
		}
        return getDataTable(list);
    }

    /**
     * 导出每日利息汇总列表
     */
    @PreAuthorize("@ss.hasPermi('xms:interestStatDay:export')")
    @Log(title = "每日利息汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InterestStatDay interestStatDay)
    {
		if(StrUtil.isNotBlank(interestStatDay.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, interestStatDay.getUserAccount())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo != null){
				interestStatDay.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<InterestStatDay> util = new ExcelUtil<InterestStatDay>(InterestStatDay.class);
				util.exportExcel(response, new ArrayList<>(), "每日利息汇总数据");
			}
		}

        List<InterestStatDay> list = interestStatDayService.selectInterestStatDayList(interestStatDay);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userIdMap = userInfoService.getUserAccountById(list.stream().map(InterestStatDay::getUserId).collect(Collectors.toList()));
			for (InterestStatDay order : list) {
				order.setUserAccount(userIdMap.get(order.getUserId()));
			}
		}
		ExcelUtil<InterestStatDay> util = new ExcelUtil<InterestStatDay>(InterestStatDay.class);
        util.exportExcel(response, list, "每日利息汇总数据");
    }

    /**
     * 获取每日利息汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:interestStatDay:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(interestStatDayService.getById(id));
    }

    /**
     * 新增每日利息汇总
     */
    @PreAuthorize("@ss.hasPermi('xms:interestStatDay:add')")
    @Log(title = "每日利息汇总", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody InterestStatDay interestStatDay) {
        return toAjax(interestStatDayService.save(interestStatDay));
    }

    /**
     * 修改每日利息汇总
     */
    @PreAuthorize("@ss.hasPermi('xms:interestStatDay:edit')")
    @Log(title = "每日利息汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody InterestStatDay interestStatDay) {
        return toAjax(interestStatDayService.updateById(interestStatDay));
    }

    /**
     * 删除每日利息汇总
     */
    @PreAuthorize("@ss.hasPermi('xms:interestStatDay:remove')")
    @Log(title = "每日利息汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(interestStatDayService.removeByIds(Arrays.asList(ids)));
    }
}
