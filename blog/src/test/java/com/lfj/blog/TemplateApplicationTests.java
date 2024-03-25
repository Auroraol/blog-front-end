package com.lfj.blog;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TemplateApplicationTests {

	@Test
	void contextLoads() {
		String t = "pc:";
		String n = "123456";
		String encodedt = new String(Base64.encodeBase64(t.getBytes()));
		String encodedn = new String(Base64.encodeBase64(n.getBytes()));
		System.out.println(encodedt);
		System.out.println(encodedn);


	}


}
