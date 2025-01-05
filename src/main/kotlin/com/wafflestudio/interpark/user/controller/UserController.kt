package com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.*
import com.wafflestudio.interpark.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
) {
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
    ): ResponseEntity<TokenResponse> {
        val (accessToken, refreshToken) = userService.signIn(request.username, request.password)
        return ResponseEntity.ok(TokenResponse(accessToken, refreshToken))
    }

    @GetMapping("/api/v1/users/me")
    fun me(
        @AuthUser user: User,
    ): ResponseEntity<User> {
        return ResponseEntity.ok(user)
    }

    @PostMapping("/api/v1/signout")
    fun signout(
        @RequestBody request: SignOutRequest,
    ): ResponseEntity<Void> {
        userService.signOut(request.refreshToken)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/api/v1/refresh_token")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest,
    ): ResponseEntity<TokenResponse> {
        //TODO("refresh Token 을 Http only secure cookie 로 변경")
        val (accessToken, refreshToken) = userService.refreshAccessToken(request.refreshToken)
        return ResponseEntity.ok(TokenResponse(accessToken, refreshToken))
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
    val refreshToken: String,
)

data class SignOutRequest(val refreshToken: String)

data class RefreshTokenRequest(val refreshToken: String)
