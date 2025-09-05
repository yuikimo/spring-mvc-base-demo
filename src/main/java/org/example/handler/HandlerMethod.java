package org.example.handler;

import org.example.annotation.RequestMethod;
import org.example.convert.ConvertHandler;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HandlerMethod {

    protected Object bean;

    protected Class type;

    protected Method method;

    protected String path;

    protected RequestMethod[] requestMethods = new RequestMethod[0];

    // 增强的参数 Plus
    protected MethodParameter[] parameters = new MethodParameter[0];

    private Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();

    private Map<Class, ConvertHandler> convertHandlerMap = new HashMap<>();

    public HandlerMethod() {
    }

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.type = bean.getClass();
        this.method = method;

        final Parameter[] parameters = method.getParameters();
        MethodParameter[] methodParameters = new MethodParameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            methodParameters[i] = new MethodParameter(method, i);
        }

        this.parameters = methodParameters;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    // 如果为空，则设置所有请求类型
    public void setRequestMethods(RequestMethod[] requestMethods) {
        if (ObjectUtils.isEmpty(requestMethods)) {
            requestMethods = RequestMethod.values();
        }
        this.requestMethods = requestMethods;
    }

    public RequestMethod[] getRequestMethods() {
        return requestMethods;
    }

    public Method getMethod() {
        return method;
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(MethodParameter[] parameters) {
        this.parameters = parameters;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public void setExceptionHandlerMethodMap(Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap) {
        this.exceptionHandlerMethodMap = exceptionHandlerMethodMap;
    }

    public Map<Class, ExceptionHandlerMethod> getExceptionHandlerMethodMap() {
        return exceptionHandlerMethodMap;
    }

    public void setConvertHandlerMap(Map<Class, ConvertHandler> convertHandlerMap) {
        this.convertHandlerMap = convertHandlerMap;
    }

    public Map<Class, ConvertHandler> getConvertHandlerMap() {
        return convertHandlerMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerMethod that = (HandlerMethod) o;
        return Objects.equals(path, that.path) &&
               Arrays.equals(requestMethods, that.requestMethods);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(path);
        result = 31 * result + Arrays.hashCode(requestMethods);
        return result;
    }

    @Override
    public String toString() {
        return "HandlerMethod{" +
               "bean=" + bean +
               ", type=" + type +
               ", method=" + method +
               ", path='" + path + '\'' +
               ", requestMethods=" + Arrays.toString(requestMethods) +
               ", parameters=" + Arrays.toString(parameters) +
               '}';
    }
}
