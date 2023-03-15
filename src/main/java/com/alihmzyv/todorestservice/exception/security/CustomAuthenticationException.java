package com.alihmzyv.todorestservice.exception.security;

public class CustomAuthenticationException extends SecurityException {
    public CustomAuthenticationException() {
    }

    public CustomAuthenticationException(String message) {
        super(message);
    }

    public CustomAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomAuthenticationException(Throwable cause) {
        super(cause);
    }

    public CustomAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
