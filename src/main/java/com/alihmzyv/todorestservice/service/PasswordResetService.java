package com.alihmzyv.todorestservice.service;

import java.util.Optional;

public interface PasswordResetService {
    String putAndGetToken(String emailAddress);
    Optional<String> getEmailAddress(String token);
    Long getExpirationTimeSeconds();
}
