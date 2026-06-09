package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.dao.domain.UserStakePosition;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.domain.Withdrawal;
import com.xms.dao.service.UserInfoService;
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
import com.xms.dao.domain.BuyHOrder;
import com.xms.dao.service.IBuyHOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 购买H代币订单Controller
 *
 * @author xms
 * @date 2026-03-10
 */
@RestController
@RequestMapping("/xms/buyHOrder")
public class BuyHOrderController extends BaseController
{
    @Autowired
    private IBuyHOrderService buyHOrderService;


	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询购买H代币订单列表
 */
@PreAuthorize("@ss.hasPermi('xms:buyHOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(BuyHOrder buyHOrder)
    {

        startPage();
        List<BuyHOrder> list = buyHOrderService.selectBuyHOrderList(buyHOrder);

        return getDataTable(list);
    }

    /**
     * 导出购买H代币订单列表
     */
    @PreAuthorize("@ss.hasPermi('xms:buyHOrder:export')")
    @Log(title = "购买H代币订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BuyHOrder buyHOrder)
    {


        List<BuyHOrder> list = buyHOrderService.selectBuyHOrderList(buyHOrder);

        ExcelUtil<BuyHOrder> util = new ExcelUtil<BuyHOrder>(BuyHOrder.class);
        util.exportExcel(response, list, "购买H代币订单数据");
    }

    /**
     * 获取购买H代币订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:buyHOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(buyHOrderService.getById(id));
    }

    /**
     * 新增购买H代币订单
     */
    @PreAuthorize("@ss.hasPermi('xms:buyHOrder:add')")
    @Log(title = "购买H代币订单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody BuyHOrder buyHOrder) {
        return toAjax(buyHOrderService.save(buyHOrder));
    }

    /**
     * 修改购买H代币订单
     */
    @PreAuthorize("@ss.hasPermi('xms:buyHOrder:edit')")
    @Log(title = "购买H代币订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody BuyHOrder buyHOrder) {
        return toAjax(buyHOrderService.updateById(buyHOrder));
    }

    /**
     * 删除购买H代币订单
     */
    @PreAuthorize("@ss.hasPermi('xms:buyHOrder:remove')")
    @Log(title = "购买H代币订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(buyHOrderService.removeByIds(Arrays.asList(ids)));
    }
}
