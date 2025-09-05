package org.example.support;

import org.example.convert.ConverterRegistry;
import org.example.intercpetor.InterceptorRegistry;

/**
 * 定义拓展点规范供子类实现,都是default
 */
public interface WebMvcConfigurer {

    default void addIntercept(InterceptorRegistry registry) {
    }

    default void addConverter(ConverterRegistry registry) {
    }
}