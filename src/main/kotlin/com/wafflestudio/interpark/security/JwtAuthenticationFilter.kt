package com.wafflestudio.interpark.security

import com.wafflestudio.interpark.user.UserAccessTokenUtil
import com.wafflestudio.interpark.user.controller.UserDetailsImpl
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
            //    유효하면 userId가 반환되고, 유효하지 않으면 null
            val subject = userAccessTokenUtil.validateAccessToken(accessToken)

            if (subject != null) {
                // 3) subject(userId)를 기반으로 DB에서 유저/권한 정보 조회 (UserDetails)
                val userDetails = userDetailsService.loadUserByUserId(subject)

                // 4) Spring Security에 Authentication 등록
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        // 5) 체인 계속 진행
        chain.doFilter(request, response)
    }
}
