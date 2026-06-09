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
import com.xms.dao.domain.RewardPoolBatch;
import com.xms.dao.service.IRewardPoolBatchService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 分红批次记录Controller
 *
 * @author xms
 * @date 2025-12-08
 */
@RestController
@RequestMapping("/xms/rewardPoolBatch")
public class RewardPoolBatchController extends BaseController
{
    @Autowired
    private IRewardPoolBatchService rewardPoolBatchService;

/**
 * 查询分红批次记录列表
 */
@PreAuthorize("@ss.hasPermi('xms:rewardPoolBatch:list')")
@GetMapping("/list")
    public TableDataInfo list(RewardPoolBatch rewardPoolBatch)
    {
        startPage();
        List<RewardPoolBatch> list = rewardPoolBatchService.selectRewardPoolBatchList(rewardPoolBatch);
        return getDataTable(list);
    }

    /**
     * 导出分红批次记录列表
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolBatch:export')")
    @Log(title = "分红批次记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RewardPoolBatch rewardPoolBatch)
    {
        List<RewardPoolBatch> list = rewardPoolBatchService.selectRewardPoolBatchList(rewardPoolBatch);
        ExcelUtil<RewardPoolBatch> util = new ExcelUtil<RewardPoolBatch>(RewardPoolBatch.class);
        util.exportExcel(response, list, "分红批次记录数据");
    }

    /**
     * 获取分红批次记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolBatch:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(rewardPoolBatchService.getById(id));
    }

    /**
     * 新增分红批次记录
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolBatch:add')")
    @Log(title = "分红批次记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody RewardPoolBatch rewardPoolBatch) {
        return toAjax(rewardPoolBatchService.save(rewardPoolBatch));
    }

    /**
     * 修改分红批次记录
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolBatch:edit')")
    @Log(title = "分红批次记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody RewardPoolBatch rewardPoolBatch) {
        return toAjax(rewardPoolBatchService.updateById(rewardPoolBatch));
    }

    /**
     * 删除分红批次记录
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolBatch:remove')")
    @Log(title = "分红批次记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(rewardPoolBatchService.removeByIds(Arrays.asList(ids)));
    }
}
