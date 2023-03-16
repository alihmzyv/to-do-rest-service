package com.alihmzyv.todorestservice.controller;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.UserNotFoundException;
import com.alihmzyv.todorestservice.mapper.UserMapper;
import com.alihmzyv.todorestservice.model.dto.base.BaseResponse;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class RegisterController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        userService.createUser(registerUserDto);
        BaseResponse<Object> resp = BaseResponse.created(messageSource).build();
        URI uri = MvcUriComponentsBuilder.fromController(this.getClass())
                .build()
                .toUri();
        return ResponseEntity.created(uri)
                .header("Location", uri.toString())
                .body(resp);
    }

    @GetMapping
    public BaseResponse<UserRespDto> getUser(Principal principal) {
        String emailAddress = principal.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress)
                        .orElseThrow(() ->
                                new UserNotFoundException(String.format(
                                        "User not found with email address: %d", emailAddress))); //never happens, since authenticated already
        UserRespDto userRespDto = userMapper.userToUserRespDto(userFound);
        BaseResponse<UserRespDto> resp = BaseResponse.ok(userRespDto, messageSource)
                .build();
        return resp;
    }
}
