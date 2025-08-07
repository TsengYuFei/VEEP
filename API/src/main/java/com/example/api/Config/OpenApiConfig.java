package com.example.api.Config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI OpenApiConfig(){
        return new OpenAPI()
                .info(new Info().title("VEEP").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public OpenApiCustomizer globalHeaderCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            Parameter sessionHeader = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .name("Session-ID")
                    .required(false)
                    .description("Log Session ID");
            operation.addParametersItem(sessionHeader);
        }));
    }
}
