package com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.*
import com.wafflestudio.interpark.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/api/v1/ping")
    @Operation(
        summary = "핑퐁 테스트",
        description = "\"ping\"을 보내면 \"pong\"을 반환합니다."
    )
    fun ping() : ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("message" to "pong"))
    }

    @PostMapping("/api/v1/signup")
    @Operation(
        summary = "사용자 회원가입",
        description = """
        새로운 사용자를 등록합니다. 
        사용자 이름, 비밀번호, 닉네임, 이메일, 전화번호를 입력받아 저장합니다.
        요청이 유효하지 않은 경우 또는 사용자 이름이 중복된 경우 적절한 에러 메시지를 반환합니다.
        """,
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "회원가입 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SignUpResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (사용자 이름 또는 비밀번호가 유효하지 않음)",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        type = "object",
                        example = """
                            {
                                "error": "비밀번호는 8~12자여야 합니다.",
                                "errorCode": "INVALID_PASSWORD"
                            }
                        """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "409",
                description = "중복된 사용자 이름 (사용자 이름이 이미 존재함)",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        type = "object",
                        example = """
                            {
                                "error": "사용자 이름이 이미 존재합니다.",
                                "errorCode": "USERNAME_CONFLICT"
                            }
                        """
                    )
                )]
            )
        ],
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 요청 데이터",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = SignUpRequest::class)
            )]
        )
    )
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
