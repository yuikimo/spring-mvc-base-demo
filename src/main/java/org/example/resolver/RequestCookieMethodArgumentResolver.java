package org.example.resolver;

import org.example.annotation.Cookie;
import org.example.convert.ConvertComposite;
import org.example.exception.NotFoundException;
import org.example.handler.HandlerMethod;
import org.example.support.WebServletRequest;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;

/**
 * 解析cookie当中的参数
 */
public class RequestCookieMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Cookie.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest,
                                  ConvertComposite convertComposite) throws Exception {
        final HttpServletRequest request = webServletRequest.getRequest();
        final Cookie parameterAnnotation = parameter.getParameterAnnotation(Cookie.class);
        String name =
                parameterAnnotation.value().equals("") ? parameter.getParameterName() : parameterAnnotation.value();

        // 获取所有的 Cookie
        final javax.servlet.http.Cookie[] cookies = request.getCookies();
        // 遍历
        for (javax.servlet.http.Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return convertComposite.convert(handlerMethod, parameter.getParameterType(), cookie.getValue());
            }
        }

        if (parameterAnnotation.require()) {
            throw new NotFoundException(handlerMethod.getPath() + "cookie没有携带：" + name);
        }
        return null;
    }
}
