package com.alihmzyv.todorestservice.model.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

import static com.alihmzyv.todorestservice.consts.Validation.DEFAULT_NOT_NULL_MESSAGE;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskDto implements Serializable {
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String name;
    @NotNull(message = DEFAULT_NOT_NULL_MESSAGE)
    LocalDate deadline;
    @NotBlank(message = DEFAULT_NOT_NULL_MESSAGE)
    String description;
    String img;
}