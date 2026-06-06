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
import com.xms.dao.domain.InteractRewardConfig;
import com.xms.dao.service.IInteractRewardConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 互动奖比例配置Controller
 *
 * @author xms
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/xms/interactRewardConfig")
public class InteractRewardConfigController extends BaseController
{
    @Autowired
    private IInteractRewardConfigService interactRewardConfigService;

/**
 * 查询互动奖比例配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:interactRewardConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(InteractRewardConfig interactRewardConfig)
    {
        startPage();
        List<InteractRewardConfig> list = interactRewardConfigService.selectInteractRewardConfigList(interactRewardConfig);
        return getDataTable(list);
    }

    /**
     * 导出互动奖比例配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:interactRewardConfig:export')")
    @Log(title = "互动奖比例配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InteractRewardConfig interactRewardConfig)
    {
        List<InteractRewardConfig> list = interactRewardConfigService.selectInteractRewardConfigList(interactRewardConfig);
        ExcelUtil<InteractRewardConfig> util = new ExcelUtil<InteractRewardConfig>(InteractRewardConfig.class);
        util.exportExcel(response, list, "互动奖比例配置数据");
    }

    /**
     * 获取互动奖比例配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:interactRewardConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(interactRewardConfigService.getById(id));
    }

    /**
     * 新增互动奖比例配置
     */
    @PreAuthorize("@ss.hasPermi('xms:interactRewardConfig:add')")
    @Log(title = "互动奖比例配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody InteractRewardConfig interactRewardConfig) {
        return toAjax(interactRewardConfigService.save(interactRewardConfig));
    }

    /**
     * 修改互动奖比例配置
     */
    @PreAuthorize("@ss.hasPermi('xms:interactRewardConfig:edit')")
    @Log(title = "互动奖比例配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody InteractRewardConfig interactRewardConfig) {
		if(interactRewardConfig.getRewardRatio() == null ||
			interactRewardConfig.getRewardRatio().compareTo(BigDecimal.ZERO)<0){
			throw new ServiceException("互动奖比例不能小于0");
		}
        return toAjax(interactRewardConfigService.updateById(interactRewardConfig));
    }

    /**
     * 删除互动奖比例配置
     */
    @PreAuthorize("@ss.hasPermi('xms:interactRewardConfig:remove')")
    @Log(title = "互动奖比例配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(interactRewardConfigService.removeByIds(Arrays.asList(ids)));
    }
}
