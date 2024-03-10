### 后端技术

| 技术            | 说明                       | 官网链接                                                     |
| --------------- | -------------------------- | ------------------------------------------------------------ |
| Spring Boot     | MVC 框架                   | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| Spring Security | 认证和授权安全框架         | [https://spring.io/projects/spring-security](https://spring.io/projects/spring-security) |
| MyBatis Plus    | ORM 框架                   | [https://mp.baomidou.com](https://mp.baomidou.com)           |
| Knife4j         | 接口文档管理框架           | [https://doc.xiaominfo.com](https://doc.xiaominfo.com)       |
| Redis           | 缓存框架                   | [https://redis.io](https://redis.io)                         |
| Lombok          | 对象封装工具               | [https://github.com/projectlombok/lombok](https://github.com/projectlombok/lombok) |
| Nginx           | Http 和反向代理 Web 服务器 | [http://nginx.org](http://nginx.org)                         |
| JustAuth        | 第三方登录工具             | [https://www.justauth.cn](https://www.justauth.cn)           |

### 前端技术

| 说明             | 官网                                                         |
| ---------------- | ------------------------------------------------------------ |
| 前端框架         | [https://vuejs.org](https://vuejs.org)                       |
| 路由框架         | [https://router.vuejs.org](https://router.vuejs.org)         |
| 全局状态管理框架 | [https://vuex.vuejs.org](https://vuex.vuejs.org)             |
| 前端 UI 框架     | [https://element-plus.gitee.io](https://element-plus.gitee.io) |
| 前端 Http 框架   | [https://github.com/axios/axios](https://github.com/axios/axios) |
| 富文本编辑器     | [https://www.wangeditor.com](https://www.wangeditor.com)     |
| Markdown 编辑器  | [http://ckang1229.gitee.io/vue-markdown-editor/zh](http://ckang1229.gitee.io/vue-markdown-editor/zh) |
| 代码语法高亮插件 | [https://github.com/highlightjs/highlight.js](https://github.com/highlightjs/highlight.js) |

## License

# 说明

## 前端

**前端框架**：**vite-vue3**

- 自动导入: [unplugin-auto-import]()
- Markdown 在线编辑器: [MdEditorV3 ](https://imzbf.github.io/md-editor-v3/zh-CN/demo#🥱 Setup 模板) 
  + 参考: [使用说明](https://blog.csdn.net/qq_16992475/article/details/130899269)
- Hook&api: [VueHook Plus](https://inhiblab-core.gitee.io/docs/hooks/guide/)
- 状态管理库: [pinia](https://pinia.vuejs.org/api/modules/pinia.html#Type-Aliases)
- UI:
  - [Element Plus ](https://element-plus.org/zh-CN/component/button.html)
  - [iconfont-阿里巴巴矢量图标库](https://www.iconfont.cn/?spm=a313x.search_index.i3.2.52c93a81WpIhXZ)
- 其他: html、css、 less、 js
- 动画: [GSAP ](https://gsap.framer.wiki/)

## 后端

**后端框架: springboot**

- 全文搜索引擎: es
- 数据库定期备份和定期删除
- 数据库: Mybatis(MySQL)
- 实时推送: WebSocket
- JMX
- 分布式相关：

  - Redis(分布式缓存)
  - Redisson(分布式锁)

- 人脸识别
- 敏感数据的保护伞——SpringBoot集成jasypt

## 其他

**部署**：Tomcat、Nginx、阿里云服务器、七牛云 CDN

**Python 相关**：百度统计的获取、Flask 提供文本分析 API

**其他**：MongoDB（目前只用来记录数据库启动）、RabbitMQ（目前只用来记录请求）、畅言

---

日志系统的建立

参考:

- 文章 4.日志系统.md

---

nginx 负载均衡

参考:

- nginx 使用

---

网站性能优化

参考:

- 网站性能优化.md

# 登录-JWT+Redis 缓存

## 接口说明

接口url：http://localhost:9000/user/login

请求方式：POST  

请求参数：

| 参数名称     | 参数类型 | 说明   |
| ------------ | -------- | ------ |
| **username** | string   | 用户名 |
| **password** | string   | 密码   |

返回数据：

~~~json
{
    "code": 200000,
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQ29udGV4dCI6IntcImlkXCI6XCIxXCIsXCJpc1N1cGVyXCI6ZmFsc2UsXCJsb25nVGVybVwiOmZhbHNlLFwibmlja05hbWVcIjpcImxmalwiLFwicm9sZVwiOjEsXCJ1c2VybmFtZVwiOlwibGZqXCJ9Iiwic3ViIjoibGZqIiwiZXhwIjoxNzEwNjgxODczfQ.njjSGM2Y_t06P6tq-rG11blawBLaf8pZvsdxVMxh9jY",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQ29udGV4dCI6IntcImlkXCI6XCIxXCIsXCJpc1N1cGVyXCI6ZmFsc2UsXCJsb25nVGVybVwiOmZhbHNlLFwibmlja05hbWVcIjpcImxmalwiLFwicm9sZVwiOjEsXCJ1c2VybmFtZVwiOlwibGZqXCJ9Iiwic3ViIjoibGZqIiwiZXhwIjoxNzExMzczMDczfQ.wbkRtLAyuXbeCGXTkU6g1wCxNVw9IeVqV8NlYl6mkfA"
    },
    "message": "响应成功"
}
~~~

## Token 实体类

```java
package com.mszlu.shop.common.security;

import lombok.Data;

/**
 * Token 实体类
 */
@Data
public class Token {
    /**
     * 访问token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

}
```

## 用户表

```sql
-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `gender` char(255) NOT NULL,
  `trueName` varchar(255) DEFAULT NULL,
  `birthday` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `personalBrief` varchar(255) DEFAULT NULL,
  `avatarImgUrl` text NOT NULL,
  `recentlyLanded` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '19940790216', '张海洋', 'a3caed36f0fe5a01e5f144db8927235e', 'male', '张海洋', '1997-07-05', '1125694337@qq.com', '', 'https://zhy-myblog.oss-cn-shenzhen.aliyuncs.com/public/user/avatar/张海洋/1575283189.png', '2019-12-02 18:31:15');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `User_id` int(11) NOT NULL,
  `Role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('1', '2');
INSERT INTO `user_role` VALUES ('1', '3');

```

## 核心实现

```java
	public ResponseResult<Token> usernameLogin(String username, String password) {
		/**
		 1. 根据用户名查找Member信息
		 2. 如果为null，就是用户不存在
		 3. 密码进行匹配，如果不匹配 密码不正确
		 4. token 生成token
		 5. jwt 生成token, token放入redis当中，accessToken 过期短， refreshToken 过期长
		 **/
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
				.eq(User::getUsername, username);
		List<User> userList = this.list(queryWrapper);

		User user = userList.get(0);
		if (ObjectUtils.isEmpty(user)) {
			return ResponseResult.accountNotFoundError();
		}

		//用的security的密码类
		if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
			return ResponseResult.accountError();
		}

		//一般会登录的时候，记录 用户的最后一次登录时间
		//MQ 考虑使用mq 把信息发到mq当中，由mq的消费者 来去更新1
		user.setRecentlylanded(LocalDateTime.now());
		this.updateById(user);
		Token token = genToken(user);
		return ResponseResult.success(token);
	}

	private Token genToken(User user) {
		Token token = new Token();
		// token存放信息
		AuthUser authUser = new AuthUser(user.getUsername(), String.valueOf(user.getId()),
				user.getUsername(), UserEnums.USER);
		// 7天
		String accessToken = TokenUtils.createToken(user.getUsername(), authUser, 7 * 24 * 60 * 60 * 1000L);
		token.setAccessToken(accessToken);
		redisTemplate.opsForValue().set(CachePrefix.ACCESS_TOKEN.name() + UserEnums.MEMBER.name() + jwtToken, "1", 7, TimeUnit.DAYS);
		// 15天
		//设置刷新token，当accessToken过期的时候，可以通过refreshToken来 重新获取accessToken 而不用访问数据库
		String refreshToken = TokenUtils.createToken(user.getUsername(), authUser, 15 * 24 * 60 * 60 * 1000L);
		token.setRefreshToken(refreshToken);
		redisTemplate.opsForValue().set(CachePrefix.REFRESH_TOKEN.name() + UserEnums.MEMBER.name() + jwtToken, "1", 15, TimeUnit.DAYS);

		return token;
	}
```

### 所需技术

#### ①spring security  —— 加密及判断密码是否相同

```XML
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.jasypt</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
</dependency>
```

数据存入[数据库](https://so.csdn.net/so/search?q=数据库&spm=1001.2101.3001.7020)库时

```JAVA
public static void main(String[] args) {
System.out.println(new BCryptPasswordEncoder().encode("123456"));
}
```

当需要登录时，可以用下面这行代码进行解密

```java
LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
    .eq(User::getUsername, username);
List<User> userList = this.list(queryWrapper);

User user = userList.get(0);
if (ObjectUtils.isEmpty(user)) {
    return ResponseResult.accountNotFoundError();
}

//用的security的密码类
if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
    return ResponseResult.accountError();
}
```

#### ②JWT  —— 生成token

具体见:      <img src="README.assets/image-20240310212945690.png" alt="image-20240310212945690" style="zoom:67%;" />

依赖:

```xml
<!--   token加密 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.12.3</version>
</dependency>
<!--   token加密结束 -->
```

#### ③Redis  —— 缓存

就是Redis缓存知识

```
redisTemplate.opsForValue().set(CachePrefix.REFRESH_TOKEN.name() + UserEnums.MEMBER.name() + jwtToken, "1", 15, TimeUnit.DAYS);
```

## 测试

![image-20240310210444090](README.assets/image-20240310210444090.png)

#  登录认证







# 表设计

## 用户表：user

| 名称           | 类型    | 长度 | 主键  | 非空  | 描述         |
| -------------- | ------- | ---- | ----- | ----- | ------------ |
| id             | int     | 11   | true  | true  | 主键，自增   |
| phone          | varchar | 255  | false | true  | 手机号       |
| username       | varchar | 255  | false | true  | 用户名       |
| password       | varchar | 255  | false | true  | 密码         |
| gender         | char    | 50   | false | true  | 性别         |
| trueName       | varchar | 255  | false | false | 姓名         |
| birthday       | char    | 100  | false | false | 生日         |
| email          | varchar | 255  | false | false | 邮箱         |
| personalBrief  | varchar | 255  | false | false | 个人简介     |
| avatarImgUrl   | varchar | 255  | false | true  | 头像url      |
| recentlyLanded | varchar | 255  | false | false | 最近登录时间 |
