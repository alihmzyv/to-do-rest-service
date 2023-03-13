package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.exception.UserNotFoundException;
import com.alihmzyv.todorestservice.mapper.UserMapper;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.User;
import com.alihmzyv.todorestservice.repo.UserRepository;
import com.alihmzyv.todorestservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    @Override
    public Integer createUser(RegisterUserDto registerUserDto) {
        User user = userMapper.registerUserDtoToUser(registerUserDto);
        userRepo.save(user);
        return user.getId();
    }

    @Override
    public UserRespDto getUserRespDtoById(Integer userId) {
        return userRepo.findById(userId)
                .map(userMapper::userToUserRespDto)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d", userId)));
    }

    @Override
    public User findUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d", userId)));
    }
}
