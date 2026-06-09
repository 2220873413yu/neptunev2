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
import com.xms.dao.domain.CardPackage;
import com.xms.dao.service.ICardPackageService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 卡片套餐Controller
 *
 * @author xms
 * @date 2025-12-04
 */
@RestController
@RequestMapping("/xms/cardPackage")
public class CardPackageController extends BaseController
{
    @Autowired
    private ICardPackageService cardPackageService;

/**
 * 查询卡片套餐列表
 */
@PreAuthorize("@ss.hasPermi('xms:cardPackage:list')")
@GetMapping("/list")
    public TableDataInfo list(CardPackage cardPackage)
    {
        startPage();
        List<CardPackage> list = cardPackageService.selectCardPackageList(cardPackage);
        return getDataTable(list);
    }

    /**
     * 导出卡片套餐列表
     */
    @PreAuthorize("@ss.hasPermi('xms:cardPackage:export')")
    @Log(title = "卡片套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardPackage cardPackage)
    {
        List<CardPackage> list = cardPackageService.selectCardPackageList(cardPackage);
        ExcelUtil<CardPackage> util = new ExcelUtil<CardPackage>(CardPackage.class);
        util.exportExcel(response, list, "卡片套餐数据");
    }

    /**
     * 获取卡片套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:cardPackage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(cardPackageService.getById(id));
    }

    /**
     * 新增卡片套餐
     */
    @PreAuthorize("@ss.hasPermi('xms:cardPackage:add')")
    @Log(title = "卡片套餐", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody CardPackage cardPackage) {
        return toAjax(cardPackageService.save(cardPackage));
    }

    /**
     * 修改卡片套餐
     */
    @PreAuthorize("@ss.hasPermi('xms:cardPackage:edit')")
    @Log(title = "卡片套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody CardPackage cardPackage) {
        return toAjax(cardPackageService.updateRecordById(cardPackage));
    }

    /**
     * 删除卡片套餐
     */
    @PreAuthorize("@ss.hasPermi('xms:cardPackage:remove')")
    @Log(title = "卡片套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cardPackageService.removeByIds(Arrays.asList(ids)));
    }
}
