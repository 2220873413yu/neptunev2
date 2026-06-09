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
import com.xms.dao.domain.AirdropRound;
import com.xms.dao.service.IAirdropRoundService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 空投轮次配置Controller
 *
 * @author xms
 * @date 2026-01-01
 */
@RestController
@RequestMapping("/xms/airdropRound")
public class AirdropRoundController extends BaseController
{
    @Autowired
    private IAirdropRoundService airdropRoundService;

/**
 * 查询空投轮次配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:airdropRound:list')")
@GetMapping("/list")
    public TableDataInfo list(AirdropRound airdropRound)
    {
        startPage();
        List<AirdropRound> list = airdropRoundService.selectAirdropRoundList(airdropRound);
        return getDataTable(list);
    }

    /**
     * 导出空投轮次配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropRound:export')")
    @Log(title = "空投轮次配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AirdropRound airdropRound)
    {
        List<AirdropRound> list = airdropRoundService.selectAirdropRoundList(airdropRound);
        ExcelUtil<AirdropRound> util = new ExcelUtil<AirdropRound>(AirdropRound.class);
        util.exportExcel(response, list, "空投轮次配置数据");
    }

    /**
     * 获取空投轮次配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropRound:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(airdropRoundService.getById(id));
    }

    /**
     * 新增空投轮次配置
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropRound:add')")
    @Log(title = "空投轮次配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody AirdropRound airdropRound) {
        return toAjax(airdropRoundService.saveRecord(airdropRound));
    }

    /**
     * 修改空投轮次配置
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropRound:edit')")
    @Log(title = "空投轮次配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody AirdropRound airdropRound) {
        return toAjax(airdropRoundService.updateRecordById(airdropRound));
    }

    /**
     * 删除空投轮次配置
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropRound:remove')")
    @Log(title = "空投轮次配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(airdropRoundService.removeByIds(Arrays.asList(ids)));
    }
}
