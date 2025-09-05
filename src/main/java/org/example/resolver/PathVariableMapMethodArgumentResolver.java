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
 * 解析路径参数转为map
 */
public class PathVariableMapMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(PathVariable.class)) {
            return false;
        }
        return parameter.getParameterType() == Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest,
                                  ConvertComposite convertComposite) throws Exception {
        // 目标：将所有路径上的参数解析解析组装成 Map 返回
        Map<Integer, String> indexMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        // 1.以 / 分割源 Path，找到变量，保存下标以及对应的变量
        final String path = handlerMethod.getPath();
        String[] split = path.split("/");
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.contains("{") && s.contains("}")) {
                indexMap.put(i, s.substring(1, s.length() - 1));
            }
        }
        final HttpServletRequest request = webServletRequest.getRequest();
        // 2.以 / 分割请求 Path，根据上一步找到的下标，找到对应的值，放入 resultMap
        split = request.getRequestURI().split("/");
        for (Integer index : indexMap.keySet()) {
            resultMap.put(indexMap.get(index), split[index]);
        }
        return resultMap;
    }
}
