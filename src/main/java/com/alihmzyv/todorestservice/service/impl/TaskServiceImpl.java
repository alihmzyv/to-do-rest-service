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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepo;
    private final UserService userService;
    private final TaskMapper taskMapper;
    @Override
    public Integer createTask(Integer userId, CreateTaskDto createTaskDto) {
        AppUser userFound = userService.findUserById(userId);
        Task task = taskMapper.createTaskDtoToTask(createTaskDto);
        task.setUser(userFound);
        taskRepo.save(task);
        return task.getId();
    }

    @Override
    public void updateTask(Integer userId, Integer taskId, UpdateTaskDto updateTaskDto) {
        AppUser userFound = userService.findUserById(userId);
        taskRepo.findByUserIdAndId(userFound.getId(), taskId)
                .ifPresentOrElse(task -> {
                    taskMapper.updateTask(updateTaskDto, task);
                    taskRepo.save(task);
                }, () -> {
                    throw new TaskNotFoundException(String.format("Task not found with id: %d", taskId));
                });
    }

    @Override
    public TaskRespDto findTaskById(Integer userId, Integer taskId) {
        AppUser userFound = userService.findUserById(userId);
        return taskRepo.findByUserIdAndId(userFound.getId(), taskId)
                .map(taskMapper::taskToTaskRespDto)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task not found with id: %d", taskId)));
    }
}
