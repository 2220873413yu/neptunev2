package com.xms.web.controller.xms;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.utils.CollectionUtil;
import com.xms.dao.domain.UserRechangeAddress;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.service.UserInfoService;
import com.xms.web.service.XmsUserInfoService;
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
import com.xms.dao.domain.ActiveOrder;
import com.xms.dao.service.IActiveOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 用户激活订单Controller
 *
 * @author xms
 * @date 2025-12-30
 */
@RestController
@RequestMapping("/xms/activeOrder")
public class ActiveOrderController extends BaseController
{
    @Autowired
    private IActiveOrderService activeOrderService;

	@Autowired
	private UserInfoService userInfoService;
/**
 * 查询用户激活订单列表
 */
@PreAuthorize("@ss.hasPermi('xms:activeOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(ActiveOrder activeOrder)
    {
		if(StrUtil.isNotBlank(activeOrder.getAddress())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, activeOrder.getAddress())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo == null){
				return getDataTable(new ArrayList<>());
			}else{
				activeOrder.setUserId(userInfo.getUserId());
			}
		}
        startPage();
        List<ActiveOrder> list = activeOrderService.selectActiveOrderList(activeOrder);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(ActiveOrder::getUserId).collect(Collectors.toList()));
			for (ActiveOrder order : list) {
				order.setAddress(userAccountMap.get(order.getUserId()));
			}
		}
        return getDataTable(list);
    }

    /**
     * 导出用户激活订单列表
     */
    @PreAuthorize("@ss.hasPermi('xms:activeOrder:export')")
    @Log(title = "用户激活订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActiveOrder activeOrder)
    {
		if(StrUtil.isNotBlank(activeOrder.getAddress())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, activeOrder.getAddress())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo == null){
				ExcelUtil<ActiveOrder> util = new ExcelUtil<ActiveOrder>(ActiveOrder.class);
				util.exportExcel(response, new ArrayList<>(), "用户激活订单数据");
			}else{
				activeOrder.setUserId(userInfo.getUserId());
			}
		}
        List<ActiveOrder> list = activeOrderService.selectActiveOrderList(activeOrder);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(ActiveOrder::getUserId).collect(Collectors.toList()));
			for (ActiveOrder order : list) {
				order.setAddress(userAccountMap.get(order.getUserId()));
			}
		}
        ExcelUtil<ActiveOrder> util = new ExcelUtil<ActiveOrder>(ActiveOrder.class);
        util.exportExcel(response, list, "用户激活订单数据");
    }

    /**
     * 获取用户激活订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:activeOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(activeOrderService.getById(id));
    }

    /**
     * 新增用户激活订单
     */
    @PreAuthorize("@ss.hasPermi('xms:activeOrder:add')")
    @Log(title = "用户激活订单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody ActiveOrder activeOrder) {
        return toAjax(activeOrderService.save(activeOrder));
    }

    /**
     * 修改用户激活订单
     */
    @PreAuthorize("@ss.hasPermi('xms:activeOrder:edit')")
    @Log(title = "用户激活订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody ActiveOrder activeOrder) {
        return toAjax(activeOrderService.updateById(activeOrder));
    }

    /**
     * 删除用户激活订单
     */
    @PreAuthorize("@ss.hasPermi('xms:activeOrder:remove')")
    @Log(title = "用户激活订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(activeOrderService.removeByIds(Arrays.asList(ids)));
    }
}
