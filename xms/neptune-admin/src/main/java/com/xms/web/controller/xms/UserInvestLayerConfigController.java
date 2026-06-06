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
import com.xms.dao.domain.UserInvestLayerConfig;
import com.xms.dao.service.IUserInvestLayerConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 层奖配置Controller
 *
 * @author xms
 * @date 2026-03-05
 */
@RestController
@RequestMapping("/xms/userInvestLayerConfig")
public class UserInvestLayerConfigController extends BaseController
{
    @Autowired
    private IUserInvestLayerConfigService userInvestLayerConfigService;

/**
 * 查询层奖配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:userInvestLayerConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(UserInvestLayerConfig userInvestLayerConfig)
    {
        startPage();
        List<UserInvestLayerConfig> list = userInvestLayerConfigService.selectUserInvestLayerConfigList(userInvestLayerConfig);
        return getDataTable(list);
    }

    /**
     * 导出层奖配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userInvestLayerConfig:export')")
    @Log(title = "层奖配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserInvestLayerConfig userInvestLayerConfig)
    {
        List<UserInvestLayerConfig> list = userInvestLayerConfigService.selectUserInvestLayerConfigList(userInvestLayerConfig);
        ExcelUtil<UserInvestLayerConfig> util = new ExcelUtil<UserInvestLayerConfig>(UserInvestLayerConfig.class);
        util.exportExcel(response, list, "层奖配置数据");
    }

    /**
     * 获取层奖配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userInvestLayerConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userInvestLayerConfigService.getById(id));
    }

    /**
     * 新增层奖配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userInvestLayerConfig:add')")
    @Log(title = "层奖配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserInvestLayerConfig userInvestLayerConfig) {
        return toAjax(userInvestLayerConfigService.save(userInvestLayerConfig));
    }

    /**
     * 修改层奖配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userInvestLayerConfig:edit')")
    @Log(title = "层奖配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserInvestLayerConfig userInvestLayerConfig) {
        return toAjax(userInvestLayerConfigService.updateConfigById(userInvestLayerConfig));
    }

    /**
     * 删除层奖配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userInvestLayerConfig:remove')")
    @Log(title = "层奖配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userInvestLayerConfigService.removeByIds(Arrays.asList(ids)));
    }
}
