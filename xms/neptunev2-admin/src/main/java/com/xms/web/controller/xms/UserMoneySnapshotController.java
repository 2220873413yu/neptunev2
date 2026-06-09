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
import com.xms.dao.domain.UserMoneySnapshot;
import com.xms.dao.service.IUserMoneySnapshotService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户余额快照Controller
 *
 * @author xms
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/xms/userMoneySnapshot")
public class UserMoneySnapshotController extends BaseController
{
    @Autowired
    private IUserMoneySnapshotService userMoneySnapshotService;

/**
 * 查询用户余额快照列表
 */
@PreAuthorize("@ss.hasPermi('xms:userMoneySnapshot:list')")
@GetMapping("/list")
    public TableDataInfo list(UserMoneySnapshot userMoneySnapshot)
    {
        startPage();
        List<UserMoneySnapshot> list = userMoneySnapshotService.selectUserMoneySnapshotList(userMoneySnapshot);
        return getDataTable(list);
    }

    /**
     * 导出用户余额快照列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userMoneySnapshot:export')")
    @Log(title = "用户余额快照", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserMoneySnapshot userMoneySnapshot)
    {
        List<UserMoneySnapshot> list = userMoneySnapshotService.selectUserMoneySnapshotList(userMoneySnapshot);
        ExcelUtil<UserMoneySnapshot> util = new ExcelUtil<UserMoneySnapshot>(UserMoneySnapshot.class);
        util.exportExcel(response, list, "用户余额快照数据");
    }

    /**
     * 获取用户余额快照详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userMoneySnapshot:query')")
    @GetMapping(value = "/{snapshotId}")
    public AjaxResult getInfo(@PathVariable("snapshotId") Long snapshotId) {
        return success(userMoneySnapshotService.getById(snapshotId));
    }

    /**
     * 新增用户余额快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userMoneySnapshot:add')")
    @Log(title = "用户余额快照", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserMoneySnapshot userMoneySnapshot) {
        return toAjax(userMoneySnapshotService.save(userMoneySnapshot));
    }

    /**
     * 修改用户余额快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userMoneySnapshot:edit')")
    @Log(title = "用户余额快照", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserMoneySnapshot userMoneySnapshot) {
        return toAjax(userMoneySnapshotService.updateById(userMoneySnapshot));
    }

    /**
     * 删除用户余额快照
     */
    @PreAuthorize("@ss.hasPermi('xms:userMoneySnapshot:remove')")
    @Log(title = "用户余额快照", businessType = BusinessType.DELETE)
    @DeleteMapping("/{snapshotIds}")
    public AjaxResult remove(@PathVariable Long[] snapshotIds) {
        return toAjax(userMoneySnapshotService.removeByIds(Arrays.asList(snapshotIds)));
    }
}
