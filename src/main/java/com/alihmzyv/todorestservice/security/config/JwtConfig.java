package com.alihmzyv.todorestservice.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JwtConfig {
    private final JwtProperties jwtProperties;

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(jwtProperties.getSecret());
    }
}
