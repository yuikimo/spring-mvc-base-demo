package org.example;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Set;

@HandlesTypes(WebApplicationInitializer.class)
public class WebServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> webApplications, ServletContext servletContext) throws ServletException {
        if (!ObjectUtils.isEmpty(webApplications)) {
            final ArrayList<WebApplicationInitializer> initializers = new ArrayList<>(webApplications.size());

            for (Class<?> webApplication : webApplications) {
                if (!webApplication.isInterface() &&
                    !Modifier.isAbstract(webApplication.getModifiers()) &&
                    WebApplicationInitializer.class.isAssignableFrom(webApplication)) {

                    try {
                        initializers.add((WebApplicationInitializer)
                                                 ReflectionUtils.accessibleConstructor(webApplication)
                                                                .newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!ObjectUtils.isEmpty(initializers)) {
                for (WebApplicationInitializer initializer : initializers) {
                    initializer.onStartUp(servletContext);
                }
            }
        }
    }
}
