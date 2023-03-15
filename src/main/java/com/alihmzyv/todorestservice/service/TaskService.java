package com.alihmzyv.todorestservice.service;

import com.alihmzyv.todorestservice.model.dto.task.CreateTaskDto;
import com.alihmzyv.todorestservice.model.dto.task.TaskRespDto;
import com.alihmzyv.todorestservice.model.dto.task.UpdateTaskDto;

public interface TaskService {
    Integer createTask(Integer userId, CreateTaskDto createTaskDto);

    void updateTask(Integer userId, Integer taskId, UpdateTaskDto updateTaskDto);

    TaskRespDto findTaskById(Integer userId, Integer taskId);
}
