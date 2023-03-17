package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @SneakyThrows
    @Override
    public void sendEmail(String to, String subject, String body, boolean multipart, boolean html) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);
        helper.setTo(to);
        helper.setSubject(messageSource.getMessage("email.title"));
        helper.setText(body, html);
        mailSender.send(message);
    }
}
