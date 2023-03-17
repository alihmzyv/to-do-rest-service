package com.alihmzyv.todorestservice.service;


import com.alihmzyv.todorestservice.model.dto.user.ForgotPasswordDto;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.ResetPasswordDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;

public interface UserService {
    Integer createUser(RegisterUserDto registerUserDto);

    UserRespDto getUserRespDtoById(Integer userId);

    AppUser findUserById(Integer userId);

    AppUser findUserByEmailAddress(String emailAddress);
    void sendResetPasswordEmail(ForgotPasswordDto forgotPasswordDto);
    void resetPassword(String token, ResetPasswordDto resetPasswordDto);
}
