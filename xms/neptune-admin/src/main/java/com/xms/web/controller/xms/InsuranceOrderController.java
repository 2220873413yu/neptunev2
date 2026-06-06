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
import com.xms.dao.domain.InsuranceOrder;
import com.xms.dao.service.IInsuranceOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 保险仓释放订单Controller
 *
 * @author xms
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/xms/insuranceOrder")
public class InsuranceOrderController extends BaseController
{
    @Autowired
    private IInsuranceOrderService insuranceOrderService;

/**
 * 查询保险仓释放订单列表
 */
@PreAuthorize("@ss.hasPermi('xms:insuranceOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(InsuranceOrder insuranceOrder)
    {
        startPage();
        List<InsuranceOrder> list = insuranceOrderService.selectInsuranceOrderList(insuranceOrder);
        return getDataTable(list);
    }

    /**
     * 导出保险仓释放订单列表
     */
    @PreAuthorize("@ss.hasPermi('xms:insuranceOrder:export')")
    @Log(title = "保险仓释放订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InsuranceOrder insuranceOrder)
    {
        List<InsuranceOrder> list = insuranceOrderService.selectInsuranceOrderList(insuranceOrder);
        ExcelUtil<InsuranceOrder> util = new ExcelUtil<InsuranceOrder>(InsuranceOrder.class);
        util.exportExcel(response, list, "保险仓释放订单数据");
    }

    /**
     * 获取保险仓释放订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:insuranceOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(insuranceOrderService.getById(id));
    }

    /**
     * 新增保险仓释放订单
     */
    @PreAuthorize("@ss.hasPermi('xms:insuranceOrder:add')")
    @Log(title = "保险仓释放订单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody InsuranceOrder insuranceOrder) {
        return toAjax(insuranceOrderService.save(insuranceOrder));
    }

    /**
     * 修改保险仓释放订单
     */
    @PreAuthorize("@ss.hasPermi('xms:insuranceOrder:edit')")
    @Log(title = "保险仓释放订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody InsuranceOrder insuranceOrder) {
        return toAjax(insuranceOrderService.updateById(insuranceOrder));
    }

    /**
     * 删除保险仓释放订单
     */
    @PreAuthorize("@ss.hasPermi('xms:insuranceOrder:remove')")
    @Log(title = "保险仓释放订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(insuranceOrderService.removeByIds(Arrays.asList(ids)));
    }
}
