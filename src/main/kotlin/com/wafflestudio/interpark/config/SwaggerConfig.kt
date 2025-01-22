package com.wafflestudio.interpark.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.Scopes
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Interpark Ticket API")
                    .version("v1")
                    .description(
                        """
                        API documentation for Interpark Ticket application.
                        - Supports JWT-based local authentication.
                        - Supports OAuth2.0 for social login (Google, Naver).
                        """.trimIndent()
                    )
            )
            .addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
            .addSecurityItem(SecurityRequirement().addList("Google OAuth2"))
            .components(
                Components()
                    .addSecuritySchemes(
                        "Bearer Authentication",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
                    .addSecuritySchemes(
                        "Google OAuth2",
                        SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .flows(
                                OAuthFlows()
                                    .authorizationCode(
                                        OAuthFlow()
                                            .authorizationUrl("https://accounts.google.com/o/oauth2/auth")
                                            .tokenUrl("https://oauth2.googleapis.com/token")
                                            .scopes(
                                                Scopes()
                                                    .addString("email", "email access")
                                            )
                                    )
                            )
                    )
            )
    }
}