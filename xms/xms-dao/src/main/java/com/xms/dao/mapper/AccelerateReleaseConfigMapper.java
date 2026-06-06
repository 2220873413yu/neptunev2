package com.xms.dao.mapper;

import java.util.List;
import com.xms.dao.mapper.XmsMapper;

import com.xms.dao.domain.AccelerateReleaseConfig;

/**
 * 收益加速释放配置Mapper接口
 *
 * @author xms
 * @date 2025-11-21
 */
public interface AccelerateReleaseConfigMapper extends XmsMapper<AccelerateReleaseConfig>
{
    /**
     * 查询收益加速释放配置列表
     *
     * @param accelerateReleaseConfig 收益加速释放配置
     * @return 收益加速释放配置集合
     */
    public List<AccelerateReleaseConfig> selectAccelerateReleaseConfigList(AccelerateReleaseConfig accelerateReleaseConfig);

}
