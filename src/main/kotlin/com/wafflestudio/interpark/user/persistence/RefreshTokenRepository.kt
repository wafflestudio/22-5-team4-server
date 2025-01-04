package com.wafflestudio.interpark.user.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, String> {
    fun findByRefreshToken(refreshToken: String): RefreshTokenEntity?

    fun findByUserId(userId: String): RefreshTokenEntity?
}