package com.alihmzyv.todorestservice.model.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRespDto implements Serializable {
    @NotNull
    Integer id;
    @NotNull
    String name;
    @NotNull
    LocalDate deadline;
    @NotNull
    String description;
    String img;
    @NotNull
    Boolean done;
    @NotNull
    Boolean archived;
    @NotNull
    Boolean important;
}