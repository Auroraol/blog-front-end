package com.lfj.blog;

import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.utils.ResponseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TemplateApplicationTests {

	@Test
	void contextLoads() {
		// 创建一个ResponseResult对象
		ResponseResult responseResult = new ResponseResult();
		responseResult.setCode(200);
		responseResult.setMessage("Success");
		responseResult.setData("Test data");


		// 使用Spring提供的MockHttpServletResponse类来模拟HttpServletResponse对象
		HttpServletResponse response = new MockHttpServletResponse();

		ResponseUtil.output(response, responseResult);
		System.out.println(response); // 替换ExpectedOutput为预期的输出结果

	}



}
