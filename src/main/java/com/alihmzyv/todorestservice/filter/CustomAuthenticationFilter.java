package com.alihmzyv.todorestservice.filter;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.security.CustomAuthenticationException;
import com.alihmzyv.todorestservice.model.dto.user.LoginFormDto;
import com.alihmzyv.todorestservice.model.dto.security.TokenDto;
import com.alihmzyv.todorestservice.security.JwtProperties;
import com.alihmzyv.todorestservice.security.tokengenerator.JwtTokenGenerator;
import com.alihmzyv.todorestservice.security.util.ResponseHandler;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ResponseHandler responseHandler;
    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final ObjectMapper objectMapper;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final MessageSource messageSource;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginFormDto loginFormDto = objectMapper.readValue(request.getInputStream(), LoginFormDto.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginFormDto.getEmailAddress(), loginFormDto.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (MismatchedInputException exc) {
            throw new CustomAuthenticationException(messageSource.getMessage("jwt.validation.failure.message"));
        } catch (BadCredentialsException badCredentialsException) {
            throw new CustomAuthenticationException(messageSource.getMessage("jwt.credentials.bad.message"));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String accessTokenBody = jwtTokenGenerator.generateToken(
                user.getUsername(),
                Instant.now().plusSeconds(jwtProperties.getAccessTokenExpireSeconds()),
                request.getServerName(),
                Map.of("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()),
                algorithm);
        String refreshTokenBody = jwtTokenGenerator.generateToken(
                user.getUsername(),
                Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpireSeconds()),
                request.getServerName(),
                Map.of("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()),
                algorithm);
        TokenDto accessToken = TokenDto.accessToken(accessTokenBody);
        TokenDto refreshToken = TokenDto.refreshToken(refreshTokenBody);
        List<TokenDto> tokens = List.of(accessToken, refreshToken);
        responseHandler.handle(response, tokens);
    }
}
