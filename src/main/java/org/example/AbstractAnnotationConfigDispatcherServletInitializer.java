package org.example;

import org.example.context.AnnotationConfigWebApplicationContext;
import org.example.context.WebApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ObjectUtils;

import javax.servlet.Filter;

public abstract class AbstractAnnotationConfigDispatcherServletInitializer
        extends AbstractDispatcherServletInitializer {

    @Override
    protected AnnotationConfigApplicationContext createRootApplicationContext() {
        final Class<?>[] rootConfigClasses = getRootConfigClasses();
        if (!ObjectUtils.isEmpty(rootConfigClasses)) {
            final AnnotationConfigApplicationContext rootContext = new AnnotationConfigApplicationContext();
            rootContext.register(rootConfigClasses);
            return rootContext;
        }
        return null;

    }

    @Override
    protected WebApplicationContext createWebApplicationContext() {
        final Class<?>[] webConfigClasses = getWebConfigClasses();
        if (!ObjectUtils.isEmpty(webConfigClasses)) {
            final AnnotationConfigWebApplicationContext webApplicationContext =
                    new AnnotationConfigWebApplicationContext();
            webApplicationContext.register(webConfigClasses);
            return webApplicationContext;
        }
        return null;

    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[0];
    }
}
