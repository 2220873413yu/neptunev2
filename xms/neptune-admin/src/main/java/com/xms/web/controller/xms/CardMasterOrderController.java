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
import com.xms.dao.domain.CardMasterOrder;
import com.xms.dao.service.ICardMasterOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 购买记录Controller
 *
 * @author xms
 * @date 2025-12-04
 */
@RestController
@RequestMapping("/xms/cardMasterOrder")
public class CardMasterOrderController extends BaseController
{
    @Autowired
    private ICardMasterOrderService cardMasterOrderService;

/**
 * 查询购买记录列表
 */
@PreAuthorize("@ss.hasPermi('xms:cardMasterOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(CardMasterOrder cardMasterOrder)
    {
        startPage();
        List<CardMasterOrder> list = cardMasterOrderService.selectCardMasterOrderList(cardMasterOrder);
        return getDataTable(list);
    }

    /**
     * 导出购买记录列表
     */
    @PreAuthorize("@ss.hasPermi('xms:cardMasterOrder:export')")
    @Log(title = "购买记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardMasterOrder cardMasterOrder)
    {
        List<CardMasterOrder> list = cardMasterOrderService.selectCardMasterOrderList(cardMasterOrder);
        ExcelUtil<CardMasterOrder> util = new ExcelUtil<CardMasterOrder>(CardMasterOrder.class);
        util.exportExcel(response, list, "购买记录数据");
    }

    /**
     * 获取购买记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:cardMasterOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(cardMasterOrderService.getById(id));
    }

    /**
     * 新增购买记录
     */
    @PreAuthorize("@ss.hasPermi('xms:cardMasterOrder:add')")
    @Log(title = "购买记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody CardMasterOrder cardMasterOrder) {
        return toAjax(cardMasterOrderService.save(cardMasterOrder));
    }

    /**
     * 修改购买记录
     */
    @PreAuthorize("@ss.hasPermi('xms:cardMasterOrder:edit')")
    @Log(title = "购买记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody CardMasterOrder cardMasterOrder) {
        return toAjax(cardMasterOrderService.updateById(cardMasterOrder));
    }

    /**
     * 删除购买记录
     */
    @PreAuthorize("@ss.hasPermi('xms:cardMasterOrder:remove')")
    @Log(title = "购买记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cardMasterOrderService.removeByIds(Arrays.asList(ids)));
    }
}
