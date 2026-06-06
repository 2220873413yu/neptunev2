package com.xms.web.controller.xms;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.protobuf.ServiceException;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.SysConstant;
import com.xms.common.core.domain.api.ResultPista;
import com.xms.dao.domain.MiningPackage;
import com.xms.dao.domain.RechargeRecord;
import com.xms.dao.entity.domain.UserInfo;
import com.xms.dao.entity.req.AddMiningOrderReq;
import com.xms.dao.service.IMiningPackageService;
import com.xms.dao.service.UserInfoService;
import com.xms.dao.service.XmsCommonService;
import com.xms.dao.service.impl.MiningPackageOrderServiceImpl;
import com.xms.system.service.ISysUserService;
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
import com.xms.dao.domain.MiningPackageOrder;
import com.xms.dao.service.IMiningPackageOrderService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 基金订单Controller
 *
 * @author xms
 * @date 2025-08-07
 */
@RestController
@RequestMapping("/xms/miningPackageOrder")
public class MiningPackageOrderController extends BaseController
{
    @Autowired
    private IMiningPackageOrderService miningPackageOrderService;

	@Autowired
	private IMiningPackageService miningPackageService;

	@Autowired
	private XmsCommonService xmsCommonServiceImpl;

	@Autowired
	private ISysUserService sysUserService;

	@Autowired
	private UserInfoService userInfoService;
	/**
	 * 查询基金订单列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:list')")
	@GetMapping("/list")
    public TableDataInfo list(MiningPackageOrder miningPackageOrder)
    {
		if(StrUtil.isNotBlank(miningPackageOrder.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, miningPackageOrder.getUserAccount())
				.one();
			if(userInfo != null){
				miningPackageOrder.setUserId(userInfo.getUserId());
			}else{
				return getDataTable(new ArrayList<>());
			}
		}
        startPage();
        List<MiningPackageOrder> list = miningPackageOrderService.selectMiningPackageOrderList(miningPackageOrder);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userCodeMap = userInfoService.getUserAccountById(list.stream().map(MiningPackageOrder::getUserId).collect(Collectors.toList()));
			for (MiningPackageOrder packageOrder : list) {
				if(packageOrder.getType().equals(0)){
					BigDecimal totalReward = MiningPackageOrderServiceImpl.getMaxReward(packageOrder.getBuyPrice(),
						packageOrder.getDayRatio().divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew),
						packageOrder.getRunDays());
					packageOrder.setPendingReward(totalReward.subtract(packageOrder.getTotalReward()));
				}
				packageOrder.setUserAccount(userCodeMap.get(packageOrder.getUserId()));
			}
		}
        return getDataTable(list);
    }

	/**
	 * 查询固定矿机基金套餐列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:list')")
	@GetMapping("/miningPackageList")
    public List<MiningPackage> miningPackageList(MiningPackageOrder miningPackageOrder)
    {
		List<MiningPackage> list = miningPackageService.lambdaQuery()
			.eq(MiningPackage::getType, 1)
			.select(MiningPackage::getId,MiningPackage::getNameCn)
			.list();
		return list;
    }

    /**
     * 导出基金订单列表
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:export')")
    @Log(title = "基金订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MiningPackageOrder miningPackageOrder)
    {
		if(StrUtil.isNotBlank(miningPackageOrder.getUserAccount())){
			UserInfo userInfo = userInfoService.lambdaQuery()
				.eq(UserInfo::getAccount, miningPackageOrder.getUserAccount())
				.one();
			if(userInfo != null){
				miningPackageOrder.setUserId(userInfo.getUserId());
			}else{
				ExcelUtil<MiningPackageOrder> util = new ExcelUtil<MiningPackageOrder>(MiningPackageOrder.class);
				util.exportExcel(response, new ArrayList<>(), "基金订单数据");
				return;
			}
		}
        List<MiningPackageOrder> list = miningPackageOrderService.selectMiningPackageOrderList(miningPackageOrder);
		if(CollectionUtil.isNotEmpty(list)){
			Map<Long, String> userCodeMap = userInfoService.getUserAccountById(list.stream().map(MiningPackageOrder::getUserId).collect(Collectors.toList()));
			for (MiningPackageOrder packageOrder : list) {
				if(packageOrder.getType().equals(0)){
					BigDecimal totalReward = MiningPackageOrderServiceImpl.getMaxReward(packageOrder.getBuyPrice(),
						packageOrder.getDayRatio().divide(SysConstant.BAIFENBI, ConstantStatic.newScale, ConstantStatic.roundingModeNew),
						packageOrder.getRunDays());
					packageOrder.setPendingReward(totalReward.subtract(packageOrder.getTotalReward()));
				}
				packageOrder.setUserAccount(userCodeMap.get(packageOrder.getUserId()));
			}
		}
        ExcelUtil<MiningPackageOrder> util = new ExcelUtil<MiningPackageOrder>(MiningPackageOrder.class);
        util.exportExcel(response, list, "基金订单数据");
    }

    /**
     * 获取基金订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(miningPackageOrderService.getById(id));
    }

	/**
     * 查询订单天数列表
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:query')")
    @GetMapping("/getDistinctDays")
    public List<Integer> getDistinctDays() {
		List<Integer> distinctDays = miningPackageOrderService.getDistinctDays();
		if(CollectionUtil.isNotEmpty(distinctDays)){
			distinctDays = distinctDays.stream()
				.filter(day -> day != null && day > 0)
				.collect(Collectors.toList());
		}
		return distinctDays;
    }

    /**
     * 手动拨付基金订单
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:add')")
    @Log(title = "手动拨付基金订单", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody AddMiningOrderReq req) throws ServiceException{
//		ResultPista resultPista = xmsCommonServiceImpl.checkMineSettleTime();
//		if (!resultPista.isSuccess()) {
//			throw new com.xms.common.exception.ServiceException(resultPista.getMsg());
//		}

		//验证码验证
		sysUserService.pubValidate(req.getAutoCode());
		return toAjax(miningPackageOrderService.saveMiningOrder(req));
    }

    /**
     * 修改基金订单
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:edit')")
    @Log(title = "基金订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody MiningPackageOrder miningPackageOrder) {
        return toAjax(miningPackageOrderService.updateById(miningPackageOrder));
    }

    /**
     * 删除基金订单
     */
    @PreAuthorize("@ss.hasPermi('xms:miningPackageOrder:remove')")
    @Log(title = "基金订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(miningPackageOrderService.removeByIds(Arrays.asList(ids)));
    }

}
