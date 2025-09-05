package org.example.intercpetor;

import org.example.handler.HandlerMethod;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class MappedInterceptor implements HandlerInterceptor {

    // 拦截器
    private HandlerInterceptor interceptor;
    // 拦截路径
    private List<String> includePatterns;
    // 排除路径
    private List<String> excludePatterns;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public MappedInterceptor(InterceptorRegistration interceptorRegistration) {
        this.interceptor = interceptorRegistration.getInterceptor();
        this.includePatterns = interceptorRegistration.getIncludePatterns();
        this.excludePatterns = interceptorRegistration.getExcludePatterns();
    }

    // 路径匹配
    public boolean match(String path) {
        // 排除了，则不加入
        for (String excludePattern : this.excludePatterns) {
            if (antPathMatcher.match(excludePattern, path)) {
                return false;
            }
        }
        // 如果匹配，则加入
        for (String includePattern : this.includePatterns) {
            if (antPathMatcher.match(includePattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response) {
        return interceptor.preHandler(request, response);
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response) {
        interceptor.postHandler(request, response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                HandlerMethod handlerMethod, Exception exception) {
        interceptor.afterCompletion(request, response, handlerMethod, exception);
    }
}
