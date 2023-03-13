package com.alihmzyv.todorestservice.service;


import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.User;

public interface UserService {
    Integer createUser(RegisterUserDto registerUserDto);
    UserRespDto getUserRespDtoById(Integer userId);
    User findUserById(Integer userId);
}
