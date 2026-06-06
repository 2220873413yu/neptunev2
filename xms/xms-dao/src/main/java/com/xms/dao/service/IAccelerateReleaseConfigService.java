package com.xms.dao.service;

import java.util.List;
import com.xms.dao.service.XmsDataService;
import com.xms.dao.domain.AccelerateReleaseConfig;

/**
 * 收益加速释放配置Service接口
 *
 * @author xms
 * @date 2025-11-21
 */
public interface IAccelerateReleaseConfigService extends XmsDataService<AccelerateReleaseConfig>
{

    /**
     * 查询收益加速释放配置列表
     *
     * @param accelerateReleaseConfig 收益加速释放配置
     * @return 收益加速释放配置集合
     */
    public List<AccelerateReleaseConfig> selectAccelerateReleaseConfigList(AccelerateReleaseConfig accelerateReleaseConfig);

	/**
     * 修改收益加速释放配置
     *
     * @param accelerateReleaseConfig 收益加速释放配置
     * @return 结果
     */
    int updateRecordById(AccelerateReleaseConfig accelerateReleaseConfig);
}
