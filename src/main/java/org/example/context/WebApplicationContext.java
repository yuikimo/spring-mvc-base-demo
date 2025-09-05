package org.example.context;

import org.springframework.context.ApplicationContext;

/**
 * 顶层 WebIoc 容器接口
 */
public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    String CHILD_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".CHILD";

}
