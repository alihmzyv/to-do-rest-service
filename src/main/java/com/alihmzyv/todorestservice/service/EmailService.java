package com.alihmzyv.todorestservice.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body, boolean multipart, boolean html);
}
