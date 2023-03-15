package com.alihmzyv.todorestservice.security.tokengenerator.impl;

import com.alihmzyv.todorestservice.security.tokengenerator.JwtTokenGenerator;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenGeneratorImpl implements JwtTokenGenerator {
    @Override
    public String generateToken(String subject, Instant expiresAt, String issuer, Map<String, List<?>> claims, Algorithm algorithm) {
        JWTCreator.Builder builder = JWT.create()
                .withSubject(subject)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer);
        claims.forEach(builder::withClaim);
        return builder.sign(algorithm);
    }
}
