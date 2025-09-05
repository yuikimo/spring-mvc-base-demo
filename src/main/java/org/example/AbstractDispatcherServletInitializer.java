package org.example;

import org.example.context.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ObjectUtils;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public abstract class AbstractDispatcherServletInitializer implements WebApplicationInitializer {

    protected static final String DEFAULT_SERVLET_NAME = "dispatcher";
    protected static final String DEFAULT_FILTERS_NAME = "filter";

    protected static final Integer M = 1024 * 1024;

    @Override
    public void onStartUp(ServletContext servletContext) {
        // 创建父容器
        final AnnotationConfigApplicationContext rootApplicationContext = createRootApplicationContext();
        // 存储到 ServletContext 中
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                                    rootApplicationContext);

        // 刷新父容器 -> 在源码当中通过 servlet 事件进行 refresh
        rootApplicationContext.refresh();

        // 创建 Dispatcher
        final WebApplicationContext webApplicationContext = createWebApplicationContext();
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);

        // 设置配置文件信息
        // 在Web容器启动后自动加载并初始化 Servlet
        dynamic.setLoadOnStartup(1);
        dynamic.addMapping(getServletMappings());
        final MultipartConfigElement multipartConfigElement = new MultipartConfigElement(null, 5 * M, 5 * M, 5);
        dynamic.setMultipartConfig(multipartConfigElement);

        // 添加过滤器
        final Filter[] filters = getServletFilters();
        if (!ObjectUtils.isEmpty(filters)) {
            for (Filter filter : filters) {
                servletContext.addFilter(DEFAULT_FILTERS_NAME, filter);
            }
        }

    }

    // 获取父 | 子容器
    protected abstract AnnotationConfigApplicationContext createRootApplicationContext();

    protected abstract WebApplicationContext createWebApplicationContext();

    // 获取包扫描配置类
    protected abstract Class<?>[] getRootConfigClasses();

    protected abstract Class<?>[] getWebConfigClasses();

    // 映射器，默认拦截 /
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // 过滤器，交给子类实现
    protected abstract Filter[] getServletFilters();
}
