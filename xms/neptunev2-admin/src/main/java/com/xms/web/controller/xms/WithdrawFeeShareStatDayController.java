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
import com.xms.dao.domain.WithdrawFeeShareStatDay;
import com.xms.dao.service.IWithdrawFeeShareStatDayService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 提现手续费分红Controller
 *
 * @author xms
 * @date 2025-11-23
 */
@RestController
@RequestMapping("/xms/withdrawFeeShareStatDay")
public class WithdrawFeeShareStatDayController extends BaseController
{
    @Autowired
    private IWithdrawFeeShareStatDayService withdrawFeeShareStatDayService;

/**
 * 查询提现手续费分红列表
 */
@PreAuthorize("@ss.hasPermi('xms:withdrawFeeShareStatDay:list')")
@GetMapping("/list")
    public TableDataInfo list(WithdrawFeeShareStatDay withdrawFeeShareStatDay)
    {
        startPage();
        List<WithdrawFeeShareStatDay> list = withdrawFeeShareStatDayService.selectWithdrawFeeShareStatDayList(withdrawFeeShareStatDay);
        return getDataTable(list);
    }

    /**
     * 导出提现手续费分红列表
     */
    @PreAuthorize("@ss.hasPermi('xms:withdrawFeeShareStatDay:export')")
    @Log(title = "提现手续费分红", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WithdrawFeeShareStatDay withdrawFeeShareStatDay)
    {
        List<WithdrawFeeShareStatDay> list = withdrawFeeShareStatDayService.selectWithdrawFeeShareStatDayList(withdrawFeeShareStatDay);
        ExcelUtil<WithdrawFeeShareStatDay> util = new ExcelUtil<WithdrawFeeShareStatDay>(WithdrawFeeShareStatDay.class);
        util.exportExcel(response, list, "提现手续费分红数据");
    }

    /**
     * 获取提现手续费分红详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:withdrawFeeShareStatDay:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(withdrawFeeShareStatDayService.getById(id));
    }

    /**
     * 新增提现手续费分红
     */
    @PreAuthorize("@ss.hasPermi('xms:withdrawFeeShareStatDay:add')")
    @Log(title = "提现手续费分红", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody WithdrawFeeShareStatDay withdrawFeeShareStatDay) {
        return toAjax(withdrawFeeShareStatDayService.save(withdrawFeeShareStatDay));
    }

    /**
     * 修改提现手续费分红
     */
    @PreAuthorize("@ss.hasPermi('xms:withdrawFeeShareStatDay:edit')")
    @Log(title = "提现手续费分红", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody WithdrawFeeShareStatDay withdrawFeeShareStatDay) {
        return toAjax(withdrawFeeShareStatDayService.updateById(withdrawFeeShareStatDay));
    }

    /**
     * 删除提现手续费分红
     */
    @PreAuthorize("@ss.hasPermi('xms:withdrawFeeShareStatDay:remove')")
    @Log(title = "提现手续费分红", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(withdrawFeeShareStatDayService.removeByIds(Arrays.asList(ids)));
    }
}
