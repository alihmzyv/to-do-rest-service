package com.alihmzyv.todorestservice.controller;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.model.dto.TokenDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.model.entity.Role;
import com.alihmzyv.todorestservice.security.JwtProperties;
import com.alihmzyv.todorestservice.security.tokengenerator.JwtTokenGenerator;
import com.alihmzyv.todorestservice.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final Algorithm algorithm;
    private final MessageSource messageSource;

    @GetMapping("/token/refresh")
    public List<TokenDto> refreshToken(HttpServletRequest request) {
        Optional<String> tokenOpt = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(headerValue -> headerValue.startsWith("Bearer "))
                .map(headerValue -> headerValue.substring("Bearer ".length()));
        if (tokenOpt.isPresent()) {
            String token = tokenOpt.get();
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String subject = decodedJWT.getSubject();
            Optional<AppUser> userOpt = userService.findUserByEmailAddress(subject);
            if (userOpt.isPresent()) {
                AppUser appUser = userOpt.get();
                String accessTokenBody = jwtTokenGenerator.generateToken(
                        appUser.getEmailAddress(),
                        Instant.now().plusSeconds(jwtProperties.getAccessTokenExpireSeconds()),
                        request.getRequestURL().toString(),
                        Map.of("roles", appUser.getRoles().stream().map(Role::getName).toList()),
                        algorithm);
                String refreshTokenBody = jwtTokenGenerator.generateToken(
                        appUser.getEmailAddress(),
                        Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpireSeconds()),
                        request.getRequestURL().toString(),
                        Map.of("roles", appUser.getRoles().stream().map(Role::getName).toList()),
                        algorithm);
                TokenDto accessToken = TokenDto.accessToken(accessTokenBody);
                TokenDto refreshToken = TokenDto.refreshToken(refreshTokenBody);
                return List.of(accessToken, refreshToken);
            } else {
                throw new BadCredentialsException(String.format("%s: %s", messageSource.getMessage("user.username.not.found"), subject));
            }
        } else {
            throw new RuntimeException(messageSource.getMessage("jwt.refresh.jwt.missing"));
        }
    }
}