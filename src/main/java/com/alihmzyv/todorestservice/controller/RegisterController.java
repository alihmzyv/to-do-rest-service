package com.alihmzyv.todorestservice.controller;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.model.dto.base.BaseResponse;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.security.util.AuthenticationFacade;
import com.alihmzyv.todorestservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class RegisterController {
    private final UserService userService;
    private final MessageSource messageSource;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<BaseResponse<Object>> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        userService.createUser(registerUserDto);
        BaseResponse<Object> resp = BaseResponse.created(messageSource).build();
        URI uri = MvcUriComponentsBuilder.fromController(this.getClass())
                .build()
                .toUri();
        return ResponseEntity.created(uri)
                .body(resp);
    }

    @GetMapping
    public BaseResponse<UserRespDto> getUser() {
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        UserRespDto userRespDto = userService.getUserRespDtoById(userFound.getId());
        return BaseResponse.ok(userRespDto, messageSource)
                .build();
    }
}
