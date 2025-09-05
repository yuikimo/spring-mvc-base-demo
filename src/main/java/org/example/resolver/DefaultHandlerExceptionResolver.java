package org.example.resolver;

import org.example.exception.ConvertCastException;
import org.example.exception.HttpRequestMethodNotSupport;
import org.example.exception.NotFoundException;
import org.example.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认异常解析器，尽可能的枚举所有上层发生的异常进行处理
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {

    private int order;

    @Override
    public Boolean resolveException(HttpServletRequest request, HttpServletResponse response,
                                    HandlerMethod handlerMethod, Exception exception) throws Exception {
        final Class<? extends Exception> type = exception.getClass();
        if (type == ConvertCastException.class) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
            return true;
        } else if (type == HttpRequestMethodNotSupport.class) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, exception.getMessage());
            return true;
        } else if (type == NotFoundException.class) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
            return true;
        }
        return false;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
