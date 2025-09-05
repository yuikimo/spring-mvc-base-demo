package org.example.support;

import org.example.convert.ConverterRegistry;
import org.example.intercpetor.InterceptorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {

    private WebMvcComposite webMvcComposite = new WebMvcComposite();

    @Autowired(required=false)
    public void setWebMvcComposite(List<WebMvcConfigurer> webMvcConfigurers) {
        webMvcComposite.addWebMvcConfigurers(webMvcConfigurers);
    }

    @Override
    protected void getIntercept(InterceptorRegistry registry) {
        webMvcComposite.addIntercept(registry);
    }

    @Override
    protected void getConverter(ConverterRegistry registry) {
        webMvcComposite.addConverter(registry);
    }
}
