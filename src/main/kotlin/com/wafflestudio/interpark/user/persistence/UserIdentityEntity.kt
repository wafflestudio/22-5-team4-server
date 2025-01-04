package com.wafflestudio.interpark.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class UserIdentityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @OneToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,
    @Column(name = "role", nullable = false)
    var role: String,
    @Column(name = "hashed_password", nullable = false)
    val hashedPassword: String,
    @Column(name = "provider", nullable = false)
    val provider: String,
    @Column(name = "social_id", nullable = true)
    val socialId: String? = null,
)
