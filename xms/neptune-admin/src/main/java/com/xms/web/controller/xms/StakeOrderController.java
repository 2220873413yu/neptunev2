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
import com.xms.dao.domain.StakeOrder;
import com.xms.dao.service.IStakeOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 质押订单Controller
 *
 * @author xms
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/xms/stakeOrder")
public class StakeOrderController extends BaseController
{
    @Autowired
    private IStakeOrderService stakeOrderService;

	@Autowired
	private UserInfoService userInfoService;

/**
 * 查询质押订单列表
 */
@PreAuthorize("@ss.hasPermi('xms:stakeOrder:list')")
@GetMapping("/list")
    public TableDataInfo list(StakeOrder stakeOrder)
    {

		if(StrUtil.isNotBlank(stakeOrder.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, stakeOrder.getUserAccount())
				.one();
			if(userInfo != null){
				stakeOrder.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}
        startPage();
        List<StakeOrder> list = stakeOrderService.selectStakeOrderList(stakeOrder);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(StakeOrder::getUserId).collect(Collectors.toList()));
			for (StakeOrder bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
        return getDataTable(list);
    }

    /**
     * 导出质押订单列表
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeOrder:export')")
    @Log(title = "质押订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakeOrder stakeOrder)
    {
		if(StrUtil.isNotBlank(stakeOrder.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, stakeOrder.getUserAccount())
				.one();
			if(userInfo != null){
				stakeOrder.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<Withdrawal> util = new ExcelUtil<Withdrawal>(Withdrawal.class);
				util.exportExcel(response, new ArrayList<>(), "质押订单数据");
				return;
			}
		}

        List<StakeOrder> list = stakeOrderService.selectStakeOrderList(stakeOrder);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userAccountMap = userInfoService.getUserAccountById(list.stream().map(StakeOrder::getUserId).collect(Collectors.toList()));
			for (StakeOrder bankVo : list) {
				bankVo.setUserAccount(userAccountMap.get(bankVo.getUserId()));
			}
		}
        ExcelUtil<StakeOrder> util = new ExcelUtil<StakeOrder>(StakeOrder.class);
        util.exportExcel(response, list, "质押订单数据");
    }

    /**
     * 获取质押订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(stakeOrderService.getById(id));
    }

    /**
     * 新增质押订单
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeOrder:add')")
    @Log(title = "质押订单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody StakeOrder stakeOrder) {
        return toAjax(stakeOrderService.save(stakeOrder));
    }

    /**
     * 修改质押订单
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeOrder:edit')")
    @Log(title = "质押订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody StakeOrder stakeOrder) {
        return toAjax(stakeOrderService.updateById(stakeOrder));
    }

    /**
     * 删除质押订单
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeOrder:remove')")
    @Log(title = "质押订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stakeOrderService.removeByIds(Arrays.asList(ids)));
    }
}
