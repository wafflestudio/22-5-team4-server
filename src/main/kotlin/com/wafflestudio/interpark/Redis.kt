package com.wafflestudio.interpark

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {

    @Value("\${redis.host:localhost}")
    private lateinit var redisHost: String

    @Value("\${redis.port:6379}")
    private lateinit var redisPort: String

    @Value("\${redis.password:}")
    private lateinit var redisPassword: String

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        val redisAddress = "redis://$redisHost:$redisPort"
        
        config.useSingleServer().apply {
            address = redisAddress
            if (redisPassword.isNotEmpty()) {
                password = redisPassword
            }
        }
        
        return Redisson.create(config)
    }
}
