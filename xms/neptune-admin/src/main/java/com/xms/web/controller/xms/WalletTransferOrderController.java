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
import com.xms.dao.domain.WalletTransferOrder;
import com.xms.dao.service.IWalletTransferOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 分发记录Controller
 *
 * @author xms
 * @date 2025-06-22
 */
@RestController
@RequestMapping("/xms/walletTransferOrder")
public class WalletTransferOrderController extends BaseController
{
    @Autowired
    private IWalletTransferOrderService walletTransferOrderService;

/**
 * 查询分发记录列表
 */
@PreAuthorize("@ss.hasPermi('xms:walletTransferOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(WalletTransferOrder walletTransferOrder)
    {
        startPage();
        List<WalletTransferOrder> list = walletTransferOrderService.selectWalletTransferOrderList(walletTransferOrder);
        return getDataTable(list);
    }

    /**
     * 导出分发记录列表
     */
    @PreAuthorize("@ss.hasPermi('xms:walletTransferOrder:export')")
    @Log(title = "分发记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WalletTransferOrder walletTransferOrder)
    {
        List<WalletTransferOrder> list = walletTransferOrderService.selectWalletTransferOrderList(walletTransferOrder);
        ExcelUtil<WalletTransferOrder> util = new ExcelUtil<WalletTransferOrder>(WalletTransferOrder.class);
        util.exportExcel(response, list, "分发记录数据");
    }

    /**
     * 获取分发记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:walletTransferOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(walletTransferOrderService.getById(id));
    }

    /**
     * 新增分发记录
     */
    @PreAuthorize("@ss.hasPermi('xms:walletTransferOrder:add')")
    @Log(title = "分发记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody WalletTransferOrder walletTransferOrder) {
        return toAjax(walletTransferOrderService.save(walletTransferOrder));
    }

    /**
     * 修改分发记录
     */
    @PreAuthorize("@ss.hasPermi('xms:walletTransferOrder:edit')")
    @Log(title = "分发记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody WalletTransferOrder walletTransferOrder) {
        return toAjax(walletTransferOrderService.updateById(walletTransferOrder));
    }

    /**
     * 删除分发记录
     */
    @PreAuthorize("@ss.hasPermi('xms:walletTransferOrder:remove')")
    @Log(title = "分发记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(walletTransferOrderService.removeByIds(Arrays.asList(ids)));
    }
}
