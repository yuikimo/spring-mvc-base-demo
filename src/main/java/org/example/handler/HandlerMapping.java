package org.example.handler;

import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 映射器规范
 */
public interface HandlerMapping extends Ordered {

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
