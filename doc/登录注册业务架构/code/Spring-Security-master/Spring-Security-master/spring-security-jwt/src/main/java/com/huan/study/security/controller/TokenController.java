package com.huan.study.security.controller;

import com.huan.study.security.configuration.TokenProperties;
import com.huan.study.security.token.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author huan.fu 2020-06-07 - 15:04
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenController {

    private final TokenProperties tokenProperties;

    @GetMapping("/getToken")
    public void getToken(HttpServletResponse response,
                         @RequestParam("userId") String userId) {
        String authenticateToken = JwtUtils.generatorJwtToken(userId,
                tokenProperties.getUserId(),
                tokenProperties.getTokenExpireSecond(),
                tokenProperties.getSecretKey());

        String refreshToken = JwtUtils.generatorJwtToken(userId,
                tokenProperties.getUserId(),
                tokenProperties.getRefreshTokenExpiredSecond(),
                tokenProperties.getSecretKey());

        response.addHeader(tokenProperties.getAuthorizationHeaderName(), authenticateToken);
        response.addHeader(tokenProperties.getRefreshHeaderName(), refreshToken);

    }

    @GetMapping("/protectedMethod")
    public String protectedMethod() {
        System.out.println("访问受保护的方法");
        return "当前登录用户" + SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
