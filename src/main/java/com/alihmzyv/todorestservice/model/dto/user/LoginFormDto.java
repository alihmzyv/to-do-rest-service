package com.alihmzyv.todorestservice.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginFormDto {
    @Schema(description = "{email.valid}", example = "alihmzyv@gmail.com")
    @Email(message = "{email.valid}")
    @NotBlank(message = "{field.notblank}")
    String emailAddress;
    @Schema(example = "alihmzyv@gmail.com")
    @NotBlank(message = "{field.notblank}")
    String password;
}
