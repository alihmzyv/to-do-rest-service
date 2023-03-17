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
    Integer id;
    String name;
    LocalDate deadline;
    String description;
    String img;
    Boolean done;
    Boolean archived;
    Boolean important;
}