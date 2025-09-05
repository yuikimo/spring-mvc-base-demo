# spring-mvc-base-demo
学习了SpringMvc架构，原理，源码后写的一款web框架，主要辅助学习SpringMvc的原理，只留核心功能。
### 架构
<img width="646" height="932" alt="image" src="https://github.com/user-attachments/assets/31f82fdf-46e6-4eaa-a0a1-e892eaed1d57" />

使用说明:
将对应的打包后进行使用

Tomcat
1.打包后引入依赖
```
		 <dependency>
            <groupId>org.example</groupId>
            <artifactId>spring-mvc-demo</artifactId>
            <version>1.0.0</version>
        </dependency>
```
2.创建读取bean类
```
@ComponentScan("org.example")
public class AppConfig {
}
```
3.继承AbstractAnnotationConfigDispatcherServletInitializer 并配置
```
public class Quick extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```
4.启动

SpringBoot
1.打包后引入依赖
```
        <dependency>
            <groupId>org.xhy</groupId>
            <artifactId>xhy-web-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
```
2.启动

拓展点
1.WebMvcConfigurer
用于配置拦截器，全局转换器
```
@EnableWebMvc
@Component
public class WebMvc implements WebMvcConfigurer {

    @Override
    public void addInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(new DemoInterceptor()).addIncludePatterns("/order/**");
    }
}
```
2.全局异常/类型转换
类上标注``@ControllerAdvice/@RestControllerAdvice``
```
@RestControllerAdvice
public class DemoControllerAdvice {
	// 注解中写捕获的异常	
    @ExceptionHandler(Exception.class)
    public String ex(Exception e){
        return e.getMessage();
    }
	// 注解中需要转换的类,方法参数必须写Object
    @ConvertType(Byte.class)
    public String byteConvert(Object value){
        return value.toString();
    }
}
```
3.局部异常/类型转换
在Controller当中创建方法标注注解
```
@ExceptionHandler(Exception.class)
    public String ex(Exception e){
        return e.getMessage();
    }
    
    @ConvertType(Integer.class)
    public Object c(Object o){
        return o;
    }
```
