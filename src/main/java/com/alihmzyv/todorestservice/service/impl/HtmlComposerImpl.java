package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.service.HtmlComposer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class HtmlComposerImpl implements HtmlComposer {
    private final TemplateEngine templateEngine;

    @Override
    public String composeHtml(String template, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        return templateEngine.process(template, context);
    }
}
