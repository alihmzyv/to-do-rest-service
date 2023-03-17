package com.alihmzyv.todorestservice.controller;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.security.CustomAuthenticationException;
import com.alihmzyv.todorestservice.exception.security.CustomAuthorizationException;
import com.alihmzyv.todorestservice.model.dto.base.BaseResponse;
import com.alihmzyv.todorestservice.model.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@ControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        Set<ErrorResponse> errorResponses = ex.getFieldErrors().stream()
                .map(fieldError -> {
                    String message = fieldError.getDefaultMessage();
                    Object rejectedValue = fieldError.getRejectedValue();
                    if (rejectedValue != null &&
                            (!(rejectedValue instanceof String) || !rejectedValue.equals("")) &&
                            (!(rejectedValue instanceof Collection coll) || !coll.isEmpty())) {
                        return ErrorResponse.fieldValidationErrResp(
                                        fieldError.getField(), message, rejectedValue)
                                .build();
                    }
                    return ErrorResponse.fieldValidationErrResp(fieldError.getField(), message)
                            .build();
                })
                .collect(Collectors.toSet());
        BaseResponse<Object> resp = BaseResponse
                .validationFailure(messageSource)
                .errors(errorResponses)
                .build();
        return ResponseEntity.badRequest()
                .body(resp);
    }

    @ExceptionHandler({CustomAuthorizationException.class, CustomAuthenticationException.class})
    public ResponseEntity<BaseResponse<Object>> handleAuth(Exception exc) {
        ErrorResponse errResp = ErrorResponse.authenticationErrResp()
                .message(exc.getMessage())
                .build();
        BaseResponse<Object> resp = BaseResponse.authenticationFailure(messageSource)
                .errors(Set.of(errResp))
                .build();
        return ResponseEntity.status(UNAUTHORIZED)
                .body(resp);
    }

    @ExceptionHandler
    public ResponseEntity<BaseResponse<Object>> handleGeneral(
            Exception exc) {
        ErrorResponse errResp = ErrorResponse.badRequestErrResp()
                .message(exc.getMessage())
                .build();
        BaseResponse<Object> resp = BaseResponse.badRequestFailure(messageSource)
                .errors(Set.of(errResp))
                .build();
        return ResponseEntity.badRequest()
                .body(resp);
    }

    @ExceptionHandler (value = {AccessDeniedException.class})
    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
                                            AccessDeniedException accessDeniedException) throws IOException {
        // 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization Failed : " + accessDeniedException.getMessage());
    }
}
