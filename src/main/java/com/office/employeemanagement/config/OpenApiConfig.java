package com.office.employeemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeManagementOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management API")
                        .description("Система управления сотрудниками: департаменты, проекты, задачи, кэширование, "
                                + "асинхронные операции")
                        .version("1.0.0"));
    }
}
