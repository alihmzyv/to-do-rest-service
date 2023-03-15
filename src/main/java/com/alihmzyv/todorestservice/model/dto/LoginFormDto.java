package com.alihmzyv.todorestservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static com.alihmzyv.todorestservice.consts.Validation.DEFAULT_NOT_NULL_MESSAGE;
import static com.alihmzyv.todorestservice.consts.Validation.DEFAULT_VALID_EMAIL_MESSAGE;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginFormDto {
    @Email(message = DEFAULT_VALID_EMAIL_MESSAGE)
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String emailAddress;
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String password;
}
