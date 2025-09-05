package org.example.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * 用户想拿到 ServletContext | ServletConfig 需要自行实现 XXAware,通过该接口获取属性
 */
public class ServletContextAwareProcessor implements BeanPostProcessor {

    private ServletContext servletContext;
    private ServletConfig servletConfig;

    public ServletContextAwareProcessor(ServletContext servletContext, ServletConfig servletConfig) {
        this.servletContext = servletContext;
        this.servletConfig = servletConfig;
    }

    /**
     * 判断是否为目标类，同时提供相应的配置
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean != null && bean instanceof ServletContextAware) {
            ((ServletContextAware) bean).setServletContext(this.servletContext);
        }

        if (bean != null && bean instanceof ServletConfigAware) {
            ((ServletConfigAware) bean).setServletConfig(this.servletConfig);
        }

        return bean;
    }
}
