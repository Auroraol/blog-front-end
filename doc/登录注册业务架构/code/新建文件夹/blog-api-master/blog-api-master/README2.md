# 分析

## 发送验证码

请求方法：POST

请求地址：/sms/send

请求参数：mobile：手机号，必传

需要access_token： 否

需要管理员权限： 否

接口说明：默认情况下，验证码5分钟内有效，同一手机号每天只能发10次；同一ip每天只能发10次；同一手机号限流120s一次。

> Tip
>
> 可以手动设置验证码到redis中，类型为Strig，key为sms:code:15649xxx56，值设置你想设置的验证码。 接口限流基于redis实现，key前缀为limit:sms:，删除redis对应的缓存即可去掉本次限流。

**响应成功：**

```json
{  
    "code": 0,  
    "message": "成功"
}
```

后端

```JAVA
@Log4j2
@RestController
@RequestMapping("/sms")
@Api(tags = "短信验证码服务",value = "/sms")
public class SmsController extends BaseController{

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    @RateLimiter(name = RedisConstant.SMS_LIMIT_NAME,max = 1,key = "#mobile", timeout = 120L, extra = "smsLimiter")
    @ApiOperation(value = "发送短信验证码",notes = "验证码有效时5分钟;同一手机号每天只能发10次;同一ip每天只能发10次;同一手机号限流120s一次")
    public ApiResponse sendSmsCode(@ApiParam("手机号") @NotNull @IsPhone @RequestParam long mobile) {
        smsCodeService.sendSmsCode(mobile);
        return createResponse();
    }
}
```

前端

```js

/**
 * 发送验证码
 * @param {Object} params
 */
export function sendCode(params) {
  return request({
    url: '/sms/send',
    method: 'post',
    params: params
  })
}
```



## 客户端 

系统用户认证区分客户端

+ pc网页端
+ 安卓客户端
+ ios客户端

各客户端可自定义设置是否支持refresh_token，自定义设置登录超时时间等

### 数据库客户端表设计

|        字段名        |                描述                |
| :------------------: | :--------------------------------: |
|          id          |                 id                 |
|      client_id       |              客户端id              |
|    client_secret     |             客户端秘钥             |
| access_token_expire  |        access_token有效时长        |
| refresh_token_expire |    refresh_token_expire有效时长    |
| enable_refresh_token | 是否启用 refresh_token，1:是，0:否 |

