package com.lfj.blog.common.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sms", ignoreInvalidFields = true)
public class SmsServiceProperties {

	private final Ali ali = new Ali();
	private final Tencent tencent = new Tencent();
	private int type = 1;
	private long expire = 300L;
	private long dayMax = 10L;

	public SmsServiceProperties() {
	}

	@Data
	public static class Ali {
		private String regionId = "cn-hangzhou";
		private String accessKeyId;
		private String accessKeySecret;
		private String signName;
		private String templateCode;
	}

	@Data
	public static class Tencent {
		private String appId;
		private String appKey;
		private String templateId;
		private String signName;
	}
}
