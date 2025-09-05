package org.example.resolver;

import org.example.annotation.RequestHeader;
import org.example.convert.ConvertComposite;
import org.example.exception.NotFoundException;
import org.example.handler.HandlerMethod;
import org.example.support.WebServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 获取请求头中的指定内容
 */
public class RequestHeaderMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeader.class) && parameter.getParameterType() != Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest,
                                  ConvertComposite convertComposite) throws Exception {
        final HttpServletRequest request = webServletRequest.getRequest();
        final RequestHeader parameterAnnotation = parameter.getParameterAnnotation(RequestHeader.class);
        String name = parameterAnnotation.value().equals("") ?
                      parameter.getParameterName() :
                      parameterAnnotation.value();

        if (parameterAnnotation.require() && ObjectUtils.isEmpty(request.getHeader(name))) {
            throw new NotFoundException(handlerMethod.getPath() + "请求头没有携带：" + name);
        }

        return convertComposite.convert(handlerMethod, parameter.getParameterType(), request.getHeader(name));
    }
}
