package com.alihmzyv.todorestservice.mapper;

import com.alihmzyv.todorestservice.model.entity.User;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User registerUserDtoToUser(RegisterUserDto registerUserDto);

    UserRespDto userToUserRespDto(User user);
}
