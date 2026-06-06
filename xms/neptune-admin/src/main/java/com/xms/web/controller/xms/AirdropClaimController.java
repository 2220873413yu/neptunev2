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
import com.xms.dao.domain.AirdropClaim;
import com.xms.dao.service.IAirdropClaimService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 空投领取记录Controller
 *
 * @author xms
 * @date 2026-01-01
 */
@RestController
@RequestMapping("/xms/airdropClaim")
public class AirdropClaimController extends BaseController
{
    @Autowired
    private IAirdropClaimService airdropClaimService;

/**
 * 查询空投领取记录列表
 */
@PreAuthorize("@ss.hasPermi('xms:airdropClaim:list')")
@GetMapping("/list")
    public TableDataInfo list(AirdropClaim airdropClaim)
    {
        startPage();
        List<AirdropClaim> list = airdropClaimService.selectAirdropClaimList(airdropClaim);
        return getDataTable(list);
    }

    /**
     * 导出空投领取记录列表
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropClaim:export')")
    @Log(title = "空投领取记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AirdropClaim airdropClaim)
    {
        List<AirdropClaim> list = airdropClaimService.selectAirdropClaimList(airdropClaim);
        ExcelUtil<AirdropClaim> util = new ExcelUtil<AirdropClaim>(AirdropClaim.class);
        util.exportExcel(response, list, "空投领取记录数据");
    }

    /**
     * 获取空投领取记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropClaim:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(airdropClaimService.getById(id));
    }

    /**
     * 新增空投领取记录
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropClaim:add')")
    @Log(title = "空投领取记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody AirdropClaim airdropClaim) {
        return toAjax(airdropClaimService.save(airdropClaim));
    }

    /**
     * 修改空投领取记录
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropClaim:edit')")
    @Log(title = "空投领取记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody AirdropClaim airdropClaim) {
        return toAjax(airdropClaimService.updateById(airdropClaim));
    }

    /**
     * 删除空投领取记录
     */
    @PreAuthorize("@ss.hasPermi('xms:airdropClaim:remove')")
    @Log(title = "空投领取记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(airdropClaimService.removeByIds(Arrays.asList(ids)));
    }
}
