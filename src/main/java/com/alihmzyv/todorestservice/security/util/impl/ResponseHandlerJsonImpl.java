package com.alihmzyv.todorestservice.security.util.impl;

import com.alihmzyv.todorestservice.security.util.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@RequiredArgsConstructor
@Component
public class ResponseHandlerJsonImpl implements ResponseHandler {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void handle(HttpServletResponse response, Object responseBody) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsonString = objectMapper.writeValueAsString(responseBody);
        out.print(jsonString);
        out.flush();
    }
}
