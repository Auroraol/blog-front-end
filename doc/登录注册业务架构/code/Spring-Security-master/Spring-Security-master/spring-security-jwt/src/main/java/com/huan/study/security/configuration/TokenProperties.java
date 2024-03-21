package com.huan.study.security.configuration;

import lombok.Data;

/**
 * @author huan.fu 2020-06-07 - 14:22
 */
@Data
public class TokenProperties {
    private String secretKey;
    private Long tokenExpireSecond;
    private String tokenHeaderPrefix;
    private String authorizationHeaderName;
    private Long refreshTokenExpiredSecond;
    private String refreshHeaderName;
    private String userId;
}
