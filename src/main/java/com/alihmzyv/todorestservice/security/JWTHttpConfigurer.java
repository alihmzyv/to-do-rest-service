package com.alihmzyv.todorestservice.security;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.filter.CustomAuthenticationFilter;
import com.alihmzyv.todorestservice.filter.CustomAuthorizationFilter;
import com.alihmzyv.todorestservice.security.tokengenerator.JwtTokenGenerator;
import com.alihmzyv.todorestservice.security.util.ResponseHandler;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class JWTHttpConfigurer extends AbstractHttpConfigurer<JWTHttpConfigurer, HttpSecurity> {

    private final ResponseHandler responseHandler;
    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final ObjectMapper objectMapper;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final MessageSource messageSource;
    @Value("#{'${jwt.permit.all.paths.all}'.split(', ')}")
    private List<String> permitPathsAll;
    @Value("#{'${jwt.permit.all.paths.post}'.split(', ')}")
    private List<String> permitPathsPost;

    @Override
    public void configure(HttpSecurity http) {
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                authenticationManager,
                responseHandler,
                jwtProperties,
                algorithm,
                objectMapper,
                jwtTokenGenerator,
                messageSource);
        customAuthenticationFilter.setFilterProcessesUrl(jwtProperties.getLoginUrl());
        CustomAuthorizationFilter customAuthorizationFilter =
                new CustomAuthorizationFilter(algorithm, messageSource, permitPathsAll, permitPathsPost);
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
