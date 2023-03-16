package com.alihmzyv.todorestservice.config.init;

import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestUserInit implements CommandLineRunner {
    private final UserService userService;

    @Override
    public void run(String... args) {
        RegisterUserDto userDto = RegisterUserDto.builder()
                .firstName("Ali")
                .lastName("Hamzayev")
                .emailAddress("alihmzyv@gmail.com")
                .password("123")
                .build();
        userService.createUser(userDto);
    }
}
