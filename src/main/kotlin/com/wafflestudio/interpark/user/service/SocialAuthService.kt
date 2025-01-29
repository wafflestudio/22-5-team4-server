package com.wafflestudio.interpark.user.service

import com.wafflestudio.interpark.user.SignInInvalidPasswordException
import com.wafflestudio.interpark.user.SocialAccountAlreadyLinkedException
import com.wafflestudio.interpark.user.SocialAccountNotFoundException
import com.wafflestudio.interpark.user.UserAccessTokenUtil
import com.wafflestudio.interpark.user.UserIdentityNotFoundException
import com.wafflestudio.interpark.user.controller.User
import com.wafflestudio.interpark.user.persistence.Provider
import com.wafflestudio.interpark.user.persistence.SocialAccountEntity
import com.wafflestudio.interpark.user.persistence.SocialAccountRepository
import com.wafflestudio.interpark.user.persistence.UserIdentityEntity
import com.wafflestudio.interpark.user.persistence.UserIdentityRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient

@Service
class SocialAuthService(
    private val socialAccountRepository: SocialAccountRepository,
    private val userIdentityRepository: UserIdentityRepository,
    private val userAccessTokenUtil: UserAccessTokenUtil,
    // 카카오 설정
    @Value("\${spring.security.oauth2.client.provider.kakao.token-uri}")
    private val kakaoTokenUri: String,
    @Value("\${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private val kakaoUserInfoUri: String,
    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private val kakaoClientId: String,
    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret:}")
    private val kakaoClientSecret: String?,

    // 네이버 설정
    @Value("\${spring.security.oauth2.client.provider.naver.token-uri}")
    private val naverTokenUri: String,
    @Value("\${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private val naverUserInfoUri: String,
    @Value("\${spring.security.oauth2.client.registration.naver.client-id}")
    private val naverClientId: String,
    @Value("\${spring.security.oauth2.client.registration.naver.client-secret}")
    private val naverClientSecret: String
) {
    @Transactional
    fun linkSocialAccount(
        username: String,
        password: String,
        provider: Provider,
        providerId: String
    ): UserIdentityEntity {
        // linkSocialAccount는 회원가입 완료된 로컬 계정에 한해 호출된다고 전제
        // 유저 확인
        val userIdentity = userIdentityRepository.findByUserUsername(username) ?: throw UserIdentityNotFoundException()
        // 로컬 계정 패스워드 확인
        if (!BCrypt.checkpw(password, userIdentity.hashedPassword)) {
            throw SignInInvalidPasswordException()
        }

        // 소셜 계정 중복 확인
        val existingSocialAccount = socialAccountRepository.findByProviderAndProviderId(provider, providerId)
        if (existingSocialAccount != null) {
            if (existingSocialAccount.userIdentity.user.username != username) {
                throw SocialAccountAlreadyLinkedException()
            }
        }

        // 소셜 계정 생성 및 연동
        val socialAccount = SocialAccountEntity(
            userIdentity = userIdentity,
            provider = provider,
            providerId = providerId
        )
        socialAccountRepository.save(socialAccount)

        return userIdentity
    }

    fun exchangeCodeForToken(
        provider: Provider,
        code: String
    ): SocialTokenResponse {
        return when (provider) {
            Provider.KAKAO -> exchangeKakaoToken(code)
            Provider.NAVER -> exchangeNaverToken(code)
            else -> throw IllegalArgumentException("Unsupported provider: $provider")
        }
    }

    private fun exchangeKakaoToken(
        code: String
    ): SocialTokenResponse {
        val bodyMap = LinkedMultiValueMap<String, String>().apply {
            this["grant_type"] = "authorization_code"
            this["client_id"] = kakaoClientId
            if (!kakaoClientSecret.isNullOrEmpty()) {
                this["client_secret"] = kakaoClientSecret
            }
            this["code"] = code
        }
        // 필요하면 redirect_uri도 추가

        // (1) webClient 인스턴스 생성 (혹은 주입)
        val client = WebClient.builder()
            .baseUrl(kakaoTokenUri)
            .build()

        // (2) POST 요청
        val response = client.post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(bodyMap)
            .retrieve() // status 4xx, 5xx 시 오류 발생하게 함
            .bodyToMono(KakaoTokenResponse::class.java) // 비동기 Mono
            .block() // 여기서는 동기 블록으로 처리(데모 용)

        // (3) 응답 파싱
        val tokenBody = response ?: throw RuntimeException("Kakao token response is null")

        return SocialTokenResponse(
            accessToken = tokenBody.accessToken ?: "",
            refreshToken = tokenBody.refreshToken,
            tokenType = tokenBody.tokenType,
            expiresIn = tokenBody.expiresIn ?: 0
        )
    }

    private fun exchangeNaverToken(code: String): SocialTokenResponse {
         val bodyMap = LinkedMultiValueMap<String, String>().apply {
            this["grant_type"] = "authorization_code"
            this["client_id"] = naverClientId
            this["client_secret"] = naverClientSecret
            this["code"] = code
        }

        val client = WebClient.builder()
            .baseUrl(naverTokenUri)
            .build()

        // 1) POST 요청 -> NaverTokenResponse
        val tokenResponse = client.post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(bodyMap)
            .retrieve()
            .bodyToMono(NaverTokenResponse::class.java)
            .block() // 동기식 블록 (데모용)

        // 2) 응답 처리 및 예외 처리
        requireNotNull(tokenResponse) { "Naver token response is null" }
        require(tokenResponse.error == null) {
            "Naver token error: ${tokenResponse.error}, desc = ${tokenResponse.errorDescription}"
        }

        // 3) 결과를 공통 DTO(SocialTokenResponse)로 변환
        return SocialTokenResponse(
            accessToken = tokenResponse.accessToken.orEmpty(),
            refreshToken = tokenResponse.refreshToken,
            tokenType = tokenResponse.tokenType,
            expiresIn = tokenResponse.expiresIn ?: 0
        )
    }

    fun getUserInfo(provider: Provider, accessToken: String): SocialUserInfo {
        return when (provider) {
            Provider.KAKAO -> getKakaoUserInfo(accessToken)
            Provider.NAVER -> getNaverUserInfo(accessToken)
            else -> throw IllegalArgumentException("Unsupported provider: $provider")
        }
    }

    private fun getKakaoUserInfo(accessToken: String): SocialUserInfo {
        val client = WebClient.builder()
            .baseUrl(kakaoUserInfoUri) // https://kapi.kakao.com/v2/user/me
            .build()

        val response = client.get()
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .bodyToMono(KakaoUserInfoResponse::class.java)
            .block()

        val body = response ?: throw RuntimeException("Kakao userinfo is null")
        val id = body.id?.toString() ?: throw RuntimeException("Kakao user id missing")
        val email = body.kakaoAccount?.email
        val nickname = body.kakaoAccount?.profile?.nickname

        return SocialUserInfo(
            provider = Provider.KAKAO,
            providerId = id,
            email = email,
            nickname = nickname
        )
    }

    private fun getNaverUserInfo(accessToken: String): SocialUserInfo {
        val client = WebClient.builder()
            .baseUrl(naverUserInfoUri) // 예: "https://openapi.naver.com/v1/nid/me"
            .build()

        // 1) GET 요청, Authorization 헤더에 Bearer 토큰
        val response = client.get()
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .bodyToMono(NaverUserInfoResponse::class.java)
            .block()

        // 2) 응답이 null이거나 에러인지 체크
        if (response == null) {
            throw RuntimeException("Naver user info response is null")
        }
        if (response.resultcode != "00") {
            throw RuntimeException("Naver userinfo error: ${response.message}")
        }

        val detail = response.response
            ?: throw RuntimeException("Naver userinfo 'response' field is null")

        // 3) 필요한 식별 정보(id, email, nickname 등) 추출
        val id = detail.id
            ?: throw RuntimeException("Naver user id is null")
        val email = detail.email
        val nickname = detail.nickname

        // 4) 공통 DTO (SocialUserInfo)로 변환
        return SocialUserInfo(
            provider = Provider.NAVER,
            providerId = id,
            email = email,
            nickname = nickname
        )
    }

    @Transactional
    fun socialLogin(
        provider: Provider,
        code: String,
    ) : SocialLoginResult {
        // (1) code -> token
        val token = exchangeCodeForToken(provider, code)

        // (2) token -> userInfo
        val userInfo = getUserInfo(provider, token.accessToken)

        // (3) DB에서 "이미 존재하는" 소셜 계정 찾기
        val socialAccount = socialAccountRepository.findByProviderAndProviderId(provider, userInfo.providerId)
            ?: throw SocialAccountNotFoundException(provider, userInfo.providerId)

        val userEntity = socialAccount.userIdentity.user
        val user = User.fromEntity(userEntity)

        // (4) 로그인 성공 -> JWT 발행
        return SocialLoginResult(
            user = user,
            accessToken = userAccessTokenUtil.generateAccessToken(userEntity.id!!),
            refreshToken = userAccessTokenUtil.generateRefreshToken(userEntity.id!!),
            providerId = userInfo.providerId
        )
    }
}

data class SocialTokenResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Int? = null
)

