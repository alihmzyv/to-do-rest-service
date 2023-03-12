package com.alihmzyv.todorestservice.mapper;

import com.alihmzyv.todorestservice.model.entity.Task;
import com.alihmzyv.todorestservice.model.dto.task.UpdateTaskDto;
import com.alihmzyv.todorestservice.model.dto.task.CreateTaskDto;
import com.alihmzyv.todorestservice.model.dto.task.TaskRespDto;
import org.mapstruct.*;

import java.util.Base64;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "img", expression = "java(base64ToByteArray(createTaskDto.getImg()))")
    Task createTaskDtoToTaskWithImage(CreateTaskDto createTaskDto);

    @Mapping(target = "img", expression = "java(base64ToByteArray(updateTaskDto.getImg()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task updateTaskFromUpdateTaskDto(UpdateTaskDto updateTaskDto, @MappingTarget Task task);

    @Mapping(target = "img", expression = "java(byteArrayToBase64(task.getImg()))")
    TaskRespDto taskToTaskRespDto(Task task);

    private byte[] base64ToByteArray(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    private String byteArrayToBase64(byte[] byteArr) {
        return Base64.getEncoder().encodeToString(byteArr);
    }
}
