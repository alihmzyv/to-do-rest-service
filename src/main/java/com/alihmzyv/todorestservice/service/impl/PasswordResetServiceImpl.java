package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.ActionNotAllowedException;
import com.alihmzyv.todorestservice.service.PasswordResetService;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    private final static HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    private final IMap<String, String> passwordResetRequestsMap =
            hazelcastInstance.getMap("passwordResetRequestsMap");
    private final MessageSource messageSource;
    @Value("${user.password.reset.request.wait.seconds}")
    private Long secondsToWait;

    @Override
    public String putAndGetToken(String emailAddress) {
        //TODO: implement blocking for a while if the user made the request more than some threshold value in a time period
        if (passwordResetRequestsMap.containsValue(emailAddress)) {
            throw new ActionNotAllowedException(String.format("%s %d",
                    messageSource.getMessage("user.password.reset.wait"),
                    secondsToWait));
        }
        UUID uuid = UUID.randomUUID();
        passwordResetRequestsMap.set(uuid.toString(), emailAddress, secondsToWait, TimeUnit.SECONDS);
        return uuid.toString();
    }

    @Override
    public Optional<String> getEmailAddress(String token) {
        return Optional.ofNullable(passwordResetRequestsMap.remove(token));
    }

    @Override
    public Long getExpirationTimeSeconds() {
        return secondsToWait;
    }
}
