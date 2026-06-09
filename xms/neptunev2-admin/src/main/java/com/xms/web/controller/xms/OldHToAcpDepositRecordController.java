package com.xms.web.controller.xms;

import com.xms.common.annotation.Log;
import com.xms.common.core.controller.BaseController;
import com.xms.common.core.page.TableDataInfo;
import com.xms.common.enums.BusinessType;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.dao.domain.OldHToAcpDepositRecord;
import com.xms.dao.service.IOldHToAcpDepositRecordService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 旧系统H换ACP入金记录Controller
 *
 * @author xms
 * @date 2026-06-07
 */
@RestController
@RequestMapping("/xms/oldHToAcpDepositRecord")
public class OldHToAcpDepositRecordController extends BaseController {
	@Autowired
	private IOldHToAcpDepositRecordService oldHToAcpDepositRecordService;

	/**
	 * 查询旧系统H换ACP入金记录列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:oldHToAcpDepositRecord:list')")
	@GetMapping("/list")
	public TableDataInfo list(OldHToAcpDepositRecord oldHToAcpDepositRecord) {
		startPage();
		List<OldHToAcpDepositRecord> list = oldHToAcpDepositRecordService.selectOldHToAcpDepositRecordList(oldHToAcpDepositRecord);
		return getDataTable(list);
	}

	/**
	 * 导出旧系统H换ACP入金记录列表
	 */
	@PreAuthorize("@ss.hasPermi('xms:oldHToAcpDepositRecord:export')")
	@Log(title = "旧H换ACP入金记录", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, OldHToAcpDepositRecord oldHToAcpDepositRecord) {
		List<OldHToAcpDepositRecord> list = oldHToAcpDepositRecordService.selectOldHToAcpDepositRecordList(oldHToAcpDepositRecord);
		ExcelUtil<OldHToAcpDepositRecord> util = new ExcelUtil<>(OldHToAcpDepositRecord.class);
		util.exportExcel(response, list, "旧H换ACP入金记录数据");
	}
}
