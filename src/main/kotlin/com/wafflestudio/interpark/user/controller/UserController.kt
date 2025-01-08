package com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.*
import com.wafflestudio.interpark.user.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/api/v1/ping")
    fun ping() : ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("message" to "pong"))
    }
    @PostMapping("/api/v1/signup")
    fun signup(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<SignUpResponse> {
        val user =
            userService.signUp(
                request.username,
                request.password,
                request.nickname,
                request.email,
                request.phoneNumber,
            )
        return ResponseEntity.ok(SignUpResponse(user))
    }

    @PostMapping("/api/v1/signin")
    fun signin(
        @RequestBody request: SignInRequest,
        response: HttpServletResponse,
    ): ResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = userService.signIn(request.username, request.password)

        val cookie =
            Cookie("refreshToken", refreshToken).apply {
                isHttpOnly = true
                secure = true
                path = "/api/v1/refresh_token"
                maxAge = 60 * 60 * 24 * 7
                // TODO("domain 설정하기")
            }
        response.addCookie(cookie)

        return ResponseEntity.ok(TokenResponse(accessToken))
    }

    @GetMapping("/api/v1/users/me")
    fun me(
        @AuthUser user: User,
    ): ResponseEntity<User> {
        return ResponseEntity.ok(user)
    }

    @PostMapping("/api/v1/signout")
    fun signout(
        @CookieValue(value = "refresh_token", required = false) refreshToken: String?,
    ): ResponseEntity<Void> {
        if (refreshToken == null) {
            throw TokenNotFoundException()
        }
        userService.signOut(refreshToken)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/api/v1/refresh_token")
    fun refreshToken(
        @CookieValue(value = "refreshToken", required = false) refreshToken: String?,
        response: HttpServletResponse,
    ): ResponseEntity<TokenResponse> {
        if (refreshToken == null) {
            throw TokenNotFoundException()
        }

        val (newAccessToken, newRefreshToken) = userService.refreshAccessToken(refreshToken)

        val cookie =
            Cookie("refreshToken", newRefreshToken).apply {
                isHttpOnly = true
                secure = true
                path = "/api/v1/refresh_token"
                maxAge = 60 * 60 * 24 * 7
                // TODO("domain 설정하기")
            }
        response.addCookie(cookie)

        return ResponseEntity.ok(TokenResponse(newAccessToken))
    }
}

data class SignUpRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val phoneNumber: String,
    val email: String,
)

data class SignUpResponse(val user: User)

data class SignInRequest(
    val username: String,
    val password: String,
)

data class TokenResponse(
    val accessToken: String,
)

data class SignOutRequest(val refreshToken: String)

data class RefreshTokenRequest(val refreshToken: String)
