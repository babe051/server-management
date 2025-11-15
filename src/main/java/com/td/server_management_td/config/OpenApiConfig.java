package com.td.server_management_td.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Server Management API")
                        .version("1.0.0")
                        .description("REST API for managing servers. Provides endpoints to create, list, rename, start, stop, and delete servers."));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("server-management")
                .pathsToMatch("/api/**")
                .packagesToScan("com.td.server_management_td.controller")
                .build();
    }
}

