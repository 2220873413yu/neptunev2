package com.xms.web.controller.xms;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.exception.ServiceException;
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
import com.xms.dao.domain.PtbDayRatioRule;
import com.xms.dao.service.IPtbDayRatioRuleService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * BOOMAI日利率调节规则Controller
 *
 * @author xms
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/xms/dayRatioRule")
public class PtbDayRatioRuleController extends BaseController
{
    @Autowired
    private IPtbDayRatioRuleService ptbDayRatioRuleService;

/**
 * 查询BOOMAI日利率调节规则列表
 */
@PreAuthorize("@ss.hasPermi('xms:dayRatioRule:list')")
@GetMapping("/list")
    public TableDataInfo list(PtbDayRatioRule ptbDayRatioRule)
    {
        startPage();
        List<PtbDayRatioRule> list = ptbDayRatioRuleService.selectPtbDayRatioRuleList(ptbDayRatioRule);
        return getDataTable(list);
    }

    /**
     * 导出BOOMAI日利率调节规则列表
     */
    @PreAuthorize("@ss.hasPermi('xms:dayRatioRule:export')")
    @Log(title = "BOOMAI日利率调节规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PtbDayRatioRule ptbDayRatioRule)
    {
        List<PtbDayRatioRule> list = ptbDayRatioRuleService.selectPtbDayRatioRuleList(ptbDayRatioRule);
        ExcelUtil<PtbDayRatioRule> util = new ExcelUtil<PtbDayRatioRule>(PtbDayRatioRule.class);
        util.exportExcel(response, list, "BOOMAI日利率调节规则数据");
    }

    /**
     * 获取BOOMAI日利率调节规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:dayRatioRule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(ptbDayRatioRuleService.getById(id));
    }

    /**
     * 新增BOOMAI日利率调节规则
     */
    @PreAuthorize("@ss.hasPermi('xms:dayRatioRule:add')")
    @Log(title = "BOOMAI日利率调节规则", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody PtbDayRatioRule ptbDayRatioRule) {
        return toAjax(ptbDayRatioRuleService.save(ptbDayRatioRule));
    }

    /**
     * 修改BOOMAI日利率调节规则
     */
    @PreAuthorize("@ss.hasPermi('xms:dayRatioRule:edit')")
    @Log(title = "BOOMAI日利率调节规则", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody PtbDayRatioRule ptbDayRatioRule) {
		if(ptbDayRatioRule.getBaseRatio() == null || ptbDayRatioRule.getBaseRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("基础利率不能小于0");
		}
		if(ptbDayRatioRule.getTriggerThreshold() == null || ptbDayRatioRule.getTriggerThreshold().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("触发调节的涨跌幅阈值不能小于0");
		}

		if(ptbDayRatioRule.getMinRatio() == null || ptbDayRatioRule.getMinRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("日利率下限不能小于0");
		}
		if(ptbDayRatioRule.getMaxRatio() == null || ptbDayRatioRule.getMaxRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("日利率上限不能小于0");
		}

		if(ptbDayRatioRule.getMinRatio().compareTo(ptbDayRatioRule.getMaxRatio())>0){
			throw new ServiceException("日利率下限不能大于上限");
		}
        return toAjax(ptbDayRatioRuleService.updateById(ptbDayRatioRule));
    }

    /**
     * 删除BOOMAI日利率调节规则
     */
    @PreAuthorize("@ss.hasPermi('xms:dayRatioRule:remove')")
    @Log(title = "BOOMAI日利率调节规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(ptbDayRatioRuleService.removeByIds(Arrays.asList(ids)));
    }
}
