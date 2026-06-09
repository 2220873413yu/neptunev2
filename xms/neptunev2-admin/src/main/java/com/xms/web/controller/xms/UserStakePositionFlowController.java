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
import com.xms.dao.domain.UserStakePositionFlow;
import com.xms.dao.service.IUserStakePositionFlowService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户持仓变动流水Controller
 *
 * @author xms
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/xms/userStakePositionFlow")
public class UserStakePositionFlowController extends BaseController
{
    @Autowired
    private IUserStakePositionFlowService userStakePositionFlowService;

/**
 * 查询用户持仓变动流水列表
 */
@PreAuthorize("@ss.hasPermi('xms:userStakePositionFlow:list')")
@GetMapping("/list")
    public TableDataInfo list(UserStakePositionFlow userStakePositionFlow)
    {
        startPage();
        List<UserStakePositionFlow> list = userStakePositionFlowService.selectUserStakePositionFlowList(userStakePositionFlow);
        return getDataTable(list);
    }

    /**
     * 导出用户持仓变动流水列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePositionFlow:export')")
    @Log(title = "用户持仓变动流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserStakePositionFlow userStakePositionFlow)
    {
        List<UserStakePositionFlow> list = userStakePositionFlowService.selectUserStakePositionFlowList(userStakePositionFlow);
        ExcelUtil<UserStakePositionFlow> util = new ExcelUtil<UserStakePositionFlow>(UserStakePositionFlow.class);
        util.exportExcel(response, list, "用户持仓变动流水数据");
    }

    /**
     * 获取用户持仓变动流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePositionFlow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userStakePositionFlowService.getById(id));
    }

    /**
     * 新增用户持仓变动流水
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePositionFlow:add')")
    @Log(title = "用户持仓变动流水", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserStakePositionFlow userStakePositionFlow) {
        return toAjax(userStakePositionFlowService.save(userStakePositionFlow));
    }

    /**
     * 修改用户持仓变动流水
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePositionFlow:edit')")
    @Log(title = "用户持仓变动流水", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserStakePositionFlow userStakePositionFlow) {
        return toAjax(userStakePositionFlowService.updateById(userStakePositionFlow));
    }

    /**
     * 删除用户持仓变动流水
     */
    @PreAuthorize("@ss.hasPermi('xms:userStakePositionFlow:remove')")
    @Log(title = "用户持仓变动流水", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userStakePositionFlowService.removeByIds(Arrays.asList(ids)));
    }
}
