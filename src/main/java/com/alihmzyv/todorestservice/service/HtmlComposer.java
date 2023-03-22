package com.alihmzyv.todorestservice.service;

import java.util.Map;

public interface HtmlComposer {
    String composeHtml(String template, Map<String, Object> data);
}
