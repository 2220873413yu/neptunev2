package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
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
import com.xms.dao.domain.BoomaiReleasePlan;
import com.xms.dao.service.IBoomaiReleasePlanService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * boomai收益线性释放计划Controller
 *
 * @author xms
 * @date 2025-11-19
 */
@RestController
@RequestMapping("/xms/boomaiReleasePlan")
public class BoomaiReleasePlanController extends BaseController
{
    @Autowired
    private IBoomaiReleasePlanService boomaiReleasePlanService;

	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询boomai收益线性释放计划列表
 */
@PreAuthorize("@ss.hasPermi('xms:boomaiReleasePlan:list')")
@GetMapping("/list")
    public TableDataInfo list(BoomaiReleasePlan boomaiReleasePlan)
    {
		if(StrUtil.isNotBlank(boomaiReleasePlan.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, boomaiReleasePlan.getUserAccount())
				.one();
			if(userInfo != null){
				boomaiReleasePlan.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}

        startPage();
        List<BoomaiReleasePlan> list = boomaiReleasePlanService.selectBoomaiReleasePlanList(boomaiReleasePlan);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(BoomaiReleasePlan::getUserId).collect(Collectors.toList()));
			for (BoomaiReleasePlan bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
		return getDataTable(list);
    }

	/**
	 * 根据 remark 中记录的来源 id 查询原始计划列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:boomaiReleasePlan:list')")
	@GetMapping("/source")
	public AjaxResult sourceList(@org.springframework.web.bind.annotation.RequestParam("remark") String remark) {
		if (StrUtil.isBlank(remark)) {
			return success(Collections.emptyList());
		}
		List<Long> ids = Arrays.stream(remark.split(","))
			.map(String::trim)
			.filter(StrUtil::isNotBlank)
			.filter(StrUtil::isNumeric)
			.map(Long::valueOf)
			.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(ids)) {
			return success(Collections.emptyList());
		}
		List<BoomaiReleasePlan> rawList = boomaiReleasePlanService.listByIds(ids);
		if (CollectionUtil.isEmpty(rawList)) {
			return success(Collections.emptyList());
		}
		Map<Long, BoomaiReleasePlan> planMap = rawList.stream()
			.collect(Collectors.toMap(BoomaiReleasePlan::getId, item -> item, (a, b) -> a));
		List<BoomaiReleasePlan> orderedList = ids.stream()
			.map(planMap::get)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(orderedList)) {
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(
				orderedList.stream().map(BoomaiReleasePlan::getUserId).distinct().collect(Collectors.toList()));
			for (BoomaiReleasePlan plan : orderedList) {
				plan.setUserAccount(userAccountMap.get(plan.getUserId()));
			}
		}
		return success(orderedList);
	}

    /**
     * 导出boomai收益线性释放计划列表
     */
    @PreAuthorize("@ss.hasPermi('xms:boomaiReleasePlan:export')")
    @Log(title = "boomai收益线性释放计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BoomaiReleasePlan boomaiReleasePlan)
    {
		if(StrUtil.isNotBlank(boomaiReleasePlan.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, boomaiReleasePlan.getUserAccount())
				.one();
			if(userInfo != null){
				boomaiReleasePlan.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<BoomaiReleasePlan> util = new ExcelUtil<BoomaiReleasePlan>(BoomaiReleasePlan.class);
				util.exportExcel(response, new ArrayList<>(), "boomai收益线性释放计划数据");
			}
		}

        List<BoomaiReleasePlan> list = boomaiReleasePlanService.selectBoomaiReleasePlanList(boomaiReleasePlan);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(BoomaiReleasePlan::getUserId).collect(Collectors.toList()));
			for (BoomaiReleasePlan bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
		ExcelUtil<BoomaiReleasePlan> util = new ExcelUtil<BoomaiReleasePlan>(BoomaiReleasePlan.class);
        util.exportExcel(response, list, "boomai收益线性释放计划数据");
    }

    /**
     * 获取boomai收益线性释放计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:boomaiReleasePlan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(boomaiReleasePlanService.getById(id));
    }

    /**
     * 新增boomai收益线性释放计划
     */
    @PreAuthorize("@ss.hasPermi('xms:boomaiReleasePlan:add')")
    @Log(title = "boomai收益线性释放计划", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BoomaiReleasePlan boomaiReleasePlan) {
        return toAjax(boomaiReleasePlanService.save(boomaiReleasePlan));
    }

    /**
     * 修改boomai收益线性释放计划
     */
    @PreAuthorize("@ss.hasPermi('xms:boomaiReleasePlan:edit')")
    @Log(title = "boomai收益线性释放计划", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody BoomaiReleasePlan boomaiReleasePlan) {
        return toAjax(boomaiReleasePlanService.updateById(boomaiReleasePlan));
    }

    /**
     * 删除boomai收益线性释放计划
     */
    @PreAuthorize("@ss.hasPermi('xms:boomaiReleasePlan:remove')")
    @Log(title = "boomai收益线性释放计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(boomaiReleasePlanService.removeByIds(Arrays.asList(ids)));
    }
}
