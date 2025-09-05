package org.example.resolver;

import org.example.convert.ConvertComposite;
import org.example.handler.HandlerMethod;
import org.example.support.WebServletRequest;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletResponse;

public class ServletResponseMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == HttpServletResponse.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest,
                                  ConvertComposite convertComposite) throws Exception {
        return webServletRequest.getResponse();
    }
}
