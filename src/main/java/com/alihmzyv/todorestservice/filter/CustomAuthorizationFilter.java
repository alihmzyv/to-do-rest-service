package com.alihmzyv.todorestservice.filter;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.security.CustomAuthorizationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final Algorithm algorithm;
    private final MessageSource messageSource;
    private final List<String> permitAllPaths;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Auth filter");
        Optional<String> tokenOpt = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(headerValue -> headerValue.startsWith("Bearer "))
                .map(headerValue -> headerValue.substring("Bearer ".length()));
        if (tokenOpt.isPresent()) {
            try {
                String token = tokenOpt.get();
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String subject = decodedJWT.getSubject();
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(subject, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception exc) {
                throw new CustomAuthorizationException(exc.getMessage()); //TODO: 'message should be internationalized
            }
        } else {
            throw new CustomAuthorizationException(messageSource.getMessage("jwt.missing.message"));
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        //TODO: the code below does not seem right
        return permitAllPaths.stream()
                .anyMatch(path -> antPathMatcher.match(path, servletPath) ||
                        (request.getMethod().equals("POST") && antPathMatcher.match("/api/users", servletPath)) ||
                        request.getMethod().equals("POST") && antPathMatcher.match("/api/users/forgot-password", servletPath) ||
                        request.getMethod().equals("POST") && antPathMatcher.match("/api/users/reset-password", servletPath));
    }
}
