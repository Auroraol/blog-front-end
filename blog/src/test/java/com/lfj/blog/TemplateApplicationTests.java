package com.lfj.blog;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TemplateApplicationTests {

	@Autowired
	private StringEncryptor stringEncryptor;

	@Test
	void contextLoads() {
		String t = "pc:";
		String n = "123456";
		String encodedt = new String(Base64.encodeBase64(t.getBytes()));
		String encodedn = new String(Base64.encodeBase64(n.getBytes()));
		System.out.println(encodedt);
		System.out.println(encodedn);
	}

	@Test
	void encryptTest() {
		String content = "KXPxn37uUMJ1zrWhZ6QlMqCyPC6LcL";
		String encryptStr = stringEncryptor.encrypt(content);
		String decryptStr = stringEncryptor.decrypt(encryptStr);
		System.out.println("加密后的内容：" + encryptStr);
		System.out.println("解密后的内容：" + decryptStr);
	}
}
