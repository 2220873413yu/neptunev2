package com.xms.web.controller.xms;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.xms.common.annotation.RepeatSubmit;
import com.xms.common.mq.dynamic.AsyncDynamicOrderSettlementService;
import com.xms.common.mq.dynamic.OrderMsgDO;
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
import com.xms.dao.domain.StakeRound;
import com.xms.dao.service.IStakeRoundService;
import com.xms.common.utils.poi.ExcelUtil;
import com.xms.common.core.page.TableDataInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 全局质押轮次Controller
 *
 * @author xms
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/xms/stakeRound")
public class StakeRoundController extends BaseController
{
    @Autowired
    private IStakeRoundService stakeRoundService;

    @Autowired
    private AsyncDynamicOrderSettlementService asyncDynamicOrderSettlementService;

/**
 * 查询全局质押轮次列表
 */
@PreAuthorize("@ss.hasPermi('xms:stakeRound:list')")
@GetMapping("/list")
    public TableDataInfo list(StakeRound stakeRound)
    {
        startPage();
        List<StakeRound> list = stakeRoundService.selectStakeRoundList(stakeRound);
        return getDataTable(list);
    }

    /**
     * 导出全局质押轮次列表
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:export')")
    @Log(title = "全局质押轮次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakeRound stakeRound)
    {
        List<StakeRound> list = stakeRoundService.selectStakeRoundList(stakeRound);
        ExcelUtil<StakeRound> util = new ExcelUtil<StakeRound>(StakeRound.class);
        util.exportExcel(response, list, "全局质押轮次数据");
    }

    /**
     * 获取全局质押轮次详细信息
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(stakeRoundService.getById(id));
    }

    /**
     * 新增全局质押轮次
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:add')")
    @Log(title = "全局质押轮次", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@RequestBody StakeRound stakeRound) {
        return toAjax(stakeRoundService.save(stakeRound));
    }

    /**
     * 修改全局质押轮次
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:edit')")
    @Log(title = "全局质押轮次", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit
    public AjaxResult edit(@RequestBody StakeRound stakeRound) {
        return toAjax(stakeRoundService.updateById(stakeRound));
    }

    /**
     * 修改进行中轮次的爆仓检测开关。
     *
     * <p>开关从关闭改为开启时，事务提交后主动投递一次 bizType=2 爆仓检测消息；关闭或重复开启不投递。
     * 消费端仍会按最新开关做兜底，避免消息在关闭后继续触发爆仓。</p>
     *
     * @param stakeRound 请求对象，仅使用 id 和 liquidationCheckEnabled
     * @return 操作结果
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:edit')")
    @Log(title = "全局质押轮次爆仓检测开关", businessType = BusinessType.UPDATE)
    @PutMapping("/liquidationCheckSwitch")
    @RepeatSubmit
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult liquidationCheckSwitch(@RequestBody StakeRound stakeRound) {
        if (stakeRound == null || stakeRound.getId() == null) {
            return error("轮次编号不能为空");
        }
        Integer newEnabled = stakeRound.getLiquidationCheckEnabled();
        if (!Integer.valueOf(0).equals(newEnabled) && !Integer.valueOf(1).equals(newEnabled)) {
            return error("爆仓检测开关值只能是0或1");
        }

        StakeRound dbStakeRound = stakeRoundService.getById(stakeRound.getId());
        if (dbStakeRound == null) {
            return error("轮次不存在");
        }
        if (!Integer.valueOf(0).equals(dbStakeRound.getStatus())) {
            return error("只有进行中轮次允许修改爆仓检测开关");
        }

        Integer oldEnabled = dbStakeRound.getLiquidationCheckEnabled();
        boolean update = stakeRoundService.lambdaUpdate()
            .eq(StakeRound::getId, stakeRound.getId())
            .eq(StakeRound::getStatus, 0)
            .set(StakeRound::getLiquidationCheckEnabled, newEnabled)
            .update();
        if (!update) {
            return error("更新爆仓检测开关失败");
        }

        if (Integer.valueOf(0).equals(oldEnabled) && Integer.valueOf(1).equals(newEnabled)) {
            sendLiquidationCheckAfterCommit(stakeRound.getId());
        }
        return success();
    }

    /**
     * 在轮次开关由关闭切换为开启后，事务提交成功再投递一次爆仓检测消息。
     *
     * @param stakeRoundId 需要检测的质押轮次ID
     */
    private void sendLiquidationCheckAfterCommit(Long stakeRoundId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                List<OrderMsgDO> orderMsgDOList = new ArrayList<>();
                OrderMsgDO orderMsgDO = new OrderMsgDO();
                orderMsgDO.setId(stakeRoundId);
                orderMsgDO.setBizType(2);
                orderMsgDOList.add(orderMsgDO);
                asyncDynamicOrderSettlementService.sendMessage(orderMsgDOList);
            }
        });
    }

    /**
     * 删除全局质押轮次
     */
    @PreAuthorize("@ss.hasPermi('xms:stakeRound:remove')")
    @Log(title = "全局质押轮次", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stakeRoundService.removeByIds(Arrays.asList(ids)));
    }
}
