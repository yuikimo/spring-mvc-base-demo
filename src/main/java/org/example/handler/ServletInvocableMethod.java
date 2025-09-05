package org.example.handler;

import org.example.convert.ConvertComposite;
import org.example.resolver.HandlerMethodArgumentResolverComposite;
import org.example.resolver.HandlerMethodReturnValueHandler;
import org.example.resolver.HandlerMethodReturnValueHandlerComposite;
import org.example.support.WebServletRequest;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * 用于执行HandlerMethod, 也会保存执行HandlerMethod的基础组件
 */
public class ServletInvocableMethod extends HandlerMethod {

    private HandlerMethod handlerMethod;

    private HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();

    private ConvertComposite convertComposite = new ConvertComposite();

    private HandlerMethodReturnValueHandlerComposite returnValueComposite =
            new HandlerMethodReturnValueHandlerComposite();

    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    public ServletInvocableMethod(Object bean, Method method) {
        super(bean, method);
    }

    public ServletInvocableMethod() {

    }

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public HandlerMethodArgumentResolverComposite getResolverComposite() {
        return resolverComposite;
    }

    public void setResolverComposite(HandlerMethodArgumentResolverComposite resolverComposite) {
        this.resolverComposite = resolverComposite;
    }

    public ConvertComposite getConvertComposite() {
        return convertComposite;
    }

    public void setConvertComposite(ConvertComposite convertComposite) {
        this.convertComposite = convertComposite;
    }

    public void setReturnValueHandlerComposite(HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite) {
        this.returnValueComposite = returnValueHandlerComposite;
    }

    public void invokeAndHandle(WebServletRequest webServletRequest, HandlerMethod handler,
                                Object... providerArgs) throws Exception {
        // 1.获取参数
        final Object[] methodArguments = getMethodArguments(webServletRequest, handler, providerArgs);
        // 2.反射执行方法
        final Object returnValue = doInvoke(methodArguments);
        // 3.选择返回值处理器，处理执行后的返回值
        this.returnValueComposite.doInvoke(returnValue, handler.getMethod(), webServletRequest);
    }

    public Object[] getMethodArguments(WebServletRequest webServletRequest, HandlerMethod handlerMethod,
                                       Object... providerArgs) throws Exception {
        final MethodParameter[] parameters = handlerMethod.getParameters();
        Object args[] = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            final MethodParameter parameter = parameters[i];
            // 初始化，用于获取 Parameter.getName()
            parameter.initParameterNameDiscovery(nameDiscoverer);

            args[i] = findProviderArgs(parameter, providerArgs);
            if (args[i] != null) {
                continue;
            }

            // 参数解析器
            if (!this.resolverComposite.supportsParameter(parameter)) {
                throw new Exception("没有参数解析器解析参数:" + parameter.getParameterType());
            }
            args[i] = this.resolverComposite.resolveArgument(parameter, handlerMethod, webServletRequest,
                                                             this.convertComposite);
        }
        return args;
    }

    private Object findProviderArgs(MethodParameter parameter, Object[] providerArgs) {
        final Class<?> parameterType = parameter.getParameterType();
        // 遍历参数
        for (Object providerArg : providerArgs) {
            if (parameterType.isInstance(providerArg)) {
                return providerArg;
            }
        }
        return null;
    }

    public Object doInvoke(Object args[]) throws Exception {
        final Object returnValue = this.handlerMethod.getMethod().invoke(this.handlerMethod.getBean(), args);
        return returnValue;
    }

}
