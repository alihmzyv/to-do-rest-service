package com.alihmzyv.todorestservice.service;


import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;

import java.util.Optional;

public interface UserService {
    Integer createUser(RegisterUserDto registerUserDto);
    UserRespDto getUserRespDtoById(Integer userId);
    AppUser findUserById(Integer userId);
    Optional<AppUser> findUserByEmailAddress(String emailAddress);
}
