package org.example.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.annotation.RequestBody;
import org.example.convert.ConvertComposite;
import org.example.handler.HandlerMethod;
import org.example.support.WebServletRequest;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * 处理json
 */
public class RequestRequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest,
                                  ConvertComposite convertComposite) throws Exception {
        final String json = getJson(webServletRequest.getRequest());
        return objectMapper.readValue(json, parameter.getParameterType());
    }

    // 获取请求体
    private String getJson(HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        String line = null;

        try (final BufferedReader reader = request.getReader()) {
            while (line != (line = reader.readLine())) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
