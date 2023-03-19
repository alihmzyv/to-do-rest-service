package com.alihmzyv.todorestservice.service;

import com.alihmzyv.todorestservice.model.dto.task.CreateTaskDto;
import com.alihmzyv.todorestservice.model.dto.task.TaskRespDto;
import com.alihmzyv.todorestservice.model.dto.task.UpdateTaskDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Integer createTask(Integer userId, CreateTaskDto createTaskDto);

    void updateTask(Integer userId, Integer taskId, UpdateTaskDto updateTaskDto);

    TaskRespDto findTaskById(Integer userId, Integer taskId);

    Page<TaskRespDto> getAllTasks(Integer userId, Pageable pageable, Predicate predicate);
}
