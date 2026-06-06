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
import com.xms.dao.domain.UserWealthVaultSnapshot;
import com.xms.dao.service.IUserWealthVaultSnapshotService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户财富仓快照Controller
 *
 * @author xms
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/xms/userWealthVaultSnapshot")
public class UserWealthVaultSnapshotController extends BaseController
{
    @Autowired
    private IUserWealthVaultSnapshotService userWealthVaultSnapshotService;

/**
 * 查询用户财富仓快照列表
 */
@PreAuthorize("@ss.hasPermi('xms:userWealthVaultSnapshot:list')")
@GetMapping("/list")
    public TableDataInfo list(UserWealthVaultSnapshot userWealthVaultSnapshot)
    {
        startPage();
        List<UserWealthVaultSnapshot> list = userWealthVaultSnapshotService.selectUserWealthVaultSnapshotList(userWealthVaultSnapshot);
        return getDataTable(list);
    }

    /**
     * 导出用户财富仓快照列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVaultSnapshot:export')")
    @Log(title = "用户财富仓快照", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserWealthVaultSnapshot userWealthVaultSnapshot)
    {
        List<UserWealthVaultSnapshot> list = userWealthVaultSnapshotService.selectUserWealthVaultSnapshotList(userWealthVaultSnapshot);
        ExcelUtil<UserWealthVaultSnapshot> util = new ExcelUtil<UserWealthVaultSnapshot>(UserWealthVaultSnapshot.class);
        util.exportExcel(response, list, "用户财富仓快照数据");
    }

    /**
     * 获取用户财富仓快照详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVaultSnapshot:query')")
    @GetMapping(value = "/{snapshotId}")
    public AjaxResult getInfo(@PathVariable("snapshotId") Long snapshotId) {
        return success(userWealthVaultSnapshotService.getById(snapshotId));
    }

    /**
     * 新增用户财富仓快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVaultSnapshot:add')")
    @Log(title = "用户财富仓快照", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserWealthVaultSnapshot userWealthVaultSnapshot) {
        return toAjax(userWealthVaultSnapshotService.save(userWealthVaultSnapshot));
    }

    /**
     * 修改用户财富仓快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVaultSnapshot:edit')")
    @Log(title = "用户财富仓快照", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserWealthVaultSnapshot userWealthVaultSnapshot) {
        return toAjax(userWealthVaultSnapshotService.updateById(userWealthVaultSnapshot));
    }

    /**
     * 删除用户财富仓快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userWealthVaultSnapshot:remove')")
    @Log(title = "用户财富仓快照", businessType = BusinessType.DELETE)
    @DeleteMapping("/{snapshotIds}")
    public AjaxResult remove(@PathVariable Long[] snapshotIds) {
        return toAjax(userWealthVaultSnapshotService.removeByIds(Arrays.asList(snapshotIds)));
    }
}
