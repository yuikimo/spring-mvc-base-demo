package org.example.resolver;

import org.example.annotation.PathVariable;
import org.example.convert.ConvertComposite;
import org.example.handler.HandlerMethod;
import org.example.support.WebServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析路径参数
 */
public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(PathVariable.class)) {
            return false;
        }
        return !(parameter.getParameterType() == Map.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest,
                                  ConvertComposite convertComposite) throws Exception {
        String name;
        Object result = null;
        // 1.获取 PathVariable 中的变量
        final PathVariable parameterAnnotation = parameter.getParameterAnnotation(PathVariable.class);
        name = parameterAnnotation.value().equals("") ? parameter.getParameterName() : parameterAnnotation.value();
        // 1.以 / 分割源 Path，找到变量，保存下标以及对应的变量
        final String path = handlerMethod.getPath();
        int index = -1;
        String[] split = path.split("/");
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.contains("{") && s.contains("}") && s.contains(name)) {
                index = i;
                break;
            }
        }
        final HttpServletRequest request = webServletRequest.getRequest();
        // 2.以 / 分割请求 Path，根据上一步找到的下标，找到对应的值，放入result
        split = request.getRequestURI().split("/");
        if (index != -1) {
            result = split[index];
        }
        return convertComposite.convert(handlerMethod, parameter.getParameterType(), result);
    }
}
