package com.lfj.blog.common.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * oss 存储配置
 **/
@Data
@ConfigurationProperties(prefix = "oss")
public class StorageProperties {

	private Netease netease;
	private Qiniu qiniu;
	private Local local;
	private Ali ali;
	private int type = 1;

	/**
	 * 本地
	 */
	@Data
	public static class Local {
		private String path;  // 文件保存根路径
		private String proxy; // 公网url
		private String virtualPath; // 虚拟路径
	}

	/**
	 * 七牛
	 */
	@Data
	public static class Qiniu {
		private String accessKey;
		private String secretKey;
		private String bucket;
		private String domain;
		private String region;
	}

	/**
	 * 网易云
	 */
	@Data
	public static class Netease {
		private String accessKey;
		private String secretKey;
		private String endpoint;
		private String bucket;
	}


	/**
	 * 阿里云
	 */
	@Data
	public static class Ali {
		private String accessKeyId;
		private String accessKeyIdSecret;
		private String bucket;
		private String endpoint;
	}
}

/*
Local: 本地存储配置，包括文件保存路径和公网 URL。
Qiniu: 七牛存储配置，包括访问密钥、秘钥、存储桶、域名和地区等信息。
Netease: 网易云存储配置，包括访问密钥、秘钥、终端节点、存储桶等信息。
Ali: 阿里云存储配置，包括访问密钥 ID、密钥、存储桶、终端节点等信息。
* */