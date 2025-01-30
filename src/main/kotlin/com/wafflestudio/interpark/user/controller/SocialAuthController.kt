package com.wafflestudio.interpark.user.controller

import com.wafflestudio.interpark.user.persistence.Provider
import com.wafflestudio.interpark.user.service.SocialAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/social")
class SocialAuthController(
    val socialAuthService: SocialAuthService
) {
    @Operation(
        summary = "소셜 로그인 요청",
        description = """
        클라이언트가 인가코드와 provider(ex. KAKAO, NAVER)를 요청에 담아 소셜로그인을 요청합니다.
        서버에서는 인가 코드를 통해 소셜 인증 서버에 액세스 토큰을 요청하고 이를 다시 소셜 계정 정보와 교환합니다.
        
        - 로그인한 소셜 계정이 로컬 계정과 연동되어 있는 계정인 경우
        기존 로컬 로그인 응답 객체에 provider와 providerId를 추가로 담아 반환합니다.
        이 때, providerId는 사용자 고유 식별자입니다.
        
        - 로그인한 소셜 계정이 로컬 계정과 연동되어 있지 않은 경우
        404에러와 본문에 provider, providerId를 담아 반환합니다.
        이 값을 이용해서 추후에 "/api/v1/social/link"로 연동 요청을 보내시면 됩니다.
        """,
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "소셜 로그인 성공",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SocialLoginResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "소셜 계정이 로컬 계정과 연동되어 있지 않음",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        type = "object",
                        example = """
                            {
                                "error": "This KAKAO account is not linked to local account",
                                "errorCode": 0,
                                "provider": "KAKAO",
                                "providerId": "1234567890"
                            }
                        """
                    )
                )]
            ),
        ],
    )
    @PostMapping("/{provider}/login")
    fun socialLogin(
        @Parameter(description = "소셜 로그인 제공자 (예: KAKAO, NAVER)", example = "KAKAO")
        @PathVariable provider: Provider,

        @Parameter(description = "인가 코드", example = "4/P7q7W91a-oMsCeLvIaQm6bTrgtp7")
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

    @Operation(
        summary = "소셜 계정 - 로컬 계정 연동 요청",
        description = """
        username, password, provider, providerId를 요청본문에 담아 로컬계정 연동을 요청합니다.
        소셜 계정 유저가 로컬 계정 유저인지 확인하기 위해 username과 password를 통해 인증 절차를 통과해야만
        연동이 완료됩니다.
        """,
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "로컬 계정과의 연동 성공",
                content = []
            ),
            ApiResponse(
                responseCode = "404",
                description = "username에 해당하는 로컬 계정이 존재하지 않음",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        type = "object",
                        example = """
                            {
                                "error": "UserIdentity not found",
                                "errorCode": 0
                            }
                        """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "비밀번호가 유효하지 앟음",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        type = "object",
                        example = """
                            {
                                "error": "Invalid Password",
                                "errorCode": 0
                            }
                        """
                    )
                )]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 연동된 계정에 대해 다시 연동 요청을 보내고 있음",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(
                        type = "object",
                        example = """
                            {
                                "error": "Social Account already linked to another user",
                                "errorCode": 0
                            }
                        """
                    )
                )]
            ),
        ],
    )
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

    @GetMapping("/callback")
    fun callback(
        @RequestParam code: String,
    ) : ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("code" to code))
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
