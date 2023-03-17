package com.alihmzyv.todorestservice.model.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskDto implements Serializable {
    @NotBlank(message = "{field.notblank}")
    String name;
    @NotNull(message = "{field.notblank}")
    LocalDate deadline;
    @NotBlank(message = "{field.notblank}")
    String description;
    String img;
}