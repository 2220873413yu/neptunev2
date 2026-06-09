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
import com.xms.dao.domain.W3UserRecommendRewardConfig;
import com.xms.dao.service.IW3UserRecommendRewardConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户推荐奖配置Controller
 *
 * @author xms
 * @date 2025-08-09
 */
@RestController
@RequestMapping("/xms/userRecommendRewardConfig")
public class W3UserRecommendRewardConfigController extends BaseController
{
    @Autowired
    private IW3UserRecommendRewardConfigService w3UserRecommendRewardConfigService;

	@Autowired
	private ISysUserService sysUserService;

/**
 * 查询用户推荐奖配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:userRecommendRewardConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(W3UserRecommendRewardConfig w3UserRecommendRewardConfig)
    {
        startPage();
        List<W3UserRecommendRewardConfig> list = w3UserRecommendRewardConfigService.selectW3UserRecommendRewardConfigList(w3UserRecommendRewardConfig);
        return getDataTable(list);
    }

    /**
     * 导出用户推荐奖配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userRecommendRewardConfig:export')")
    @Log(title = "用户推荐奖配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, W3UserRecommendRewardConfig w3UserRecommendRewardConfig)
    {
        List<W3UserRecommendRewardConfig> list = w3UserRecommendRewardConfigService.selectW3UserRecommendRewardConfigList(w3UserRecommendRewardConfig);
        ExcelUtil<W3UserRecommendRewardConfig> util = new ExcelUtil<W3UserRecommendRewardConfig>(W3UserRecommendRewardConfig.class);
        util.exportExcel(response, list, "用户推荐奖配置数据");
    }

    /**
     * 获取用户推荐奖配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userRecommendRewardConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(w3UserRecommendRewardConfigService.getById(id));
    }

    /**
     * 新增用户推荐奖配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userRecommendRewardConfig:add')")
    @Log(title = "用户推荐奖配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody W3UserRecommendRewardConfig w3UserRecommendRewardConfig) {
        return toAjax(w3UserRecommendRewardConfigService.save(w3UserRecommendRewardConfig));
    }

    /**
     * 修改用户推荐奖配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userRecommendRewardConfig:edit')")
    @Log(title = "用户推荐奖配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody W3UserRecommendRewardConfig w3UserRecommendRewardConfig) {
		//验证码验证
		sysUserService.pubValidate(w3UserRecommendRewardConfig.getAutoCode());
        return toAjax(w3UserRecommendRewardConfigService.updateById(w3UserRecommendRewardConfig));
    }

    /**
     * 删除用户推荐奖配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userRecommendRewardConfig:remove')")
    @Log(title = "用户推荐奖配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(w3UserRecommendRewardConfigService.removeByIds(Arrays.asList(ids)));
    }
}
