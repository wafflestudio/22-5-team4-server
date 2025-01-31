package com.wafflestudio.interpark.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

@Configuration
class JwtConfig {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Bean
    fun secretKeySpec(): SecretKey {
        return Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
    }
}