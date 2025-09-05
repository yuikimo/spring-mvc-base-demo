package org.example.adapter;

import org.example.handler.HandlerMethod;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 适配器接口规范
 */
public interface HandlerMethodAdapter extends Ordered {

    boolean support(HandlerMethod handlerMethod);

    void handler(HttpServletRequest req, HttpServletResponse res, HandlerMethod handlerMethod) throws Exception;
}
