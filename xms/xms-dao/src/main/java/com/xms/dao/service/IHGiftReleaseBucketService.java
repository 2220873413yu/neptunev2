package com.xms.dao.service;

import com.xms.dao.domain.HGiftReleaseBucket;

import java.math.BigDecimal;
import java.util.List;

/**
 * H赠送释放桶Service接口
 *
 * @author xms
 * @date 2026-06-07
 */
public interface IHGiftReleaseBucketService extends XmsDataService<HGiftReleaseBucket> {
	/**
	 * 查询H赠送释放桶列表
	 *
	 * @param hGiftReleaseBucket H赠送释放桶
	 * @return H赠送释放桶集合
	 */
	public List<HGiftReleaseBucket> selectHGiftReleaseBucketList(HGiftReleaseBucket hGiftReleaseBucket);

	/**
	 * 后台手动创建H赠送释放桶
	 *
	 * @param hGiftReleaseBucket H赠送释放桶
	 * @return 是否成功
	 */
	public boolean createManualBucket(HGiftReleaseBucket hGiftReleaseBucket);

	/**
	 * 正常ACP入金后创建H赠送释放桶
	 *
	 * @param userId 用户ID
	 * @param account 用户钱包地址
	 * @param sourceOrderNo 来源订单号
	 * @param giftHAmount 本单应赠送H总量
	 * @return 是否创建成功
	 */
	public boolean createAcpDepositBucket(Long userId, String account, String sourceOrderNo, BigDecimal giftHAmount);

	/**
	 * 旧系统H换ACP入金后创建H赠送释放桶
	 *
	 * @param userId 用户ID
	 * @param account 用户钱包地址
	 * @param sourceOrderNo 来源订单号
	 * @param giftHAmount 本单应赠送H总量
	 * @return 是否创建成功
	 */
	public boolean createOldHToAcpDepositBucket(Long userId, String account, String sourceOrderNo, BigDecimal giftHAmount);

	/**
	 * 用户H余额换ACP入金后创建H赠送释放桶
	 *
	 * @param userId 用户ID
	 * @param account 用户钱包地址
	 * @param sourceOrderNo 来源订单号
	 * @param giftHAmount 本单应赠送H总量
	 * @return 是否成功
	 */
	public boolean createWalletHToAcpDepositBucket(Long userId, String account, String sourceOrderNo, BigDecimal giftHAmount);

	/**
	 * 冻结释放桶
	 *
	 * @param id 主键
	 * @return 是否成功
	 */
	public boolean freeze(Long id);

	/**
	 * 解冻释放桶
	 *
	 * @param id 主键
	 * @return 是否成功
	 */
	public boolean unfreeze(Long id);
}
