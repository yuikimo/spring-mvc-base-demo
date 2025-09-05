package org.example.support;

import org.example.convert.ConverterRegistry;
import org.example.intercpetor.InterceptorRegistry;

import java.util.ArrayList;
import java.util.List;

public class WebMvcComposite implements WebMvcConfigurer {

    private List<WebMvcConfigurer> webMvcConfigurers = new ArrayList<>();

    public void addWebMvcConfigurers(List<WebMvcConfigurer> webMvcConfigurers) {
        this.webMvcConfigurers.addAll(webMvcConfigurers);
    }

    @Override
    public void addIntercept(InterceptorRegistry registry) {
        for (WebMvcConfigurer webMvcConfigurer : webMvcConfigurers) {
            // 将拦截器注册中心传递给 WebMvcConfigurer 实现类
            webMvcConfigurer.addIntercept(registry);
        }
    }

    @Override
    public void addConverter(ConverterRegistry registry){
        for (WebMvcConfigurer webMvcConfigurer : webMvcConfigurers) {
            webMvcConfigurer.addConverter(registry);
        }
    }
}
