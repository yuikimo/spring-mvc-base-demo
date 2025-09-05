package org.example.resolver;

import org.example.annotation.RequestBody;
import org.example.annotation.RequestParam;
import org.example.convert.ConvertComposite;
import org.example.handler.HandlerMethod;
import org.example.multipart.MultipartFile;
import org.example.support.WebServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 获取普通数据
 */
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 只接受普通对象，基础数据类型
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final Class<?> parameterType = parameter.getParameterType();
        if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
            return false;
        }
        if (isMultipartFile(parameter)) {
            return false;
        }
        if (parameterType == Map.class) {
            return false;
        }
        if (parameter.hasParameterAnnotation(RequestBody.class)) {
            return false;
        }
        return true;
    }

    /**
     * 需要处理对象以及基本数据类型
     * @param parameter
     * @param handlerMethod
     * @param webServletRequest
     * @param convertComposite
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
                                  WebServletRequest webServletRequest,
                                  ConvertComposite convertComposite) throws Exception {
        final HttpServletRequest request = webServletRequest.getRequest();
        final Class<?> parameterType = parameter.getParameterType();
        // 基础数据类型
        if (BeanUtils.isSimpleProperty(parameterType)) {
            String name = parameter.getParameterName();
            if (parameter.hasParameterAnnotation(RequestParam.class)) {
                final RequestParam parameterAnnotation = parameter.getParameterAnnotation(RequestParam.class);
                name = parameterAnnotation.value().equals("") ? parameter.getParameterName() :
                       parameterAnnotation.value();
            }
            return convertComposite.convert(handlerMethod, parameterType, request.getParameter(name));
        } else {
            // 对象
            // 0.如果当前标注了RequestParam则报错
            if (parameter.hasParameterAnnotation(RequestParam.class)) {
                throw new IllegalArgumentException(
                        handlerMethod.getBean().getClass().getName() + " " + handlerMethod.getMethod().getName() +
                        "@RequestParam  不支持标注在对象上");
            }
            // 1.获取所有参数
            final Map<String, String[]> parameterMap = request.getParameterMap();
            // 2.创建对象
            final Object o = ReflectionUtils.accessibleConstructor(parameterType).newInstance();
            // 3.遍历对象中的字段赋值
            final Field[] fields = parameterType.getDeclaredFields();
            initObject(parameterMap, o, fields, handlerMethod, convertComposite);
            return o;
        }
    }

    public void initObject(Map<String, String[]> parameterMap, Object o, Field[] fields, HandlerMethod handlerMethod,
                           ConvertComposite convertComposite) throws Exception {
        for (Field field : fields) {
            final Class<?> type = field.getType();
            if (BeanUtils.isSimpleProperty(type)) {
                // 基础数据类型
                if (parameterMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(o, convertComposite.convert(handlerMethod, type, parameterMap.get(field.getName())[0]));
                    field.setAccessible(false);
                }
            } else {
                // 对象数据类型
                final Object o1 = ReflectionUtils.accessibleConstructor(type).newInstance();
                // 属性填充
                final Field[] fields1 = type.getDeclaredFields();
                initObject(parameterMap, o1, fields1, handlerMethod, convertComposite);
                field.setAccessible(true);
                field.set(o, o1);
                field.setAccessible(false);
            }
        }
    }

    // 如果是文件上传，或者List中泛型写的是文件
    private boolean isMultipartFile(MethodParameter parameter) {
        final Class<?> type = parameter.getParameterType();
        if (MultipartFile.class == type || type.getComponentType() == MultipartFile.class) {
            return true;
        } else if (Collection.class == type || List.class == type) {
            final Type parameterGenericParameterType = parameter.getGenericParameterType();
            ParameterizedType pt = (ParameterizedType) parameterGenericParameterType;
            return pt.getActualTypeArguments()[0] == MultipartFile.class;
        }
        return false;
    }
}
