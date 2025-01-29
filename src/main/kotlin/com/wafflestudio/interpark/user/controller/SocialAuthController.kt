package com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.persistence.Provider
import com.wafflestudio.interpark.user.service.SocialAuthService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class SocialAuthController(
    val socialAuthService: SocialAuthService
) {
    @PostMapping("/{provider}/login")
    fun socialLogin(
        @PathVariable provider: Provider,
        @RequestParam("code") authorizationCode: String,
        response: HttpServletResponse
    ): ResponseEntity<SocialLoginResponse> {
        val result = socialAuthService.socialLogin(provider, authorizationCode)
        val (user, accessToken, refreshToken, providerId) = result
        val cookie =
            Cookie("refreshToken", refreshToken).apply {
                isHttpOnly = true
                secure = true
                path = "/api/v1/auth"
                maxAge = 60 * 60 * 24 * 7
                // TODO("domain 설정하기")
            }
        response.addCookie(cookie)

        return ResponseEntity.ok(SocialLoginResponse(user, accessToken, provider, providerId))
    }

    @PostMapping("/link")
    fun linkSocialAccount(
        @RequestBody request: LinkSocialAccountRequest
    ): ResponseEntity<Void> {
        socialAuthService.linkSocialAccount(
            username = request.username,
            password = request.password,
            provider = request.provider,
            providerId = request.providerId
        )
        return ResponseEntity.ok().build()
    }
}

data class SocialLoginResponse(
    val user: User,
    val accessToken: String,
    val provider: Provider,
    val providerId: String
)

data class LinkSocialAccountRequest(
    val username: String,
    val password: String,
    val provider: Provider,
    val providerId: String,
)
