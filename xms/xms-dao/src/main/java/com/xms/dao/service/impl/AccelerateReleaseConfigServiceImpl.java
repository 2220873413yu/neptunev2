package com.xms.dao.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.xms.common.exception.ServiceException;
import com.xms.dao.service.impl.XmsDataServiceImpl;
import org.springframework.stereotype.Service;
import com.xms.dao.mapper.AccelerateReleaseConfigMapper;
import com.xms.dao.domain.AccelerateReleaseConfig;
import com.xms.dao.service.IAccelerateReleaseConfigService;

/**
 * 收益加速释放配置Service业务层处理
 *
 * @author xms
 * @date 2025-11-21
 */
@Service
public class AccelerateReleaseConfigServiceImpl extends XmsDataServiceImpl<AccelerateReleaseConfigMapper, AccelerateReleaseConfig> implements IAccelerateReleaseConfigService
{


    /**
     * 查询收益加速释放配置列表
     *
     *
     * @param accelerateReleaseConfig 收益加速释放配置
     * @return 收益加速释放配置
     */
    @Override
    public List<AccelerateReleaseConfig> selectAccelerateReleaseConfigList(AccelerateReleaseConfig accelerateReleaseConfig)
    {
        return baseMapper.selectAccelerateReleaseConfigList(accelerateReleaseConfig);
    }

    @Override
    public int updateRecordById(AccelerateReleaseConfig cfg) {
        if (cfg.getTargetDays() <= 0) {
            throw new ServiceException("加速后总释放天数必须大于 0");
        }
        BigDecimal newRatio = cfg.getFuelRatio();

        // 1. 与更短周期比较（必须更小）
        AccelerateReleaseConfig shorter = lambdaQuery()
                .lt(AccelerateReleaseConfig::getTargetDays, cfg.getTargetDays())
                .ne(AccelerateReleaseConfig::getId, cfg.getId())
                .orderByDesc(AccelerateReleaseConfig::getTargetDays)
                .last("limit 1")
                .one();
        if (shorter != null && newRatio.compareTo(shorter.getFuelRatio()) >= 0) {
            throw new ServiceException(StrUtil.format(
                    "燃料占比必须小于 {} 天的配置（{}%）", shorter.getTargetDays(), shorter.getFuelRatio()));
        }

        // 2. 与更长周期比较（必须更大）
        AccelerateReleaseConfig longer = lambdaQuery()
                .gt(AccelerateReleaseConfig::getTargetDays, cfg.getTargetDays())
                .ne(AccelerateReleaseConfig::getId, cfg.getId())
                .orderByAsc(AccelerateReleaseConfig::getTargetDays)
                .last("limit 1")
                .one();
        if (longer != null && newRatio.compareTo(longer.getFuelRatio()) <= 0) {
            throw new ServiceException(StrUtil.format(
                    "燃料占比必须大于 {} 天的配置（{}%）", longer.getTargetDays(), longer.getFuelRatio()));
        }

        updateById(cfg);
        return 1;
    }
}
