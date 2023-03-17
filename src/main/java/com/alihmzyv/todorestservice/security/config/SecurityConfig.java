package com.alihmzyv.todorestservice.security.config;

import com.alihmzyv.todorestservice.security.JWTHttpConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTHttpConfigurer jwtHttpConfigurer;
    @Value("#{'${jwt.permit.all.paths}'.split(', ')}")
    private List<String> permitAllPaths;

    @Bean
    public SecurityFilterChain http(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers(permitAllPaths.toArray(String[]::new)).permitAll()
                .requestMatchers(POST, "/api/users", "/api/users/forgot-password", "/api/users/reset-password").permitAll()
                .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_USER")
                .anyRequest().authenticated().and()
                .apply(jwtHttpConfigurer).and()
                .httpBasic().and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
