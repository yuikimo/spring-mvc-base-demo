package org.example.context;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.Nullable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * 存放servletContext定义规范接口类
 */
public interface ConfigurableWebApplicationContext extends WebApplicationContext, ConfigurableApplicationContext {

    void setServletContext(@Nullable ServletContext servletContext);


    void setServletConfig(@Nullable ServletConfig servletConfig);


    ServletConfig getServletConfig();


    ServletContext getServletContext();

}
