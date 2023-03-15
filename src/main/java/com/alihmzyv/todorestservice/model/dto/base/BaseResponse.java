package com.alihmzyv.todorestservice.model.dto.base;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.model.dto.error.ErrorResponse;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.hateoas.PagedModel;

import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class BaseResponse<A> {
    public static final String DEFAULT_CREATED_MESSAGE = "base.response.created.message";
    public static final String DEFAULT_BAD_REQUEST_MESSAGE = "base.response.bad.request.message";
    public static final String DEFAULT_VALIDATION_FAILURE_MESSAGE = "base.response.validation.failure.message";
    public static final String DEFAULT_NOT_FOUND_MESSAGE = "base.response.not.found.message";
    public static final String DEFAULT_OK_MESSAGE = "base.response.ok.message";
    public static final String DEFAULT_INTERNAL_ERROR_MESSAGE = "base.response.internal.error.message";
    private static final String DEFAULT_AUTHENTICATION_FAILURE_MESSAGE = "base.response.authentication.failure.message";
    private static final String DEFAULT_SUCCESSFUL_AUTHENTICATION_MESSAGE = "base.response.authentication.successful.message";
    private static final String DEFAULT_DELETED_MESSAGE = "base.response.deleted.message";

    Boolean success;
    Integer status;
    String message;
    A payload;
    PagedModel.PageMetadata pageMetadata;
    Set<ErrorResponse> errors;

    public static BaseResponseBuilder<Object> failure() {
        return builder()
                .success(false);
    }

    public static BaseResponseBuilder<Object> authenticationFailure(MessageSource messageSource) {
        return failure()
                .message(messageSource.getMessage(DEFAULT_AUTHENTICATION_FAILURE_MESSAGE))
                .status(UNAUTHORIZED.value());
    }

    public static BaseResponseBuilder<Object> badRequestFailure(MessageSource messageSource) {
        return failure()
                .message(messageSource.getMessage(DEFAULT_BAD_REQUEST_MESSAGE))
                .status(BAD_REQUEST.value());
    }

    public static BaseResponseBuilder<Object> validationFailure(MessageSource messageSource) {
        return badRequestFailure(messageSource)
                .message(messageSource.getMessage(DEFAULT_VALIDATION_FAILURE_MESSAGE));
    }

    public static BaseResponseBuilder<Object> success() {
        return builder()
                .success(true);
    }

    public static BaseResponseBuilder<Object> ok(MessageSource messageSource) {
        return success()
                .status(OK.value())
                .message(messageSource.getMessage(DEFAULT_OK_MESSAGE));
    }

    public static <A> BaseResponseBuilder<A> ok(A payload, MessageSource messageSource) {
        return BaseResponse.<A>builder()
                .status(OK.value())
                .success(true)
                .message(messageSource.getMessage(DEFAULT_OK_MESSAGE))
                .payload(payload);
    }

    public static BaseResponseBuilder<Object> created(MessageSource messageSource) {
        return success()
                .status(CREATED.value())
                .message(messageSource.getMessage(DEFAULT_CREATED_MESSAGE));
    }

    public static <A> BaseResponseBuilder<A> authenticationSuccess(A tokens, MessageSource messageSource) {
        return ok(tokens, messageSource)
                .message(messageSource.getMessage(DEFAULT_SUCCESSFUL_AUTHENTICATION_MESSAGE));
    }

    public static BaseResponseBuilder<Object> deleted(MessageSource messageSource) {
        return ok(messageSource)
                .message(messageSource.getMessage(DEFAULT_DELETED_MESSAGE));
    }
}
