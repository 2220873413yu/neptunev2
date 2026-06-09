package com.xms.web.controller.xms;

import java.util.Arrays;
import java.util.List;

import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.constant.ConstantStatic;
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
import com.xms.dao.domain.MarketsCoin;
import com.xms.dao.service.IMarketsCoinService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 币种图标配置Controller
 *
 * @author xms
 * @date 2025-08-15
 */
@RestController
@RequestMapping("/xms/marketsCoin")
public class MarketsCoinController extends BaseController
{
    @Autowired
    private IMarketsCoinService marketsCoinService;

	@Autowired
	private XmsRedis xmsRedis;
/**
 * 查询币种图标配置列表
 */
@PreAuthorize("@ss.hasPermi('xms:marketsCoin:list')")
@GetMapping("/list")
    public TableDataInfo list(MarketsCoin marketsCoin)
    {
        startPage();
        List<MarketsCoin> list = marketsCoinService.selectMarketsCoinList(marketsCoin);
        return getDataTable(list);
    }

    /**
     * 导出币种图标配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:marketsCoin:export')")
    @Log(title = "币种图标配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MarketsCoin marketsCoin)
    {
        List<MarketsCoin> list = marketsCoinService.selectMarketsCoinList(marketsCoin);
        ExcelUtil<MarketsCoin> util = new ExcelUtil<MarketsCoin>(MarketsCoin.class);
        util.exportExcel(response, list, "币种图标配置数据");
    }

    /**
     * 获取币种图标配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:marketsCoin:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(marketsCoinService.getById(id));
    }

    /**
     * 新增币种图标配置
     */
    @PreAuthorize("@ss.hasPermi('xms:marketsCoin:add')")
    @Log(title = "币种图标配置", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody MarketsCoin marketsCoin) {
		boolean save = marketsCoinService.save(marketsCoin);
		xmsRedis.del(ConstantStatic.market_icon);
        return toAjax(save);
    }

    /**
     * 修改币种图标配置
     */
    @PreAuthorize("@ss.hasPermi('xms:marketsCoin:edit')")
    @Log(title = "币种图标配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody MarketsCoin marketsCoin) {
		boolean b = marketsCoinService.updateById(marketsCoin);
		xmsRedis.del(ConstantStatic.market_icon);
        return toAjax(b);
    }

    /**
     * 删除币种图标配置
     */
    @PreAuthorize("@ss.hasPermi('xms:marketsCoin:remove')")
    @Log(title = "币种图标配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
		boolean b = marketsCoinService.removeByIds(Arrays.asList(ids));
		xmsRedis.del(ConstantStatic.market_icon);
        return toAjax(b);
    }
}