```sql
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `client_id` varchar(50) NOT NULL COMMENT '客户端id，客户端唯一标识',
  `client_secret` varchar(255) NOT NULL COMMENT '客户端密码',
  `access_token_expire` bigint(20) DEFAULT NULL COMMENT 'access_token有效时长',
  `refresh_token_expire` bigint(20) DEFAULT NULL COMMENT 'refresh_token_expire有效时长',
  `enable_refresh_token` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否支持刷新refresh_token,1:是，0:否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_client_id` (`client_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='客户端表';

```

### 保存客户端

#### API 接口

请求方法：POST

请求地址：/client/save

请求数据格式：application/json

需要access_token： 是

需要管理员权限： 是

**请求体数据格式：**

```json
{
    "id": "id",
    "clientId": "客户端id, 唯一",
    "clientSecret": "客户端秘钥",
    "accessTokenExpire": "access_Token有效时间(s)",
    "refreshTokenExpire": "refresh_token有效时间(s)",
    "enableRefreshToken": "是否启用refresh_token,1:是，0:否"
}
```

接口说明：

+ id为null则新增，id不为null则更新

+ 超时时间单位为秒(小时: 60L * 60 * n,   天: 24 * 60L * 60 * n )
+ 客户端id和客户端秘钥不能为空

**保存成功：**

```json
{
    "code": 200000,
    "message": "响应成功"
}
```

#### Postman

```
POST http://localhost:9000/client/save
```

**携带access_token**

![image-20240326190546780](README2.assets/image-20240326190546780.png)

**id为不null时更新**

![image-20240324163405346](README2.assets/image-20240324163405346.png)

保存到数据库中

<img src="README2.assets/image-20240324163414872.png" style="zoom:90%;" />

**id为null时新增**

![image-20240324163441829](README2.assets/image-20240324163441829.png)

### 删除客户端

#### API 接口

请求方法：DELETE

请求地址：/client/delete/{id}

需要access_token： 是

需要管理员权限： 是

接口说明：物理删除。

**删除成功：**

```json
{
    "code": 200000,
    "message": "响应成功"
}
```

#### Postman

```
DELETE http://localhost:9000/client/delete/1
```

**携带access_token**

![image-20240326190553522](README2.assets/image-20240326190553522.png)

**删除成功**

![image-20240324162535047](README2.assets/image-20240324162535047.png)

**删除失败**

![image-20240324162547096](README2.assets/image-20240324162547096.png)

### 分页获取客户端

#### API 接口

请求方法：GET

请求地址：/client/page

请求参数：
current：当前页，非必传，默认1
size：每页数量，非必传，默认5

需要access_token： 是

需要管理员权限： 是

**获取成功：**

```json
{
    "code": 200000,
    "data": {
        "records": [
            {
                "id": 1,
                "clientId": "1",
                "clientSecret": "客户端秘钥",
                "accessTokenExpire": 7,
                "refreshTokenExpire": 15,
                "enableRefreshToken": 1
            }
        ],
        "total": 2,
        "size": 1,
        "current": 1,
        "orders": [],
        "optimizeCountSql": true,
        "searchCount": true,
        "maxLimit": null,
        "countId": null,
        "pages": 2
    },
    "message": "响应成功"
}
```

#### Postman

```
GET http://localhost:9000/client/page?current=1&size=1
```

**携带access_token**

![image-20240326190610638](README2.assets/image-20240326190610638.png)

**需要管理员权限： 是**

![image-20240321232131572](README2.assets/image-20240321232131572.png)

**需要管理员权限： 否**

![image-20240326184401110](README2.assets/image-20240326184401110.png)

## 短信发送

具体代码见:  

<img src="README2.assets/image-20240324132814183.png" alt="image-20240324132814183" style="zoom:67%;" />

### API 接口

```
POST http://localhost:9000/sms/send
```

![image-20240323153651633](README2.assets/image-20240323153651633.png)

## 认证

### 认证区分客户端

登录、退出、刷新access_token接口都需要在请求头带上客户端信息。

例子： `Authorization: Basic cGM6MTIzNDU2`

这里的`cGM6MTIzNDU2`为pc:123456经过base64加密后的密文。pc为clientId，123456为clientSecret。

![image-20240324163826181](README2.assets/image-20240324163826181.png)

### 账号密码登录

#### API 接口

请求方法：POST

请求地址：/account/login

请求参数：
username：用户名/手机号，必传
password：密码，必传

需要access_token： 否

需要管理员权限： 否

需要客户端认证： 是

接口说明：账号可以是用户名，也可以是手机号。

**登录成功：**

```json
{
    "code": 200000,
    "data": {
        "access_token": "fdbb3720-1a1d-4b90-935b-4c6d46029d80",
        "token_type": "Bearer",
        "refresh_token": "62385ed4-b184-4bf5-be18-f8762fafd5a4"
    },
    "message": "响应成功"
}
```

#### Postman

![image-20240324163829926](README2.assets/image-20240324163829926.png)

![image-20240324163603022](README2.assets/image-20240324163603022.png)

**登录成Redis储存**

<img src="README2.assets/image-20240326200510361.png" alt="image-20240326200510361" style="zoom: 67%;" />

### 手机号验证码登录

#### API 接口

请求方法：POST

请求地址：/mobile/login

请求参数：
mobile：手机号，必传
code：验证码，必传

需要access_token： 否

需要管理员权限： 否

需要客户端认证： 是

接口说明：验证码调发送验证码接口获取。

**登录成功：**

```json
{
    "code": 200000,
    "data": {
        "access_token": "fdbb3720-1a1d-4b90-935b-4c6d46029d80",
        "token_type": "Bearer",
        "refresh_token": "62385ed4-b184-4bf5-be18-f8762fafd5a4"
    },
    "message": "响应成功"
}
```

#### Postman

![image-20240326210832847](README2.assets/image-20240326210832847.png)

### 第三方登录

#### API 接口

请求方法：POST

请求地址：/oauth

请求参数：
type：类型，必传，1:qq，2:github，3:gitee
code：认证code，必传

需要access_token： 否

需要管理员权限： 否

需要[客户端认证](https://copoile.github.io/api/auth/)： 是

**接口说明：这里的code是第三方登录回调获取到的code。**

**登录成功：**

```json
{
  "code": 0,
  "message": "成功",
  "data": {
    "access_token": "6a2dc65f-74f6-4eb6-b4d2-2eba2af4f587",
    "token_type": "Bearer",
    "refresh_token": "17456dd5-5dbd-41ec-ad4f-7cf101c9ab3e"
  }
}
```

#### Postman

gitee

第三方回调

![image-20240326231433150](README2.assets/image-20240326231433150.png)

第三方登录接口

![image-20240326224817375](README2.assets/image-20240326224817375.png)



### 刷新access_token

请求方法：POST

请求地址：/refresh_access_token

请求参数：refresh_token：refresh_token，必传

需要access_token： 否

需要管理员权限： 否

需要[客户端认证](https://copoile.github.io/api/auth/)： 是

接口说明：需要客户的启用refresh_token；access_token过期时间一般比refresh_token短，使用refresh_token刷新access_token可避免频繁登录。

**刷新成功：**

```json
{
  "code": 0,
  "message": "成功",
  "data": {
   "access_token": "6a2dc65f-74f6-4eb6-b4d2-2eba2af4f587",
   "token_type": "Bearer",
   "refresh_token": "17456dd5-5dbd-41ec-ad4f-7cf101c9ab3e"
  }
}
```

### 退出

请求方法：DELETE

请求地址：/logout

请求参数：access_token：access_token，必传

需要access_token： 否

需要管理员权限： 否

需要[客户端认证](https://copoile.github.io/api/auth/)： 是

接口说明：这里的access_token以请求参数形式传递而非请求头形式。

**退出成功：**

```json
{
  "code": 0,
  "message": "成功"
}
```



### 综合

```java
package cn.poile.blog.controller;

import cn.poile.blog.biz.OauthService;
import cn.poile.blog.common.constant.ErrorEnum;
import cn.poile.blog.common.exception.ApiException;
import cn.poile.blog.common.response.ApiResponse;
import cn.poile.blog.common.security.AuthenticationToken;
import cn.poile.blog.common.security.RedisTokenStore;
import cn.poile.blog.common.sms.SmsCodeService;
import cn.poile.blog.common.validator.annotation.IsPhone;
import cn.poile.blog.controller.model.dto.AccessTokenDTO;
import cn.poile.blog.entity.Client;
import cn.poile.blog.service.AuthenticationService;
import cn.poile.blog.service.IClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author: yaohw
 * @create: 2019-10-25 10:55
 **/
@RestController
@Log4j2
@Api(tags = "认证服务", value = "/")
public class AuthenticationController extends BaseController {

    private static final String TOKEN_TYPE = "Bearer";

    private static final String AUTHORIZATION_TYPE = "Basic";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTokenStore tokenStore;

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private IClientService clientService;


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OauthService oauthService;

    @PostMapping("/account/login")
    @ApiOperation(value = "账号密码登录", notes = "账号可以是用户名或手机号")
    public ApiResponse<AccessTokenDTO> accountLogin(@ApiParam("用户名或手机号") @NotBlank(message = "账号不能为空") @RequestParam String username,
                                                    @ApiParam("密码") @NotBlank(message = "密码不能为空") @RequestParam String password,
                                                    @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = authenticationService.usernameOrMobilePasswordAuthenticate(username, password, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return createResponse(response);
    }

    @PostMapping("/mobile/login")
    @ApiOperation(value = "手机号验证码登录", notes = "验证码调用发送验证码接口获取")
    public ApiResponse<AccessTokenDTO> mobileLogin(@ApiParam("手机号") @NotNull(message = "手机号不能为空") @IsPhone @RequestParam long mobile,
                                                   @ApiParam("手机号验证码") @NotBlank(message = "验证码不能为空") @RequestParam String code,
                                                   @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = authenticationService.mobileCodeAuthenticate(mobile, code, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return createResponse(response);
    }

    @PostMapping("/oauth")
    @ApiOperation(value = "第三方登录", notes = "不需要accessToken")
    public ApiResponse<AccessTokenDTO> oauth(
            @ApiParam("认证类型") @NotNull(message = "认证类型不能为空") @RequestParam Integer type,
            @ApiParam("第三方授权码") @NotBlank(message = "授权码不能为空") @RequestParam String code,
            @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = oauthService.oauth(type, code, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return createResponse(response);
    }

    @DeleteMapping("/logout")
    @ApiOperation(value = "用户登出")
    public ApiResponse logout(@RequestHeader(value = "Authorization") String authorization,
                              @ApiParam("access_token") @RequestParam("access_token") String accessToken) {
        Client client = getAndValidatedClient(authorization);
        authenticationService.remove(accessToken, client);
        return createResponse();
    }

    @PostMapping("/refresh_access_token")
    @ApiOperation(value = "刷新accessToken")
    public ApiResponse<AccessTokenDTO> refreshAccessToken(
            @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization,
            @ApiParam("refresh_token") @NotBlank(message = "refresh_token不能为空") @RequestParam("refresh_token") String refreshToken) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = authenticationService.refreshAccessToken(refreshToken, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return createResponse(response);
    }

    /**
     * 获取并校验client
     *
     * @param authorization
     * @return
     */
    private Client getAndValidatedClient(String authorization) {
        String[] clientIdAndClientSecret = extractClientIdAndClientSecret(authorization);
        String clientId = clientIdAndClientSecret[0];
        String clientSecret = clientIdAndClientSecret[1];
        Client client = clientService.getClientByClientId(clientId);
        if (client == null || !clientSecret.equals(client.getClientSecret())) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST.getErrorCode(), "无效客户端");
        }
        return client;
    }

    /**
     * 提取客户端id和客户端密码
     *
     * @param authorization
     * @return
     */
    private String[] extractClientIdAndClientSecret(String authorization) {
        if (!authorization.startsWith(AUTHORIZATION_TYPE)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST.getErrorCode(), "无效客户端");
        }
        String base64Data = authorization.substring(6);
        try {
            String data = new String(Base64.decodeBase64(base64Data));
            String separator = ":";
            String[] split = data.split(separator);
            int length = split.length;
            int matched = 2;
            if (length != matched) {
                throw new ApiException(ErrorEnum.INVALID_REQUEST.getErrorCode(), "无效客户端");
            }
            return split;
        } catch (Exception e) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST.getErrorCode(), "无效客户端");
        }
    }
}

```





## 用户

系统用户

+ 系统管理员:   拥有全部权限
+ 普通用户:  拥有评论、留言等权限

### 数据库用户表设计

|   字段名    |                   描述                   |
| :---------: | :--------------------------------------: |
|     id      |                    id                    |
|  username   |                  用户名                  |
|  password   |                   密码                   |
|   mobile    |                  手机号                  |
|  nickname   |                   昵称                   |
|   gender    |           性别，1：男 ，0：女            |
|  birthday   |                   生日                   |
|    email    |                   邮箱                   |
|    brief    |                 个性签名                 |
|   avatar    |                   头像                   |
|   status    | 状态，0：正常，1：锁定，2：禁用，3：过期 |
|    admin    |         是否管理员，1：是，0：否         |
| create_time |                 注册时间                 |

### 用户注册

#### API 接口

请求方法：POST

请求地址：/user/register

请求数据格式：application/json

需要access_token： 否

需要管理员权限： 否

请求体json：

```json
{
  "code": "验证码",
  "mobile": "手机号",
  "password": "密码",
  "username": "用户名"
}
```

接口说明：

+ 验证码调发送验证码接口获取；
+ 用户名只能字母开头，允许2-16字节，允许字母数字下划线；密码不能少于6位数。
+ 默认创建普通用户

注册成功：

```json
{  
    "code": 0,  
    "message": "成功"
}
```

#### Postman

```
POST http://localhost:9000/user/register
```

1、发起短信

![image-20240324151030647](README2.assets/image-20240324151030647.png)

<img src="README2.assets/image-20240324151112439.png" alt="image-20240324151112439" style="zoom: 33%;" />

2、用户注册

![image-20240324151023627](README2.assets/image-20240324151023627.png)

3、查看数据库

![image-20240324151014768](README2.assets/image-20240324151014768.png)

