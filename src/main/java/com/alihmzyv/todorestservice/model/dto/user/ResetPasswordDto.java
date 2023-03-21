package com.alihmzyv.todorestservice.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordDto {
    @Schema(description = "{password.strong}", example = "Ali1234$", pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")
    //TODO: read regex from properties
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$", message = "{password.strong}")
    @NotBlank(message = "{field.notblank}")
    String newPassword;
}
