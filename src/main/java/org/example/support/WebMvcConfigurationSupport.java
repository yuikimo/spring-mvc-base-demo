package org.example.support;

import org.example.adapter.HandlerMethodAdapter;
import org.example.adapter.RequestMappingHandlerMethodAdapter;
import org.example.convert.ConvertHandler;
import org.example.convert.ConverterRegistry;
import org.example.handler.HandlerMapping;
import org.example.handler.RequestMappingHandlerMapping;
import org.example.intercpetor.InterceptorRegistry;
import org.example.intercpetor.MappedInterceptor;
import org.example.resolver.DefaultHandlerExceptionResolver;
import org.example.resolver.ExceptionHandlerExceptionResolver;
import org.example.resolver.HandlerExceptionResolver;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

/**
 * 初始化组件
 */
public abstract class WebMvcConfigurationSupport {

    protected abstract void getIntercept(InterceptorRegistry registry);

    protected abstract void getConverter(ConverterRegistry registry);

    @Bean
    public HandlerMapping handlerMapping() {
        final RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setOrder(0);

        final InterceptorRegistry registry = new InterceptorRegistry();
        getIntercept(registry);
        // 通过 registry 获取 MappedInterceptor 并添加
        // 添加拦截器
        final List<MappedInterceptor> interceptors = registry.getInterceptors();
        requestMappingHandlerMapping.addHandlerInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }

    @Bean
    public HandlerMethodAdapter handlerMethodAdapter() {
        final RequestMappingHandlerMethodAdapter requestMappingHandlerMethodAdapter =
                new RequestMappingHandlerMethodAdapter();
        requestMappingHandlerMethodAdapter.setOrder(0);

        final ConverterRegistry registry = new ConverterRegistry();
        getConverter(registry);
        // 通过 registry 获取 自定义拦截器并添加
        final Map<Class, ConvertHandler> converts = registry.getConverts();
        requestMappingHandlerMethodAdapter.addConvertMap(converts);
        return requestMappingHandlerMethodAdapter;
    }

    @Bean
    public HandlerExceptionResolver defaultHandlerExceptionResolver() {
        final DefaultHandlerExceptionResolver defaultHandlerExceptionResolver = new DefaultHandlerExceptionResolver();
        defaultHandlerExceptionResolver.setOrder(1);
        return defaultHandlerExceptionResolver;
    }

    @Bean
    public HandlerExceptionResolver exceptionHandlerExceptionResolver() {
        final ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver =
                new ExceptionHandlerExceptionResolver();
        exceptionHandlerExceptionResolver.setOrder(0);
        return exceptionHandlerExceptionResolver;
    }
}
