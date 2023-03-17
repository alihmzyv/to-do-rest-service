package com.alihmzyv.todorestservice.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordDto {
    @NotBlank(message = "{field.notblank}")
    @Email(message = "{email.valid}")
    String emailAddress;
}
