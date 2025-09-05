package org.example.resolver;

import org.example.support.WebServletRequest;

import java.lang.reflect.Method;

public interface HandlerMethodReturnValueHandler {

    // 当前method 是否支持
    boolean supportsReturnType(Method method);

    // 执行
    void handleReturnValue(Object returnValue, WebServletRequest webServletRequest) throws Exception;
}

