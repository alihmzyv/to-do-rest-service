package com.alihmzyv.todorestservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ErrorControllerImpl implements ErrorController {

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) throws Throwable {
        if (request.getAttribute("jakarta.servlet.error.exception") != null) {
            throw (Exception) request.getAttribute("jakarta.servlet.error.exception");
        }
    }
}
