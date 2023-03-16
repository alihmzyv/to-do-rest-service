package com.alihmzyv.todorestservice.mapper;

import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import org.mapstruct.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    AppUser registerUserDtoToUser(RegisterUserDto registerUserDto);
    UserRespDto userToUserRespDto(AppUser user);
}
