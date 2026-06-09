package com.xms.web.controller.xms;

import java.util.Arrays;
import java.util.List;

import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.exception.ServiceException;
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
import com.xms.dao.domain.AccelerateReleaseConfig;
import com.xms.dao.service.IAccelerateReleaseConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 收益加速释放配置Controller
 *
 * @author xms
 * @date 2025-11-21
 */
@RestController
@RequestMapping("/xms/accelerateReleaseConfig")
public class AccelerateReleaseConfigController extends BaseController
{
    @Autowired
    private IAccelerateReleaseConfigService accelerateReleaseConfigService;

/**
 * 查询收益加速释放配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:accelerateReleaseConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(AccelerateReleaseConfig accelerateReleaseConfig)
    {
        startPage();
        List<AccelerateReleaseConfig> list = accelerateReleaseConfigService.selectAccelerateReleaseConfigList(accelerateReleaseConfig);
        return getDataTable(list);
    }

    /**
     * 导出收益加速释放配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:accelerateReleaseConfig:export')")
    @Log(title = "收益加速释放配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccelerateReleaseConfig accelerateReleaseConfig)
    {
        List<AccelerateReleaseConfig> list = accelerateReleaseConfigService.selectAccelerateReleaseConfigList(accelerateReleaseConfig);
        ExcelUtil<AccelerateReleaseConfig> util = new ExcelUtil<AccelerateReleaseConfig>(AccelerateReleaseConfig.class);
        util.exportExcel(response, list, "收益加速释放配置数据");
    }

    /**
     * 获取收益加速释放配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:accelerateReleaseConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(accelerateReleaseConfigService.getById(id));
    }

    /**
     * 新增收益加速释放配置
     */
    @PreAuthorize("@ss.hasPermi('xms:accelerateReleaseConfig:add')")
    @Log(title = "收益加速释放配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody AccelerateReleaseConfig accelerateReleaseConfig) {
        return toAjax(accelerateReleaseConfigService.save(accelerateReleaseConfig));
    }

    /**
     * 修改收益加速释放配置
     */
    @PreAuthorize("@ss.hasPermi('xms:accelerateReleaseConfig:edit')")
    @Log(title = "收益加速释放配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody AccelerateReleaseConfig accelerateReleaseConfig) {
        return toAjax(accelerateReleaseConfigService.updateRecordById(accelerateReleaseConfig));
    }

    /**
     * 删除收益加速释放配置
     */
    @PreAuthorize("@ss.hasPermi('xms:accelerateReleaseConfig:remove')")
    @Log(title = "收益加速释放配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(accelerateReleaseConfigService.removeByIds(Arrays.asList(ids)));
    }
}
