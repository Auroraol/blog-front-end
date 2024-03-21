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

### 保存客户端

请求方法：POST

请求地址：/client/save

请求数据格式：application/json

需要access_token： 是

需要管理员权限： 是

**请求体数据格式：**

```json
{
  "accessTokenExpire": "access_Token有效时间",
  "clientId": "客户端id",
  "clientSecret": "客户端秘钥",
  "id": "id",
  "refreshTokenExpire": "refresh_token有效时间",
  "enableRefreshToken": "是否启用refresh_token,1:是，0:否"
}
```

接口说明：

+ id为null则新增，id不为null则更新

+ 超时时间单位为秒
+ 客户端id和客户端秘钥不能为空

**保存成功：**

```json
{  
    "code": 0,  
    "message": "成功"
}
```

后端

```java
    @PostMapping("/save")
    @ApiOperation(value = "新增或更新客户端,id为null时新增",notes = "需要accessToken，需要管理员权限")
    public ApiResponse save(@Validated @RequestBody Client client) {
        validateExist(client);
        clientService.saveOrUpdate(client);
        clientService.clearCache();
        return createResponse();
    }
```

前端

```js
/**
 * 保存客户端
 * @param {Object} data
 */
export function saveClient(data) {
  return request.post('/client/save', data)
}
```

### 删除客户端

请求方法：DELETE

请求地址：/client/delete/{id}

需要access_token： 是

需要管理员权限： 是

接口说明：物理删除。

**删除成功：**

```json
{  
    "code": 0,  
    "message": "成功"
}
```

后端

```java
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除客户端",notes = "需要accessToken，需要管理员权限")
    public ApiResponse delete(@ApiParam("id") @PathVariable(value = "id") int id) {
        clientService.removeById(id);
        clientService.clearCache();
        return createResponse();
    }
```

前端

```js
/**
 * 删除客户端
 * @param {Object} id
 */
export function deleteClient(id) {
  return request.delete('/client/delete/' + id)
}
```

### 分页获取客户端

请求方法：GET

请求地址：/client/page

请求参数：
current：当前页，非必传，默认1
size：每页数量，非必传，默认5

需要access_token： 是

需要管理员权限： 是

获取成功：

```json
{
  "code": 0,
  "message": "成功",
  "data": {
    "records": [
      {
        "id": 1,
        "clientId": "pc",
        "clientSecret": "123456",
        "accessTokenExpire": 7200,
        "refreshTokenExpire": 2592000,
        "enableRefreshToken": 1
      },
      {
        "id": 2,
        "clientId": "test",
        "clientSecret": "test",
        "enableRefreshToken": 0
      }
    ],
    "total": 2,
    "size": 5,
    "current": 1,
    "searchCount": true,
    "pages": 1
  }
}
```

后端

```java
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "分页获取客户端列表",notes = "需要accessToken，需要管理员权限")
    public ApiResponse<IPage<Client>> page(@ApiParam("页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
                                           @ApiParam("每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
        return createResponse(clientService.page(new Page<>(current,size)));
    }
```

前端

```js
/**
 * 分页获取客户端列表
 * @data {Object} data
 */
export function pageClient(params) {
  return request.get('/client/page', { params })
}
```

### 综合

后端

```java
package cn.poile.blog.controller;


import cn.poile.blog.common.constant.ErrorEnum;
import cn.poile.blog.common.exception.ApiException;
import cn.poile.blog.common.response.ApiResponse;
import cn.poile.blog.entity.Client;
import cn.poile.blog.service.IClientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 客户端表 前端控制器
 * </p>
 *
 * @author yaohw
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/client")
@Api(tags = "客户端服务",value = "/client")
public class ClientController extends BaseController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "分页获取客户端列表",notes = "需要accessToken，需要管理员权限")
    public ApiResponse<IPage<Client>> page(@ApiParam("页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
                                           @ApiParam("每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
        return createResponse(clientService.page(new Page<>(current,size)));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除客户端",notes = "需要accessToken，需要管理员权限")
    public ApiResponse delete(@ApiParam("id") @PathVariable(value = "id") int id) {
        clientService.removeById(id);
        clientService.clearCache();
        return createResponse();
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增或更新客户端,id为null时新增",notes = "需要accessToken，需要管理员权限")
    public ApiResponse save(@Validated @RequestBody Client client) {
        validateExist(client);
        clientService.saveOrUpdate(client);
        clientService.clearCache();
        return createResponse();
    }

    /**
     * 校验是否已存在
     * @param client
     */
    private void validateExist(Client client) {
        if (client.getId() == null) {
            QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Client::getClientId,client.getClientId());
            int count = clientService.count(queryWrapper);
            if (count != 0) {
                throw new ApiException(ErrorEnum.INVALID_REQUEST.getErrorCode(),"客户端已存在");
            }
        }
    }

}

```

前端

```js
import request from '@/utils/request'

/**
 * 
 * @data {Object} data
 */
export function pageClient(params) {
  return request.get('/client/page', { params })
}

/**
 * 保存客户端
 * @param {Object} data
 */
export function saveClient(data) {
  return request.post('/client/save', data)
}

/**
 * 删除客户端
 * @param {Object} id
 */
export function deleteClient(id) {
  return request.delete('/client/delete/' + id)
}
```

## 认证

认证区分客户端，因此，登录、退出、刷新access_token接口都需要在请求头带上客户端信息。

例子： `Authorization: Basic cGM6MTIzNDU2`

这里的`cGM6MTIzNDU2`为pc:123456经过base64加密后的密文。pc为clientId，123456为clientSecret。

### 账号密码登录

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
  "code": 0,
  "message": "成功",
  "data": {
    "access_token": "6a2dc65f-74f6-4eb6-b4d2-2eba2af4f587",
    "token_type": "Bearer",
    "refresh_token": "17456dd5-5dbd-41ec-ad4f-7cf101c9ab3e"
  }
}
```

### 手机号验证码登录

请求方法：POST

请求地址：/mobile/login

请求参数：
mobile：手机号，必传
code：验证码，必传

需要access_token： 否

需要管理员权限： 否

需要[客户端认证](https://copoile.github.io/api/auth/)： 是

接口说明：验证码调发送验证码接口获取。

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

### 第三方登录

请求方法：POST

请求地址：/oauth

请求参数：
type：类型，必传，1:qq，2:github，3:gitee
code：认证code，必传

需要access_token： 否

需要管理员权限： 否

需要[客户端认证](https://copoile.github.io/api/auth/)： 是

接口说明：这里的code是第三方登录回调获取到的code。

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

