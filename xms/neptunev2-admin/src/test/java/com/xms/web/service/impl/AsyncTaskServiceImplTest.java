package com.xms.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xms.dao.domain.PtbDayRatioRule;
import com.xms.dao.service.IPtbDayRatioRuleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 针对 AsyncTaskServiceImpl 中日利率调整逻辑的集成测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AsyncTaskServiceImplTest {

	@Autowired
	private AsyncTaskServiceImpl asyncTaskService;

	@Autowired
	private IPtbDayRatioRuleService ptbDayRatioRuleService;

	@Before
	public void initRule() {
		ptbDayRatioRuleService.remove(new LambdaQueryWrapper<PtbDayRatioRule>()
			.eq(PtbDayRatioRule::getCoinType, 1L));
		PtbDayRatioRule rule = PtbDayRatioRule.builder()
			.coinType(1L)
			.baseRatio(new BigDecimal("0.0100"))
			.triggerThreshold(new BigDecimal("10"))
			.stepPerc(new BigDecimal("0.0001"))
			.minRatio(new BigDecimal("0.0050"))
			.maxRatio(new BigDecimal("0.0300"))
			.enabled(1L)
			.build();
		ptbDayRatioRuleService.save(rule);
	}

	@Test
	public void testAdjustBoomaiDayRatioIncrease() {
		ReflectionTestUtils.invokeMethod(asyncTaskService, "adjustBoomaiDayRatio", new BigDecimal("25"));
		PtbDayRatioRule updatedRule = ptbDayRatioRuleService.getOne(new LambdaQueryWrapper<PtbDayRatioRule>()
			.eq(PtbDayRatioRule::getCoinType, 1L));
		Assert.assertNotNull(updatedRule);
		Assert.assertEquals(new BigDecimal("0.0115"), updatedRule.getBaseRatio());
	}

	@Test
	public void testAdjustBoomaiDayRatioDecreaseClamp() {
		ReflectionTestUtils.invokeMethod(asyncTaskService, "adjustBoomaiDayRatio", new BigDecimal("-80"));
		PtbDayRatioRule updatedRule = ptbDayRatioRuleService.getOne(new LambdaQueryWrapper<PtbDayRatioRule>()
			.eq(PtbDayRatioRule::getCoinType, 1L));
		Assert.assertNotNull(updatedRule);
		Assert.assertEquals(new BigDecimal("0.0050"), updatedRule.getBaseRatio());
	}
}

