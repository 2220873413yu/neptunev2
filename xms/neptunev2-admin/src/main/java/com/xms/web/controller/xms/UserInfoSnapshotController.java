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
import com.xms.dao.domain.UserInfoSnapshot;
import com.xms.dao.service.IUserInfoSnapshotService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户信息快照Controller
 *
 * @author xms
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/xms/userInfoSnapshot")
public class UserInfoSnapshotController extends BaseController
{
    @Autowired
    private IUserInfoSnapshotService userInfoSnapshotService;

/**
 * 查询用户信息快照列表
 */
@PreAuthorize("@ss.hasPermi('xms:userInfoSnapshot:list')")
@GetMapping("/list")
    public TableDataInfo list(UserInfoSnapshot userInfoSnapshot)
    {
        startPage();
        List<UserInfoSnapshot> list = userInfoSnapshotService.selectUserInfoSnapshotList(userInfoSnapshot);
        return getDataTable(list);
    }

    /**
     * 导出用户信息快照列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userInfoSnapshot:export')")
    @Log(title = "用户信息快照", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserInfoSnapshot userInfoSnapshot)
    {
        List<UserInfoSnapshot> list = userInfoSnapshotService.selectUserInfoSnapshotList(userInfoSnapshot);
        ExcelUtil<UserInfoSnapshot> util = new ExcelUtil<UserInfoSnapshot>(UserInfoSnapshot.class);
        util.exportExcel(response, list, "用户信息快照数据");
    }

    /**
     * 获取用户信息快照详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userInfoSnapshot:query')")
    @GetMapping(value = "/{snapshotId}")
    public AjaxResult getInfo(@PathVariable("snapshotId") Long snapshotId) {
        return success(userInfoSnapshotService.getById(snapshotId));
    }

    /**
     * 新增用户信息快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userInfoSnapshot:add')")
    @Log(title = "用户信息快照", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserInfoSnapshot userInfoSnapshot) {
        return toAjax(userInfoSnapshotService.save(userInfoSnapshot));
    }

    /**
     * 修改用户信息快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userInfoSnapshot:edit')")
    @Log(title = "用户信息快照", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserInfoSnapshot userInfoSnapshot) {
        return toAjax(userInfoSnapshotService.updateById(userInfoSnapshot));
    }

    /**
     * 删除用户信息快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userInfoSnapshot:remove')")
    @Log(title = "用户信息快照", businessType = BusinessType.DELETE)
    @DeleteMapping("/{snapshotIds}")
    public AjaxResult remove(@PathVariable Long[] snapshotIds) {
        return toAjax(userInfoSnapshotService.removeByIds(Arrays.asList(snapshotIds)));
    }
}
