package com.alihmzyv.todorestservice.security.util;

import jakarta.servlet.http.HttpServletResponse;

public interface ResponseHandler {
    void handle(HttpServletResponse response, Object responseBody);
}
