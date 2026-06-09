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
import com.xms.dao.domain.NodePlanOrder;
import com.xms.dao.service.INodePlanOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户节点订单Controller
 *
 * @author xms
 * @date 2026-01-16
 */
@RestController
@RequestMapping("/xms/nodePlanOrder")
public class NodePlanOrderController extends BaseController
{
    @Autowired
    private INodePlanOrderService nodePlanOrderService;

/**
 * 查询用户节点订单列表
 */
@PreAuthorize("@ss.hasPermi('xms:nodePlanOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(NodePlanOrder nodePlanOrder)
    {
        startPage();
        List<NodePlanOrder> list = nodePlanOrderService.selectNodePlanOrderList(nodePlanOrder);
        return getDataTable(list);
    }

    /**
     * 导出用户节点订单列表
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlanOrder:export')")
    @Log(title = "用户节点订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NodePlanOrder nodePlanOrder)
    {
        List<NodePlanOrder> list = nodePlanOrderService.selectNodePlanOrderList(nodePlanOrder);
        ExcelUtil<NodePlanOrder> util = new ExcelUtil<NodePlanOrder>(NodePlanOrder.class);
        util.exportExcel(response, list, "用户节点订单数据");
    }

    /**
     * 获取用户节点订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlanOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(nodePlanOrderService.getById(id));
    }

    /**
     * 新增拨付用户节点
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlanOrder:add')")
    @Log(title = "拨付用户节点", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody NodePlanOrder nodePlanOrder) {
        return toAjax(nodePlanOrderService.saveNodePlanOrder(nodePlanOrder));
    }

    /**
     * 修改用户节点订单
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlanOrder:edit')")
    @Log(title = "用户节点订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody NodePlanOrder nodePlanOrder) {
        return toAjax(nodePlanOrderService.updateById(nodePlanOrder));
    }

    /**
     * 删除用户节点订单
     */
    @PreAuthorize("@ss.hasPermi('xms:nodePlanOrder:remove')")
    @Log(title = "用户节点订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(nodePlanOrderService.removeByIds(Arrays.asList(ids)));
    }
}
