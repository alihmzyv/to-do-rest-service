package com.alihmzyv.todorestservice.model.dto.user;

import com.alihmzyv.todorestservice.model.entity.AppUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import static com.alihmzyv.todorestservice.consts.Validation.*;

/**
 * A DTO for the {@link AppUser} entity
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserDto implements Serializable {
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String firstName;
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String lastName;
    @Email(message = DEFAULT_VALID_EMAIL_MESSAGE)
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String emailAddress;
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])(?=.{8,})$\n",
            message = DEFAULT_STRONG_PASSWORD_MESSAGE)
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String password;
}