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
import com.xms.dao.domain.MiningPackage;
import com.xms.dao.service.IMiningPackageService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 基金套餐Controller
 *
 * @author xms
 * @date 2025-08-07
 */
@RestController
@RequestMapping("/xms/miningPackage")
public class MiningPackageController extends BaseController
{
    @Autowired
    private IMiningPackageService miningPackageService;

/**
 * 查询基金套餐列表
 */
@PreAuthorize("@ss.hasPermi('xms:miningPackage:list')")
@GetMapping("/list")
    public TableDataInfo list(MiningPackage miningPackage)
    {
        startPage();
        List<MiningPackage> list = miningPackageService.selectMiningPackageList(miningPackage);
        return getDataTable(list);
    }

    /**
     * 导出基金套餐列表
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackage:export')")
    @Log(title = "基金套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MiningPackage miningPackage)
    {
        List<MiningPackage> list = miningPackageService.selectMiningPackageList(miningPackage);
        ExcelUtil<MiningPackage> util = new ExcelUtil<MiningPackage>(MiningPackage.class);
        util.exportExcel(response, list, "基金套餐数据");
    }

    /**
     * 获取基金套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(miningPackageService.getById(id));
    }

    /**
     * 新增基金套餐
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackage:add')")
    @Log(title = "基金套餐", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody MiningPackage miningPackage) {
        return toAjax(miningPackageService.save(miningPackage));
    }

    /**
     * 修改基金套餐
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackage:edit')")
    @Log(title = "基金套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody MiningPackage miningPackage) {
        return toAjax(miningPackageService.updateRecordById(miningPackage));
    }

    /**
     * 删除基金套餐
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackage:remove')")
    @Log(title = "基金套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(miningPackageService.removeByIds(Arrays.asList(ids)));
    }
}
