package com.wafflestudio.interpark.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { disable() }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS // 세션 비활성화
            }
            authorizeHttpRequests {
                // 사용자 권한
                authorize(HttpMethod.GET, "/api/v1/performance/search", permitAll) // 공연 조회
                authorize(HttpMethod.GET, "/api/v2/performance/search", permitAll) // 공연 조회 + 페이지네이션
                authorize(HttpMethod.GET, "/api/v1/performance/{performanceId}", permitAll) // 공연 상세정보 반환
                authorize(HttpMethod.GET, "/api/v1/performance-event", permitAll)
                authorize(HttpMethod.GET, "/api/v1/performance-event/{performanceId}/{performanceDate}", permitAll)
                authorize(HttpMethod.GET, "/api/v1/performance-hall", permitAll)
                authorize(HttpMethod.GET, "/api/v1/ping", permitAll)
                authorize(HttpMethod.POST, "/api/v1/local/signup", permitAll)
                authorize(HttpMethod.POST, "/api/v1/local/signin", permitAll)
                authorize(HttpMethod.POST, "/api/v1/auth/signout", permitAll)
                authorize(HttpMethod.POST, "/api/v1/auth/refresh_token", permitAll)
                authorize(HttpMethod.GET, "/api/v1/seat/{performanceEventId}/available", permitAll)
                authorize(HttpMethod.GET, "/api/v1/performance/{performanceId}/review", permitAll)
                authorize(HttpMethod.GET, "/api/v2/performance/{performanceId}/review", permitAll)
                authorize(HttpMethod.GET, "/api/v1/review/{reviewId}/reply", permitAll)
                authorize(HttpMethod.GET, "/api/v2/review/{reviewId}/reply", permitAll)
                authorize(HttpMethod.POST, "/api/v1/social/**", permitAll)
                authorize("/api/v1/**", hasAnyRole("USER", "ADMIN")) // 그 외 모두 유저 권한 필요
                authorize("/admin/v1/**", hasRole("ADMIN"))

                // Swagger 관련 경로 허용
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/swagger-resources/**", permitAll)
                authorize("/webjars/**", permitAll)

                authorize(anyRequest, authenticated)
            }
            exceptionHandling {
                accessDeniedHandler = RestAccessDeniedHandler()
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
        }
        return http.build()
    }
}
