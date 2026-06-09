package com.xms.web.controller.xms;

import com.xms.common.annotation.Log;
import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.core.controller.BaseController;
import com.xms.common.core.domain.AjaxResult;
import com.xms.common.core.page.TableDataInfo;
import com.xms.common.enums.BusinessType;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.dao.domain.HGiftReleaseBucket;
import com.xms.dao.service.IHGiftReleaseBucketService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * H赠送释放Controller
 *
 * @author xms
 * @date 2026-06-07
 */
@RestController
@RequestMapping("/xms/hGiftRelease")
public class HGiftReleaseBucketController extends BaseController {
	@Autowired
	private IHGiftReleaseBucketService hGiftReleaseBucketService;

	/**
	 * 查询H赠送释放列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:hGiftRelease:list')")
	@GetMapping("/list")
	public TableDataInfo list(HGiftReleaseBucket hGiftReleaseBucket) {
		startPage();
		List<HGiftReleaseBucket> list = hGiftReleaseBucketService.selectHGiftReleaseBucketList(hGiftReleaseBucket);
		return getDataTable(list);
	}

	/**
	 * 导出H赠送释放列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:hGiftRelease:export')")
	@Log(title = "H赠送释放", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, HGiftReleaseBucket hGiftReleaseBucket) {
		List<HGiftReleaseBucket> list = hGiftReleaseBucketService.selectHGiftReleaseBucketList(hGiftReleaseBucket);
		ExcelUtil<HGiftReleaseBucket> util = new ExcelUtil<>(HGiftReleaseBucket.class);
		util.exportExcel(response, list, "H赠送释放数据");
	}

	/**
	 * 获取H赠送释放详细信息
	 */
	@PreAuthorize("@ss.hasPermi('xms:hGiftRelease:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult getInfo(@PathVariable("id") Long id) {
		return success(hGiftReleaseBucketService.getById(id));
	}

	/**
	 * 后台新增H赠送释放桶
	 */
	@PreAuthorize("@ss.hasPermi('xms:hGiftRelease:add')")
	@Log(title = "H赠送释放", businessType = BusinessType.INSERT)
	@PostMapping
	@RepeatSubmit
	public AjaxResult add(@RequestBody HGiftReleaseBucket hGiftReleaseBucket) {
		return toAjax(hGiftReleaseBucketService.createManualBucket(hGiftReleaseBucket));
	}

	/**
	 * 冻结释放桶
	 */
	@PreAuthorize("@ss.hasPermi('xms:hGiftRelease:freeze')")
	@Log(title = "H赠送释放", businessType = BusinessType.UPDATE)
	@PutMapping("/freeze/{id}")
	public AjaxResult freeze(@PathVariable("id") Long id) {
		return toAjax(hGiftReleaseBucketService.freeze(id));
	}

	/**
	 * 解冻释放桶
	 */
	@PreAuthorize("@ss.hasPermi('xms:hGiftRelease:unfreeze')")
	@Log(title = "H赠送释放", businessType = BusinessType.UPDATE)
	@PutMapping("/unfreeze/{id}")
	public AjaxResult unfreeze(@PathVariable("id") Long id) {
		return toAjax(hGiftReleaseBucketService.unfreeze(id));
	}

}
