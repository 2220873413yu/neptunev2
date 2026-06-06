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
import com.xms.dao.domain.CardUpgradeLog;
import com.xms.dao.service.ICardUpgradeLogService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 卡片升级日志Controller
 *
 * @author xms
 * @date 2025-12-06
 */
@RestController
@RequestMapping("/xms/cardUpgradeLog")
public class CardUpgradeLogController extends BaseController
{
    @Autowired
    private ICardUpgradeLogService cardUpgradeLogService;

/**
 * 查询卡片升级日志列表
 */
@PreAuthorize("@ss.hasPermi('xms:cardUpgradeLog:list')")
@GetMapping("/list")
    public TableDataInfo list(CardUpgradeLog cardUpgradeLog)
    {
        startPage();
        List<CardUpgradeLog> list = cardUpgradeLogService.selectCardUpgradeLogList(cardUpgradeLog);
        return getDataTable(list);
    }

    /**
     * 导出卡片升级日志列表
     */
    @PreAuthorize("@ss.hasPermi('xms:cardUpgradeLog:export')")
    @Log(title = "卡片升级日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CardUpgradeLog cardUpgradeLog)
    {
        List<CardUpgradeLog> list = cardUpgradeLogService.selectCardUpgradeLogList(cardUpgradeLog);
        ExcelUtil<CardUpgradeLog> util = new ExcelUtil<CardUpgradeLog>(CardUpgradeLog.class);
        util.exportExcel(response, list, "卡片升级日志数据");
    }

    /**
     * 获取卡片升级日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:cardUpgradeLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(cardUpgradeLogService.getById(id));
    }

    /**
     * 新增卡片升级日志
     */
    @PreAuthorize("@ss.hasPermi('xms:cardUpgradeLog:add')")
    @Log(title = "卡片升级日志", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody CardUpgradeLog cardUpgradeLog) {
        return toAjax(cardUpgradeLogService.save(cardUpgradeLog));
    }

    /**
     * 修改卡片升级日志
     */
    @PreAuthorize("@ss.hasPermi('xms:cardUpgradeLog:edit')")
    @Log(title = "卡片升级日志", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody CardUpgradeLog cardUpgradeLog) {
        return toAjax(cardUpgradeLogService.updateById(cardUpgradeLog));
    }

    /**
     * 删除卡片升级日志
     */
    @PreAuthorize("@ss.hasPermi('xms:cardUpgradeLog:remove')")
    @Log(title = "卡片升级日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cardUpgradeLogService.removeByIds(Arrays.asList(ids)));
    }
}
