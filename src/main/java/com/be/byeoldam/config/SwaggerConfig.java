package com.be.byeoldam.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("accessToken", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)  // API Key 타입으로 변경
                                .name("accessToken")  // 헤더에서 "accessToken" 키로 가져옴
                                .in(SecurityScheme.In.HEADER)))  // 헤더에서 찾음
                .security(List.of(new SecurityRequirement().addList("accessToken")))
                .info(new Info()
                        .title("Beyeoldam API")
                        .version("1.0.0")
                        .description("This is the API documentation for my project."));
    }
}

