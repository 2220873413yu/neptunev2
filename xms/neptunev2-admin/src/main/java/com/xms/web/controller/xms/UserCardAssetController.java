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
import com.xms.dao.domain.UserCardAsset;
import com.xms.dao.service.IUserCardAssetService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 卡片持有信息Controller
 *
 * @author xms
 * @date 2025-12-05
 */
@RestController
@RequestMapping("/xms/userCardAsset")
public class UserCardAssetController extends BaseController
{
    @Autowired
    private IUserCardAssetService userCardAssetService;

/**
 * 查询卡片持有信息列表
 */
@PreAuthorize("@ss.hasPermi('xms:userCardAsset:list')")
@GetMapping("/list")
    public TableDataInfo list(UserCardAsset userCardAsset)
    {
        startPage();
        List<UserCardAsset> list = userCardAssetService.selectUserCardAssetList(userCardAsset);
        return getDataTable(list);
    }

    /**
     * 导出卡片持有信息列表
     */
    @PreAuthorize("@ss.hasPermi('xms:userCardAsset:export')")
    @Log(title = "卡片持有信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserCardAsset userCardAsset)
    {
        List<UserCardAsset> list = userCardAssetService.selectUserCardAssetList(userCardAsset);
        ExcelUtil<UserCardAsset> util = new ExcelUtil<UserCardAsset>(UserCardAsset.class);
        util.exportExcel(response, list, "卡片持有信息数据");
    }

    /**
     * 获取卡片持有信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userCardAsset:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userCardAssetService.getById(id));
    }

    /**
     * 新增卡片持有信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userCardAsset:add')")
    @Log(title = "卡片持有信息", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody UserCardAsset userCardAsset) {
        return toAjax(userCardAssetService.save(userCardAsset));
    }

    /**
     * 修改卡片持有信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userCardAsset:edit')")
    @Log(title = "卡片持有信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody UserCardAsset userCardAsset) {
        return toAjax(userCardAssetService.updateById(userCardAsset));
    }

    /**
     * 删除卡片持有信息
     */
    @PreAuthorize("@ss.hasPermi('xms:userCardAsset:remove')")
    @Log(title = "卡片持有信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userCardAssetService.removeByIds(Arrays.asList(ids)));
    }
}
