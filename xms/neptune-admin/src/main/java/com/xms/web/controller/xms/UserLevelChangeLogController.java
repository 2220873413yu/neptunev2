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
import com.xms.dao.domain.UserLevelChangeLog;
import com.xms.dao.service.IUserLevelChangeLogService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户等级变动日志Controller
 *
 * @author xms
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/xms/userLevelChangeLog")
public class UserLevelChangeLogController extends BaseController
{
    @Autowired
    private IUserLevelChangeLogService userLevelChangeLogService;

/**
 * 查询用户等级变动日志列表
 */
@PreAuthorize("@ss.hasPermi('xms:userLevelChangeLog:list')")
@GetMapping("/list")
    public TableDataInfo list(UserLevelChangeLog userLevelChangeLog)
    {
        startPage();
        List<UserLevelChangeLog> list = userLevelChangeLogService.selectUserLevelChangeLogList(userLevelChangeLog);
        return getDataTable(list);
    }

    /**
     * 导出用户等级变动日志列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userLevelChangeLog:export')")
    @Log(title = "用户等级变动日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserLevelChangeLog userLevelChangeLog)
    {
        List<UserLevelChangeLog> list = userLevelChangeLogService.selectUserLevelChangeLogList(userLevelChangeLog);
        ExcelUtil<UserLevelChangeLog> util = new ExcelUtil<UserLevelChangeLog>(UserLevelChangeLog.class);
        util.exportExcel(response, list, "用户等级变动日志数据");
    }

    /**
     * 获取用户等级变动日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userLevelChangeLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userLevelChangeLogService.getById(id));
    }

    /**
     * 新增用户等级变动日志
     */
    @PreAuthorize("@ss.hasPermi('xms:userLevelChangeLog:add')")
    @Log(title = "用户等级变动日志", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserLevelChangeLog userLevelChangeLog) {
        return toAjax(userLevelChangeLogService.save(userLevelChangeLog));
    }

    /**
     * 修改用户等级变动日志
     */
    @PreAuthorize("@ss.hasPermi('xms:userLevelChangeLog:edit')")
    @Log(title = "用户等级变动日志", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserLevelChangeLog userLevelChangeLog) {
        return toAjax(userLevelChangeLogService.updateById(userLevelChangeLog));
    }

    /**
     * 删除用户等级变动日志
     */
    @PreAuthorize("@ss.hasPermi('xms:userLevelChangeLog:remove')")
    @Log(title = "用户等级变动日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userLevelChangeLogService.removeByIds(Arrays.asList(ids)));
    }
}
