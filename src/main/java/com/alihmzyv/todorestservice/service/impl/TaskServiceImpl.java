package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.exception.TaskNotFoundException;
import com.alihmzyv.todorestservice.mapper.TaskMapper;
import com.alihmzyv.todorestservice.model.dto.task.CreateTaskDto;
import com.alihmzyv.todorestservice.model.dto.task.TaskRespDto;
import com.alihmzyv.todorestservice.model.dto.task.UpdateTaskDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.model.entity.Task;
import com.alihmzyv.todorestservice.repo.TaskRepository;
import com.alihmzyv.todorestservice.service.TaskService;
import com.alihmzyv.todorestservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepo;
    private final UserService userService;
    private final TaskMapper taskMapper;

    @Override
    public Integer createTask(Integer userId, CreateTaskDto createTaskDto) {
        AppUser appUserFound = userService.findUserById(userId);
        Task task = taskMapper.createTaskDtoToTask(createTaskDto);
        task.setAppUser(appUserFound);
        taskRepo.save(task);
        return task.getId();
    }

    @Override
    public void updateTask(Integer userId, Integer taskId, UpdateTaskDto updateTaskDto) {
        AppUser appUserFound = userService.findUserById(userId);
        taskRepo.findByAppUserIdAndId(appUserFound.getId(), taskId)
                .ifPresentOrElse(task -> {
                    taskMapper.updateTask(updateTaskDto, task);
                    taskRepo.save(task);
                }, () -> {
                    throw new TaskNotFoundException(String.format("Task not found with id: %d", taskId));
                });
    }

    @Override
    public TaskRespDto findTaskById(Integer userId, Integer taskId) {
        AppUser appUserFound = userService.findUserById(userId);
        return taskRepo.findByAppUserIdAndId(appUserFound.getId(), taskId)
                .map(taskMapper::taskToTaskRespDto)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task not found with id: %d", taskId)));
    }
}
