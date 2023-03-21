package com.alihmzyv.todorestservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "TO DO API", version = "1.0.0"),
        servers = {@Server(url = "https://to-do-rest-service-production.up.railway.app/", description = "Default Server URL"),
                @Server(url = "http://localhost:7070/", description = "Local Server URL")}
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        description = "Replace 'username' property name with 'emailAddress' at '/api/login' endpoint"
)
@Configuration
public class OpenApiConfig {
}
