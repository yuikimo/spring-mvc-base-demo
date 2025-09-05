package org.example.resolver;

import org.example.annotation.ControllerAdvice;
import org.example.annotation.ExceptionHandler;
import org.example.handler.ExceptionHandlerMethod;
import org.example.handler.HandlerMethod;
import org.example.handler.ServletInvocableMethod;
import org.example.support.WebServletRequest;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExceptionHandlerExceptionResolver extends ApplicationObjectSupport implements HandlerExceptionResolver,
                                                                                           InitializingBean {
    private int order;

    private Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();

    private HandlerMethodArgumentResolverComposite methodArgumentResolverComposite =
            new HandlerMethodArgumentResolverComposite();

    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite =
            new HandlerMethodReturnValueHandlerComposite();


    @Override
    public Boolean resolveException(HttpServletRequest request, HttpServletResponse response,
                                    HandlerMethod handlerMethod, Exception exception) throws Exception {
        final ExceptionHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);

        if (!ObjectUtils.isEmpty(exceptionHandlerMethod)) {
            final WebServletRequest webServletRequest = new WebServletRequest(request, response);

            final ServletInvocableMethod servletInvocableMethod = new ServletInvocableMethod();
            servletInvocableMethod.setExceptionHandlerMethodMap(exceptionHandlerMethodMap);
            servletInvocableMethod.setReturnValueHandlerComposite(returnValueHandlerComposite);
            servletInvocableMethod.setResolverComposite(methodArgumentResolverComposite);
            servletInvocableMethod.setHandlerMethod(exceptionHandlerMethod);

            Object[] args = {exception, exceptionHandlerMethod};
            servletInvocableMethod.invokeAndHandle(webServletRequest, exceptionHandlerMethod, args);
            return true;
        }
        return false;
    }

    public ExceptionHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception ex) {
        Class aClass = ex.getClass();
        ExceptionHandlerMethod exceptionHandlerMethod = null;

        // 找局部
        if (handlerMethod != null && handlerMethod.getExceptionHandlerMethodMap().size() != 0) {
            final Map<Class, ExceptionHandlerMethod> exMap = handlerMethod.getExceptionHandlerMethodMap();
            // 找不到再从父类中找
            while (exceptionHandlerMethod == null) {
                exceptionHandlerMethod = exMap.get(aClass);
                aClass = aClass.getSuperclass();
                if (aClass == Throwable.class && exceptionHandlerMethod == null) {
                    break;
                }
            }
        }

        aClass = ex.getClass();

        // 再找全局
        while (exceptionHandlerMethod == null) {
            exceptionHandlerMethod = this.exceptionHandlerMethodMap.get(aClass);
            aClass = aClass.getSuperclass();
            if (aClass == Throwable.class && exceptionHandlerMethod == null) {
                break;
            }
        }
        return exceptionHandlerMethod;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionHandlerMethodMap.putAll(initExceptionHandler());
        methodArgumentResolverComposite.addResolvers(getDefaultArgumentResolver());
        returnValueHandlerComposite.addMethodReturnValueHandlers(getDefaultMethodReturnValueHandler());
    }

    // 初始化返回值处理器
    public List<HandlerMethodReturnValueHandler> getDefaultMethodReturnValueHandler() {
        final ArrayList<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();
        handlerMethodReturnValueHandlers.add(new RequestResponseBodyMethodReturnValueHandler());

        return handlerMethodReturnValueHandlers;
    }

    // 初始化参数解析器
    public List<HandlerMethodArgumentResolver> getDefaultArgumentResolver() {
        final ArrayList<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();
        handlerMethodArgumentResolvers.add(new ServletRequestMethodArgumentResolver());
        handlerMethodArgumentResolvers.add(new ServletResponseMethodArgumentResolver());

        return handlerMethodArgumentResolvers;
    }

    // 初始化异常解析器
    public Map<Class, ExceptionHandlerMethod> initExceptionHandler() {
        final ApplicationContext context = obtainApplicationContext();
        Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();
        // 从容器中拿带有 @ControllerAdvice bean
        final String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, ControllerAdvice.class);
        for (String name : names) {
            final Class<?> type = context.getType(name);
            final Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                if (AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class)) {
                    final ExceptionHandler exceptionHandler =
                            AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
                    final Class<? extends Throwable> exType = exceptionHandler.value();
                    exceptionHandlerMethodMap.put(exType, new ExceptionHandlerMethod(context.getBean(name), method));
                }
            }
        }
        return exceptionHandlerMethodMap;
    }
}
