package com.huan.study.security.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huan.study.security.configuration.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huan 2020-06-07 - 14:34
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TokenAuthenticateFilter extends OncePerRequestFilter {

    private final TokenProperties tokenProperties;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取 认证头
        String authorizationHeader = request.getHeader(tokenProperties.getAuthorizationHeaderName());
        if (!checkIsTokenAuthorizationHeader(authorizationHeader)) {
            log.debug("获取到认证头Authorization的值:[{}]但不是我们系统中登录后签发的。", authorizationHeader);
            filterChain.doFilter(request, response);
            return;
        }
        // 获取到真实的token
        String realToken = getRealAuthorizationToken(authorizationHeader);
        // 解析 jwt token
        Jws<Claims> jws = JwtUtils.parserAuthenticateToken(realToken, tokenProperties.getSecretKey());
        // token 不合法
        if (null == jws) {
            writeJson(response, "认证token不合法");
            return;
        }
        // token 是否过期
        if (JwtUtils.isJwtExpired(jws)) {
            // 处理过期
            handleTokenExpired(response, request, filterChain);
            return;
        }

        // 构建认证对象
        JwtUtils.buildAuthentication(jws, tokenProperties.getUserId());

        filterChain.doFilter(request, response);
    }

    /**
     * 处理token过期情况
     *
     * @param response
     * @param request
     * @param filterChain
     * @return
     * @throws IOException
     */
    private void handleTokenExpired(HttpServletResponse response, HttpServletRequest request, FilterChain filterChain) throws IOException, ServletException {
        // 获取刷新 token
        String refreshTokenHeader = request.getHeader(tokenProperties.getRefreshHeaderName());
        // 检测 refresh-token 是否是我们系统中签发的
        if (!checkIsTokenAuthorizationHeader(refreshTokenHeader)) {
            log.debug("获取到刷新认证头:[{}]的值:[{}]但不是我们系统中登录后签发的。", tokenProperties.getRefreshHeaderName(), refreshTokenHeader);
            writeJson(response, "token过期了，refresh token 不是我们系统签发的");
            return;
        }
        // 解析 refresh-token
        Jws<Claims> refreshToken = JwtUtils.parserAuthenticateToken(getRealAuthorizationToken(refreshTokenHeader),
                tokenProperties.getSecretKey());
        // 判断 refresh-token 是否不合法
        if (null == refreshToken) {
            writeJson(response, "refresh token不合法");
            return;
        }
        // 判断 refresh-token 是否过期
        if (JwtUtils.isJwtExpired(refreshToken)) {
            writeJson(response, "refresh token 过期了");
            return;
        }
        // 重新签发 token

        String newToken = JwtUtils.generatorJwtToken(
                refreshToken.getBody().get(tokenProperties.getUserId()),
                tokenProperties.getUserId(),
                tokenProperties.getTokenExpireSecond(),
                tokenProperties.getSecretKey()
        );
        response.addHeader(tokenProperties.getAuthorizationHeaderName(), newToken);

        // 构建认证对象
        JwtUtils.buildAuthentication(JwtUtils.parserAuthenticateToken(newToken, tokenProperties.getSecretKey()), tokenProperties.getUserId());

        filterChain.doFilter(request, response);
    }

    /**
     * 写 json 数据给前端
     *
     * @param response
     * @throws IOException
     */
    private void writeJson(HttpServletResponse response, String msg) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, String> params = new HashMap<>(4);
        params.put("msg", msg);
        response.getWriter().print(OBJECT_MAPPER.writeValueAsString(params));
    }

    /**
     * 获取到真实的 token 串
     *
     * @param authorizationToken
     * @return
     */
    private String getRealAuthorizationToken(String authorizationToken) {
        return StringUtils.substring(authorizationToken, tokenProperties.getTokenHeaderPrefix().length()).trim();
    }

    /**
     * 判断是否是系统中登录后签发的token
     *
     * @param authorizationHeader
     * @return
     */
    private boolean checkIsTokenAuthorizationHeader(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            return false;
        }
        if (!StringUtils.startsWith(authorizationHeader, tokenProperties.getTokenHeaderPrefix())) {
            return false;
        }
        return true;
    }
}