data class SocialUserInfo(
    val provider: Provider,
    val providerId: String,
    val email: String?,
    val nickname: String?
    // 소셜 계정의 email과 nickname을 현재는 따로 사용하지 않음
)

data class SocialLoginResult(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
    val providerId: String
    // 필요하면 JWT, refreshToken, etc...
)

// 카카오 토큰 응답
data class KakaoTokenResponse(
    val tokenType: String? = null,
    val accessToken: String? = null,
    val expiresIn: Int? = null,
    val refreshToken: String? = null,
    val refreshTokenExpiresIn: Int? = null,
    //@JsonProperty("scope")
    //val scope: String? = null
)

// 카카오 유저정보 응답
data class KakaoUserInfoResponse(
    val id: Long? = null,
    val kakaoAccount: KakaoAccount? = null
)

data class KakaoAccount(
    val email: String? = null,
    val profile: Profile? = null,
)

data class Profile(
    val nickname: String? = null,
    // etc...
)

// 네이버 토큰 응답
data class NaverTokenResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Int? = null,
    val error: String? = null,
    val errorDescription: String? = null
)

// 네이버 유저정보 응답
data class NaverUserInfoResponse(
    val resultcode: String? = null,
    val message: String? = null,
    val response: NaverUserInfoDetail? = null
)
data class NaverUserInfoDetail(
    val id: String?,
    val email: String?,
    val nickname: String?,
    // etc...
)
