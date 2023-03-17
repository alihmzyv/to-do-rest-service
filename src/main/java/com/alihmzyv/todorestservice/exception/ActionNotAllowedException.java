package com.alihmzyv.todorestservice.exception;

public class ActionNotAllowedException extends RuntimeException {
    public ActionNotAllowedException() {
    }

    public ActionNotAllowedException(String message) {
        super(message);
    }

    public ActionNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionNotAllowedException(Throwable cause) {
        super(cause);
    }

    public ActionNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
