package com.wafflestudio.interpark.user

import com.wafflestudio.interpark.user.persistence.RefreshTokenEntity
import com.wafflestudio.interpark.user.persistence.RefreshTokenRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class UserAccessTokenUtil(
    private var refreshTokenRepository: RefreshTokenRepository,
    private val secretKey: SecretKey
) {
    fun generateAccessToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + ACCESS_EXPIRATION_TIME)
        return Jwts.builder()
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .compact()
    }

    fun validateAccessToken(accessToken: String): String? {
        return try {
            val claims =
                Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .also { jws ->
                        if (jws.header.algorithm != SignatureAlgorithm.HS256.value) {
                            throw InvalidTokenException()
                        }
                    }
                    .body
            if (claims.expiration < Date()) {
                throw TokenExpiredException()
            }
            return claims.subject
        } catch (e: Exception) {
            null
        }
    }

    fun generateRefreshToken(userId: String): String {
        val now = Date()
        val expiryDate = Date(now.time + REFRESH_EXPIRATION_TIME)
        val refreshToken = UUID.randomUUID().toString()

        // 해당 유저의 다른 refreshToken 이 있다면 삭제
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

        if (storedRefreshToken.expiryDate < Date()) throw TokenExpiredException()

        val newAccessToken = generateAccessToken(storedRefreshToken.userId)
        val newRefreshToken = generateRefreshToken(storedRefreshToken.userId)

        return Pair(newAccessToken, newRefreshToken)
    }

    fun removeRefreshToken(refreshToken: String) {
        val storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken) ?: return
        refreshTokenRepository.delete(storedRefreshToken)
    }

    companion object {
        private const val ACCESS_EXPIRATION_TIME = 1000 * 60 * 15 // 15 minutes
        private const val REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 // 1 day
    }
}
