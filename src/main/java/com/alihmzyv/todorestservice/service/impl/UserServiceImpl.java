package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.exception.UserNotFoundException;
import com.alihmzyv.todorestservice.mapper.UserMapper;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.model.entity.Role;
import com.alihmzyv.todorestservice.repo.UserRepository;
import com.alihmzyv.todorestservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    @Override
    public Integer createUser(RegisterUserDto registerUserDto) {
        AppUser appUser = userMapper.registerUserDtoToUser(registerUserDto);
        userRepo.save(appUser);
        return appUser.getId();
    }

    @Override
    public UserRespDto getUserRespDtoById(Integer userId) {
        return userRepo.findById(userId)
                .map(userMapper::userToUserRespDto)
                .orElseThrow(() -> new UserNotFoundException(String.format("AppUser not found with id: %d", userId)));
    }

    @Override
    public AppUser findUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("AppUser not found with id: %d", userId)));
    }

    @Override
    public Optional<AppUser> findUserByEmailAddress(String emailAddress) {
        return userRepo.findByEmailAddress(emailAddress);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //email address is used instead of username
        return findUserByEmailAddress(username)
                .map(user -> {
                    List<SimpleGrantedAuthority> authorities = user.getRoles()
                            .stream()
                            .map(Role::getName)
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    return User.builder()
                            .username(user.getEmailAddress())
                            .password(user.getPassword())
                            .authorities(authorities)
                            .build();
                })
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User not found with username: %s", username)));
    }
}
