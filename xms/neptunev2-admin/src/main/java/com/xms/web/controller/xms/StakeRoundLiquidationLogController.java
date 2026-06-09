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
import com.xms.dao.domain.StakeRoundLiquidationLog;
import com.xms.dao.service.IStakeRoundLiquidationLogService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 轮次爆仓判定与执行日志Controller
 *
 * @author xms
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/xms/stakeRoundLiquidationLog")
public class StakeRoundLiquidationLogController extends BaseController
{
    @Autowired
    private IStakeRoundLiquidationLogService stakeRoundLiquidationLogService;

/**
 * 查询轮次爆仓判定与执行日志列表
 */
@PreAuthorize("@ss.hasPermi('xms:stakeRoundLiquidationLog:list')")
@GetMapping("/list")
    public TableDataInfo list(StakeRoundLiquidationLog stakeRoundLiquidationLog)
    {
        startPage();
        List<StakeRoundLiquidationLog> list = stakeRoundLiquidationLogService.selectStakeRoundLiquidationLogList(stakeRoundLiquidationLog);
        return getDataTable(list);
    }

    /**
     * 导出轮次爆仓判定与执行日志列表
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRoundLiquidationLog:export')")
    @Log(title = "轮次爆仓判定与执行日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakeRoundLiquidationLog stakeRoundLiquidationLog)
    {
        List<StakeRoundLiquidationLog> list = stakeRoundLiquidationLogService.selectStakeRoundLiquidationLogList(stakeRoundLiquidationLog);
        ExcelUtil<StakeRoundLiquidationLog> util = new ExcelUtil<StakeRoundLiquidationLog>(StakeRoundLiquidationLog.class);
        util.exportExcel(response, list, "轮次爆仓判定与执行日志数据");
    }

    /**
     * 获取轮次爆仓判定与执行日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRoundLiquidationLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(stakeRoundLiquidationLogService.getById(id));
    }

    /**
     * 新增轮次爆仓判定与执行日志
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRoundLiquidationLog:add')")
    @Log(title = "轮次爆仓判定与执行日志", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody StakeRoundLiquidationLog stakeRoundLiquidationLog) {
        return toAjax(stakeRoundLiquidationLogService.save(stakeRoundLiquidationLog));
    }

    /**
     * 修改轮次爆仓判定与执行日志
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRoundLiquidationLog:edit')")
    @Log(title = "轮次爆仓判定与执行日志", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody StakeRoundLiquidationLog stakeRoundLiquidationLog) {
        return toAjax(stakeRoundLiquidationLogService.updateById(stakeRoundLiquidationLog));
    }

    /**
     * 删除轮次爆仓判定与执行日志
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRoundLiquidationLog:remove')")
    @Log(title = "轮次爆仓判定与执行日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stakeRoundLiquidationLogService.removeByIds(Arrays.asList(ids)));
    }
}
