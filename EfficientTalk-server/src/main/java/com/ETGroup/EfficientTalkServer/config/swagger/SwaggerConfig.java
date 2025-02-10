package com.pigstory.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig
{
    @Bean
    public OpenAPI springDocOpenAPI ()
    {
        return new OpenAPI().info(new Info()
                .title("测试系统接口文档")   //设置API文档网站标题
                .description("接口测试")    //网站介绍
                .version("1.0"));
    }
}
