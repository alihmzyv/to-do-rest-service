package com.alihmzyv.todorestservice.controller;


import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.model.dto.base.BaseResponse;
import com.alihmzyv.todorestservice.model.dto.task.CreateTaskDto;
import com.alihmzyv.todorestservice.model.dto.task.TaskRespDto;
import com.alihmzyv.todorestservice.model.dto.task.UpdateTaskDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.model.entity.QTask;
import com.alihmzyv.todorestservice.model.entity.Task;
import com.alihmzyv.todorestservice.security.util.AuthenticationFacade;
import com.alihmzyv.todorestservice.service.TaskService;
import com.alihmzyv.todorestservice.service.UserService;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users/tasks", produces = APPLICATION_JSON_VALUE)
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final AuthenticationFacade authenticationFacade;
    private final PagedResourcesAssembler<TaskRespDto> pagedResourcesAssembler;

    @Operation(
            tags = {"Task"},
            summary = "Get a single task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The required request body or parameters missing or invalid"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT is not present in 'Authentication' header or is invalid " +
                                    "or unauthorized"
                    )})
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

    @Operation(
            tags = {"Task"},
            summary = "Get tasks of the user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The required request body or parameters missing or invalid"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT is not present in 'Authentication' header or is invalid " +
                                    "or unauthorized")})
    @GetMapping
    public BaseResponse<List<TaskRespDto>> getTasks(
            @RequestParam(name = "overdue") Optional<Boolean> overdue, //TODO: can be incorporated into Predicate below?
            @ParameterObject @QuerydslPredicate(root = Task.class) Predicate predicate,
            @ParameterObject Pageable pageable) {
        Predicate finalPredicate = overdue.map(isOverdue ->
                        (Predicate) (isOverdue ?
                                QTask.task.deadline.after(now()).and(predicate) :
                                QTask.task.deadline.before(now()).and(predicate)))
                .orElse(predicate);
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        Page<TaskRespDto> allTasks = taskService.getAllTasks(userFound.getId(), pageable, finalPredicate);
        List<TaskRespDto> payLoad = allTasks.getContent();
        PagedModel.PageMetadata pageMetadata = pagedResourcesAssembler.toModel(allTasks).getMetadata();
        return BaseResponse.ok(payLoad, messageSource)
                .pageMetadata(pageMetadata)
                .build();
    }

    @Operation(
            tags = {"Task"},
            summary = "Create a task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The required request body or parameters missing or invalid"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT is not present in 'Authentication' header or is invalid " +
                                    "or unauthorized")})
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

    @Operation(
            tags = {"Task"},
            summary = "Update a task of the user by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The required request body or parameters missing or invalid"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT is not present in 'Authentication' header or is invalid " +
                                    "or unauthorized"
                    )})
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
