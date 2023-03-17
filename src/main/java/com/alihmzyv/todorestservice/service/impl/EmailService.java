package com.alihmzyv.todorestservice.service.impl;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
