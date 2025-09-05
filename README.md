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
