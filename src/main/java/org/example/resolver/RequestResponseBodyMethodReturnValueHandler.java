package org.example.resolver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.annotation.ResponseBody;
import org.example.support.WebServletRequest;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class RequestResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    // 避免对应实体类没有 Get方法
    final ObjectMapper objectMapper =
            new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    @Override
    public boolean supportsReturnType(Method method) {
        // 判断方法所在的类以及方法上是否有 ResponseBody 注解
        return AnnotatedElementUtils.hasAnnotation(method.getDeclaringClass(), ResponseBody.class) ||
               AnnotatedElementUtils.hasAnnotation(method, ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, WebServletRequest webServletRequest) throws Exception {
        final HttpServletResponse response = webServletRequest.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(objectMapper.writeValueAsString(returnValue));
        response.getWriter().flush();
    }
}
