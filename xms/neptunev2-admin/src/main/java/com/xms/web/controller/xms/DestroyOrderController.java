package com.xms.web.controller.xms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xms.common.annotation.RepeatSubmit;
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
import com.xms.dao.domain.DestroyOrder;
import com.xms.dao.service.IDestroyOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 销毁记录Controller
 *
 * @author xms
 * @date 2025-11-18
 */
@RestController
@RequestMapping("/xms/destroyOrder")
public class DestroyOrderController extends BaseController
{
    @Autowired
    private IDestroyOrderService destroyOrderService;

	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询销毁记录列表
 */
@PreAuthorize("@ss.hasPermi('xms:destroyOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(DestroyOrder destroyOrder)
    {
		if(StrUtil.isNotBlank(destroyOrder.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, destroyOrder.getUserAccount())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo != null){
				destroyOrder.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}

        startPage();
        List<DestroyOrder> list = destroyOrderService.selectDestroyOrderList(destroyOrder);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userIdMap = userInfoService.getUserAccountById(list.stream().map(DestroyOrder::getUserId).collect(Collectors.toList()));
			for (DestroyOrder order : list) {
				order.setUserAccount(userIdMap.get(order.getUserId()));
			}
		}
		return getDataTable(list);
    }

    /**
     * 导出销毁记录列表
     */
    @PreAuthorize("@ss.hasPermi('xms:destroyOrder:export')")
    @Log(title = "销毁记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DestroyOrder destroyOrder)
    {
		if(StrUtil.isNotBlank(destroyOrder.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, destroyOrder.getUserAccount())
				.select(UserInfo::getUserId)
				.one();
			if(userInfo != null){
				destroyOrder.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<DestroyOrder> util = new ExcelUtil<DestroyOrder>(DestroyOrder.class);
				util.exportExcel(response, new ArrayList<>(), "销毁记录数据");
			}
		}

        List<DestroyOrder> list = destroyOrderService.selectDestroyOrderList(destroyOrder);

		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userIdMap = userInfoService.getUserAccountById(list.stream().map(DestroyOrder::getUserId).collect(Collectors.toList()));
			for (DestroyOrder order : list) {
				order.setUserAccount(userIdMap.get(order.getUserId()));
			}
		}
        ExcelUtil<DestroyOrder> util = new ExcelUtil<DestroyOrder>(DestroyOrder.class);
        util.exportExcel(response, list, "销毁记录数据");
    }

    /**
     * 获取销毁记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:destroyOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(destroyOrderService.getById(id));
    }

    /**
     * 新增销毁记录
     */
    @PreAuthorize("@ss.hasPermi('xms:destroyOrder:add')")
    @Log(title = "销毁记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody DestroyOrder destroyOrder) {
        return toAjax(destroyOrderService.save(destroyOrder));
    }

    /**
     * 修改销毁记录
     */
    @PreAuthorize("@ss.hasPermi('xms:destroyOrder:edit')")
    @Log(title = "销毁记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody DestroyOrder destroyOrder) {
        return toAjax(destroyOrderService.updateById(destroyOrder));
    }

    /**
     * 删除销毁记录
     */
    @PreAuthorize("@ss.hasPermi('xms:destroyOrder:remove')")
    @Log(title = "销毁记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(destroyOrderService.removeByIds(Arrays.asList(ids)));
    }

	/**
	 * 销毁订单看板统计
	 */
	@PreAuthorize("@ss.hasPermi('xms:destroyOrder:list')")
	@GetMapping("/statistics")
	public AjaxResult statistics() {
		return success(destroyOrderService.dashboardStatistics());
	}
}
