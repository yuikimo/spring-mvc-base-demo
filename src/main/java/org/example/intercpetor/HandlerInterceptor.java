package org.example.intercpetor;

import org.example.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    default boolean preHandler(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    default void postHandler(HttpServletRequest request, HttpServletResponse response) {

    }

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod,
                                 Exception exception) {

    }
}
