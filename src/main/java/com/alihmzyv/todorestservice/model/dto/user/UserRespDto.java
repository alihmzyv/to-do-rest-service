package com.alihmzyv.todorestservice.model.dto.user;

import com.alihmzyv.todorestservice.model.entity.AppUser;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * A DTO for the {@link AppUser} entity
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRespDto implements Serializable {
    String firstName;
    String lastName;
    String emailAddress;
}