package com.alihmzyv.todorestservice.consts;

public class Validation {
    public static final String DEFAULT_NOT_NULL_MESSAGE = "Field cannot be null or empty";
    public static final String DEFAULT_VALID_EMAIL_MESSAGE = "Email address should be a valid email address";
    public static final String DEFAULT_STRONG_PASSWORD_MESSAGE =
            "Password should have " +
            " at least one uppercase letter," +
            " one lowercase letter," +
            " one digit, and" +
            " a minimum length of 8";

    public static final String DEFAULT_PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
}
