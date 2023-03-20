package com.alihmzyv.todorestservice.model.dto.task;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTaskDto implements Serializable {
    String name;
    LocalDateTime deadline;
    String description;
    String img;
    Boolean done;
    Boolean archived;
    Boolean important;
}