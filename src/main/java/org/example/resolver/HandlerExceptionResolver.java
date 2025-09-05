package org.example.resolver;

import org.example.handler.HandlerMethod;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver extends Ordered {

    Boolean resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod,
                             Exception exception) throws Exception;

    @Override
    int getOrder();
}
