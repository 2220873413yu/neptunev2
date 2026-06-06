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
import com.xms.dao.domain.IdoOrder;
import com.xms.dao.service.IIdoOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * ido订单记录Controller
 *
 * @author xms
 * @date 2025-12-25
 */
@RestController
@RequestMapping("/xms/idoOrder")
public class IdoOrderController extends BaseController
{
    @Autowired
    private IIdoOrderService idoOrderService;

/**
 * 查询ido订单记录列表
 */
@PreAuthorize("@ss.hasPermi('xms:idoOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(IdoOrder idoOrder)
    {
        startPage();
        List<IdoOrder> list = idoOrderService.selectIdoOrderList(idoOrder);
        return getDataTable(list);
    }

    /**
     * 导出ido订单记录列表
     */
    @PreAuthorize("@ss.hasPermi('xms:idoOrder:export')")
    @Log(title = "ido订单记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IdoOrder idoOrder)
    {
        List<IdoOrder> list = idoOrderService.selectIdoOrderList(idoOrder);
        ExcelUtil<IdoOrder> util = new ExcelUtil<IdoOrder>(IdoOrder.class);
        util.exportExcel(response, list, "ido订单记录数据");
    }

    /**
     * 获取ido订单记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:idoOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(idoOrderService.getById(id));
    }

    /**
     * 新增ido订单记录
     */
    @PreAuthorize("@ss.hasPermi('xms:idoOrder:add')")
    @Log(title = "ido订单记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody IdoOrder idoOrder) {
        return toAjax(idoOrderService.save(idoOrder));
    }

    /**
     * 修改ido订单记录
     */
    @PreAuthorize("@ss.hasPermi('xms:idoOrder:edit')")
    @Log(title = "ido订单记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody IdoOrder idoOrder) {
        return toAjax(idoOrderService.updateById(idoOrder));
    }

    /**
     * 删除ido订单记录
     */
    @PreAuthorize("@ss.hasPermi('xms:idoOrder:remove')")
    @Log(title = "ido订单记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(idoOrderService.removeByIds(Arrays.asList(ids)));
    }
}
