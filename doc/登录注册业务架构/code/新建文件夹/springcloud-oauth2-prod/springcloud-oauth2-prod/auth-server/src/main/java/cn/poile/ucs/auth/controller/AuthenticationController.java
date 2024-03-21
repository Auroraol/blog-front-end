package cn.poile.ucs.auth.controller;

import cn.poile.ucs.auth.response.ApiResponse;
import cn.poile.ucs.auth.vo.UserDetailImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * @author: yaohw
 * @create: 2019-09-25 16:49
 **/
@RestController
@Log4j2
public class AuthenticationController extends BaseController {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 更新用户信息时更新redis中的用户信息
     * @param authentication
     * @return java.lang.String
     */
    @GetMapping("/update")
    public @ResponseBody String updateCacheUserInfo(Authentication authentication) {
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            Authentication userAuthentication = auth2Authentication.getUserAuthentication();
            OAuth2Authentication newOAuth2Authentication = null;
            if (userAuthentication instanceof UsernamePasswordAuthenticationToken) {
                UserDetailImpl userDetails = (UserDetailImpl) userDetailsService.loadUserByUsername("admin");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                newOAuth2Authentication = new OAuth2Authentication(auth2Authentication.getOAuth2Request(), usernamePasswordAuthenticationToken);
            }
            OAuth2AccessToken accessToken = tokenStore.getAccessToken(auth2Authentication);
            if (newOAuth2Authentication != null) {
                tokenStore.storeAccessToken(accessToken,newOAuth2Authentication);
            }
        }
        return "ok";
    }

    @GetMapping("/user")
    public @ResponseBody
    ApiResponse<Principal> userInfo(Principal user) {
        log.info("user:{}",user);
        return createResponse(user);
    }

    /**
     * 退出时将token清空（使用RedisStore时就是删除掉对应缓存
     * 注: 这里的路径不能使用/logout，因为这个路径被LogoutFilter占用，配置文件配置了访问logout会转发到这里
     * 所以/logout和remove都能登出
     * @param authorization
     * @return
     */
    @DeleteMapping("/remove")
    public @ResponseBody String logout(@RequestHeader(value = "Authorization") String authorization) {
        String accessToken = authorization.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
        consumerTokenServices.revokeToken(accessToken);
        return "ok";
    }

    /**
     * 不需要token访问测试
     * @return
     */
    @GetMapping("/test/no_need_token")
    public @ResponseBody String test() {
        return "no_need_token";
    }

    /**
     * 需要token访问接口测试
     * @return
     */
    @GetMapping("/test/need_token")
    public @ResponseBody String test2() {
        return "need_token";
    }

    /**
     * 需要需要管理员权限
     * @return
     */
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/test/need_admin")
    public @ResponseBody String admin() {
        return "need_admin";
    }

    /**
     * 需要需要管理员2权限
     *
     * @return
     */
    @PreAuthorize("hasAuthority('admin2')")
    @GetMapping("/test/need_admin2")
    public @ResponseBody
    String admin2() {
        return "need_admin2";
    }

    /**
     * 测试需要客户端的test权限
     *
     * @return
     */
    @PreAuthorize("#oauth2.clientHasRole('test')")
    @GetMapping("/test/need_client_test")
    public @ResponseBody
    String test4() {
        return "need_client_test";
    }


    /**
     * 认证页面
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require() {
        log.info("---认证页面---");
        return new ModelAndView("ftl/login");
    }

    /**
     * scope 控制测试,该方法只有配置有scope为sever2的客户端能访问，针对的是客户端
     * @return
     */
    @GetMapping("/test/scope")
    @PreAuthorize("#oauth2.hasScope('sever2')")
    public @ResponseBody String test3() {
        return "scope-test";
    }

}
