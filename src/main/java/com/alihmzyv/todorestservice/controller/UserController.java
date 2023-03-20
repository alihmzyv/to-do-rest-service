package com.alihmzyv.todorestservice.controller;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.model.dto.base.BaseResponse;
import com.alihmzyv.todorestservice.model.dto.user.ForgotPasswordDto;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.ResetPasswordDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.security.util.AuthenticationFacade;
import com.alihmzyv.todorestservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final MessageSource messageSource;
    private final AuthenticationFacade authenticationFacade;

    @Operation(
            tags = {"User"},
            summary = "Register a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The required request body or parameters missing or invalid",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class)))})
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

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            tags = {"User"},
            summary = "Get user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT is not present in 'Authentication' header or is invalid " +
                                    "or unauthorized",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    )})
    @GetMapping
    public BaseResponse<UserRespDto> getUser() {
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        UserRespDto userRespDto = userService.getUserRespDtoById(userFound.getId());
        return BaseResponse.ok(userRespDto, messageSource)
                .build();
    }

    @Operation(
            tags = {"User"},
            summary = "Send reset password email",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The required request body or parameters missing or invalid",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class)))})
    @PostMapping("/forgot-password")
    public BaseResponse<Object> forgotPassword(
            @RequestBody @Valid ForgotPasswordDto forgotPasswordDto) {
        userService.sendResetPasswordEmail(forgotPasswordDto);
        return BaseResponse.ok(messageSource)
                .message(messageSource.getMessage("user.password.forgot.successful"))
                .build();
    }

    @Operation(
            tags = {"User"},
            summary = "Reset the password of the user by providing the token and new password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The required request body or parameters missing or invalid",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class)))})
    @PostMapping("/reset-password")
    public BaseResponse<Object> resetPassword(
            @RequestParam(name = "token") String token,
            @RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(token, resetPasswordDto);
        return BaseResponse.ok(messageSource)
                .message(messageSource.getMessage("user.password.reset.successful"))
                .build();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            tags = {"User"},
            summary = "Delete the user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "JWT is not present in 'Authentication' header or is invalid " +
                                    "or unauthorized",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResponse.class))
                    )})
    @DeleteMapping("/delete")
    public BaseResponse<Object> deleteUser() {
        Authentication authentication = authenticationFacade.getAuthentication();
        String emailAddress = authentication.getName();
        AppUser userFound = userService.findUserByEmailAddress(emailAddress);
        userService.deleteUserById(userFound.getId());
        return BaseResponse.deleted(messageSource)
                .build();
    }
}
