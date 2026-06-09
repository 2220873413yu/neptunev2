package com.xms.web.controller.xms;

import java.util.Arrays;
import java.util.List;

import com.xms.common.annotation.RepeatSubmit;
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
import com.xms.dao.domain.AiMarketInsight;
import com.xms.dao.service.IAiMarketInsightService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * AI分析行情Controller
 *
 * @author xms
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/xms/aiMarketInsight")
public class AiMarketInsightController extends BaseController
{
    @Autowired
    private IAiMarketInsightService aiMarketInsightService;

/**
 * 查询AI分析行情列表
 */
@PreAuthorize("@ss.hasPermi('xms:aiMarketInsight:list')")
@GetMapping("/list")
    public TableDataInfo list(AiMarketInsight aiMarketInsight)
    {
        startPage();
        List<AiMarketInsight> list = aiMarketInsightService.selectAiMarketInsightList(aiMarketInsight);
        return getDataTable(list);
    }

    /**
     * 导出AI分析行情列表
     */
    @PreAuthorize("@ss.hasPermi('xms:aiMarketInsight:export')")
    @Log(title = "AI分析行情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiMarketInsight aiMarketInsight)
    {
        List<AiMarketInsight> list = aiMarketInsightService.selectAiMarketInsightList(aiMarketInsight);
        ExcelUtil<AiMarketInsight> util = new ExcelUtil<AiMarketInsight>(AiMarketInsight.class);
        util.exportExcel(response, list, "AI分析行情数据");
    }

    /**
     * 获取AI分析行情详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:aiMarketInsight:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiMarketInsightService.getById(id));
    }

    /**
     * 新增AI分析行情
     */
    @PreAuthorize("@ss.hasPermi('xms:aiMarketInsight:add')")
    @Log(title = "AI分析行情", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody AiMarketInsight aiMarketInsight) {
        return toAjax(aiMarketInsightService.save(aiMarketInsight));
    }

    /**
     * 修改AI分析行情
     */
    @PreAuthorize("@ss.hasPermi('xms:aiMarketInsight:edit')")
    @Log(title = "AI分析行情", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody AiMarketInsight aiMarketInsight) {
        return toAjax(aiMarketInsightService.updateById(aiMarketInsight));
    }

    /**
     * 删除AI分析行情
     */
    @PreAuthorize("@ss.hasPermi('xms:aiMarketInsight:remove')")
    @Log(title = "AI分析行情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(aiMarketInsightService.deleteRecordById(Arrays.asList(ids)));
    }
}
