package com.alihmzyv.todorestservice.security.tokengenerator;

import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface JwtTokenGenerator {
    String generateToken(String subject, Instant expiresAt, String issuer, Map<String, List<?>> claims, Algorithm algorithm);
}
