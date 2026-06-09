package com.xms.web.controller.xms;

import java.util.Arrays;
import java.util.List;

import com.xms.common.annotation.RepeatSubmit;
import com.xms.system.service.ISysUserService;
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
import com.xms.dao.domain.W3UserLevelConfig;
import com.xms.dao.service.IW3UserLevelConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户等级考核配置Controller
 *
 * @author xms
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/xms/w3UserLevelConfig")
public class W3UserLevelConfigController extends BaseController
{
    @Autowired
    private IW3UserLevelConfigService w3UserLevelConfigService;


	@Autowired
	private ISysUserService sysUserService;
/**
 * 查询用户等级考核配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:w3UserLevelConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(W3UserLevelConfig w3UserLevelConfig)
    {
        startPage();
        List<W3UserLevelConfig> list = w3UserLevelConfigService.selectW3UserLevelConfigList(w3UserLevelConfig);
        return getDataTable(list);
    }

    /**
     * 导出用户等级考核配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:w3UserLevelConfig:export')")
    @Log(title = "用户等级考核配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, W3UserLevelConfig w3UserLevelConfig)
    {
        List<W3UserLevelConfig> list = w3UserLevelConfigService.selectW3UserLevelConfigList(w3UserLevelConfig);
        ExcelUtil<W3UserLevelConfig> util = new ExcelUtil<W3UserLevelConfig>(W3UserLevelConfig.class);
        util.exportExcel(response, list, "用户等级考核配置数据");
    }

    /**
     * 获取用户等级考核配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:w3UserLevelConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(w3UserLevelConfigService.getById(id));
    }

//    /**
//     * 新增用户等级考核配置
//     */
//    @PreAuthorize("@ss.hasPermi('xms:w3UserLevelConfig:add')")
//    @Log(title = "用户等级考核配置", businessType = BusinessType.INSERT)
//    @PostMapping
//    @RepeatSubmit
//    public AjaxResult add(@RequestBody W3UserLevelConfig w3UserLevelConfig) {
//        return toAjax(w3UserLevelConfigService.save(w3UserLevelConfig));
//    }

    /**
     * 修改用户等级考核配置
     */
    @PreAuthorize("@ss.hasPermi('xms:w3UserLevelConfig:edit')")
    @Log(title = "用户等级考核配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody W3UserLevelConfig w3UserLevelConfig) {
		//验证码验证
		sysUserService.pubValidate(w3UserLevelConfig.getAutoCode());
        return toAjax(w3UserLevelConfigService.updateRecordById(w3UserLevelConfig));
    }

//    /**
//     * 删除用户等级考核配置
//     */
//    @PreAuthorize("@ss.hasPermi('xms:w3UserLevelConfig:remove')")
//    @Log(title = "用户等级考核配置", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(w3UserLevelConfigService.removeByIds(Arrays.asList(ids)));
//    }
}
