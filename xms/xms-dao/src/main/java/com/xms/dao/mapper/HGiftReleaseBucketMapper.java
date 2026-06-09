package com.xms.dao.mapper;

import com.xms.dao.domain.HGiftReleaseBucket;

import java.util.List;

/**
 * H赠送释放桶Mapper接口
 *
 * @author xms
 * @date 2026-06-07
 */
public interface HGiftReleaseBucketMapper extends XmsMapper<HGiftReleaseBucket> {
	/**
	 * 查询H赠送释放桶列表
	 *
	 * @param hGiftReleaseBucket H赠送释放桶
	 * @return H赠送释放桶集合
	 */
	public List<HGiftReleaseBucket> selectHGiftReleaseBucketList(HGiftReleaseBucket hGiftReleaseBucket);
}
