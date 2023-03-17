package com.alihmzyv.todorestservice.exception;

public class DuplicateNotAllowedException extends RuntimeException {
    public DuplicateNotAllowedException() {
    }

    public DuplicateNotAllowedException(String message) {
        super(message);
    }

    public DuplicateNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateNotAllowedException(Throwable cause) {
        super(cause);
    }

    public DuplicateNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
