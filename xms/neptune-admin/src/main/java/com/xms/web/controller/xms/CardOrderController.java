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
import com.xms.dao.domain.CardOrder;
import com.xms.dao.service.ICardOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 卡片订单Controller
 *
 * @author xms
 * @date 2025-12-04
 */
@RestController
@RequestMapping("/xms/cardOrder")
public class CardOrderController extends BaseController
{
    @Autowired
    private ICardOrderService cardOrderService;

/**
 * 查询卡片订单列表
 */
@PreAuthorize("@ss.hasPermi('xms:cardOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(CardOrder cardOrder)
    {
        startPage();
        List<CardOrder> list = cardOrderService.selectCardOrderList(cardOrder);
        return getDataTable(list);
    }

    /**
     * 导出卡片订单列表
     */
    @PreAuthorize("@ss.hasPermi('xms:cardOrder:export')")
    @Log(title = "卡片订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardOrder cardOrder)
    {
        List<CardOrder> list = cardOrderService.selectCardOrderList(cardOrder);
        ExcelUtil<CardOrder> util = new ExcelUtil<CardOrder>(CardOrder.class);
        util.exportExcel(response, list, "卡片订单数据");
    }

    /**
     * 获取卡片订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:cardOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(cardOrderService.getById(id));
    }

    /**
     * 新增卡片订单
     */
    @PreAuthorize("@ss.hasPermi('xms:cardOrder:add')")
    @Log(title = "卡片订单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody CardOrder cardOrder) {
        return toAjax(cardOrderService.save(cardOrder));
    }

    /**
     * 修改卡片订单
     */
    @PreAuthorize("@ss.hasPermi('xms:cardOrder:edit')")
    @Log(title = "卡片订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody CardOrder cardOrder) {
        return toAjax(cardOrderService.updateById(cardOrder));
    }

    /**
     * 删除卡片订单
     */
    @PreAuthorize("@ss.hasPermi('xms:cardOrder:remove')")
    @Log(title = "卡片订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cardOrderService.removeByIds(Arrays.asList(ids)));
    }
}
