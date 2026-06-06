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
import com.xms.dao.domain.NodePlan;
import com.xms.dao.service.INodePlanService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 认购节点配置Controller
 *
 * @author xms
 * @date 2026-01-16
 */
@RestController
@RequestMapping("/xms/nodePlan")
public class NodePlanController extends BaseController
{
    @Autowired
    private INodePlanService nodePlanService;

/**
 * 查询认购节点配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:nodePlan:list')")
@GetMapping("/list")
    public TableDataInfo list(NodePlan nodePlan)
    {
        startPage();
        List<NodePlan> list = nodePlanService.selectNodePlanList(nodePlan);
        return getDataTable(list);
    }

    /**
     * 导出认购节点配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlan:export')")
    @Log(title = "认购节点配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NodePlan nodePlan)
    {
        List<NodePlan> list = nodePlanService.selectNodePlanList(nodePlan);
        ExcelUtil<NodePlan> util = new ExcelUtil<NodePlan>(NodePlan.class);
        util.exportExcel(response, list, "认购节点配置数据");
    }

    /**
     * 获取认购节点配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(nodePlanService.getById(id));
    }

    /**
     * 新增认购节点配置
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlan:add')")
    @Log(title = "拨付节点配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody NodePlan nodePlan) {
        return toAjax(nodePlanService.save(nodePlan));
    }

    /**
     * 修改认购节点配置
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlan:edit')")
    @Log(title = "认购节点配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody NodePlan nodePlan) {
        return toAjax(nodePlanService.updateNodePlanById(nodePlan));
    }

    /**
     * 删除认购节点配置
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlan:remove')")
    @Log(title = "认购节点配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(nodePlanService.removeByIds(Arrays.asList(ids)));
    }
}
