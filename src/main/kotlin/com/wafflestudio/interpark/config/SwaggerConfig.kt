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
            .addSecurityItem(SecurityRequirement().addList("Kakao OAuth2"))
            .addSecurityItem(SecurityRequirement().addList("Naver OAuth2"))
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
                    .addSecuritySchemes(
                        "Kakao OAuth2",
                        SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .flows(
                                OAuthFlows()
                                    .authorizationCode(
                                        OAuthFlow()
                                            .authorizationUrl("https://kauth.kakao.com/oauth/authorize")
                                            .tokenUrl("https://kauth.kakao.com/oauth/token")
                                            .scopes(
                                                Scopes()
                                                    .addString("account_email", "email access")
                                                    .addString("profile", "profile access")
                                            )
                                    )
                            )
                    )
                    .addSecuritySchemes(
                        "Naver OAuth2",
                        SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .flows(
                                OAuthFlows()
                                    .authorizationCode(
                                        OAuthFlow()
                                            .authorizationUrl("https://nid.naver.com/oauth2.0/authorize")
                                            .tokenUrl("https://nid.naver.com/oauth2.0/token")
                                            .scopes(
                                                Scopes()
                                                    .addString("email", "email access")
                                                    .addString("name", "name access")
                                            )
                                    )
                            )
                    )
            )
    }
}