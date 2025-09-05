package org.example;

import org.example.context.AbstractRefreshableWebApplicationContext;
import org.example.context.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class BaseHttpServlet extends HttpServlet {

    protected ApplicationContext webApplicationContext;

    public BaseHttpServlet(ApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    // WebIoc的初始化以及配置
    @Override
    public void init() {
        initServletContext();
    }

    /**
     * 设置父子容器关系，并刷新子上下文
     */
    private void initServletContext() {
        final ServletContext servletContext = getServletContext();
        final ServletConfig servletConfig = getServletConfig();

        ApplicationContext rootContext = (ApplicationContext) servletContext.getAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        if (!ObjectUtils.isEmpty(webApplicationContext)) {
            if (!(this.webApplicationContext instanceof AnnotationConfigApplicationContext)) {
                AbstractRefreshableWebApplicationContext wac =
                        (AbstractRefreshableWebApplicationContext) this.webApplicationContext;

                if (wac.getParent() == null) {
                    wac.setParent(rootContext);
                }
                wac.setServletContext(servletContext);
                wac.setServletConfig(servletConfig);
                // web容器刷新
                wac.refresh();
            }
        }
        // 预留接口给子类实现
        onRefresh(webApplicationContext);
    }

    protected abstract void onRefresh(ApplicationContext webApplicationContext);
}