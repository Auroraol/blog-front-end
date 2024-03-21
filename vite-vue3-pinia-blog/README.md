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

## 用户实体类

```java
package com.lfj.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 *
	 */
	@TableField(value = "phone")
	private String phone;
	/**
	 *
	 */
	@TableField(value = "username")
	private String username;
	/**
	 *
	 */
	@TableField(value = "password")
	private String password;
	/**
	 *
	 */
	@TableField(value = "gender")
	private String gender;
	/**
	 *
	 */
	@TableField(value = "trueName")
	private String truename;
	/**
	 *
	 */
	@TableField(value = "birthday")
	private String birthday;
	/**
	 *
	 */
	@TableField(value = "email")
	private String email;
	/**
	 *
	 */
	@TableField(value = "personalBrief")
	private String personalbrief;
	/**
	 *
	 */
	@TableField(value = "avatarImgUrl")
	private String avatarimgurl;
	/**
	 *
	 */
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "recentlyLanded")
	private LocalDateTime recentlylanded;
}
```



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

## Token 实体类

### Token 实体类

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

### Token 实体类存放的信息

```java
package com.lfj.blog.common.security;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 17:32
 *  存储到Token中的信息
 */
@Data
@AllArgsConstructor
public class AuthUser implements Serializable {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * id
	 */
	private String id;

	/**
	 * 长期有效（用于手机app登录场景或者信任场景等）
	 */
	private Boolean longTerm = false;

	/**
	 * @see UserEnums
	 * 角色
	 */
	private UserEnums role;


	/**
	 * 是否是超级管理员
	 */
	private Boolean isSuper = false;

	public AuthUser(String username, String id, String nickName, UserEnums role) {
		this.username = username;
		this.id = id;
		this.role = role;
		this.nickName = nickName;
	}

	public AuthUser(String username, String id, UserEnums manager, String nickName, Boolean isSuper) {
		this.username = username;
		this.id = id;
		this.role = manager;
		this.isSuper = isSuper;
		this.nickName = nickName;
	}
}

```

## 核心实现

![image-20240311164152147](README.assets/image-20240311164152147.png)

