package com.xms.web.controller.xms;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
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
import com.xms.dao.domain.PtbDailyPrice;
import com.xms.dao.service.IPtbDailyPriceService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 平台币每日价格Controller
 *
 * @author xms
 * @date 2025-08-09
 */
@RestController
@RequestMapping("/xms/ptbDailyPrice")
public class PtbDailyPriceController extends BaseController
{
    @Autowired
    private IPtbDailyPriceService ptbDailyPriceService;

/**
 * 查询平台币每日价格列表
 */
@PreAuthorize("@ss.hasPermi('xms:ptbDailyPrice:list')")
@GetMapping("/list")
    public TableDataInfo list(PtbDailyPrice ptbDailyPrice)
    {
        startPage();
        List<PtbDailyPrice> list = ptbDailyPriceService.selectPtbDailyPriceList(ptbDailyPrice);
		if(CollectionUtil.isNotEmpty(list)){
			for (PtbDailyPrice dailyPrice : list) {
				Long dateValue = dailyPrice.getDate();
				if (dateValue == null) {
					dailyPrice.setDateStr(null);
					continue;
				}
				String dateStr = String.valueOf(dateValue);
				if (dateStr.length() == 8) {
					String formatted = DateUtil.parse(dateStr, "yyyyMMdd").toString("yyyy-MM-dd");
					dailyPrice.setDateStr(formatted);
				} else {
					dailyPrice.setDateStr(dateStr);
				}
			}
		}
        return getDataTable(list);
    }

    /**
     * 导出平台币每日价格列表
     */
    @PreAuthorize("@ss.hasPermi('xms:ptbDailyPrice:export')")
    @Log(title = "平台币每日价格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PtbDailyPrice ptbDailyPrice)
    {
        List<PtbDailyPrice> list = ptbDailyPriceService.selectPtbDailyPriceList(ptbDailyPrice);
		if(CollectionUtil.isNotEmpty(list)){
			for (PtbDailyPrice dailyPrice : list) {
				Long dateValue = dailyPrice.getDate();
				if (dateValue == null) {
					dailyPrice.setDateStr(null);
					continue;
				}
				String dateStr = String.valueOf(dateValue);
				if (dateStr.length() == 8) {
					String formatted = DateUtil.parse(dateStr, "yyyyMMdd").toString("yyyy-MM-dd");
					dailyPrice.setDateStr(formatted);
				} else {
					dailyPrice.setDateStr(dateStr);
				}
			}
		}
        ExcelUtil<PtbDailyPrice> util = new ExcelUtil<PtbDailyPrice>(PtbDailyPrice.class);
        util.exportExcel(response, list, "平台币每日价格数据");
    }

    /**
     * 获取平台币每日价格详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:ptbDailyPrice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(ptbDailyPriceService.getById(id));
    }

    /**
     * 新增平台币每日价格
     */
    @PreAuthorize("@ss.hasPermi('xms:ptbDailyPrice:add')")
    @Log(title = "平台币每日价格", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody PtbDailyPrice ptbDailyPrice) {
        return toAjax(ptbDailyPriceService.save(ptbDailyPrice));
    }

    /**
     * 修改平台币每日价格
     */
    @PreAuthorize("@ss.hasPermi('xms:ptbDailyPrice:edit')")
    @Log(title = "平台币每日价格", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody PtbDailyPrice ptbDailyPrice) {
        return toAjax(ptbDailyPriceService.updateById(ptbDailyPrice));
    }

    /**
     * 删除平台币每日价格
     */
    @PreAuthorize("@ss.hasPermi('xms:ptbDailyPrice:remove')")
    @Log(title = "平台币每日价格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(ptbDailyPriceService.removeByIds(Arrays.asList(ids)));
    }
}
