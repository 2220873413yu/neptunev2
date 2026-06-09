package com.xms.web.controller.xms;

import java.math.BigDecimal;
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
import com.xms.dao.domain.CoinPrice;
import com.xms.dao.service.ICoinPriceService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 币种价格配置Controller
 *
 * @author xms
 * @date 2025-11-19
 */
@RestController
@RequestMapping("/xms/coinPrice")
public class CoinPriceController extends BaseController
{
    @Autowired
    private ICoinPriceService coinPriceService;

	/**
	 * 查询币种价格配置列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:coinPrice:list')")
	@GetMapping("/list")
    public TableDataInfo list(CoinPrice coinPrice)
    {
        startPage();
        List<CoinPrice> list = coinPriceService.selectCoinPriceList(coinPrice);
        return getDataTable(list);
    }

    /**
     * 导出币种价格配置列表
     */
    @PreAuthorize("@ss.hasPermi('xms:coinPrice:export')")
    @Log(title = "币种价格配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CoinPrice coinPrice)
    {
        List<CoinPrice> list = coinPriceService.selectCoinPriceList(coinPrice);
        ExcelUtil<CoinPrice> util = new ExcelUtil<CoinPrice>(CoinPrice.class);
        util.exportExcel(response, list, "币种价格配置数据");
    }

    /**
     * 获取币种价格配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:coinPrice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(coinPriceService.getById(id));
    }

//    /**
//     * 新增币种价格配置
//     */
//    @PreAuthorize("@ss.hasPermi('xms:coinPrice:add')")
//    @Log(title = "币种价格配置", businessType = BusinessType.INSERT)
//    @PostMapping
//    @RepeatSubmit
//    public AjaxResult add(@RequestBody CoinPrice coinPrice) {
//        return toAjax(coinPriceService.save(coinPrice));
//    }

    /**
     * 修改币种价格配置
     */
    @PreAuthorize("@ss.hasPermi('xms:coinPrice:edit')")
    @Log(title = "币种价格配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody CoinPrice coinPrice) {
		if(coinPrice.getInitPrice().compareTo(BigDecimal.ZERO)<0){
			return error("初始价格不能小于0");
		}
		if(coinPrice.getCurrentPrice().compareTo(BigDecimal.ZERO)  <=0){
			return error("当前价格不能小于0");
		}
        return toAjax(coinPriceService.updateById(coinPrice));
    }

    /**
     * 删除币种价格配置
     */
//    @PreAuthorize("@ss.hasPermi('xms:coinPrice:remove')")
//    @Log(title = "币种价格配置", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(coinPriceService.removeByIds(Arrays.asList(ids)));
//    }
}
