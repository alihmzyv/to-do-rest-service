package com.alihmzyv.todorestservice.model.dto.user;

import com.alihmzyv.todorestservice.model.entity.AppUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * A DTO for the {@link AppUser} entity
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserDto implements Serializable {
    @Schema(example = "Ali")
    @NotBlank(message = "{field.notblank}")
    String firstName;
    @Schema(example = "Hamzayev")
    @NotBlank(message = "{field.notblank}")
    String lastName;
    @Schema(description = "{email.valid}", example = "alihmzyv@gmail.com")
    @Email(message = "{email.valid}")
    @NotBlank(message = "{field.notblank}")
    String emailAddress;
    @Schema(description = "{password.strong}", example = "Ali1234$", pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$", //TODO: read from properties file
            message = "{password.strong}")
    @NotBlank(message = "{field.notblank}")
    String password;
}