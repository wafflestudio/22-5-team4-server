package com.wafflestudio.interpark.user

import com.wafflestudio.interpark.user.service.UserDetailsServiceImpl
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationFilter(
    private val userAccessTokenUtil: UserAccessTokenUtil,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        // 1) 헤더에서 "Authorization" 값 추출
        val header = request.getHeader("Authorization")
        // 예: "Authorization: Bearer <token>"
        if (!header.isNullOrBlank() && header.startsWith("Bearer ")) {
            val accessToken = header.split(" ")[1]

            // 2) UserAccessTokenUtil로 토큰 유효성 검사
            //    유효하면 userId가 반환되고,
            //    유효하지 않으면 null
            val subject = userAccessTokenUtil.validateAccessToken(accessToken)

            if (subject != null) {
                // 3) subject를 기반으로 DB에서 유저/권한 정보 조회 (UserIdentityEntity)
                val identity = userDetailsService.getUserIdentityByUserId(subject)

                if (identity != null) {
                    // 예: role -> SimpleGrantedAuthority("ROLE_USER"/"ROLE_ADMIN" 등)
                    val roleName = "ROLE_${identity.role.name}"  // e.g. ROLE_USER
                    val authorities = listOf(SimpleGrantedAuthority(roleName))

                    // 4) Spring Security에 Authentication 등록
                    // principal에는 identity(또는 더 확장된 CustomUserDetails)를 넣어도 됨
                    val authentication = UsernamePasswordAuthenticationToken(
                        identity, // principal
                        null,     // credentials
                        authorities
                    )
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }

        // 5) 체인 계속 진행
        chain.doFilter(request, response)
    }
}