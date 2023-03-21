package com.alihmzyv.todorestservice.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordDto {
    @Schema(description = "{email.valid}", example = "alihmzyv@gmail.com")
    @NotBlank(message = "{field.notblank}")
    @Email(message = "{email.valid}")
    String emailAddress;
}
