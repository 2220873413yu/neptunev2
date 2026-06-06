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
import com.xms.dao.domain.StakeRound;
import com.xms.dao.service.IStakeRoundService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 全局质押轮次Controller
 *
 * @author xms
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/xms/stakeRound")
public class StakeRoundController extends BaseController
{
    @Autowired
    private IStakeRoundService stakeRoundService;

/**
 * 查询全局质押轮次列表
 */
@PreAuthorize("@ss.hasPermi('xms:stakeRound:list')")
@GetMapping("/list")
    public TableDataInfo list(StakeRound stakeRound)
    {
        startPage();
        List<StakeRound> list = stakeRoundService.selectStakeRoundList(stakeRound);
        return getDataTable(list);
    }

    /**
     * 导出全局质押轮次列表
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:export')")
    @Log(title = "全局质押轮次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakeRound stakeRound)
    {
        List<StakeRound> list = stakeRoundService.selectStakeRoundList(stakeRound);
        ExcelUtil<StakeRound> util = new ExcelUtil<StakeRound>(StakeRound.class);
        util.exportExcel(response, list, "全局质押轮次数据");
    }

    /**
     * 获取全局质押轮次详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(stakeRoundService.getById(id));
    }

    /**
     * 新增全局质押轮次
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:add')")
    @Log(title = "全局质押轮次", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody StakeRound stakeRound) {
        return toAjax(stakeRoundService.save(stakeRound));
    }

    /**
     * 修改全局质押轮次
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:edit')")
    @Log(title = "全局质押轮次", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody StakeRound stakeRound) {
        return toAjax(stakeRoundService.updateById(stakeRound));
    }

    /**
     * 删除全局质押轮次
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:remove')")
    @Log(title = "全局质押轮次", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stakeRoundService.removeByIds(Arrays.asList(ids)));
    }
}