```java
package com.lfj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfj.blog.common.cache.CachePrefix;
import com.lfj.blog.common.security.AuthUser;
import com.lfj.blog.common.security.Token;
import com.lfj.blog.common.security.UserEnums;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.entity.User;
import com.lfj.blog.mapper.UserMapper;
import com.lfj.blog.service.UserService;
import com.lfj.blog.utils.token.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * @author 16658
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-03-09 17:46:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
		implements UserService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}

	@Override
	public ResponseResult<Token> usernameLogin(String username, String password) {
		/**
		 1. 根据用户名查找Member信息
		 2. 如果为null，就是用户不存在
		 3. 密码进行匹配，如果不匹配 密码不正确
		 4. jwt 生成token
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
		String jwtAccessToken = TokenUtils.createToken(user.getUsername(), authUser, 7 * 24 * 60 * 60 * 1000L);
		token.setAccessToken(jwtAccessToken);
		redisTemplate.opsForValue().set(CachePrefix.ACCESS_TOKEN.name() + UserEnums.USER.name() + jwtAccessToken
				,"1", 7, TimeUnit.DAYS); // 储存到Redis中 前缀+用户类型+jwtToken

		// 15天
		//设置刷新token，当accessToken过期的时候，可以通过refreshToken来 重新获取accessToken 而不用访问数据库
		String jwtRefreshToken = TokenUtils.createToken(user.getUsername(), authUser, 15 * 24 * 60 * 60 * 1000L);
		token.setRefreshToken(jwtRefreshToken);
		redisTemplate.opsForValue().set(CachePrefix.REFRESH_TOKEN.name() + UserEnums.USER.name() + jwtRefreshToken
				,"1", 15, TimeUnit.DAYS);

		return token;
	}



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

参考: [JWT令牌生成和解析（库：jjwt 版本：0.12.3）](https://blog.csdn.net/qq_45137726/article/details/135885870)

#### ③Redis  —— 缓存

就是Redis缓存知识

```java
redisTemplate.opsForValue().set(CachePrefix.REFRESH_TOKEN.name() + UserEnums.USER.name() + "jwtRefreshToken", "1", 15, TimeUnit.DAYS);
```

## 测试结果

![image-20240310210444090](README.assets/image-20240310210444090.png)



#  登录认证

![image-20240311172134694](README.assets/image-20240311172134694.png)

##  导包

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
~~~

## security配置类

~~~java
package com.lfj.blog.config.security;

import com.lfj.blog.handler.security.CustomAccessDeniedHandler;
import com.lfj.blog.handler.security.UserAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @Author: LFJ
 * @Date: 2024-03-10 22:55
 * security配置类
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 忽略验权配置
	 */
	@Autowired
	private IgnoredUrlsProperties ignoredUrlsProperties;

	/**
	 * spring security 权限不足处理
	 */
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
				.authorizeRequests();
		//配置的url 不需要授权
		for (String url : ignoredUrlsProperties.getUrls()) {
			registry.antMatchers(url).permitAll();
		}
		registry
				.and()
				//禁止网页iframe
				.headers().frameOptions().disable()
				.and()
				.logout()
				.permitAll()
				.and()
				.authorizeRequests()
				//任何请求
				.anyRequest()
				//需要身份认证
				.authenticated()
				.and()
				//允许跨域
				.cors().and()
				//关闭跨站请求防护
				.csrf().disable()
				//前后端分离采用JWT 不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				//自定义权限拒绝处理类
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.and()
				//添加JWT认证过滤器(自定义)
				.addFilter(new UserAuthenticationFilter(authenticationManager(), redisTemplate));
	}

}
~~~

## 忽略配置类

~~~java
package com.lfj.blog.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LFJ
 * @Date: 2024-03-10 22:57
 */
@Configuration
@ConfigurationProperties(prefix = "ignored")
@Data
public class IgnoredUrlsProperties {
	private List<String> urls = new ArrayList<>();
}

~~~

使用:   在配置文件写不用进行token认证的请求路径

~~~yml
ignored:
  urls:
    - /pages/**
    - /pageData/**
    - /article/**
    - /goods/**
    - /members/**
    - /category/**
    - /common/**
~~~

## 认证过滤器

~~~java
package com.lfj.blog.utils.token;

/**
 * @Author: LFJ
 * @Date: 2024-03-09 15:24
 */
public class SecurityKey {
	public static final String USER_CONTEXT = "userContext";

	public static final String ACCESS_TOKEN = "accessToken";
}
~~~

实现

~~~java
package com.lfj.blog.handler.security;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lfj.blog.common.cache.CachePrefix;
import com.lfj.blog.common.security.AuthUser;
import com.lfj.blog.common.security.UserEnums;
import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.utils.token.SecretKeyUtil;
import com.lfj.blog.utils.token.SecurityKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.lfj.blog.utils.ResponseUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: LFJ
 * @Date: 2024-03-10 22:59
 *
 * 认证结果过滤器
 *
 */
@Slf4j
public class UserAuthenticationFilter extends BasicAuthenticationFilter {
	private StringRedisTemplate redisTemplate;

	/**
	 * 自定义构造器
	 *
	 * @param authenticationManager
	 */
	public UserAuthenticationFilter(AuthenticationManager authenticationManager
			, StringRedisTemplate redisTemplate) {
		super(authenticationManager);
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {

		//从header中获取jwt
		String jwt = request.getHeader(SecurityKey.ACCESS_TOKEN);
		try {
			//如果没有token 则return
			if (StringUtils.isBlank(jwt)) {
				chain.doFilter(request, response);
				return;
			}
			//获取用户信息，存入context
			UsernamePasswordAuthenticationToken authentication = getAuthentication(jwt, response);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			log.error("BuyerAuthenticationFilter-> member authentication exception:", e);
		}
		chain.doFilter(request, response);
	}

	/**
	 * 解析用户
	 * 读取Token信息，创建UsernamePasswordAuthenticationToken对象
	 * @param jwt
	 * @param response
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(String jwt, HttpServletResponse response) {

		try {
			Claims claims = Jwts.parser()
					.verifyWith(SecretKeyUtil.generalKey()) // 传递密钥
					.build()
					.parseSignedClaims(jwt)//传递jwt令牌参数
					.getPayload();  // 获取- Payload(有效载荷）
			//获取存储在claims中的用户信息
			String json = claims.get(SecurityKey.USER_CONTEXT).toString();
			AuthUser authUser = JSON.parseObject(json, AuthUser.class);

			//校验redis中是否有权限
			Boolean hasKey = redisTemplate.hasKey(CachePrefix.ACCESS_TOKEN.name() + UserEnums.USER.name() + jwt);
			if (hasKey != null && hasKey) {
				//构造返回信息
				List<GrantedAuthority> auths = new ArrayList<>();
				auths.add(new SimpleGrantedAuthority("ROLE_" + authUser.getRole().name()));
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser.getUsername(), null, auths);
				authentication.setDetails(authUser);
				return authentication;
			}
			ResponseUtil.output(response, 401001, ResponseResult.noLogin());
			return null;
		} catch (ExpiredJwtException e) {
			log.debug("user analysis exception:", e);
		} catch (Exception e) {
			log.error("user analysis exception:", e);
		}
		return null;
	}
}
~~~

##  认证失败的返回权限不足

~~~java
package com.lfj.blog.handler.security;

import com.lfj.blog.common.vo.ResponseResult;
import com.lfj.blog.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author: LFJ
 * @Date: 2024-03-10 23:13
 * 认证失败的返回
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
		ResponseUtil.output(httpServletResponse, ResponseResult.noPermission());
	}
}
~~~

## 工具类

**作用:  用于任何地方发起响应数据**

~~~java
package com.lfj.blog.utils;

import com.alibaba.fastjson2.JSON;
import com.lfj.blog.common.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: LFJ
 * @Date: 2024-03-10 23:14
 * response 输出响应工具, 用于任何地方发起响应数据, 不需要PostMapping()等等
 */
@Slf4j
public class ResponseUtil {

	static final String ENCODING = "UTF-8";
	static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	/**
	 * 输出前端内容以及状态指定
	 *
	 * @param response
	 * @param status
	 * @param content
	 */
	public static void output(HttpServletResponse response, Integer status, String content) {
		ServletOutputStream servletOutputStream = null;
		try {
			response.setCharacterEncoding(ENCODING);
			response.setContentType(CONTENT_TYPE);
			response.setStatus(status);
			servletOutputStream = response.getOutputStream();
			servletOutputStream.write(content.getBytes());
		} catch (Exception e) {
			log.error("response output error: ", e);
		} finally {
			if (servletOutputStream != null) {
				try {
					servletOutputStream.flush();
					servletOutputStream.close();
				} catch (IOException e) {
					log.error("response output IO close error:", e);
				}
			}
		}
	}


	/**
	 * response 输出JSON
	 *
	 * @param response
	 * @param status    response 状态
	 * @param result
	 */
	public static void output(HttpServletResponse response, Integer status, ResponseResult result) {
		response.setStatus(status);
		output(response, result);
	}


	/**
	 * response 输出JSON
	 *
	 * @param response
	 * @param result
	 */
	public static void output(HttpServletResponse response, ResponseResult result) {
		ServletOutputStream servletOutputStream = null;
		try {
			response.setCharacterEncoding(ENCODING);
			response.setContentType(CONTENT_TYPE);
			servletOutputStream = response.getOutputStream();
			servletOutputStream.write(JSON.toJSONString(result).getBytes());
		} catch (Exception e) {
			log.error("response output error:", e);
		} finally {
			if (servletOutputStream != null) {
				try {
					servletOutputStream.flush();
					servletOutputStream.close();
				} catch (IOException e) {
					log.error("response output IO close error:", e);
				}
			}
		}
	}
}
~~~

## 测试结果

**情况1:  请求头没带token, 返回空**

![image-20240311170523945](README.assets/image-20240311170523945.png)

**情况2:  请求头带过期token, 返回登录已失效，请重新登录**



删除Redis



**情况2:  请求头带有效token, 返回成功响应数据**

![image-20240311191415030](README.assets/image-20240311191415030.png)





# 更新昵称

![image-20240315233101784](README.assets/image-20240315233101784.png)

![image-20240315233045253](README.assets/image-20240315233045253.png)



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





# 个人中心

![image-20240318225848609](README.assets/image-20240318225848609.png)





# 文章

## 使用md-editor-v3

写文章

```vue
<template>
  <MdEditor v-model="text" previewTheme="vuepress" codeTheme="a11y"/>
</template>

<script setup>
import { ref } from 'vue';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

const text = ref('Hello Editor!');
</script>
```

看文章

```vue
<template>
	<!--文章内容-->
  <MdPreview :editorId="id" :modelValue="text" previewTheme="vuepress" codeTheme="a11y"/>
	<!--文章目录-->
  <MdCatalog :editorId="id" :scrollElement="scrollElement" />
</template>

<script setup>
import { ref } from 'vue';
import { MdPreview, MdCatalog } from 'md-editor-v3';
// preview.css相比style.css少了编辑器那部分样式
import 'md-editor-v3/lib/preview.css';

const id = 'preview-only';
const text = ref(`
# Hello Editor
# Hello 
This is a sample Markdown content. You can use Markdown syntax to format text, such as:

- **Bold text**
- *Italic text*
- Code blocks

\`\`\`javascript
function greet(name) {
    return 'Hello, ' + name + '!';
}
\`\`\`
## 第一
Enjoy writing in Markdown!
## 第二
### 一
`);
    
//md-catalog目录的监听设置    
const scrollElement = document.documentElement;
</script>
```

**效果**

![image-20240319172604269](README.assets/image-20240319172604269.png)

