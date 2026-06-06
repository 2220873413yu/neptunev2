package com.xms.web.controller.xms;

import java.util.Arrays;
import java.util.List;

import com.google.protobuf.ServiceException;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.config.redis.XmsRedis;
import com.xms.common.config.redis.delayqueue.config.RedissonTemplate;
import com.xms.common.constant.ConstantStatic;
import com.xms.common.constant.RedisConstant;
import com.xms.dao.service.UserInfoService;
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
import com.xms.dao.domain.MarketTradeConfig;
import com.xms.dao.service.IMarketTradeConfigService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;

/**
 * 交易产品行情数据管理Controller
 *
 * @author xms
 * @date 2025-08-12
 */
@RestController
@RequestMapping("/xms/marketTradeConfig")
public class MarketTradeConfigController extends BaseController {
	@Autowired
	private IMarketTradeConfigService marketTradeConfigService;

	@Autowired
	private XmsRedis xmsRedis;

	@Autowired
	private RedissonTemplate redissonTemplate;

	/**
	 * 查询交易产品行情数据管理列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:marketTradeConfig:list')")
	@GetMapping("/list")
	public TableDataInfo list(MarketTradeConfig marketTradeConfig) {
		startPage();
		List<MarketTradeConfig> list = marketTradeConfigService.selectMarketTradeConfigList(marketTradeConfig);
		return getDataTable(list);
	}

	/**
	 * 导出交易产品行情数据管理列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:marketTradeConfig:export')")
	@Log(title = "交易产品行情数据管理", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, MarketTradeConfig marketTradeConfig) {
		List<MarketTradeConfig> list = marketTradeConfigService.selectMarketTradeConfigList(marketTradeConfig);
		ExcelUtil<MarketTradeConfig> util = new ExcelUtil<MarketTradeConfig>(MarketTradeConfig.class);
		util.exportExcel(response, list, "交易产品行情数据管理数据");
	}

	/**
	 * 获取交易产品行情数据管理详细信息
	 */
	@PreAuthorize("@ss.hasPermi('xms:marketTradeConfig:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult getInfo(@PathVariable("id") Long id) {
		return success(marketTradeConfigService.getById(id));
	}

	/**
	 * 新增交易产品行情数据管理
	 */
	@PreAuthorize("@ss.hasPermi('xms:marketTradeConfig:add')")
	@Log(title = "交易产品行情数据管理", businessType = BusinessType.INSERT)
	@PostMapping
	@RepeatSubmit
	public AjaxResult add(@RequestBody MarketTradeConfig marketTradeConfig) {
		return toAjax(marketTradeConfigService.save(marketTradeConfig));
	}

	/**
	 * 修改交易产品行情数据管理
	 */
	@PreAuthorize("@ss.hasPermi('xms:marketTradeConfig:edit')")
	@Log(title = "交易产品行情数据管理", businessType = BusinessType.UPDATE)
	@PutMapping
	@RepeatSubmit
	public AjaxResult edit(@RequestBody MarketTradeConfig marketTradeConfig) {
		boolean b = marketTradeConfigService.updateById(marketTradeConfig);
		clearCache();
		return toAjax(b);
	}

	/**
	 * 是否订阅盘口
	 */
	@PreAuthorize("@ss.hasPermi('xms:marketTradeConfig:edit')")
	@Log(title = "是否订阅盘口", businessType = BusinessType.UPDATE)
	@GetMapping("/handleDataPankou")
	public AjaxResult handleDataPankou(Long id, Integer isPankou) throws ServiceException {
		MarketTradeConfig marketTradeConfig = marketTradeConfigService.lambdaQuery()
			.eq(MarketTradeConfig::getId, id)
			.one();
		if(marketTradeConfig == null){
			return toAjax(1);
		}
		if(isPankou == 1){
			Long count = marketTradeConfigService.lambdaQuery()
				.eq(MarketTradeConfig::getType, marketTradeConfig.getType())
				.eq(MarketTradeConfig::getDataPankou, 1)
				.count();
			if(count>20){
				throw new ServiceException("该市场最大支持20个");
			}
		}

		marketTradeConfigService.lambdaUpdate()
			.eq(MarketTradeConfig::getId, id)
			.set(MarketTradeConfig::getDataPankou, isPankou)
			.update();
		clearCache();

		return toAjax(1);
	}

	private void clearCache() {
		String key1 = ConstantStatic.market_trade + "WI"+ RedisConstant.SEPARATOR + 1;
		String key12 = ConstantStatic.market_trade + "WI"+ RedisConstant.SEPARATOR + 2;
		String key13 = ConstantStatic.market_trade + "WI"+ RedisConstant.SEPARATOR + 3 ;
		String key14 = ConstantStatic.market_trade + "WI"+ RedisConstant.SEPARATOR + 4;
		String key15 = ConstantStatic.market_trade + "WI"+ RedisConstant.SEPARATOR + 5;
		String key2 = ConstantStatic.market_trade + "WX"+ RedisConstant.SEPARATOR + 1;
		String key22 = ConstantStatic.market_trade + "WX"+ RedisConstant.SEPARATOR + 2;
		String key23 = ConstantStatic.market_trade + "WX"+ RedisConstant.SEPARATOR + 3;
		String key24 = ConstantStatic.market_trade + "WX"+ RedisConstant.SEPARATOR + 4;
		String key25 = ConstantStatic.market_trade + "WX"+ RedisConstant.SEPARATOR + 5;

		String key3 = ConstantStatic.market_trade + "WA"+ RedisConstant.SEPARATOR + 1;
		String key32 = ConstantStatic.market_trade + "WA"+ RedisConstant.SEPARATOR + 2;
		String key33 = ConstantStatic.market_trade + "WA"+ RedisConstant.SEPARATOR + 3;
		String key34 = ConstantStatic.market_trade + "WA"+ RedisConstant.SEPARATOR + 4;
		String key35 = ConstantStatic.market_trade + "WA"+ RedisConstant.SEPARATOR + 5;
		xmsRedis.del(key1);
		xmsRedis.del(key12);
		xmsRedis.del(key13);
		xmsRedis.del(key14);
		xmsRedis.del(key15);

		xmsRedis.del(key2);
		xmsRedis.del(key22);
		xmsRedis.del(key23);
		xmsRedis.del(key24);
		xmsRedis.del(key25);


		xmsRedis.del(key3);
		xmsRedis.del(key32);
		xmsRedis.del(key33);
		xmsRedis.del(key34);
		xmsRedis.del(key35);
		// 将所有key拼接后传入方法
		redissonTemplate.sendCleanCacheWithDelay(
			key1 + "," + key12 + "," + key13 + "," + key14 + "," + key15 + "," +
				key2 + "," + key22 + "," + key23 + "," + key24 + "," + key25 + "," +
				key3 + "," + key32 + "," + key33 + "," + key34 + "," + key35
		);
	}

	/**
	 * 删除交易产品行情数据管理
	 */
	@PreAuthorize("@ss.hasPermi('xms:marketTradeConfig:remove')")
	@Log(title = "交易产品行情数据管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		return toAjax(marketTradeConfigService.removeByIds(Arrays.asList(ids)));
	}
}
