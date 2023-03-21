package com.alihmzyv.todorestservice.model.dto.user;

import com.alihmzyv.todorestservice.model.entity.AppUser;
import lombok.AccessLevel;
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
public class UserRespDto implements Serializable {
    String firstName;
    String lastName;
    String emailAddress;
}