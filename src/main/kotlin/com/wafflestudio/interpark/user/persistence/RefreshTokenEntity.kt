package com.wafflestudio.interpark.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant
import java.util.*

@Entity
class RefreshTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "user_id", nullable = false)
    val userId: String,
    @Column(name = "refresh_token", nullable = false, unique = true)
    val refreshToken: String,
    @Column(name = "expiry_date", nullable = false)
    val expiryDate: Date,
)