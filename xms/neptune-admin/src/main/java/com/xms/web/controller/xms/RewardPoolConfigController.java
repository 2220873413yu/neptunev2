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
import com.xms.dao.domain.RewardPoolConfig;
import com.xms.dao.service.IRewardPoolConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 分红池配置Controller
 *
 * @author xms
 * @date 2025-12-08
 */
@RestController
@RequestMapping("/xms/rewardPoolConfig")
public class RewardPoolConfigController extends BaseController
{
    @Autowired
    private IRewardPoolConfigService rewardPoolConfigService;

/**
 * 查询分红池配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:rewardPoolConfig:list')")
@GetMapping("/list")
    public TableDataInfo list(RewardPoolConfig rewardPoolConfig)
    {
        startPage();
        List<RewardPoolConfig> list = rewardPoolConfigService.selectRewardPoolConfigList(rewardPoolConfig);
        return getDataTable(list);
    }

    /**
     * 导出分红池配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolConfig:export')")
    @Log(title = "分红池配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RewardPoolConfig rewardPoolConfig)
    {
        List<RewardPoolConfig> list = rewardPoolConfigService.selectRewardPoolConfigList(rewardPoolConfig);
        ExcelUtil<RewardPoolConfig> util = new ExcelUtil<RewardPoolConfig>(RewardPoolConfig.class);
        util.exportExcel(response, list, "分红池配置数据");
    }

    /**
     * 获取分红池配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(rewardPoolConfigService.getById(id));
    }

    /**
     * 新增分红池配置
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolConfig:add')")
    @Log(title = "分红池配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody RewardPoolConfig rewardPoolConfig) {
        return toAjax(rewardPoolConfigService.save(rewardPoolConfig));
    }

    /**
     * 修改分红池配置
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolConfig:edit')")
    @Log(title = "分红池配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody RewardPoolConfig rewardPoolConfig) {
		if(rewardPoolConfig.getPoolType() == 1 || rewardPoolConfig.getPoolType() == 2){
			if(rewardPoolConfig.getDailyOutput() == null ||
				rewardPoolConfig.getDailyOutput().compareTo(BigDecimal.ZERO) < 0){
				throw new ServiceException("每日产量不能小于0");
			}
		}
        return toAjax(rewardPoolConfigService.updateRecordById(rewardPoolConfig));
    }

    /**
     * 删除分红池配置
     */
    @PreAuthorize("@ss.hasPermi('xms:rewardPoolConfig:remove')")
    @Log(title = "分红池配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(rewardPoolConfigService.removeByIds(Arrays.asList(ids)));
    }
}
