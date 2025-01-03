package com.wafflestudio.interpark.user

import com.wafflestudio.interpark.user.persistence.RefreshTokenEntity
import com.wafflestudio.interpark.user.persistence.RefreshTokenRepository
import com.wafflestudio.interpark.user.persistence.UserEntity
import com.wafflestudio.interpark.user.persistence.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

@Component
object UserAccessTokenUtil{
    //object 에는 매개변수를 넣을 수 없어 @Autowired 사용
    @Autowired
    private lateinit var refreshTokenRepository : RefreshTokenRepository

    fun generateAccessToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + ACCESS_EXPIRATION_TIME)
        return Jwts.builder()
            .signWith(SECRET_KEY)
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .compact()
    }

    fun validateAccessToken(accessToken: String): String? {
        return try {
            val claims =
                Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(accessToken)
                    .body
            if (claims.expiration < Date()) {
                return null
            }
            return claims.subject
        } catch (e: Exception) {
            null
        }
    }

    fun generateRefreshToken(userId: String): String {
        val now = Date()
        val expiryDate = Date(now.time+REFRESH_EXPIRATION_TIME)
        val refreshToken = UUID.randomUUID().toString()

        //해당 유저의 다른 refreshToken 이 있다면 삭제
        val existingToken = refreshTokenRepository.findByUserId(userId)
        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken)
        }

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                refreshToken = refreshToken,
                expiryDate = expiryDate,
            ),
        )
        return refreshToken
    }

    fun refreshAccessToken(refreshToken: String): Pair<String, String>? {
        val storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken) ?: return null

        if(storedRefreshToken.expiryDate < Date()) return null

        val newAccessToken = generateAccessToken(storedRefreshToken.userId)
        val newRefreshToken = generateRefreshToken(storedRefreshToken.userId)

        return Pair(newAccessToken, newRefreshToken)
    }
    private const val ACCESS_EXPIRATION_TIME = 1000 * 60 * 15 // 15 minutes
    private const val REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 // 1 day
    private val SECRET_KEY = Keys.hmacShaKeyFor("THISSHOULDBEPROTECTED".toByteArray(StandardCharsets.UTF_8))
}