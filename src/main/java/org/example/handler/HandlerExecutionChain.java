package org.example.handler;

import org.example.intercpetor.HandlerInterceptor;
import org.example.intercpetor.MappedInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * HandlerMethod的进一步封装，用于设置方法拦截器
 */
public class HandlerExecutionChain {

    private final HandlerMethod handlerMethod;

    private List<HandlerInterceptor> interceptors = new ArrayList<>();

    public HandlerExecutionChain(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setInterceptors(List<HandlerInterceptor> interceptors) {
        // 路径映射匹配
        for (HandlerInterceptor interceptor : interceptors) {
            if (interceptor instanceof MappedInterceptor) {
                if (((MappedInterceptor) interceptor).match(handlerMethod.getPath())) {
                    this.interceptors.add(interceptor);
                }
            } else {
                this.interceptors.add(interceptor);
            }
        }
    }

    // 多个拦截器执行，一旦有一个拦截器返回false，整个链路可以崩掉
    public boolean applyPreInterceptor(HttpServletRequest req, HttpServletResponse resp) {
        for (HandlerInterceptor interceptor : this.interceptors) {
            if (!interceptor.preHandler(req, resp)) {
                return false;
            }
        }
        return true;
    }

    public void applyPostInterceptor(HttpServletRequest req, HttpServletResponse resp) {
        for (HandlerInterceptor interceptor : this.interceptors) {
            interceptor.postHandler(req, resp);
        }
    }

    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, HandlerMethod handlerMethod,
                                Exception exception) {
        for (HandlerInterceptor interceptor : this.interceptors) {
            interceptor.afterCompletion(req, resp, handlerMethod, exception);
        }
    }

}
