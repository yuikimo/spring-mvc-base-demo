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

参数解析器
解析器	说明
@PathVariable	路径获取参数
@CookieValue	从cookie中获取参数
@RequestHeader	从请求头中获取参数
@RequestParam	获取参数
MultipartFile	文件
@RequestBody	json
@HttpServletRequest	HttpServletRequest
@HttpServletResponse	HttpServletResponse
@PathVariable
```
@GetMapping("/order/{id}")
    public String path(@PathVariable String id){
        
    }
    @GetMapping("/order/{id}/{name}")
    public String path(@PathVariable("id") String id,@PathVariable String name){

    }
    @GetMapping("/order/{id}/{name}")
    public String path(@PathVariable Map map){

    }
```
```
@CookieValue
  @GetMapping("/order/get")
    public String get(@CookieValue String name){
        
    }

    @GetMapping("/order/get")
    public String get(@CookieValue("token") String name){

    }
```
```
@RequestHeader
@GetMapping("/order/header")
    public String get(@RequestHeader String name){
        
    }

    @GetMapping("/order/header")
    public String get(@RequestHeader Map map){

    }
```
```
@RequestParam
@GetMapping("/order/par")
    public String par(@RequestParam String name,Integer age){

    }

    @GetMapping("/order/par")
    public String par(User user){

    }
```
```
MultipartFile
@GetMapping("/order/file")
    public String file(List<MultipartFile> file,MultipartFile[] files,Collection<MultipartFile> files,MultipartFile file){

    }
@RequestBody
@GetMapping("/order")
    public String file(@RequestBody User user){

    }
```
HttpServletRequest
@GetMapping("/order/request")
    public String file(HttpServletRequest request){

    }
HttpServletResponse
 @GetMapping("/order/request")
    public String file(HttpServletResponse response){

    }
```
可在映射器中支持的参数

以上所有参数

可在异常解析器上支持的参数
``
1.异常类

2.HttpServletRequest

3.HttpServletResponse

4.HandlerMethod
``
返回值处理器
``
@ResponseBody 方法级别

@RestController 类级别
``
只支持返回json 主要核心功能需要
