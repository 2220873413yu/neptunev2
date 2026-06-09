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
import com.xms.dao.domain.UserYieldRateConfig;
import com.xms.dao.service.IUserYieldRateConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户收益率规则配置Controller
 *
 * @author xms
 * @date 2026-03-05
 */
@RestController
@RequestMapping("/xms/userYieldRateConfig")
public class UserYieldRateConfigController extends BaseController
{
    @Autowired
    private IUserYieldRateConfigService userYieldRateConfigService;

/**
 * 查询用户收益率规则配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:userYieldRateConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(UserYieldRateConfig userYieldRateConfig)
    {
        startPage();
        List<UserYieldRateConfig> list = userYieldRateConfigService.selectUserYieldRateConfigList(userYieldRateConfig);
        return getDataTable(list);
    }

    /**
     * 导出用户收益率规则配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userYieldRateConfig:export')")
    @Log(title = "用户收益率规则配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserYieldRateConfig userYieldRateConfig)
    {
        List<UserYieldRateConfig> list = userYieldRateConfigService.selectUserYieldRateConfigList(userYieldRateConfig);
        ExcelUtil<UserYieldRateConfig> util = new ExcelUtil<UserYieldRateConfig>(UserYieldRateConfig.class);
        util.exportExcel(response, list, "用户收益率规则配置数据");
    }

    /**
     * 获取用户收益率规则配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userYieldRateConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userYieldRateConfigService.getById(id));
    }

    /**
     * 新增用户收益率规则配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userYieldRateConfig:add')")
    @Log(title = "用户收益率规则配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserYieldRateConfig userYieldRateConfig) {
        return toAjax(userYieldRateConfigService.save(userYieldRateConfig));
    }

    /**
     * 修改用户收益率规则配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userYieldRateConfig:edit')")
    @Log(title = "用户收益率规则配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserYieldRateConfig userYieldRateConfig) {
		if(userYieldRateConfig.getInitialDailyRate() == null || userYieldRateConfig.getInitialDailyRate().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("初始日收益率必须大于0");
		}

		if(userYieldRateConfig.getMinDailyRate() == null || userYieldRateConfig.getMinDailyRate().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("最低日收益率必须大于0");
		}

		if(userYieldRateConfig.getMaxDailyRate() == null || userYieldRateConfig.getMaxDailyRate().compareTo(BigDecimal.ZERO)<=0){
			throw new ServiceException("最高日收益率必须大于0");
		}
		if(userYieldRateConfig.getGrowthConsecutiveDays() == null || userYieldRateConfig.getGrowthConsecutiveDays() <= 0){
			throw new ServiceException("增长连续天数必须大于0");

		}
        return toAjax(userYieldRateConfigService.updateById(userYieldRateConfig));
    }

    /**
     * 删除用户收益率规则配置
     */
    @PreAuthorize("@ss.hasPermi('xms:userYieldRateConfig:remove')")
    @Log(title = "用户收益率规则配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userYieldRateConfigService.removeByIds(Arrays.asList(ids)));
    }
}
