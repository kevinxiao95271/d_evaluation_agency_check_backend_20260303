package com.zjmc.evaluation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("浙江医管中心考核评估系统API")
                .description("机构考核评估、评分管理、结果统计")
                .version("1.0.0")
                .contact(new Contact()
                    .name("ZJMC")));
    }
}
