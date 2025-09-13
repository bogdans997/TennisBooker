package com.example.tennisbokker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "TennisBooker API",
                version = "v1",
                description = "Booking courts, matches, results"
        ),
        servers = {
                @Server(url = "/api/v1", description = "Default")
        }
)
@Configuration
public class OpenApiConfig {}
