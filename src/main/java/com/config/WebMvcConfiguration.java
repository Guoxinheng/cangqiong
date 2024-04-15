package com.config;

import com.interceptor.JwtTokenAdminInterceptor;
import com.interceptor.JwtTokenUserInterceptor;
import com.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {//注册一个拦截器
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()//制定swagger文档的标题，版本二，描述信息
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)//设置文档类型
                .groupName("管理端接口")
                .apiInfo(apiInfo)//设置api文档的标题，描述信息
                .select()//设置扫描接口的包
                .apis(RequestHandlerSelectors.basePackage("com.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    @Bean
    public Docket docket2() {
        ApiInfo apiInfo = new ApiInfoBuilder()//制定swagger文档的标题，版本二，描述信息
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)//设置文档类型
                .groupName("用户端接口")
                .apiInfo(apiInfo)//设置api文档的标题，描述信息
                .select()//设置扫描接口的包
                .apis(RequestHandlerSelectors.basePackage("com.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }


    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");//用来访问swagger-ui.html页面
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");//用来存放资源路径
    }
    /**
     * 扩展消息转换器列表。
     * 该方法被用于向消息转换器列表中添加更多的转换器。默认情况下，不会添加任何额外的转换器，
     * 但子类可以通过重写此方法来扩展消息转换器的集合。
     *
     * @param converters 消息转换器列表，这是一个可变的列表，子类可以通过添加新的转换器来扩展它。
     */

    @Override//扩展消息转换器
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //为消息转换器设置一个对象转换器让java对象序列化为json
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转换器加入到集合中
        converters.add(0,converter);

    }
}
