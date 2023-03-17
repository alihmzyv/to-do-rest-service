package com.alihmzyv.todorestservice.controller;


import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.model.dto.base.BaseResponse;
import com.alihmzyv.todorestservice.model.dto.task.CreateTaskDto;
import com.alihmzyv.todorestservice.model.dto.task.TaskRespDto;
import com.alihmzyv.todorestservice.model.dto.task.UpdateTaskDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.security.util.AuthenticationFacade;
import com.alihmzyv.todorestservice.service.TaskService;
import com.alihmzyv.todorestservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final AuthenticationFacade authenticationFacade;
    private final PagedResourcesAssembler<TaskRespDto> pagedResourcesAssembler;

    @GetMapping("/{id}")
    public BaseResponse<TaskRespDto> getTask(
            @PathVariable("id") Integer taskId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        TaskRespDto taskRespDto = taskService.findTaskById(userFound.getId(), taskId);
        return BaseResponse.ok(taskRespDto, messageSource)
                .build();
    }

    @GetMapping
    public BaseResponse<List<TaskRespDto>> getTasks(
            Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        Page<TaskRespDto> allTasks = taskService.getAllTasks(userFound.getId(), pageable);
        List<TaskRespDto> payLoad = allTasks.getContent();
        PagedModel.PageMetadata pageMetadata = pagedResourcesAssembler.toModel(allTasks).getMetadata();
        return BaseResponse.ok(payLoad, messageSource)
                .pageMetadata(pageMetadata)
                .build();
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> createTask(
            @RequestBody @Valid CreateTaskDto createTaskDto) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        Integer taskId = taskService.createTask(userFound.getId(), createTaskDto);
        BaseResponse<Object> resp = BaseResponse.created(messageSource).build();
        URI uri = MvcUriComponentsBuilder
                .fromMethodName(this.getClass(), "getTask", taskId) //TODO: proxy issue not letting to use method call
                .build()
                .toUri();
        return ResponseEntity.created(uri)
                .body(resp);
    }

    @PutMapping("/{id}")
    public BaseResponse<Object> updateTask(
            @PathVariable("id") Integer taskId,
            @RequestBody @Valid UpdateTaskDto updateTaskDto) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        taskService.updateTask(userFound.getId(), taskId, updateTaskDto);
        return BaseResponse.ok(messageSource)
                .build();
    }
}
