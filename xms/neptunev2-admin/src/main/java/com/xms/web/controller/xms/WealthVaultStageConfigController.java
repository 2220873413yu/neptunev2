package com.xms.web.controller.xms;

import java.math.BigDecimal;
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
import com.xms.dao.domain.WealthVaultStageConfig;
import com.xms.dao.service.IWealthVaultStageConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 财富仓阶段解锁配置Controller
 *
 * @author xms
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/xms/wealthVaultStageConfig")
public class WealthVaultStageConfigController extends BaseController
{
    @Autowired
    private IWealthVaultStageConfigService wealthVaultStageConfigService;

/**
 * 查询财富仓阶段解锁配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:wealthVaultStageConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(WealthVaultStageConfig wealthVaultStageConfig)
    {
        startPage();
        List<WealthVaultStageConfig> list = wealthVaultStageConfigService.selectWealthVaultStageConfigList(wealthVaultStageConfig);
        return getDataTable(list);
    }

    /**
     * 导出财富仓阶段解锁配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:wealthVaultStageConfig:export')")
    @Log(title = "财富仓阶段解锁配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WealthVaultStageConfig wealthVaultStageConfig)
    {
        List<WealthVaultStageConfig> list = wealthVaultStageConfigService.selectWealthVaultStageConfigList(wealthVaultStageConfig);
        ExcelUtil<WealthVaultStageConfig> util = new ExcelUtil<WealthVaultStageConfig>(WealthVaultStageConfig.class);
        util.exportExcel(response, list, "财富仓阶段解锁配置数据");
    }

    /**
     * 获取财富仓阶段解锁配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:wealthVaultStageConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(wealthVaultStageConfigService.getById(id));
    }

    /**
     * 新增财富仓阶段解锁配置
     */
    @PreAuthorize("@ss.hasPermi('xms:wealthVaultStageConfig:add')")
    @Log(title = "财富仓阶段解锁配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody WealthVaultStageConfig wealthVaultStageConfig) {
        return toAjax(wealthVaultStageConfigService.save(wealthVaultStageConfig));
    }

    /**
     * 修改财富仓阶段解锁配置
     */
    @PreAuthorize("@ss.hasPermi('xms:wealthVaultStageConfig:edit')")
    @Log(title = "财富仓阶段解锁配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody WealthVaultStageConfig wealthVaultStageConfig) {
		if(wealthVaultStageConfig.getUnlockPrice() == null || wealthVaultStageConfig.getUnlockPrice().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("解锁价格必须大于0");
		}
        return toAjax(wealthVaultStageConfigService.updateById(wealthVaultStageConfig));
    }

    /**
     * 删除财富仓阶段解锁配置
     */
    @PreAuthorize("@ss.hasPermi('xms:wealthVaultStageConfig:remove')")
    @Log(title = "财富仓阶段解锁配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(wealthVaultStageConfigService.removeByIds(Arrays.asList(ids)));
    }
}
