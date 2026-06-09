package com.xms.app.controller;

import com.xms.common.core.domain.api.ResultPista;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("")
public class TestController {

	/**
	 * 测试
	 *
	 * @return 随机数
	 */
	@ApiOperation(value = "测试")
	@GetMapping(value = "/")
	public ResultPista t1()  throws Exception{
		return ResultPista.success();
	}
}
