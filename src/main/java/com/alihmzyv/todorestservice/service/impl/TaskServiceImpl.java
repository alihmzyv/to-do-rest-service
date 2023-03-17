package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.DuplicateNotAllowedException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepo;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final MessageSource messageSource;

    @Override
    public Integer createTask(Integer userId, CreateTaskDto createTaskDto) {
        requiresTaskDoesNotExistByName(createTaskDto.getName());
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
                    throw new TaskNotFoundException(String.format("%s: %d",
                            messageSource.getMessage("task.id.not.found"),
                            taskId));
                });
    }

    @Override
    public TaskRespDto findTaskById(Integer userId, Integer taskId) {
        AppUser userFound = userService.findUserById(userId);
        return taskRepo.findByUserIdAndId(userFound.getId(), taskId)
                .map(taskMapper::taskToTaskRespDto)
                .orElseThrow(() -> new TaskNotFoundException(String.format("%s: %d",
                        messageSource.getMessage("task.id.not.found"),
                        taskId)));
    }

    @Override
    public Page<TaskRespDto> getAllTasks(Integer userId, Pageable pageable) {
        return taskRepo.findAll(pageable)
                .map(taskMapper::taskToTaskRespDto);
    }

    private void requiresTaskDoesNotExistByName(String name) {
        if (taskRepo.existsByName(name))
            throw new DuplicateNotAllowedException(String.format(
                    "%s: %s",
                    messageSource.getMessage("task.name.exists"),
                    name));
    }
}
