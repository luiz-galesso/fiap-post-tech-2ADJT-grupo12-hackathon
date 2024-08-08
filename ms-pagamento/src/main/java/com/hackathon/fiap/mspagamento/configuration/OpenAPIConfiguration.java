package com.hackathon.fiap.mspagamento.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("MS-PAGAMENTO")
                .version("1.0.0")
                .description("Microserviço para gerenciamento de pagamentos");

        return new OpenAPI().info(info);
    }
}