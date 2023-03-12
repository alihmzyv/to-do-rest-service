package com.alihmzyv.todorestservice.model.dto.user;

import com.alihmzyv.todorestservice.model.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserDto implements Serializable {
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    @NotNull
    String emailAddress;
    @NotNull
    String password;
}