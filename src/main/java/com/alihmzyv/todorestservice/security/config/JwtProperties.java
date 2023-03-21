package com.alihmzyv.todorestservice.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {
    @Value("${jwt.login.url}")
    private String loginUrl;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.token.expire.seconds}")
    private Long accessTokenExpireSeconds;

    @Value("${jwt.refresh.token.expire.seconds}")
    private Long refreshTokenExpireSeconds;
}
