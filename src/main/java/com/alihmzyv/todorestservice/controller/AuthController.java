package com.alihmzyv.todorestservice.controller;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.UserNotFoundException;
import com.alihmzyv.todorestservice.exception.security.CustomAuthorizationException;
import com.alihmzyv.todorestservice.model.dto.base.BaseResponse;
import com.alihmzyv.todorestservice.model.dto.security.TokenDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.model.entity.Role;
import com.alihmzyv.todorestservice.security.JwtProperties;
import com.alihmzyv.todorestservice.security.tokengenerator.JwtTokenGenerator;
import com.alihmzyv.todorestservice.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = "Bearer Authentication")
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

    @Operation(
            tags = {"Authentication"},
            summary = "Refresh Token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Authorization header is not present or token is invalid",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class)))})
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
            AppUser appUser; //TODO: refactor
            try {
                appUser = userService.findUserByEmailAddress(subject);
            } catch (UserNotFoundException exc) {
                throw new BadCredentialsException(String.format("%s: %s", messageSource.getMessage("user.email.not.found"), subject));
            }
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
            throw new CustomAuthorizationException(messageSource.getMessage("jwt.refresh.jwt.missing"));
        }
    }
}