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
import com.xms.dao.domain.StakeDailySnapshot;
import com.xms.dao.service.IStakeDailySnapshotService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 每日质押数据快照Controller
 *
 * @author xms
 * @date 2026-03-30
 */
@RestController
@RequestMapping("/xms/stakeDailySnapshot")
public class StakeDailySnapshotController extends BaseController
{
    @Autowired
    private IStakeDailySnapshotService stakeDailySnapshotService;

/**
 * 查询每日质押数据快照列表
 */
@PreAuthorize("@ss.hasPermi('xms:stakeDailySnapshot:list')")
@GetMapping("/list")
    public TableDataInfo list(StakeDailySnapshot stakeDailySnapshot)
    {
        startPage();
        List<StakeDailySnapshot> list = stakeDailySnapshotService.selectStakeDailySnapshotList(stakeDailySnapshot);
        return getDataTable(list);
    }

    /**
     * 导出每日质押数据快照列表
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeDailySnapshot:export')")
    @Log(title = "每日质押数据快照", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakeDailySnapshot stakeDailySnapshot)
    {
        List<StakeDailySnapshot> list = stakeDailySnapshotService.selectStakeDailySnapshotList(stakeDailySnapshot);
        ExcelUtil<StakeDailySnapshot> util = new ExcelUtil<StakeDailySnapshot>(StakeDailySnapshot.class);
        util.exportExcel(response, list, "每日质押数据快照数据");
    }

    /**
     * 获取每日质押数据快照详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeDailySnapshot:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(stakeDailySnapshotService.getById(id));
    }

    /**
     * 新增每日质押数据快照
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeDailySnapshot:add')")
    @Log(title = "每日质押数据快照", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody StakeDailySnapshot stakeDailySnapshot) {
        return toAjax(stakeDailySnapshotService.save(stakeDailySnapshot));
    }

    /**
     * 修改每日质押数据快照
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeDailySnapshot:edit')")
    @Log(title = "每日质押数据快照", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody StakeDailySnapshot stakeDailySnapshot) {
        return toAjax(stakeDailySnapshotService.updateById(stakeDailySnapshot));
    }

    /**
     * 删除每日质押数据快照
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeDailySnapshot:remove')")
    @Log(title = "每日质押数据快照", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stakeDailySnapshotService.removeByIds(Arrays.asList(ids)));
    }
}
